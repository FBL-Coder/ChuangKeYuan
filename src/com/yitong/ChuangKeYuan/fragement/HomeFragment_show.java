package com.yitong.ChuangKeYuan.fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.MyItmeAdapter_build;
import com.yitong.ChuangKeYuan.utils.AsyncLoaderData_List;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.utils.UiUtils;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/23.
 * 幼儿园创客教育方案页面
 */
public class HomeFragment_show extends Fragment {

    private ListView mListView;
    private MyItmeAdapter_build build;
    private FormBody body;
    /**
     * 刷新使用的int标记；
     */
    private int count = 0;
    private ProgressBar mProgressBar;
    private ScrollView scrollView;
    private SuperSwipeRefreshLayout layout;
    /**
     * 自定义进度条；
     */
    private View headerView;
    private View view;
    private LinearLayout ku_xiu_yichang,ku_xiu_denglu;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = UiUtils.inflateView(R.layout.fragment_xiu);
        //初始化标题栏
        initTitlebar();
        //初始化ListView
        initListView();
        //初始化进度条
        return view;
    }

    /**
     * 初始化ListView
     */
    private void initListView() {

        mListView = (ListView) view.findViewById(R.id.lv_xiu);
        ku_xiu_yichang = (LinearLayout) view.findViewById(R.id.ku_xiu_yichang);
        ku_xiu_denglu = (LinearLayout) view.findViewById(R.id.ku_xiu_denglu);
        int ID = 12;
        try {
            ID = Integer.parseInt(MyApplication.getUtil().mListBeen.get(4).getTerm_id());
        }catch (Exception e){}
        body = new FormBody.Builder()
                .add("termid", ID+"")
                .build();
        long isShow = 1;
        new AsyncLoaderData_List(getActivity(), build
                ,ku_xiu_denglu,ku_xiu_yichang, mListView
                , body, "Data_Xiu", count, true,isShow,layout)
                .execute(DatasUtil.URL_BASE + DatasUtil.URL_article);
        count += 5;
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {

        //自定义进度条；
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, getActivity());


        scrollView = (ScrollView) view.findViewById(R.id.scrollview_xiu);
        scrollView.smoothScrollTo(0, 20);

        layout = (SuperSwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout_xiu);
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
}

