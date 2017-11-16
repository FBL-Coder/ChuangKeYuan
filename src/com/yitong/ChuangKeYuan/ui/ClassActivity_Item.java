package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.MyClassItmeAdapter;
import com.yitong.ChuangKeYuan.domain.ArticleList;
import com.yitong.ChuangKeYuan.utils.AsyncLoaderData_List;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import java.util.Random;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/5/25.
 * 班级小创客中的子页面
 */
public class ClassActivity_Item extends Activity implements OnClickListener {


    private ListView mListView;
    private TextView mDes;
    private FormBody body;
    private ImageView mBack, mSearch, mTeacher, simpledraweeview_class_item, class_item_add;
    private int count = 0;
    private ProgressBar mProgressBar;
    private ScrollView scrollView;
    private SuperSwipeRefreshLayout layout;
    private View headerView;
    private MyClassItmeAdapter adapter;
    private LinearLayout ku_class_yichang, ku_class_nodata;
    private String[] strings = {"创意美术", "玩转数学", "生活点滴", "艺术天地", "多彩游戏"};
    private AsyncLoaderData_List list;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_item);
        id = getIntent().getStringExtra("termid");
        //初始化标题栏
        initBar();
        //初始化控件
        initView();
        //加载数据
        initData();
    }

    @Override
    protected void onRestart() {
        if (MyApplication.getFLAG() == 1010){
            //初始化ListView
            initData();
            MyApplication.setFLAG(0);
        }
        super.onRestart();
    }

    /**
     * 初始化标题栏
     */
    private void initBar() {
        mDes = (TextView) findViewById(R.id.tv_teach_title);
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mTeacher = (ImageView) findViewById(R.id.iv_teach_teacher);

        mSearch.setImageResource(R.drawable.xuesheng);
        mTeacher.setImageResource(R.drawable.jiaoshi);

        Random random = new Random();
        mDes.setText(getIntent().getStringExtra("name") + " · " + strings[random.nextInt(4)]);
        mTeacher.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mTeacher.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        simpledraweeview_class_item = (ImageView) findViewById(R.id.simpledraweeview_class_item);
        mListView = (ListView) findViewById(R.id.lv_class_item);
        ku_class_yichang = (LinearLayout) findViewById(R.id.ku_class_yichang);
        ku_class_nodata = (LinearLayout) findViewById(R.id.ku_class_nodata);
        class_item_add = (ImageView) findViewById(R.id.class_item_add);

        //用户类型判断，1、2、3分别表示管理员、家长、老师；
        if ((MyApplication.getuserType() != null && "3".equals(MyApplication.getuserType())
                && getIntent().getStringExtra("name").equals(MyApplication.getuserClassName()))
                | (MyApplication.getuserType() != null && "1".equals(MyApplication.getuserType()))) {
            class_item_add.setVisibility(View.VISIBLE);
            class_item_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ClassActivity_Item.this,
                            UploadActivity.class).putExtra("id", getIntent().getStringExtra("termid")));
                }
            });
        }

        //自定义进度条；
        headerView = LayoutInflater.from(this).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, this);

        scrollView = (ScrollView) findViewById(R.id.scrollview_class_item);
        scrollView.smoothScrollTo(0, 20);

        //刷新组件的初始化以及设置回调；
        layout = (SuperSwipeRefreshLayout) findViewById(R.id.ssrl_class_item);
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
                .add("termid", getIntent().getStringExtra("termid"))
                .add("pagesize",10000+"").build();

        list = new AsyncLoaderData_List(this, adapter, ku_class_nodata,
                ku_class_yichang, mListView, simpledraweeview_class_item
                , body, "Data_Class_Item", count, true, -1,layout);
        list.execute(DatasUtil.URL_BASE + DatasUtil.URL_article);


        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                int TAB;
                final ArticleList result = (ArticleList) msg.obj;

                //刷新界面列表数据
                if (result != null && !("".equals(result)) && Integer.parseInt(result.getCode()) == 0) {
                    mListView.setVisibility(View.VISIBLE);
                    ku_class_nodata.setVisibility(View.GONE);
                    ku_class_yichang.setVisibility(View.GONE);

                    if (adapter == null) {
                        adapter = new MyClassItmeAdapter(result, simpledraweeview_class_item, MyApplication.getContext(), true);
                        mListView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ClassActivity_Item.this, ArticleDetailActivity.class);
                            intent.putExtra("aid", result.getList().get(position).getId());
                            intent.putExtra("author", result.getList().get(position).getAuthor());
                            Log.i("Listid", "ListPostion" + position + "");
                            ClassActivity_Item.this.startActivity(intent);
                        }
                    });
                } else if (result != null && Integer.parseInt(result.getCode()) == 10101) {
                    mListView.setVisibility(View.GONE);
                    ku_class_nodata.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.showToast(ClassActivity_Item.this, "对不起，服务器异常，请求数据未成功...");
                    mListView.setVisibility(View.GONE);
                    ku_class_yichang.setVisibility(View.VISIBLE);
                }
                super.handleMessage(msg);
            }
        };
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
            case R.id.iv_teach_search:
                Intent intent1 = new Intent(ClassActivity_Item.this, StudentActivity.class);
                intent1.putExtra("name", getIntent().getStringExtra("name"));
                intent1.putExtra("classid", getIntent().getStringExtra("termid"));
                startActivity(intent1);
                break;
            case R.id.iv_teach_teacher:
                Intent intent = new Intent(ClassActivity_Item.this, TeacherClassListActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("termid"));
                startActivity(intent);
                break;
        }
    }
}

