package com.wbo.Activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.wbo.Adapter.TopicListAdapter;
import com.wbo.Adapter.TopicPictureListAdapter;
import com.wbo.Bean.TopicData;
import com.wbo.Interface.impl.HttpCallback;
import com.wbo.Interface.onClickListenerCallBack;
import com.wbo.R;
import com.wbo.Utils.HttpUtil;
import com.wbo.Utils.RequestUrl;
import com.wbo.Utils.Utility;
import com.wbo.Utils.VolleyControl;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements onClickListenerCallBack, PictureActivity.CallBack {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private static GridView gridView;
    private VolleyControl volleyControl;
    private ListView listView;
    private TopicListAdapter adapter;
    private List<TopicData> dataList;

    private static Activity activity = new MainActivity();

    private static int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }

    public static Activity getActivity() {
        return activity;
    }

    public void Init() {


        setExitSharedElementCallback(new android.app.SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
//                Log.i("TAG", "Init " + position);
                sharedElements.put("sharedView", gridView.getChildAt(position).findViewById(R.id.img_gridview));
            }
        });

        getDatas();
        volleyControl = VolleyControl.getInstance();

        listView = (ListView) findViewById(R.id.listview);
        View view = LayoutInflater.from(this).inflate(R.layout.loading, null);
        listView.addFooterView(view);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        TopicListAdapter.setIsListViewIDLE(true);
                        adapter.notifyDataSetChanged();
                        if (view.getLastVisiblePosition() >= view.getCount() - 2) {
                            View childView = view.getChildAt(view.getLastVisiblePosition() - view.getFirstVisiblePosition() - 1);
                            if (childView.getBottom() > view.getHeight()) {
                                break;
                            }
                        } else {
                            break;
                        }
//                        Log.i("TAG", "onFinish: ");
                        HttpUtil.sendJsonObjectRequest(RequestUrl.getHot_Topic_URL(), new HttpCallback() {
                            @Override
                            public void onFinish(JSONObject jsonObject) {
                                if(jsonObject==null){
//                                    view.removeAllViews();
                                    return;
                                }
                                dataList.addAll(Utility.handleHotTopicResponse(jsonObject));
                                adapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "数据加载完毕", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    case SCROLL_STATE_FLING:
                        TopicListAdapter.setIsListViewIDLE(false);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("热门微博");


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                Toast.makeText(MainActivity.this, "666", Toast.LENGTH_LONG).show();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }


    public void getDatas() {

        HttpUtil.sendJsonObjectRequest(RequestUrl.getHot_Topic_URL(), new HttpCallback() {
            @Override
            public void onFinish(JSONObject jsonObject) {
                dataList = Utility.handleHotTopicResponse(jsonObject);
                adapter = new TopicListAdapter(MainActivity.this, dataList, MainActivity.this);
                listView.setAdapter(adapter);
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(this, TrendingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void GridViewItemClick(final View view, int listviewPos, int gridviewPos) {

        gridView = (GridView) view;
        position = gridviewPos;

        Intent intent = new Intent(MainActivity.this, PictureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("pic_url", (ArrayList<String>) dataList.get(listviewPos).getPic_url());
        bundle.putInt("position", gridviewPos);
        bundle.putString("activity", "MainActivity");
        intent.putExtras(bundle);
//        this.startActivity(intent, ActivityOptions.makeScaleUpAnimation(view, 0,0,0,0).toBundle());
        this.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                gridView.getChildAt(gridviewPos).findViewById(R.id.img_gridview), "sharedView").toBundle());
    }

    @Override
    public void pageChanged(int position) {

        this.position = position;
        Log.i("TAG", "pageChanged " + position);
    }
}
