package com.wbo.Activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wbo.Adapter.TopicListAdapter;
import com.wbo.Adapter.TopicPictureListAdapter;
import com.wbo.Bean.TopicData;
import com.wbo.Interface.impl.HttpCallback;
import com.wbo.Interface.onClickListenerCallBack;
import com.wbo.MyView.MyGridView;
import com.wbo.R;
import com.wbo.Utils.HttpUtil;
import com.wbo.Utils.RequestUrl;
import com.wbo.Utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 丶 on 2017/3/21.
 */

public class TopicActivity extends AppCompatActivity implements onClickListenerCallBack,PictureActivity.CallBack {

    private Toolbar toolbar;
    private RelativeLayout relativeLayout;
    private EditText editText;
    private Button button;

    private LayoutInflater inflater;

    private ListView listView;
    private TopicListAdapter adapter;
    private List<TopicData> datas;

    private GridView gridView;

    private static int page = 1;

    private String containerid;
    private List<String> gridDataList;

    private static Activity activity=new TopicActivity();
    private static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        containerid = getIntent().getExtras().getString("containerid");
        page=1;
        Init();
    }

    public static Activity getActivity(){
        return activity;
    }

    public void Init() {


        setExitSharedElementCallback(new android.app.SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);

                sharedElements.put("sharedView", gridView.getChildAt(position).findViewById(R.id.img_gridview));
            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_toolbar);
        relativeLayout.setVisibility(View.VISIBLE);
        inflater = LayoutInflater.from(this);
        editText = new EditText(this);
        editText.setText(containerid);
        editText.setSelection(containerid.length());
        editText.setSingleLine(true);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId)
                {
                    case EditorInfo.IME_ACTION_SEARCH:
                        page=1;
                        containerid=v.getText().toString();
                        getDatas();
                        break;
                }
                return false;
            }
        });
        relativeLayout.addView(editText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listView = (ListView) findViewById(R.id.listview);
        View view = LayoutInflater.from(this).inflate(R.layout.loading, null);
        listView.addFooterView(view);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
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

                        String url = RequestUrl.getTrending_Detail_URL() + "&containerid=100103type%3D1%26q%3D" + containerid + "%26t%3D3&page=" + page;
                        HttpUtil.sendJsonObjectRequest(url, new HttpCallback() {
                            @Override
                            public void onFinish(JSONObject jsonObject) {
                                datas.addAll(Utility.handleTopicResponse(jsonObject));
                                adapter.notifyDataSetChanged();
                                page++;
                                Toast.makeText(TopicActivity.this, "数据加载完毕", Toast.LENGTH_SHORT).show();
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


        getDatas();
    }

    public void getDatas() {

        String url = RequestUrl.getTrending_Detail_URL() + "&containerid=100103type%3D1%26q%3D" + containerid + "%26t%3D3&page=" + page;
        page++;
        HttpUtil.sendJsonObjectRequest(url, new HttpCallback() {
            @Override
            public void onFinish(JSONObject jsonObject) {
                datas = Utility.handleTopicResponse(jsonObject);
                adapter = new TopicListAdapter(TopicActivity.this, datas, TopicActivity.this);
                listView.setAdapter(adapter);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void GridViewItemClick(View view, int listviewPos, int gridviewPos) {
        Intent intent = new Intent(this, PictureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("pic_url", (ArrayList<String>) datas.get(listviewPos).getPic_url());
        bundle.putInt("position", gridviewPos);
        bundle.putString("activity", "TopicActivity");
        intent.putExtras(bundle);
        gridView = (GridView) view;
        position=gridviewPos;
        this.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, gridView.getChildAt(gridviewPos).findViewById(R.id.img_gridview), "sharedView").toBundle());

    }

    @Override
    public void pageChanged(int position) {

        this.position = position;
//        Log.i("TAG", "pageChanged "+pos);

    }

}
