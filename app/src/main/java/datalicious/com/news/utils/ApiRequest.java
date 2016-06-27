package datalicious.com.news.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nayan on 27/2/16.
 */
public class ApiRequest {

    private ProgressDialog progressDialog;

    public static final String TAG_NETWORK_EXCEPTION = "tag_network_exception";

    public  static void getStringResponse(final Context context, String url, final ApiCallback callback, int methodType,final String tag) {
        if (validateMethod(methodType)) {
            StringRequest stringRequest = new StringRequest(methodType, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callback.onResult(response, 200,tag);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(error.getLocalizedMessage(), error.networkResponse.statusCode, tag);
                    error.printStackTrace();
                }
            });
            Communicator.getInstance(context).addToRequestQueue(context, stringRequest);
        } else {
            callback.onResult("Method type not supported", 400,tag);
        }
    }

    public static void getJSONResponse(final Context context, String url, final ApiCallback callback, int methodType,final String tag) {
        if (validateMethod(methodType)) {

            JsonObjectRequest stringRequest = new JsonObjectRequest(methodType, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            int statusCode = 200;
                            try {
                                if (response != null && response.has("statusCode")) {
                                    statusCode = response.getInt("statusCode");
                                }
                            } catch (JSONException e) {
                                Log.e(TAG_NETWORK_EXCEPTION, e.getLocalizedMessage());
                            }

                            callback.onResult(response, statusCode, tag);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    callback.onError(error.getLocalizedMessage(), error.networkResponse.statusCode, tag);
                    error.printStackTrace();
                }
            });
            Communicator.getInstance(context).addToRequestQueue(context, stringRequest);
        } else {
            callback.onResult("Method type not supported", 400, tag);
        }
    }


    private static boolean validateMethod(int method) {
        boolean result = true;
        switch (method) {
            case MethodTypes.GET:
                break;
            case MethodTypes.POST:
                break;
            case MethodTypes.PUT:
                break;
            case MethodTypes.DELETE:
                break;
            case MethodTypes.HEAD:
                break;
            case MethodTypes.OPTIONS:
                break;
            case MethodTypes.TRACE:
                break;
            case MethodTypes.PATCH:
                break;
            default:
                result = false;
        }
        return result;
    }

    public static interface MethodTypes extends StringRequest.Method {

    }
}
