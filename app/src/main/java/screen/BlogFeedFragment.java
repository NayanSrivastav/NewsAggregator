package screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import datalicious.com.news.R;
import datalicious.com.news.utils.ApiCallback;
import datalicious.com.news.utils.ApiRequest;
import datalicious.com.news.utils.ConnectionUtils;
import datalicious.com.news.utils.DateUtil;
import datalicious.com.news.utils.rssUtil.RssFeed;
import datalicious.com.news.utils.rssUtil.RssItem;
import datalicious.com.news.utils.rssUtil.RssReader;

public class BlogFeedFragment extends DataliciousFragment implements ApiCallback {
    private static final String BLOG_URL = "http://blog.datalicious.com/feed/";
    private static final String BLOG_FEED_TAG = "blog_feed";
    private FeedAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recycler_view_layoout, container, false);
        adapter = new FeedAdapter(this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rec_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                setRefreshEnabled(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpFeed();
    }

    @Override
    public void onRefresh() {
        setUpFeed();
    }

    private void setUpFeed() {
        if (ConnectionUtils.isConnected(getContext())) {
            dismissSnakebar();
            ApiRequest.getStringResponse(getContext(), BLOG_URL, this, ApiRequest.MethodTypes.GET, BLOG_FEED_TAG);
        } else {
            showSnakebar("No connection available", Snackbar.LENGTH_INDEFINITE);
        }
    }

    @Override
    public void onResult(String response, int httpCode, String tag) {
        if (httpCode == HttpURLConnection.HTTP_OK) {
            try {
                RssFeed feed = RssReader.read(response);
                ArrayList<RssItem> rssItems = feed.getRssItems();
                adapter.setRssItems(rssItems);
            } catch (SAXException|IOException e) {
                e.printStackTrace();
            }
        }
        stopRefreshing();
    }

    @Override
    public void onResult(JSONObject response, int httpCode, String tag) {
        // do nothing
    }

    @Override
    public void onError(String request, int httpCode, String tag) {
        showSnakebar("Something went wrong", Snackbar.LENGTH_INDEFINITE);
        stopRefreshing();
    }

    private static class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>  {

        List<RssItem> rssItems;
        WeakReference<BlogFeedFragment> blogFeedFragment;

        public FeedAdapter(BlogFeedFragment fragment) {
            rssItems = new ArrayList<>();
            blogFeedFragment=new WeakReference<>(fragment);
        }

        public void setRssItems(List<RssItem> rssItems) {
            this.rssItems.clear();
            this.rssItems.addAll(rssItems);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_blog_feed, parent, false),blogFeedFragment);
        }

        RssItem getFeed(int position) {
            return rssItems.get(position);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            RssItem rssItem = getFeed(position);
            holder.date.setText(DateUtil.dateToString(rssItem.getPubDate().toString()));
            holder.main.setText(rssItem.getTitle());
            holder.info.setText(Html.fromHtml(rssItem.getDescription()));
        }


        @Override
        public int getItemCount() {
            return rssItems.size();
        }


        static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView main, info, date;
            WeakReference<BlogFeedFragment>  blogFeedFragment;
            public ViewHolder(@NonNull View itemView, WeakReference<BlogFeedFragment>  blogFeedFragment) {
                super(itemView);
                this.blogFeedFragment=blogFeedFragment;
                main = (TextView) itemView.findViewById(R.id.main);
                info = (TextView) itemView.findViewById(R.id.info);
                date = (TextView) itemView.findViewById(R.id.date);
                itemView.setOnClickListener(this);
            }

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // TODO: 27/6/16 remove this 
                if(blogFeedFragment.get()!=null&&blogFeedFragment.get().getActivity()!=null)
                {
                    blogFeedFragment.get().showSnakebar("clicked @ "+getLayoutPosition(), Snackbar.LENGTH_LONG);
                }
            }
        }
    }
}
