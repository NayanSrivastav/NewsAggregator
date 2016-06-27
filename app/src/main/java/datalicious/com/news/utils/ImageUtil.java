package datalicious.com.news.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by nayan on 24/2/16.
 */
public class ImageUtil {

    public static void setImage(Context context, String url, final ImageView imageView) {
        setImage(context, url, imageView, 0);
    }

    public  static void setImage(Context context, String url, final ImageView imageView, int errorResource) {
      setImage(context, url,imageView, null, errorResource);
    }

    public static void setImage(Context context, String url, final ImageView imageView,View view, int errorResource)
    {
        Communicator.getInstance(context).getImageLoader(context).get(url, new ImageRequest(imageView, errorResource));
    }

    public static class ImageRequest implements ImageLoader.ImageListener {
        ImageView mImageView;
        int errorResource;
        View tempView;

        /**
         * @param view
         */
        public ImageRequest(ImageView view) {
            this(view, null, 0);
        }

        /**
         * @param imageView
         * @param errorResource
         */
        public ImageRequest(ImageView imageView, int errorResource) {
            this(imageView, null, errorResource);
        }

        /**
         * @param imageView
         * @param tempView
         * @param errorResource
         */
        public ImageRequest(ImageView imageView, View tempView, int errorResource) {
            this.tempView = tempView;
            this.mImageView = imageView;
            this.errorResource = errorResource;
        }

        public ImageRequest(ImageView imageView, View tempView) {
            this(imageView, tempView, 0);
        }

        /**
         * Listens for non-error changes to the loading of the image request.
         *
         * @param response    Holds all information pertaining to the request, as well
         *                    as the bitmap (if it is loaded).
         * @param isImmediate True if this was called during ImageLoader.get() variants.
         *                    This can be used to differentiate between a cached image loading and a network
         *                    image loading in order to, for example, run an animation to fade in network loaded
         */
        @Override
        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
            if (response.getBitmap() != null) {
                mImageView.setImageBitmap(response.getBitmap());
            }
        }

        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         *
         * @param error
         */
        @Override
        public void onErrorResponse(VolleyError error) {
            if (errorResource != 0) {
                mImageView.setImageDrawable(mImageView.getContext().getResources().getDrawable(errorResource));
            }
        }
    }
}
