package com.wbo.Activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wbo.Adapter.PictureViewPagerAdapter;
import com.wbo.MyView.MyImageView;
import com.wbo.MyView.SlideLayoutView;
import com.wbo.R;
import com.wbo.Utils.BitmapCache;
import com.wbo.Utils.VolleyControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 丶 on 2017/3/24.
 */

public class PictureActivity extends Activity implements ViewPager.OnPageChangeListener, SlideLayoutView.ScrollFinishCallback, MyImageView.CallBack {

    private ViewPager viewPager;
    private PictureViewPagerAdapter adapter;
    private List<MyImageView> viewList = new ArrayList<MyImageView>();

    private TextView textView_index;

    private List<String> list;
    private ImageLoader imageLoader;

    private RequestQueue requestQueue;
    private VolleyControl volleyControl;
    private int position;

    private Transition transition;

    private Activity activity;
    private String activityName;

    private CallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        list = getIntent().getExtras().getStringArrayList("pic_url");
        position = getIntent().getExtras().getInt("position");
        activityName = getIntent().getExtras().getString("activity");
        Init();
    }

    public void Init() {

        if (activityName.equals("MainActivity")) {
            activity = MainActivity.getActivity();
            callBack = (CallBack) activity;
        } else if (activityName.equals("TopicActivity")) {
            activity = TopicActivity.getActivity();
            callBack = (CallBack) activity;
        }

        transition = getWindow().getSharedElementExitTransition();

        //设置顶级布局无状态栏
        this.getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
        volleyControl = VolleyControl.getInstance();
        requestQueue = volleyControl.getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, BitmapCache.getInstace());

        for (final String str :
                list) {
            final MyImageView imageView = new MyImageView(this);
            imageView.setPosition(viewList.size());
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    int width = imageView.getWidth();
                    int height = imageView.getHeight();
                    imageView.setWidth(width);
                    imageView.setHeight(height);
                    imageView.InitBitmap(str);
                }
            });
            viewList.add(imageView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new PictureViewPagerAdapter(this, viewList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(this);
        textView_index = (TextView) findViewById(R.id.textview_index);
        textView_index.setText(position + 1 + "/" + viewList.size());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        textView_index.setText(position + 1 + "/" + viewList.size());
        SlideLayoutView.setCurrentPos(position);
        boolean flag = viewList.get(position).IsLargePicture();
        SlideLayoutView.setSlideEnable(!flag);

        this.position = position;
        callBack.pageChanged(position);
//        int betyCounts=((BitmapDrawable)((NetworkImageView)viewList.get(position)).getDrawable()).getBitmap().getByteCount();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onFinish() {
        finishAfterTransition();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//             finish();
//                overridePendingTransition(0, 0);
//            }
//        }, 666);
    }

    @Override
    public void onClick() {
        finishAfterTransition();
    }

    public interface CallBack {
        void pageChanged(int position);
    }

}
