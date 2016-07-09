package screen;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;

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

import datalicious.com.news.utils.ApiCallback;
import datalicious.com.news.utils.ApiRequest;
import datalicious.com.news.utils.ConnectionUtils;

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

    static {
        ACCOUNTS = new ArrayList<>(10);
        ACCOUNTS.add("datalicious");
        ACCOUNTS.add("supertag");
        ACCOUNTS.add("cbartens");
    }

    @Override
    protected void setUpFeed() {
        if (ConnectionUtils.isConnected(getContext())) {
            dismissSnakebar();
            getAuthToken();
        } else {
            showSnakebar("No connection available", Snackbar.LENGTH_INDEFINITE);
            stopRefreshing();
        }
    }

    private void getAuthToken() {
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
                    TWITTER_TOKEN_TAG, null, headers, "grant_type=client_credentials");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    public void onResult(String response, int httpCode, String tag) {
        try {
            JSONArray feed=new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject response, int httpCode, String tag) {
        if (httpCode == HttpURLConnection.HTTP_OK && getContext() != null) {
            switch (tag) {
                case TWITTER_TOKEN_TAG:
                    showFeeds(response, 0);
                    break;
                case TWITTER_FEED_TAG:
                    break;
            }
        }
        stopRefreshing();
    }

    private void showFeeds(JSONObject tokenResponse, int page) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + tokenResponse.optString("access_token"));
        headers.put("Content-Type", "application/json");

        for (String account : ACCOUNTS) {
            ApiRequest.getStringResponse(getContext(), TWITTER_STREAM_URL + account, this, ApiRequest.MethodTypes.GET, TWITTER_FEED_TAG, null, headers, null);
        }
    }

    @Override
    public void onError(String request, int httpCode, String tag) {

    }

}
