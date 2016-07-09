package screen;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

public class BlogFeedFragment extends ListFragment implements ApiCallback {
    private static final String BLOG_URL = "http://blog.datalicious.com/feed/";
    private static final String BLOG_FEED_TAG = "blog_feed";
    private FeedAdapter adapter;

    protected void setUpFeed() {
        if (ConnectionUtils.isConnected(getContext())) {
            dismissSnakebar();
            ApiRequest.getStringResponse(getContext(), BLOG_URL, this, ApiRequest.MethodTypes.GET, BLOG_FEED_TAG, null, null, null);
        } else {
            showSnakebar(getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE);
            stopRefreshing();
        }
    }

    @Override
    public void onResult(String response, int httpCode, String tag) {
        if (httpCode == HttpURLConnection.HTTP_OK) {
            try {
                RssFeed feed = RssReader.read(response);
                ArrayList<RssItem> rssItems = feed.getRssItems();
                adapter.setRssItems(rssItems);
            } catch (SAXException | IOException e) {
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

    @Override
    protected RecyclerView.Adapter getAdapter() {
        if (adapter == null) {
            adapter = new FeedAdapter(this);
        }
        return adapter;
    }

    private static class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> implements View.OnClickListener{

        List<RssItem> rssItems;
        WeakReference<BlogFeedFragment> blogFeedFragment;

        public FeedAdapter(BlogFeedFragment fragment) {
            rssItems = new ArrayList<>();
            blogFeedFragment = new WeakReference<>(fragment);
        }

        public void setRssItems(List<RssItem> rssItems) {
            this.rssItems.clear();
            this.rssItems.addAll(rssItems);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_blog_feed,
                    parent, false), blogFeedFragment);
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
            holder.itemView.setOnClickListener(this);
            holder.itemView.setTag(rssItem.getLink());
        }


        @Override
        public int getItemCount() {
            return rssItems.size();
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            // TODO: 27/6/16 remove this
            if (blogFeedFragment.get() != null && blogFeedFragment.get().getActivity() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag().toString()));
                v.getContext().startActivity(intent);
            }
        }
        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView main, info, date;
            WeakReference<BlogFeedFragment> blogFeedFragment;

            public ViewHolder(@NonNull View itemView, WeakReference<BlogFeedFragment> blogFeedFragment) {
                super(itemView);
                this.blogFeedFragment = blogFeedFragment;
                main = (TextView) itemView.findViewById(R.id.main);
                info = (TextView) itemView.findViewById(R.id.info);
                date = (TextView) itemView.findViewById(R.id.date);
            }
        }
    }
}
