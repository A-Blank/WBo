package com.wbo.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;

import com.wbo.Activity.TrendingActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 丶 on 2017/3/26.
 */

public class TextUtil {

    public static SpannableString UrlHandle(String content) {
        final List<String> termList = new ArrayList<String>();
        String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
        Pattern pattern = Pattern.compile(patternString);

        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            termList.add(matcher.group());
        }

        if (termList.size() == 0) {
            return new SpannableString(content);
        }
        SpannableString spannableString = null;
        List<Integer> index = new ArrayList<Integer>();

        for (String str :
                termList) {
            Integer start = content.indexOf(str);
            index.add(start);
            content = content.replace(str, "链接");
        }
        spannableString = new SpannableString(content);
        for (int i = 0; i < termList.size(); i++) {
            int start = index.get(i);
            final int finalI = i;
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View widget) {
//                    Log.i("TAG", "onClick: spanURl");
                    Uri uri = Uri.parse(termList.get(finalI));   //指定网址
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);           //指定Action
                    intent.setData(uri);                            //设置Uri
                    (MyApplication.getContext()).startActivity(intent);
//                    TrendingActivity.trendingActivity.startActivity(intent);
                }
            }, start, start + 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.rgb(0, 0, 200)), start, start + 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

//        spannableString.set;

//        for (final String temp : termList) {

//            spannableString.setSpan(new URLSpan(temp){
//                @Override
//                public void onClick(View widget) {
//                    super.onClick(widget);
//                    Log.i("TAG", "onClick: spanURl");
//                }
////            },0,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

////            Log.i("TAG", "url=" + temp);
//        }

        return spannableString;
    }

    public static SpannableString TopicHandle(SpannableString content) {
        String text= String.valueOf(content);
        final List<String> termList = new ArrayList<String>();
        String patternString = "#(.*?)#";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            termList.add(matcher.group());
        }

        if (termList.size() == 0) {
            return content;
        }

        for (String str :
                termList) {
            Integer start = text.indexOf(str);
            content.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View widget) {
                    Log.i("TAG", "onClick: spanURl");
                }
            }, start, start+str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            content.setSpan(new ForegroundColorSpan(Color.rgb(0, 0, 200)), 0, 0, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return content;
    }

}
