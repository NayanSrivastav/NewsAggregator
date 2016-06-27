package screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import datalicious.com.news.R;
import datalicious.com.news.utils.ApiCallback;
import datalicious.com.news.utils.ApiRequest;
import datalicious.com.news.utils.ConnectionUtils;
import datalicious.com.news.utils.rssUtil.RssFeed;
import datalicious.com.news.utils.rssUtil.RssItem;
import datalicious.com.news.utils.rssUtil.RssReader;

public class BlogFeedFragment extends Fragment implements ApiCallback {
    private static final String BLOG_URL = "http://blog.datalicious.com/feed/";
    private static final String BLOG_FEED_TAG = "blog_feed";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recycler_view_layoout, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpFeed();
    }

    private void setUpFeed() {
        if (ConnectionUtils.isConnected(getContext())) {
            ApiRequest.getStringResponse(getContext(), BLOG_URL, this, ApiRequest.MethodTypes.GET, BLOG_FEED_TAG);
        } else {
            Snackbar.make(getView(), "No connection available", Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public void onResult(String response, int httpCode, String tag) {
        if (httpCode == HttpURLConnection.HTTP_OK) {
            try {
                RssFeed feed = RssReader.read(response);
                ArrayList<RssItem> rssItems = feed.getRssItems();
                for (RssItem s : rssItems) {

                }
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            XmlParserCreator parserCreator = new XmlParserCreator() {
//                @Override
//                public XmlPullParser createParser() {
//                    try {
//                        return XmlPullParserFactory.newInstance().newPullParser();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            };
//
//            GsonXml gsonXml = new GsonXmlBuilder()
//                    .setXmlParserCreator(parserCreator)
//                    .create();
//            FeedResp feeds=gsonXml.fromXml(response,FeedResp.class);
//            Toast.makeText(getContext(), feeds.channel.feeds.toString()+"", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResult(JSONObject response, int httpCode, String tag) {
        // do nothing
    }

    @Override
    public void onError(String request, int httpCode, String tag) {
        Snackbar.make(getView(), "Something went wrong", Snackbar.LENGTH_INDEFINITE);
    }

    private static class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_blog_feed, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView main, info;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                main = (TextView) itemView.findViewById(R.id.main);
                info = (TextView) itemView.findViewById(R.id.info);
                itemView.setOnClickListener(this);
            }

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {

            }
        }
    }

    static class FeedResp {

        private Channel channel;

        static class Channel {

            RSSFeed[] feeds;
        }
    }
}
