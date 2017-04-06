package com.wbo.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wbo.MyView.MyImageView;

import java.util.List;

/**
 * Created by 丶 on 2017/3/24.
 */

public class PictureViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<MyImageView> viewList;

    public PictureViewPagerAdapter(Context context, List<MyImageView> viewList){
        this.context=context;
        this.viewList=viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(viewList.get(position));

    }
}
