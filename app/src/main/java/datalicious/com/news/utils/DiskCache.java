package datalicious.com.news.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Class mainly used with communicator to cache images for further usage
 * Created by nayan on 21/2/16.
 */
public class DiskCache extends LruCache<String, Bitmap> implements
        ImageCache {
    public static int getDeafultCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
 
        return cacheSize;
    }
 
    public DiskCache() {
        this(getDeafultCacheSize());
    }
 
    public DiskCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }
 
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }
 
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }
 
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}