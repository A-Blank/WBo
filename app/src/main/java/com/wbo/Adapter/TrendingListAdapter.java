package com.wbo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.wbo.Bean.TrendingData;
import com.wbo.R;
import com.wbo.Utils.BitmapCache;
import com.wbo.Utils.VolleyControl;

import java.util.List;

/**
 * Created by 丶 on 2017/3/20.
 */

public class TrendingListAdapter extends BaseAdapter {

    private Context mContext;
    private List<TrendingData> mDatasList;
    private LayoutInflater mInflater;

    private VolleyControl volleyControl;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public TrendingListAdapter(Context context, List<TrendingData> datas) {
        mContext = context;
        mDatasList = datas;
        mInflater=LayoutInflater.from(context);
        volleyControl=VolleyControl.getInstance();
        requestQueue=volleyControl.getRequestQueue();
        imageLoader=new ImageLoader(requestQueue,BitmapCache.getInstace());
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
        if(convertView==null){
            convertView= mInflater.inflate(R.layout.item_activity_trending,null);
            viewHolder=new ViewHolder();
            viewHolder.pic= (NetworkImageView) convertView.findViewById(R.id.img_pic);
            viewHolder.desc= (TextView) convertView.findViewById(R.id.textview_desc);
            viewHolder.desc_extr= (TextView) convertView.findViewById(R.id.textview_desc_extra);
            viewHolder.icon= (NetworkImageView) convertView.findViewById(R.id.img_icon);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.pic.setImageUrl(mDatasList.get(position).getPicture_Url(),imageLoader);
        viewHolder.desc.setText(mDatasList.get(position).getDescribe());
        viewHolder.desc_extr.setText(mDatasList.get(position).getDescribe_Extra());
        viewHolder.icon.setImageUrl(mDatasList.get(position).getIcon_Url(),imageLoader);

        return convertView;
    }

    class ViewHolder {
        private NetworkImageView pic;
        private TextView desc;
        private TextView desc_extr;
        private NetworkImageView icon;
    }

}
