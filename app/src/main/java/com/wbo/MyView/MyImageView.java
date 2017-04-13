package com.wbo.MyView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 丶 on 2017/3/31.
 */

public class MyImageView extends ImageView {

    private BitmapFactory.Options options;
    private Bitmap bitmap;
    private BitmapRegionDecoder bitmapRegionDecoder;
    private int width, height, viewWidth, viewHeight;
    private int left, top, right, bottom;
    private float downX, downY;
    private float X, Y;
    private float oldX, oldY;
    private Rect rect = new Rect();
    private boolean flag;
    private CallBack callBack;
    private boolean isMoved;

    private float mScale;

    /**
     * 播放GIF动画的关键类
     */
    private Movie mMovie;

    /**
     * 记录动画开始的时间
     */
    private long mMovieStart;

    /**
     * GIF图片的宽度
     */
    private int mImageWidth;

    /**
     * GIF图片的高度
     */
    private int mImageHeight;

    /**
     * 图片是否正在播放
     */
    private boolean isPlaying = true;

    private int position;

    public MyImageView(Context context) {
        super(context);
        callBack = (CallBack) context;
        Init();
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    public void Init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public boolean IsLargePicture() {
        return flag;
    }

    public void setWidth(int width) {
        this.viewWidth = width;
    }

    public void setHeight(int height) {
        this.viewHeight = height;
    }

    public void setPosition(int position){
        this.position=position;
    }

    public void InitBitmap(final String Url) {

        this.setScaleType(ScaleType.FIT_CENTER);
        options = new BitmapFactory.Options();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL(Url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    InputStream in = conn.getInputStream();
                    byte[] bytes = null;
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        bytes = getbytes(in);
                    }

                    //使用Movie类对输入流解码
                    mMovie = Movie.decodeByteArray(bytes, 0, bytes.length);


                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                    width = options.outWidth;
                    height = options.outHeight;

                    mScale = (float) (1.0 * viewWidth / width);

//                    Log.i("TAG", "Scale=" + mScale);
                    in.close();
                    conn.disconnect();

                    float inSampleSize_1 = (float) (1.0 * viewHeight / viewWidth);
                    float inSampleSize_2 = (float) (1.0 * height / width);
                    float inSampleSize_3 = height / viewHeight < width / viewWidth ? height / viewHeight : width / viewWidth;
                    options.inSampleSize = (int) inSampleSize_3;
                    Log.i("TAG", "inSampleSize=" + height / viewHeight+" "+ width / viewWidth);
                    options.inJustDecodeBounds=false;
                    conn = (HttpURLConnection) url.openConnection();
                    in = conn.getInputStream();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        bitmapRegionDecoder = BitmapRegionDecoder.newInstance(in, false);
                    }
                    in.close();
                    conn.disconnect();
                    rect.left = rect.top = 0;
                    rect.right = width;
                    if (inSampleSize_1 < inSampleSize_2) {
//                        Log.i("TAG", "SlideEnable=true" );
                        flag = true;
                        rect.bottom = (int) (width * inSampleSize_1);
                    } else {
//                        Log.i("TAG", "SlideEnable=false" );
                        flag=false;
                        rect.bottom = height;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                post(new Runnable() {
                    @Override
                    public void run() {
                        setBitmap();
                    }
                });
            }
        }).start();
    }

    public byte[] getbytes(InputStream in) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        byte[] bytes = new byte[1024];
        int length;
        try {
            while ((length = in.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        viewWidth = getWidth();
//        viewHeight = getHeight();
//        if (mMovie != null) {
//            setMeasuredDimension(viewWidth, viewHeight);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.i("TAG", "onDraw" + " " + isHardwareAccelerated());
//        Log.i("TAG", "SlideEnable="+this.hashCode() );
        SlideLayoutView.setSlideEnable(!flag);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
        if (mMovie == null) {
            super.onDraw(canvas);
//            canvas.drawBitmap(bitmap, null, paint);
        } else {
            this.setImageBitmap(null);

            // 正在播放就继续调用playMovie()方法，一直到动画播放结束为止
            canvas.scale(mScale, mScale);
            if (playMovie(canvas)) {
                isPlaying = false;
            } else {
            }
            invalidate();
        }
//        Log.i("TAG", "onDraw");
    }

    public boolean playMovie(Canvas canvas) {

        Log.i("TAG", "playMovie" + viewHeight + " " + mScale + " " + mMovie.height() + " " + (viewHeight / 2 - mMovie.height() * (float) (1.0 / mScale) / 2));
        long now = SystemClock.uptimeMillis();
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        int duration = mMovie.duration();
        if (duration == 0) {
            duration = 1000;
        }
        int relTime = (int) ((now - mMovieStart) % duration);
        mMovie.setTime(relTime);
        mMovie.draw(canvas, 0, (viewHeight / 2 - height * mScale / 2) / mScale);
        if ((now - mMovieStart) >= duration) {
            mMovieStart = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 这句加了才能同时响应onClickListener
         */
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = downX = event.getRawX();
                oldY = downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                X = event.getRawX();
                Y = event.getRawY();
                float subX = X - oldX;
                float subY = Y - oldY;
//                if (rect.left - subX < 0) {
//                    rect.left = 0;
//                    rect.right = width;
//                } else if (right - subX > width) {
//                    rect.left += width - rect.right;
//                    rect.right = width;
//                } else {
//                    rect.left -= subX;
//                    rect.right -= subX;
//                }

                if (rect.top - subY < 0) {
                    rect.bottom -= rect.top;
                    rect.top = 0;
                } else if (rect.bottom - subY > height) {
                    rect.top += height - rect.bottom;
                    rect.bottom = height;
                } else {
                    rect.top -= subY;
                    rect.bottom -= subY;
                }
                setBitmap();
                oldX = X;
                oldY = Y;
                isMoved = true;
                break;
            case MotionEvent.ACTION_UP:
                oldX = oldY = 0;
                if (!isMoved) {
                    callBack.onClick();
                }
                isMoved = false;
                break;
        }
        return true;
    }

    public void setBitmap() {
        if (mMovie == null) {
            if (bitmapRegionDecoder != null) {
                Log.i("TAG", "option size=" + options.inSampleSize);
                bitmap = bitmapRegionDecoder.decodeRegion(rect, options);
                this.setImageBitmap(bitmap);
            }
        } else {
            invalidate();
        }
    }

    public interface CallBack {
        void onClick();
    }

}
