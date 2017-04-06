package com.wbo.Utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by 丶 on 2017/3/21.
 */

public class BitmapCache implements ImageLoader.ImageCache {

    private static BitmapCache bitmapCache;
    private LruCache<String, Bitmap> mCache;

    private BitmapCache() {
        int maxSize = (int) Runtime.getRuntime().maxMemory() / 8;
        Log.i("TAG", "BitmapCache: " + maxSize);
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    public static BitmapCache getInstace() {
        if (bitmapCache == null) {
            bitmapCache = new BitmapCache();
        }
        return bitmapCache;
    }

    @Override
    public Bitmap getBitmap(String s) {
//        if (mCache.get(s) != null)
//            Log.i("TAG", "BitmapCache: get" + mCache.get(s).getByteCount());
        return mCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        mCache.put(s, bitmap);
//        Log.i("TAG", "BitmapCache: put" + bitmap.getByteCount());

    }
}
