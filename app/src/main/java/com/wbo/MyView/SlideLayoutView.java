package com.wbo.MyView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.wbo.Activity.PictureActivity;

/**
 * Created by 丶 on 2017/3/29.
 */

/**
 * 滑动结束Activity的布局实现
 */

public class SlideLayoutView extends FrameLayout {

    private float oldX, oldY, X, Y;
    private float downX, downY;
    private float InterceptDownX, InterceptX;
    private float InterceptDownY, InterceptY;
    private ViewGroup viewGroup;
    private OverScroller scroller;
    private ScrollFinishCallback callback;
    private boolean InterceptFlag = false;
    private boolean flag = false;
    private static boolean SlideEnable;

    /**
     * 当前显示图片Index
     */
    private static int position;

    public SlideLayoutView(Context context) {
        super(context);
        Init(context);
    }

    public SlideLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public SlideLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    public static void setCurrentPos(int position){
        SlideLayoutView.position=position;
    }

    public static void setSlideEnable(boolean Enable) {
            SlideEnable = Enable;
    }

    public void Init(Context context) {
        callback = (ScrollFinishCallback) context;
        scroller = new OverScroller(context);
        SlideEnable = true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewGroup = (ViewGroup) this.getParent();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = oldY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                X = event.getRawX();
                Y = event.getRawY();
                if (oldX == 0) {
                    oldX = X;
                }
                if (oldY == 0) {
                    oldY = Y;
                }
                int subX = (int) (X - oldX);
                int subY = (int) (Y - oldY);
                viewGroup.scrollBy(-subX, -subY);
                setAlpha(viewGroup.getScrollY());
                oldX = X;
                oldY = Y;
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(viewGroup.getScrollY() * 3) > viewGroup.getHeight() || Math.abs(viewGroup.getScaleX() * 3) > viewGroup.getWidth()) {
//                    if (viewGroup.getScrollY() < 0) {
//                        scroller.startScroll(0, viewGroup.getScrollY(), 0, -viewGroup.getHeight() + viewGroup.getScrollY(), 1500);
//                    }
//                    if (viewGroup.getScrollY() > 0) {
//                        scroller.startScroll(0, viewGroup.getScrollY(), 0, viewGroup.getHeight() - viewGroup.getScrollY(), 1500);
//                    }
                    callback.onFinish();
                    flag = true;
                } else {
//                    Log.i("TAG", "action up");
                    scroller.startScroll(0, viewGroup.getScrollY(), 0, -viewGroup.getScrollY(), 500);
                    flag = false;
                }
                oldX = oldY = 0;
                invalidate();
                break;
        }

        /**
         * 若返回true继续处理事件
         * 若返回false事件处理完毕
         */
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!SlideEnable) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                InterceptDownX = ev.getRawX();
                InterceptDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                InterceptX = ev.getRawX();
                InterceptY = ev.getRawY();
//                Log.i("TAG", ""+InterceptX);
                int subX = (int) (InterceptX - InterceptDownX);
                int subY = (int) (InterceptY - InterceptDownY);
                if (Math.abs(subY) > Math.abs(subX)) {
                    /**
                     * 由于ACTION_DOWN事件未被捕获
                     * 因此需要在此处初始化oldY的值
                     */
                    oldY = 0;
                    return true;
                } else {
                    break;
                }
            case MotionEvent.ACTION_UP:
                break;
        }

        return false;

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            viewGroup.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            setAlpha(scroller.getCurrY());
            postInvalidate();
        }
        if (flag) {
            callback.onFinish();
        }
    }

    /**
     * 改变背景透明度:
     * setAlpha改变的是整个view的透明度,setBackgroundColor才能改变Layout的背景透明度
     */
    public void setAlpha(float CurrY) {

        int alpha = (int) ((getHeight() - Math.abs(CurrY)) / getHeight() * 255);
        String alphaHex = Integer.toHexString(alpha);
        if (alphaHex.length() != 2) {
            alphaHex = "00";
        }
        viewGroup.setBackgroundColor(Color.parseColor("#" + alphaHex + "000000"));

    }

    public interface ScrollFinishCallback {
        void onFinish();
    }

}
