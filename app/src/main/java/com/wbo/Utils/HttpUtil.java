package com.wbo.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wbo.Interface.HttpCallbackListener;
import com.wbo.Interface.impl.HttpCallback;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 丶 on 2017/3/19.
 */

public class HttpUtil {


//    public static void sendStringRequest(final String address,final HttpCallback listener){
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url=new URL(address);
//                    HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
//                    httpURLConnection.setRequestMethod("GET");
//                    httpURLConnection.setConnectTimeout(5000);
//                    httpURLConnection.setReadTimeout(5000);
//                    InputStream in=httpURLConnection.getInputStream();
//                    BufferedReader br=new BufferedReader(new InputStreamReader(in));
//                    StringBuilder builder=new StringBuilder();
//                    String str;
//                    if(httpURLConnection.getResponseCode()==200){
//                        while((str=br.readLine())!=null){
//                            builder.append(str);
//                        }
//                        listener.onFinish(builder.toString());
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }

    public static void sendStringRequest(final String address,final HttpCallback listener){



    }

    public static void sendJsonObjectRequest(final String address,final HttpCallback listener){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(address, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.i("TAG", "onResponse: ");
                listener.onFinish(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("TAG", "ERROR!");
            }
        });

        VolleyControl volleyControl=VolleyControl.getInstance();
        volleyControl.addToRequestQueue(jsonObjectRequest);

    }

    public static void sendImageRequest(final String address,final int position,final HttpCallback listener){

        ImageRequest imageRequest= new ImageRequest(address, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.i("TAG", "onResponse: ");
                listener.onFinish(bitmap);
            }
        }, 300, 300, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        VolleyControl volleyControl=VolleyControl.getInstance();
        volleyControl.addToRequestQueue(imageRequest);

    }

    public static void getBitmapFromServer(final String address, final HttpCallbackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL(address);
                    HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    InputStream in=httpURLConnection.getInputStream();
                    if(httpURLConnection.getResponseCode()==200){
                        Bitmap bitmap= BitmapFactory.decodeStream(in);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
