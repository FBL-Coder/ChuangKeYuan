package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.MyItmeAdapter_build;
import com.yitong.ChuangKeYuan.utils.AsyncLoaderData_List;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/30.
 * 做中学互动页面
 */
public class DoSchoolActivity extends Activity implements OnClickListener {

    private ListView mListView;
    public TextView mDes;
    public ImageView mSearch, mBack, mUp;
    private MyItmeAdapter_build build;
    private FormBody body;
    private int count = 0;
    private ProgressBar mProgressBar;
    private ScrollView scrollView;
    private SuperSwipeRefreshLayout layout;
    private View headerView;
    private LinearLayout ku_xue_yichang, ku_xue_nodata;
    private AsyncLoaderData_List list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xue);
        //初始化标题栏
        initTitlebar();
        //初始化ListView
        initListView();
    }

    @Override
    protected void onRestart() {
        if (MyApplication.getFLAG() == 1010) {
            //初始化ListView
            initListView();
            MyApplication.setFLAG(0);
        }
        super.onRestart();
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        mListView = (ListView) findViewById(R.id.lv_study);
        ku_xue_yichang = (LinearLayout) findViewById(R.id.ku_xue_yichang);
        ku_xue_nodata = (LinearLayout) findViewById(R.id.ku_xue_nodata);
        String ID = "45";
        try {
            ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(3).getTerm_id();
        }catch (Exception e){}

        body = new FormBody.Builder()
                .add("termid",ID )
                .add("pagesize", 10000 + "").build();

        list = new AsyncLoaderData_List(this, build, ku_xue_nodata, ku_xue_yichang, mListView, body, "Data_Study", count, true, layout);
        list.execute(DatasUtil.URL_BASE + DatasUtil.URL_article);
        count += 5;
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        //标题栏
        mDes = (TextView) findViewById(R.id.tv_teach_title);
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mUp = (ImageView) findViewById(R.id.iv_teach_up);
        try {
            mDes.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(3).getName());
        } catch (Exception e) {
            mDes.setText("典型的DIY过程分解");
        }
        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
        //身份识别
        if (MyApplication.getuserType() != null && ("3".equals(MyApplication.getuserType())
                || "1".equals(MyApplication.getuserType()))) {
            mUp.setVisibility(View.VISIBLE);
            mUp.setOnClickListener(this);
        }

        //自定义进度条；
        headerView = LayoutInflater.from(this).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, this);


        scrollView = (ScrollView) findViewById(R.id.scrollview_study_item);
        scrollView.smoothScrollTo(0, 20);

        layout = (SuperSwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout_study);
        layout.setHeaderView(headerView);
        layout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

            @Override
            public void onRefresh() {
                //TODO 开始刷新
                count = 0;
                initListView();
                count++;
            }

            @Override
            public void onPullDistance(int distance) {
                //TODO 下拉距离
            }

            @Override
            public void onPullEnable(boolean enable) {
                //TODO 下拉过程中，下拉的距离是否足够出发刷新
            }
        });
        layout.setTargetScrollWithLayout(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                if (list != null) {
                    list.cancel(true);
                }
                finish();
                break;
            case R.id.iv_teach_up:
                startActivity(new Intent(DoSchoolActivity.this, UploadActivity.class).putExtra("id", getIntent().getStringExtra("id")));
                break;
        }
    }
}
