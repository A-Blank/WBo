package com.wbo.Interface.impl;

import android.graphics.Bitmap;

import com.wbo.Bean.TrendingData;
import com.wbo.Interface.HttpCallbackListener;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by 丶 on 2017/3/20.
 */

public class HttpCallback implements HttpCallbackListener{
    @Override
    public void onFinish(String Response) {

    }

    @Override
    public void onFinish(List<TrendingData> trendingDataList) {

    }

    @Override
    public void onFinish(JSONObject jsonObject) {

    }

    @Override
    public void onError(Exception e) {

    }
}
