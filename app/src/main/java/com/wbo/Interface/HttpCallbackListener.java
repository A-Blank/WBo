package com.wbo.Interface;

import android.graphics.Bitmap;

import com.wbo.Bean.TrendingData;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by 丶 on 2017/3/19.
 */

public interface HttpCallbackListener {

    void onFinish(String Response);
    void onFinish(List<TrendingData> trendingDataList);
    void onFinish(JSONObject jsonObject);
    void onError(Exception e);


}
