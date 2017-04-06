package com.wbo.Utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by 丶 on 2017/3/21.
 */

public class MyApplication extends Application {

    public static RequestQueue queue;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = new Volley().newRequestQueue(getApplicationContext());
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static RequestQueue getRequestQueue() {
        return queue;
    }

}
