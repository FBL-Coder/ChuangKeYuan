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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.utils.AsyncTeacherData_List_Tutor;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/23.
 * 老师列表
 */
public class TeacherActivity extends Activity implements OnClickListener {

    private ListView mListView;
    private ImageView mBack, mUp;
    private ImageView mSearch;
    private SimpleDraweeView teacherImage;
    private TextView mTitle;
    private FormBody body;
    /**
     * 数据适配器
     */
    private AsyncTeacherData_List_Tutor.MyTeacherAdapter_build build;
    private ProgressBar mProgressBar, progressBar;
    private ScrollView scrollView;
    private SuperSwipeRefreshLayout layout;
    private View headerView;
    private RelativeLayout rl_detail_pro;
    private int count = 0;
    /**
     * 数据源
     */
    private AsyncTeacherData_List_Tutor list;
    private LinearLayout ku_build_yichang, ku_build_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        //初始化标题栏
        initTitlebar();
        //初始化ListView
        initView();
        //初始化数据
        initData();
    }

    @Override
    protected void onRestart() {
        if (MyApplication.getFLAG() == 1010) {//判断是否刷新
            //初始化数据
            initData();
            MyApplication.setFLAG(0);//刷新后置0
        }
        super.onRestart();
    }

    /**
     * 初始化ListView
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_teacher);
        ku_build_yichang = (LinearLayout) findViewById(R.id.ku_build_yichang);
        ku_build_nodata = (LinearLayout) findViewById(R.id.ku_build_nodata);

        scrollView = (ScrollView) findViewById(R.id.scrollview_teacher);
        rl_detail_pro = (RelativeLayout) findViewById(R.id.rl_detail_pro);
        progressBar = (ProgressBar) findViewById(R.id.pb_detail_pro);
        Progressbar_Util.ProVisibility(progressBar, this);
        //自定义进度条；
        headerView = LayoutInflater.from(this).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, this);
        scrollView.smoothScrollTo(0, 20);

        //设置刷新事件
        layout = (SuperSwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout_teacher);
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
     * 初始化数据
     */
    private void initData() {

        body = new FormBody.Builder()
                .add("termid", "8")
                .add("pagesize", 10000 + "").build();

        list = new AsyncTeacherData_List_Tutor(this, build, ku_build_nodata
                , ku_build_yichang, mListView, rl_detail_pro, layout, body, null, count);
        list.execute(DatasUtil.URL_BASE + DatasUtil.URL_teacherList);
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
        teacherImage = (SimpleDraweeView) findViewById(R.id.sdv_teacherlist);
        try {
            mTitle.setText(MyApplication.getUtil().mListBeen.get(1).getParent().get(0).getName());
        } catch (Exception e) {
            mTitle.setText("老师列表");
        }
        try {
            teacherImage.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(1).getParent().get(0).getBanner()));
        } catch (Exception e) {
            teacherImage.setImageResource(R.drawable.noimage);
        }
        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
        //身份识别
        if (MyApplication.getuserType() != null && "1".equals(MyApplication.getuserType())) {
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
                startActivity(new Intent(TeacherActivity.this, UploadTeacherActivity.class));
                break;
        }
    }
}
