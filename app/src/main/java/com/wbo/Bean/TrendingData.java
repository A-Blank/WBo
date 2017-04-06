package com.wbo.Bean;

import android.graphics.Bitmap;

/**
 * Created by 丶 on 2017/3/20.
 */

public class TrendingData {

    private String Title_Url;
    private String picture_Url;
    private String Describe;
    private String Describe_Extra;
    private String Icon_Url;

    public String getTitle_Url() {
        return Title_Url;
    }

    public void setTitle_Url(String title_Url) {
        Title_Url = title_Url;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public String getPicture_Url() {
        return picture_Url;
    }

    public void setPicture_Url(String picture_Url) {
        this.picture_Url = picture_Url;
    }

    public String getIcon_Url() {
        return Icon_Url;
    }

    public void setIcon_Url(String icon_Url) {
        Icon_Url = icon_Url;
    }

    public String getDescribe_Extra() {
        return Describe_Extra;
    }

    public void setDescribe_Extra(String describe_Extra) {
        Describe_Extra = describe_Extra;
    }
}
