package com.wbo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wbo.Bean.TopicData;
import com.wbo.Bean.TrendingData;
import com.wbo.R;
import com.wbo.Utils.BitmapCache;
import com.wbo.Utils.VolleyControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 丶 on 2017/3/20.
 */

public class TopicPictureListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDatasList = new ArrayList<String>();
    private LayoutInflater mInflater;

    private VolleyControl volleyControl;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private static boolean IsListViewIDLE = true;

    public TopicPictureListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        volleyControl = VolleyControl.getInstance();
        requestQueue = volleyControl.getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, BitmapCache.getInstace());
//        Log.i("TAG", "TopicPictureListAdapter: ");
    }

    public void setDatas(List<String> datas) {
        this.mDatasList = datas;
    }

    public static void setIsListViewIDLE(boolean IsListViewIDLE) {
        TopicPictureListAdapter.IsListViewIDLE = IsListViewIDLE;
    }

    @Override
    public int getCount() {
        return mDatasList.size();

    }

    @Override
    public Object getItem(int position) {
        return mDatasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_gridview, null);
            viewHolder = new ViewHolder();
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.img_gridview);
            viewHolder.gif= (TextView) convertView.findViewById(R.id.textview_Gif);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        Log.i("TAG", "TopciPictureListAdapter getView position: " + position);
        /**
         * 手动设置图片宽高
         */
//        viewHolder.pic.setImageUrl(mDatasList.get(position), imageLoader);
        String Url=mDatasList.get(position);
        if(Url.substring(Url.length()-3,Url.length()).equals("gif")){
            viewHolder.gif.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.gif.setVisibility(View.INVISIBLE);
        }

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.pic, R.drawable.defult_pictrue, R.drawable.defult_pictrue);
        imageLoader.get(mDatasList.get(position), listener, 100, 100);
        return convertView;
    }

    class ViewHolder {
        private ImageView pic;
        private TextView gif;
    }

}
