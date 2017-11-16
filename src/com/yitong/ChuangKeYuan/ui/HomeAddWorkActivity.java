package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.MyItmeAdapter_build;
import com.yitong.ChuangKeYuan.domain.ArticleList;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Dtat_Cache;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/5/25.
 * 家庭工作坊
 */
public class HomeAddWorkActivity extends Activity implements OnClickListener {

    private ListView mListView_view;
    private MyItmeAdapter_build build;
    private TextView mDes;
    private ImageView mBack, mSearch, mUp, iv_teach;
    private FormBody body;
    private int count = 0;
    private ProgressBar mProgressBar;
    private ScrollView scrollView;
    private SuperSwipeRefreshLayout layout;
    /**
     * 自定义进度条
     */
    private View headerView;
    private LinearLayout ku_build_yichang, ku_build_nodata;
    /**
     * 数据源
     */
    private AsyncData_List list;
    private String itmeid;
    private String name;
    private boolean IsHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_item);
        itmeid = getIntent().getStringExtra("termid");
        name = getIntent().getStringExtra("classname");
        IsHome = getIntent().getBooleanExtra("IsHome", false);
        //初始化标题栏
        initBar();
    }

    @Override
    protected void onStart() {
        //初始化控件
        initView();
        //加载数据
        initData();
        super.onStart();
    }

    /**
     * 初始化标题栏
     */
    private void initBar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mUp = (ImageView) findViewById(R.id.iv_teach_up);
        mDes = (TextView) findViewById(R.id.tv_teach_title);
        iv_teach = (ImageView) findViewById(R.id.iv_teach);

        try {
            mDes.setText(name + "-" + MyApplication.getUtil().mListBeen.get(2).getParent().get(2).getName());
        } catch (Exception e) {
            mDes.setText(name);
        }
        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);

        //身份识别
        if ((MyApplication.getuserType() != null && "2".equals(MyApplication.getuserType())
                && MyApplication.getuserClassName().equals(getIntent().getStringExtra("classname")))
                | (MyApplication.getuserType() != null && "1".equals(MyApplication.getuserType()))) {
            mUp.setVisibility(View.VISIBLE);
            mUp.setOnClickListener(this);
        }

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

        //刷新 组件的初始化以及设置下拉事件；
        layout = (SuperSwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        layout.setHeaderView(headerView);
        //下拉刷新；
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
     * 加载数据
     */
    private void initData() {

        body = new FormBody.Builder()
                .add("termid", getIntent().getStringExtra("termid"))
                .add("pagesize", "10000")
                .build();
        list = new AsyncData_List(this, itmeid, build, ku_build_nodata, ku_build_yichang, mListView_view, body, "Data_build_1", count, true);
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
                //判断是否登录
                if (!("".equals(MyApplication.getToKen())) && MyApplication.getToKen() != null) {
                    startActivity(new Intent(HomeAddWorkActivity.this, UploadActivity.class).putExtra("id", itmeid));
                } else {
                    ToastUtil.showToast(HomeAddWorkActivity.this, "还未登录，请先登录...");
                    startActivity(new Intent(HomeAddWorkActivity.this, LoginActivity.class));
                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    /**
     * 异步加载数据
     */
    private class AsyncData_List extends AsyncTask<String, Integer, ArticleList> {

        private MyItmeAdapter_build build;
        private boolean ratingBar;
        private ListView mListView;
        private Context context;
        private FormBody body;
        private String filename;
        private String data;
        private LinearLayout ll_nodata, ll_error;
        private String itmeid;

        /**
         * 构造器
         *
         * @param context   上下文
         * @param build     适配器
         * @param ll_nodata 没数据布局
         * @param ll_error  错误布局
         * @param listView  listview布局
         * @param body      网络请求参数
         * @param filename  缓存文件名
         * @param count     int标记
         * @param ratingBar 是否星级显示
         */
        public AsyncData_List(Context context, String itmeid, MyItmeAdapter_build build, LinearLayout ll_nodata, LinearLayout ll_error, ListView listView,
                              FormBody body, String filename, int count, boolean ratingBar) {
            this.context = context;
            this.itmeid = itmeid;
            this.ll_nodata = ll_nodata;
            this.ll_error = ll_error;
            this.build = build;
            this.body = body;
            this.filename = filename;
            this.ratingBar = ratingBar;
            this.mListView = listView;
        }

        @Override
        protected void onPostExecute(final ArticleList result) {
            //刷新界面列表数据
            if (result != null && !("".equals(result)) && Integer.parseInt(result.getCode()) == 0) {//获取数据是否正常
                mListView.setVisibility(View.VISIBLE);
                ll_nodata.setVisibility(View.GONE);
                ll_error.setVisibility(View.GONE);

                if (result.getBanner() != null) {
                    iv_teach.setImageURI(Uri.parse(result.getBanner()));
                }
                if (build == null) {
                    build = new MyItmeAdapter_build(result, MyApplication.getContext(), ratingBar);
                    mListView.setAdapter(build);
                } else {
                    build.notifyDataSetChanged();
                }
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, ArticleDetailActivity.class);
                        intent.putExtra("author", result.getList().get(position).getAuthor());
                        intent.putExtra("aid", result.getList().get(position).getId());
                        intent.putExtra("IsHome", IsHome);
                        Log.i("Listid", "Author" + result.getList().get(position).getAuthor() + "");
                        context.startActivity(intent);
                    }
                });

            } else if (result != null && Integer.parseInt(result.getCode()) == 10101) {//获取数据为空
                mListView.setVisibility(View.GONE);
                ll_nodata.setVisibility(View.VISIBLE);
            } else {
                ToastUtil.showToast(context, "对不起，服务器异常，请求数据未成功...");
                mListView.setVisibility(View.GONE);
                ll_error.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(result);
        }

        @Override
        protected ArticleList doInBackground(String... params) {
            try {
                data = HttpDataUtil.getPublicData_POST(params[0], body);
                if (filename != null && !("".equals(filename))) {
                    if (data == null | "".equals(data)) {
                        data = Dtat_Cache.readFile(filename);
                    } else {
                        Dtat_Cache.writeFileToSD(data, filename);
                    }
                }
            } catch (Exception e) {
                return null;
            }
            if (data != null && !("".equals(data))) {
                Log.i("JIA", data);
                Gson gson = new Gson();
                ArticleList articleList = gson.fromJson(data, ArticleList.class);
                return articleList;
            }
            return null;
        }
    }

}
