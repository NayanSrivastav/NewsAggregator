package datalicious.com.news.utils;

import org.json.JSONObject;

/**
 * Created by nayan on 27/2/16.
 */
public interface ApiCallback {

    public void onResult(String response, int httpCode, String tag);

    public void onResult(JSONObject response, int httpCode, String tag);

    public void onError(String request, int httpCode, String tag);
}
