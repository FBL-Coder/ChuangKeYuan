package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.DaoShiList;
import com.yitong.ChuangKeYuan.utils.AsyncParentsData_List;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import java.util.List;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/23.
 * 创客家长介绍页面
 */
public class ParentsActivity extends Activity implements OnClickListener {


    private ListView mListView;
    private ImageView mBack, mUp;
    private ImageView mSearch, sdv_teacherlist;
    private TextView mTitle;
    private FormBody body;
    /**
     * listView 异步下载数据以及适配器；
     */
    private AsyncParentsData_List.MyTeacherAdapter_build build;
    private ProgressBar mProgressBar, progressBar;
    private ScrollView scrollView;
    /**
     * android 自带刷新组件
     */
    private SuperSwipeRefreshLayout layout;
    /**
     * 自定义刷新组件
     */
    private View headerView;
    /**
     * 数据源
     */
    private List<DaoShiList.ListBean> resultList_more = null;
    private int count = 0;
    /**
     * 数据源
     */
    private AsyncParentsData_List list;
    private RelativeLayout rl_detail_pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        //初始化ListView
        initView();
        //初始化标题栏
        initTitlebar();
        //初始化数据
        initData();
    }

    @Override
    protected void onRestart() {
        if (MyApplication.getFLAG() == 1010){
            //初始化数据
            initData();
            MyApplication.setFLAG(0);
        }
        super.onRestart();
    }

    /**
     * 初始化ListView
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_teacher);
        scrollView = (ScrollView) findViewById(R.id.scrollview_teacher);

        rl_detail_pro = (RelativeLayout) findViewById(R.id.rl_detail_pro);
        progressBar = (ProgressBar) findViewById(R.id.pb_detail_pro);
        Progressbar_Util.ProVisibility(progressBar, this);

        //自定义进度条；
        headerView = LayoutInflater.from(this).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, this);
        scrollView.smoothScrollTo(0, 20);

        //刷新组件初始化以及点击事件
        layout = (SuperSwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout_teacher);
        layout.setHeaderView(headerView);
        layout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

            @Override
            public void onRefresh() {
                //TODO 开始刷新
                count = 0;
                initData();
                count++;
                layout.setRefreshing(false);
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

    /**
     * 初始化数据
     */
    private void initData() {

        body = new FormBody.Builder()
                .add("termid", "9")
                .add("pagesize", 10000 + "")
                .build();
        list = new AsyncParentsData_List(this,
                build, mListView, rl_detail_pro,layout, body, null, count);

        list.execute(DatasUtil.URL_BASE + DatasUtil.URL_parentsList);
        count += 5;
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        //标题栏
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mUp = (ImageView) findViewById(R.id.iv_teach_up);
        sdv_teacherlist = (ImageView) findViewById(R.id.sdv_teacherlist);
        try {
            mTitle.setText(MyApplication.getUtil().mListBeen.get(1).getParent().get(1).getName());
        }catch (Exception e){
            mTitle.setText("家长列表");
        }
        try {
            sdv_teacherlist.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(1).getParent().get(1).getBanner()));
        }catch (Exception e){
            sdv_teacherlist.setImageResource(R.drawable.noimage);
        }
        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
        //身份识别
        if (MyApplication.getuserType() != null && ("3".equals(MyApplication.getuserType())
                || "1".equals(MyApplication.getuserType()))) {
            mUp.setVisibility(View.VISIBLE);
            mUp.setOnClickListener(this);
        }
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
                startActivity(new Intent(ParentsActivity.this, UploadParentsActivity.class));
                break;
        }
    }
}
