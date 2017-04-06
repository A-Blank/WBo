package com.wbo.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.wbo.Adapter.TrendingListAdapter;
import com.wbo.Bean.TrendingData;
import com.wbo.Interface.HttpCallbackListener;
import com.wbo.Interface.impl.HttpCallback;
import com.wbo.R;
import com.wbo.Utils.BitmapCache;
import com.wbo.Utils.HttpUtil;
import com.wbo.Utils.MyApplication;
import com.wbo.Utils.RequestUrl;
import com.wbo.Utils.Utility;
import com.wbo.Utils.VolleyControl;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 丶 on 2017/3/19.
 */

public class TrendingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * ListView列表相关对象
     */
    private ListView listView;
    private TrendingListAdapter mAdapter;
    private List<TrendingData> mDatas;

    private NetworkImageView Img_Title;

    private VolleyControl volleyControl;
    private RequestQueue requestQueue;

    private ImageLoader imageLoader;

    private Toolbar toolbar;

    public static TrendingActivity trendingActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        trendingActivity=this;
        Init();
    }

    public void Init() {

//        imageLoader=new ImageLoader(requestQueue,new BitmapCache());

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("实时热搜榜");
        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(this);

        getDatas();


    }

    public void getDatas() {

        HttpUtil.sendJsonObjectRequest(RequestUrl.getTrending_URL(),new HttpCallback(){
            @Override
            public void onFinish(JSONObject jsonObject) {
                mDatas = Utility.handleTrendingResponse(jsonObject);
                mAdapter= new TrendingListAdapter(TrendingActivity.this, mDatas);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(mAdapter);
                    }
                });
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String containerid=mDatas.get(position).getDescribe();
        Intent intent=new Intent(this,TopicActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("containerid",containerid);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
