package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.utils.AsyncTeacherData_List;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/6/15.
 * 班级小创客中的老师介绍
 */
public class TeacherClassListActivity extends Activity implements OnClickListener {

    private ListView mListView;
    private ImageView mBack;
    private ImageView mSearch;
    private TextView mTitle;
    private FormBody body;
    private AsyncTeacherData_List.MyTeacherAdapter_build build;
    private AsyncTeacherData_List list;
    private RelativeLayout rl_detail_pro;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherlist);
        //初始化ListView
        initListView();
        //初始化标题栏
        initTitlebar();
        //初始化数据
        initData();
    }
    /**
     * 初始化ListView
     */
    private void initListView() {
        mListView = (ListView) findViewById(R.id.lv_teacher);
        rl_detail_pro = (RelativeLayout) findViewById(R.id.rl_detail_pro);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_detail_pro);
        Progressbar_Util.ProVisibility(mProgressBar, this);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        body = new FormBody.Builder()
                .add("classid",getIntent().getStringExtra("id"))
                .add("pagesize",10000+"").build();

        list = new AsyncTeacherData_List(this, build, mListView,rl_detail_pro, body, "TeacherData",0);
                list.execute(DatasUtil.URL_BASE + DatasUtil.URL_class_teacherList);

    }
    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        //标题栏
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mTitle.setText("教师");
        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_teach_back:
                if (list != null){
                    list.cancel(true);
                }
                finish();
                break;
        }
    }
}
