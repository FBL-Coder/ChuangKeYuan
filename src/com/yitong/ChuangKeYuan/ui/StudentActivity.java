package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ClassInfoEntity;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/6/15.
 * 班级介绍
 */
public class StudentActivity extends Activity implements OnClickListener {

    private ImageView mBack;
    private ImageView mSearch;
    private TextView mTitle;
    private WebView classdetail;
    private FormBody body;
    private LinearLayout ku_task_yichang;
    /**
     * 数据源
     */
    private AsyncClassInfo list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        //初始化标题栏
        initTitlebar();
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        //标题栏
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        classdetail = (WebView) findViewById(R.id.classdetail);
        ku_task_yichang = (LinearLayout) findViewById(R.id.ku_task_yichang);
        mSearch.setVisibility(View.GONE);
        mTitle.setText(getIntent().getStringExtra("name")+"介绍");

        mBack.setOnClickListener(this);
        //加载数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        body = new FormBody.Builder().add("classid", getIntent().getStringExtra("classid")).build();
        list = new AsyncClassInfo();
        list.execute(DatasUtil.URL_BASE + DatasUtil.URL_classInfo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                if (list != null){
                    list.cancel(true);
                }
                finish();
                break;
        }
    }

    /**
     * 异步线程类，加载数据
     */
    class AsyncClassInfo extends AsyncTask<String, Void, ClassInfoEntity> {

        @Override
        protected void onPostExecute(ClassInfoEntity classInfoEntity) {

            if (classInfoEntity != null && Integer.parseInt(classInfoEntity.getCode()) == 0) {//刷新页面显示
                ku_task_yichang.setVisibility(View.GONE);
                classdetail.loadUrl(classInfoEntity.getContent_url());
                classdetail.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
            } else {
                ku_task_yichang.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(classInfoEntity);
        }

        @Override
        protected ClassInfoEntity doInBackground(String... params) {
            String data;
            try {
                data = HttpDataUtil.getPublicData_POST(params[0], body);
                if (!("".equals(data)) && data != null) {
                    Log.i("CLASSINFO", "班级介绍详情数据:" + data);

                    Gson gson = new Gson();
                    ClassInfoEntity entity = gson.fromJson(data, ClassInfoEntity.class);
                    return entity;
                }
            } catch (IOException e) {
                return null;
            }
            return null;
        }
    }
}
