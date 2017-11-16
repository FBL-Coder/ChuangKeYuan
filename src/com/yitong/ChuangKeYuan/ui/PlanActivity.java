package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
 * Created by Say GoBay on 2016/5/23.
 * 幼儿园创客教策划方案
 */
public class PlanActivity extends Activity implements OnClickListener {

    private ListView mListView;
    private ImageView refresh, mUp;
    private ImageView mBack, mTeach;
    private TextView mTeachTitle;
    /**
     * ListView适配器
     */
    private MyItmeAdapter_build build;
    private FormBody body;
    private int count = 0;
    private ProgressBar mProgressBar;
    private ScrollView scrollView;
    /**
     * android 自带刷新 可下拉组件
     */
    private SuperSwipeRefreshLayout layout;
    /**
     * 自定义进度条
     */
    private View headerView;
    private LinearLayout ku_build_yichang, ku_build_nodata;
    private AsyncLoaderData_List list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_item);
        //初始化标题栏
        initTitlebar();
        //初始化ListView
        initListView();
        //初始化进度条
    }

    @Override
    protected void onRestart() {
        if (MyApplication.getFLAG() == 1010) {//判断刷新标记，刷新页面
            //初始化ListView
            initListView();
            MyApplication.setFLAG(0);//判断后置0
        }
        super.onRestart();
    }

    /**
     * 初始化ListView
     */
    private void initListView() {

        mListView = (ListView) findViewById(R.id.lv_teach);
        ku_build_yichang = (LinearLayout) findViewById(R.id.ku_build_yichang);
        ku_build_nodata = (LinearLayout) findViewById(R.id.ku_build_nodata);
        int ID = 5;
        try {
            ID = Integer.parseInt(MyApplication.getUtil().mListBeen.get(0).getParent().get(1).getTerm_id());
        } catch (Exception e) {
        }

        body = new FormBody.Builder()
                .add("termid", ID+"")
                .add("pagesize", 10000 + "")//list数据默认返回10条，这里没有写页面翻页，所以给个10000，表示加载所有数据
                .build();

        list = new AsyncLoaderData_List(this, build
                , ku_build_nodata, ku_build_yichang
                , mListView, body, "Data_build_2", count, false, ID, layout);//实例化异步线程
        list.execute(DatasUtil.URL_BASE + DatasUtil.URL_article);
        count += 5;

    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        //标题栏
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        refresh = (ImageView) findViewById(R.id.iv_teach_search);

        mUp = (ImageView) findViewById(R.id.iv_teach_up);

        mTeach = (ImageView) findViewById(R.id.iv_teach);
        mTeachTitle = (TextView) findViewById(R.id.tv_teach_title);

        try {
            mTeachTitle.setText(MyApplication.getUtil().mListBeen.get(0).getParent().get(1).getName());
        }catch (Exception e){
            mTeachTitle.setText("班级项目策划方案");
        }
        try {
            mTeach.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(0).getParent().get(1).getBanner()));
        }catch (Exception e){
            mTeach.setImageResource(R.drawable.noimage);
        }
        //身份识别
        if (MyApplication.getuserType() != null && "1".equals(MyApplication.getuserType())) {
            mUp.setVisibility(View.VISIBLE);
            mUp.setOnClickListener(this);
        }

        refresh.setVisibility(View.GONE);
        //自定义进度条；
        headerView = LayoutInflater.from(this).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, this);

        mBack.setOnClickListener(this);

        scrollView = (ScrollView) findViewById(R.id.scrollview_item);
        scrollView.smoothScrollTo(0, 20);

        //刷新组件初始化 以及 点击事件
        layout = (SuperSwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
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
                startActivity(new Intent(PlanActivity.this, UploadActivity.class).putExtra("id", "5"));
                //跳转以及携带参数

                break;
        }
    }
}

