package com.yitong.ChuangKeYuan.fragement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.ListViewAdapter;
import com.yitong.ChuangKeYuan.domain.DataPublicBean_Index;
import com.yitong.ChuangKeYuan.ui.ArticleDetailActivity;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Dtat_Cache;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.utils.UiUtils;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/23.
 * 精彩推荐页面
 */
public class HomeFragment_recommend extends Fragment {
    //ViewPager
    private ViewPager mViewPager;
    /**
     * 图片标题正文的那些点
     */
    private List<View> dots;
    /**
     * 图片标题
     */
    private TextView textViews;
    /**
     * 当前图片的索引号
     */
    private int currentItem = 0;

    private ScheduledExecutorService scheduledExecutorService;
    //ListView
    private ListView mListView;
    private ListViewAdapter mListViewAdapter;
    private RelativeLayout rl_detail_pro;
    private ScrollView scrollView;

    private SuperSwipeRefreshLayout layout;
    private ProgressBar mProgressBar;

    /**
     * 自定义进度条；
     */
    private View headerView;

    /**
     * 切换当前显示的图片
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 切换当前显示的图片
            mViewPager.setCurrentItem(currentItem);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = UiUtils.inflateView(R.layout.fragment_home_recommend);

        initalldata(view);

        return view;
    }

    /**
     * 初始化ViewPager数据
     *
     * @param view
     */
    public void initalldata(final View view) {

        mListView = (ListView) view.findViewById(R.id.listView);
        //初始化控件
        initView(view);

        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.v_dot0));
        dots.add(view.findViewById(R.id.v_dot1));
        dots.add(view.findViewById(R.id.v_dot2));
        dots.add(view.findViewById(R.id.v_dot3));
        dots.add(view.findViewById(R.id.v_dot4));

        textViews = (TextView) view.findViewById(R.id.tv_title);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_fragment_viewpager);
        scrollView = (ScrollView) view.findViewById(R.id.recom_scrollView);
        scrollView.smoothScrollTo(0, 20);

        // 设置填充ViewPager页面的适配器
        mViewPager.setAdapter(new MyAdapter());
        // 设置一个监听器，当ViewPager中的页面改变时调用
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());


        //自定义进度条；
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, getActivity());


        layout = (SuperSwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout_xiu);
        layout.setHeaderView(headerView);
        layout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

            @Override
            public void onRefresh() {
                //TODO 开始刷新
                reFreshView(view, layout);
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
     * 下拉刷新页面数据
     */
    private void reFreshView(final View view, final SuperSwipeRefreshLayout layout) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                layout.setRefreshing(false);
                if (msg.what == 1) {
                    MyApplication.getUtil().Been_index = (DataPublicBean_Index) msg.obj;
                    initView(view);
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataPublicBean_Index Been = null;
                String data = null;
                try {
                    if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {

                        data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_index, new FormBody.Builder().build());
                        if (!("".equals(data)) && data != null) {
                            Dtat_Cache.writeFileToSD(data, "Data_index");
                        }
                    }
                    if (data == null | "".equals(data)) {
                        data = Dtat_Cache.readFile("Data_index");
                    }
                    if (!"".equals(data)) {
                        Gson gson = new Gson();
                        Been = gson.fromJson(data, DataPublicBean_Index.class);

                        if (Integer.parseInt(Been.getCode()) == 0 && Been != null) {

                            Message message = handler.obtainMessage();
                            message.obj = Been;
                            message.what = 1;
                            handler.sendMessage(message);
                        } else {
                            Message message = handler.obtainMessage();
                            handler.sendMessage(message);
                            Looper.prepare();
                            ToastUtil.showToast(MyApplication.getContext(), "获取数据异常，请稍后再试！");
                            System.exit(0);
                            Looper.loop();
                        }
                    }
                } catch (Exception e) {
                    Message message = handler.obtainMessage();
                    handler.sendMessage(message);
                    Log.e("Exception", "Exception  :" + e);
                    Looper.prepare();
                    ToastUtil.showToast(MyApplication.getContext(), "获取数据异常，请稍后再试！");
                    System.exit(0);
                    Looper.loop();
                }
            }
        }).start();
    }

    @Override
    public void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
        super.onStart();
    }

    @Override
    public void onStop() {
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    /**
     * 换行切换任务
     */
    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (mViewPager) {
                currentItem = (currentItem + 1) % MyApplication.getUtil().Been_index.getBanner().size();
                // 通过Handler切换图片
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        //页面状态改变的时候调用
        public void onPageSelected(int position) {
            currentItem = position;
            textViews.setText(MyApplication.getUtil().Been_index.getBanner().get(position).getTitle());
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

    }

    /**
     * 填充ViewPager页面的适配器
     */
    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (MyApplication.getUtil().Been_index == null || MyApplication.getUtil().Been_index.getBanner() == null || MyApplication.getUtil().Been_index.getBanner().size() == 0)
                return 0;
            return MyApplication.getUtil().Been_index.getBanner().size();
        }

        @Override
        public Object instantiateItem(View arg0, final int arg1) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager, null);
            ImageView iv = (ImageView) view.findViewById(R.id.viewpager);
            try {
                iv.setImageURI(Uri.parse(MyApplication.getUtil().Been_index.getBanner().get(arg1).getPic()));
            } catch (Exception e) {
            }
            ((ViewPager) arg0).addView(view, 0);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                    intent.putExtra("aid", MyApplication.getUtil().Been_index.getBanner().get(arg1).getPostid());
                    startActivity(intent);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        mListViewAdapter = new ListViewAdapter(DatasUtil.getRecomListViewData(), getActivity().getApplicationContext());
        mListView.setAdapter(mListViewAdapter);
    }
}
