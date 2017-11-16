
package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.MyItmeAdapter;
import com.yitong.ChuangKeYuan.utils.AsyncLoaderfavoritesList;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;

/**
 * 作者：FBL  时间： 2016/7/1.
 * 收藏页面
 *
 */
public class CollectActivity extends Activity implements View.OnClickListener{
    private ImageView mBack, mSearch;
    private TextView mTitle;
    public int Number;
    private ListView listview;
    private MyItmeAdapter adapter;
    private AsyncLoaderfavoritesList list;
    private RelativeLayout rl_detail_pro;
    private ProgressBar mProgressBar;
    private LinearLayout ku_collect, ku_collect_yichang, ku_collect_denglu;
    private AsyncLoaderfavoritesList loaderfavoritesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        //初始化标题栏
        initTitlebar();
        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //判断是否已经登录过
        if (MyApplication.getToKen() != null && !"".equals(MyApplication.getToKen())) {
            list = new AsyncLoaderfavoritesList(this, adapter, listview, ku_collect,rl_detail_pro, ku_collect_yichang, 0);
                    list.execute(DatasUtil.URL_BASE + DatasUtil.URL_ArticlefavoritesList);
        }else{
            ku_collect_denglu.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化组件以及标题
     */
    private void initTitlebar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        listview = (ListView) findViewById(R.id.lv_teach);
        ku_collect = (LinearLayout) findViewById(R.id.ku_collect);
        ku_collect_yichang = (LinearLayout) findViewById(R.id.ku_collect_yichang);
        ku_collect_denglu = (LinearLayout) findViewById(R.id.ku_collect_denglu);

        rl_detail_pro = (RelativeLayout) findViewById(R.id.rl_detail_pro);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_detail_pro);
        Progressbar_Util.ProVisibility(mProgressBar, this);

        mTitle.setText(getIntent().getStringExtra("title"));
        mTitle.setTextSize(20);
        mSearch.setVisibility(View.GONE);
        mBack.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        initData();
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_teach_back:
                if (list != null){//在销毁页面之前判断是否还在网络请求，是的话就结束
                    list.cancel(true);
                }
                finish();
        }
    }
}
