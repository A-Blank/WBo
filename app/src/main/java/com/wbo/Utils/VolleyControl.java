package com.wbo.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by 丶 on 2017/3/21.
 */

public class VolleyControl {

    //私有化属性
    private static VolleyControl volleyControl;
    private RequestQueue requestQueue;
    private static Context context;
    //私有化构造
    private VolleyControl(){
        this.context=MyApplication.getContext();
        requestQueue=getRequestQueue();
    }
    //提供获得请求队列的方法
    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
    //提供获取类对象的方法
    public static synchronized VolleyControl getInstance(){   //synchronized加锁防止并发
        if(volleyControl==null){
            volleyControl=new VolleyControl();
        }
        return  volleyControl;
    }
    public <T> void  addToRequestQueue(Request<T> req){
//        requestQueue.add(req);  //防止被回收造成空指针异常，所以一般不用
        getRequestQueue().add(req);
    }

}
