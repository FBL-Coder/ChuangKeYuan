package com.yitong.ChuangKeYuan.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.MyTaskAdapter;
import com.yitong.ChuangKeYuan.ui.UploadAllTaskActivity;
import com.yitong.ChuangKeYuan.utils.AsyncLoaderData_AllTask;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.utils.UiUtils;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/24.
 * 我负责的页面
 */
public class TaskFragment_all extends Fragment {

    /**
     * 所有任务数据源
     */
    private ListView mAllTask;
    private MyTaskAdapter myTaskAdapter;
    private FormBody body;
    /**
     * 刷新int标记
     */
    private int count = 0;
    private ProgressBar mProgressBar;
    private ScrollView scrollView;
    /**
     * android 自带刷新控件；
     */
    private SuperSwipeRefreshLayout layout;
    /**
     * 自定义进度条
     */
    private View headerView;
    private View view;
    private LinearLayout ku_task_yichang, ku_task_nodata, ku_task_denglu;
    private TextView mTitle;
    private RelativeLayout rl_detail_pro;
    private ImageView mUp, mSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = UiUtils.inflateView(R.layout.fragment_alltask_all);
        //初始化标题栏
        initTitlebar(view);
        initProgressBar();
        //初始化ListView
        initListView();

        //初始化进度条
        return view;
    }

    @Override
    public void onStart() {
        //初始化ListView
        if (MyApplication.getFLAG_TASK() == -1) {
            initListView();
            MyApplication.setFLAG_TASK(0);
        }
        super.onStart();
    }

    /**
     * 初始化标题栏
     * @param view
     */
    private void initTitlebar(View view) {
        //标题栏
        mTitle = (TextView) view.findViewById(R.id.tv_titlebar_title);
        mSearch = (ImageView) view.findViewById(R.id.iv_titlebar_right);
        mUp = (ImageView) view.findViewById(R.id.iv_titlebar_up);

        //身份识别
        if (MyApplication.getuserType() != null &&
                ("3".equals(MyApplication.getuserType()) || "1".equals(MyApplication.getuserType()))) {
            mUp.setVisibility(View.VISIBLE);
            mUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), UploadAllTaskActivity.class));
                }
            });
        }

        mTitle.setText("所有任务");
        mSearch.setVisibility(View.GONE);
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        rl_detail_pro = (RelativeLayout) view.findViewById(R.id.rl_detail_pro);
        mAllTask = (ListView) view.findViewById(R.id.lv_task);
        ku_task_yichang = (LinearLayout) view.findViewById(R.id.ku_task_yichang);
        ku_task_nodata = (LinearLayout) view.findViewById(R.id.ku_task_nodata);
        ku_task_denglu = (LinearLayout) view.findViewById(R.id.ku_task_denglu);

        //判断是否已登录，否则显示这里需要登录;
        if (MyApplication.getToKen() != null && !("".equals(MyApplication.getToKen()))) {
            ku_task_denglu.setVisibility(View.GONE);
            body = new FormBody.Builder()
                    .add("username", MyApplication.getusername())
                    .add("password", MyApplication.getuserpassword())
                    .add("pagesize", "10000")
                    .add("termid", "13")
                    .build();
            new AsyncLoaderData_AllTask(getActivity(), myTaskAdapter,
                    rl_detail_pro, ku_task_nodata, ku_task_yichang,
                    mAllTask, body, null, count,layout)
                    .execute(DatasUtil.URL_BASE + DatasUtil.URL_TaskAll);
            count += 5;
        } else {
            ku_task_denglu.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化标题栏
     */
    private void initProgressBar() {

        //自定义进度条；
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, getActivity());

        scrollView = (ScrollView) view.findViewById(R.id.scrollview_task);
        scrollView.smoothScrollTo(0, 20);

        layout = (SuperSwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout_task);
        layout.setHeaderView(headerView);
        layout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

            @Override
            public void onRefresh() {
                //TODO 开始刷新
                count = 0;
                initListView();
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
}
