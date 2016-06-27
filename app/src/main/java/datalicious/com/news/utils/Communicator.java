package datalicious.com.news.utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * This class should be used to make network calls for data and images.
 * Created by nayan on 21/2/16.
 */
public class Communicator {

    private static Communicator mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static final String TAG=Communicator.class.getSimpleName();

    private Communicator(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new DiskCache());
    }

    public static synchronized Communicator getInstance(Context context) {
        if(mInstance ==null)
        {
            mInstance=new Communicator(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader(Context context) {

        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(getRequestQueue(context),
                    new DiskCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Context context,Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(context).add(req);
    }

    public <T> void addToRequestQueue(Context context,Request<T> req) {
        req.setTag(TAG);
        getRequestQueue(context).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
