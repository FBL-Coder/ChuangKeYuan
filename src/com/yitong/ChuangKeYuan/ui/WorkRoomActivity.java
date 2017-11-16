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
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/5/25.
 * 创客工作室页面
 */
public class WorkRoomActivity extends Activity implements OnClickListener {

    private ListView mListView_view;
    private MyItmeAdapter_build build;
    private TextView mDes;
    private ImageView mBack, mSearch, iv_teach, mUp;
    private FormBody body;
    private int count = 0;
    private ProgressBar mProgressBar;
    private ScrollView scrollView;
    private SuperSwipeRefreshLayout layout;
    private View headerView;
    private LinearLayout ku_build_yichang, ku_build_nodata;
    private AsyncLoaderData_List list;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_item);
        id = getIntent().getStringExtra("termid");
        //初始化标题栏
        initBar();
        //初始化控件
        initView();
        //加载数据

    }

    @Override
    protected void onRestart() {
        if (MyApplication.getFLAG() == 1010) {//判断是否需要刷新页面
            //初始化ListView
            initData();
            MyApplication.setFLAG(0);//刷新后置0
        }
        super.onRestart();
    }

    /**
     * 初始化标题栏
     */
    private void initBar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mUp = (ImageView) findViewById(R.id.iv_teach_up);

        //身份识别；
        if (MyApplication.getuserType() != null && ("3".equals(MyApplication.getuserType())
                || "1".equals(MyApplication.getuserType()))) {
            mUp.setVisibility(View.VISIBLE);
            mUp.setOnClickListener(this);
        }

        mDes = (TextView) findViewById(R.id.tv_teach_title);
        iv_teach = (ImageView) findViewById(R.id.iv_teach);

        try {
            iv_teach.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getBanner()));

        } catch (Exception e) {
            iv_teach.setImageResource(R.drawable.noimage);
        }

        mDes.setText(MyApplication.getUtil().mListBeen
                .get(2).getParent()
                .get(3).getParent_ParentBean()
                .get(getIntent().getIntExtra("index", 0)).getName());

        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);

        //自定义进度条；
        headerView = LayoutInflater.from(this).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mListView_view = (ListView) findViewById(R.id.lv_teach);
        ku_build_yichang = (LinearLayout) findViewById(R.id.ku_build_yichang);
        ku_build_nodata = (LinearLayout) findViewById(R.id.ku_build_nodata);

        scrollView = (ScrollView) findViewById(R.id.scrollview_item);
        scrollView.smoothScrollTo(0, 20);

        layout = (SuperSwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        initData();
        layout.setHeaderView(headerView);
        layout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

            @Override
            public void onRefresh() {
                //TODO 开始刷新
                count = 0;
                initData();
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

    /**
     * 加载数据
     */
    private void initData() {

        body = new FormBody.Builder()
                .add("termid", id)
                .add("pagesize", 10000 + "").build();
        list = new AsyncLoaderData_List(this, build, ku_build_nodata, ku_build_yichang, mListView_view, body, "Data_build_1", count, true, layout);
        list.execute(DatasUtil.URL_BASE + DatasUtil.URL_article);
        count += 5;

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
                if (!("".equals(MyApplication.getToKen())) && MyApplication.getToKen() != null) {
                    startActivity(new Intent(WorkRoomActivity.this, UploadActivity.class).putExtra("id", id));
                } else {
                    ToastUtil.showToast(WorkRoomActivity.this, "还未登录，请先登录...");
                    startActivity(new Intent(WorkRoomActivity.this, LoginActivity.class));
                }
                break;
        }
    }

}
