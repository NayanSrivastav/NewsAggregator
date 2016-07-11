package screen;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datalicious.com.news.R;
import datalicious.com.news.utils.ApiCallback;
import datalicious.com.news.utils.ApiRequest;
import datalicious.com.news.utils.ConnectionUtils;
import datalicious.com.news.utils.Constants;
import datalicious.com.news.utils.DateUtil;
import datalicious.com.news.utils.ImageUtil;

/**
 * Created by nayan on 9/7/16.
 */
public class TwitterFragment extends ListFragment implements ApiCallback {

    public static final List<String> ACCOUNTS;
    public static final String KEY = "MnWmjSW8cwjGUrdjQAFIQi5h4";
    public static final String CLIENT_SECRET = "GP8Biek9ILeyDCZlroVExOF7iJRnVI9tKeHWccmXQWwff69kji";
    final static String TWITTER_TOKEN_URL = "https://api.twitter.com/oauth2/token/";
    final static String TWITTER_STREAM_URL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
    private static final String TWITTER_TOKEN_TAG = "twitter_tag_token";
    private static final String TWITTER_FEED_TAG = "twitter_feed_tag";
    private JSONObject accessToken;

    static {
        ACCOUNTS = new ArrayList<>(10);
        ACCOUNTS.add("thesupertag");
        ACCOUNTS.add("datalicious");
        ACCOUNTS.add("cbartens");
    }

    private Adapter adapter;

    @Override
    protected void setUpFeed() {
        if (ConnectionUtils.isConnected(getContext())) {
            dismissSnakebar();
            startFeed(0);
        } else {
            showSnakebar("No connection available", Snackbar.LENGTH_INDEFINITE);
            stopRefreshing();
        }
    }

    private void startFeed(int page) {
        for (String account : ACCOUNTS) {
            getAuthToken(account, page);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setRefreshEnabled(false);
    }

    private void getAuthToken(String account, int page) {
        if (accessToken == null) {
            String twitterUrlApiKey;
            try {
                twitterUrlApiKey = URLEncoder.encode(KEY, "UTF-8");
                String twitterUrlApiSecret = URLEncoder.encode(CLIENT_SECRET, "UTF-8");
                String twitterKeySecret = twitterUrlApiKey + ":" + twitterUrlApiSecret;
                String twitterKeyBase64 = Base64.encodeToString(twitterKeySecret.getBytes(), Base64.NO_WRAP);
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + twitterKeyBase64);
                headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                ApiRequest.getJSONResponse(getContext(), TWITTER_TOKEN_URL, this, ApiRequest.MethodTypes.POST,
                        TWITTER_TOKEN_TAG + "-" + account + "-" + page, null, headers, "grant_type=client_credentials");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            onResult(accessToken, 200, TWITTER_TOKEN_TAG + "-" + account + "-" + page);
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        if (adapter == null) {
            adapter = new Adapter();
        }
        return adapter;
    }

    @Override
    public void onResult(String response, int httpCode, String tag) {
        try {
            if (getActivity() != null && isVisible()) {
                JSONArray feed = new JSONArray(response);
                ((Adapter) getAdapter()).addData(feed);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject response, int httpCode, String tag) {
        if (httpCode == HttpURLConnection.HTTP_OK && getContext() != null) {
            showFeeds(response, tag);
        }
        stopRefreshing();
    }

    private void showFeeds(JSONObject tokenResponse, String page) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + tokenResponse.optString("access_token"));
        headers.put("Content-Type", "application/json");
        String[] account = page.split("-");
        int pageNumber = Integer.parseInt(account[2]);
        ApiRequest.getStringResponse(getContext(), TWITTER_STREAM_URL + account[1] + "&page=" + pageNumber,
                this, ApiRequest.MethodTypes.GET, TWITTER_FEED_TAG, null, headers, null);
        if (pageNumber < 3) {
            getAuthToken(account[1], pageNumber + 1);
        }
    }

    @Override
    public void onError(String request, int httpCode, String tag) {
        if (httpCode == 401) {
            accessToken = null;
        }
    }

    private static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements View.OnClickListener {
        JSONArray data;

        public Adapter() {
            data = new JSONArray();
        }

        public void addData(JSONArray data) {
            for (int i = 0; i < data.length(); i++) {
                try {
                    this.data.put(data.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.twitter_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                JSONObject data = this.data.getJSONObject(position);

                JSONObject user = data.getJSONObject("user");

                ImageUtil.setImage(holder.imageView.getContext(), user.optString("profile_image_url")
                        , holder.imageView, holder.itemView.findViewById(R.id.progress), 0);
                holder.date.setText(DateUtil.dateToString(data.getString("created_at")));
                holder.detail.setText(data.optString("text"));
                holder.userName.setText("@" + user.optString("name"));
                holder.itemView.setTag(data);
                holder.itemView.setOnClickListener(this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return data.length();
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(v.getContext(), TwitterDetailActivity.class);
            intent.putExtra(Constants.TWEET,v.getTag().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView date, detail, userName;
            public ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.date);
                detail = (TextView) itemView.findViewById(R.id.details);
                imageView = (ImageView) itemView.findViewById(R.id.img_view);
                userName = (TextView) itemView.findViewById(R.id.details_1);
            }
        }
    }
}
