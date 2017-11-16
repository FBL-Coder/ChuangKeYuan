package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ClassTeacherDelail;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/6/7.
 * 教师详情页面
 */

public class ClassTeactherDetail extends Activity {

    private ImageView mBack, mSearch, iv_firstteacher;
    private TextView mTitle, name, geyan, zhicheng, politics, year, describe, name1, geyan1, zhicheng1, politics1, year1, describe1;
    private FormBody body;
    private LinearLayout ll_nodata_teacherdetail;
    private RelativeLayout rl_nodata_teacherdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherdetail);

        //初始化标题栏
        initTitlebar();
        //得到数据后，设置数据；
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        //给boby设置值，
        body = new FormBody.Builder().add("id",getIntent().getStringExtra("id")).build();

        //进行异步加载；
        new AsyncLoaderData_Detail().execute(DatasUtil.URL_BASE+DatasUtil.URL_classteacherList);
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {

        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        iv_firstteacher = (ImageView) findViewById(R.id.iv_firstteacher);
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        name = (TextView) findViewById(R.id.tv_teacher_name);
        geyan = (TextView) findViewById(R.id.tv_teacher_geyan);
        zhicheng = (TextView) findViewById(R.id.tv_teacher_zhicheng);
        year = (TextView) findViewById(R.id.tv_teacher_year);
        politics = (TextView) findViewById(R.id.tv_teacher_politics);
        describe = (TextView) findViewById(R.id.tv_teacher_describe);
        name1 = (TextView) findViewById(R.id.tv_teacher_name1);
        geyan1 = (TextView) findViewById(R.id.tv_teacher_geyan1);
        zhicheng1 = (TextView) findViewById(R.id.tv_teacher_zhicheng1);
        year1 = (TextView) findViewById(R.id.tv_teacher_year1);
        politics1 = (TextView) findViewById(R.id.tv_teacher_politics1);
        describe1 = (TextView) findViewById(R.id.tv_teacher_describe1);

        ll_nodata_teacherdetail = (LinearLayout) findViewById(R.id.ll_nodata_teacherdetail);
        rl_nodata_teacherdetail = (RelativeLayout) findViewById(R.id.rl_nodata_teacherdetail);


        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSearch.setVisibility(View.GONE);
    }

    /**
     * 异步线程
     */
    class AsyncLoaderData_Detail extends AsyncTask<String, Void, ClassTeacherDelail> {
        private String data;

        @Override
        protected void onPostExecute(ClassTeacherDelail entity) {//根据返回数据进行判断，以及给组件设置内容；

            if (entity != null && Integer.parseInt(entity.getCode()) == 0) {//0表示获取书记成功，其他值都是不成功的，10101，数据为空。10000参数异常....

                mTitle.setText(entity.getUser_nicename() + "详情");
                iv_firstteacher.setImageURI(Uri.parse(entity.getThumb()));
                name.setText("姓        名：");
                geyan.setText("教育格言：");
                zhicheng.setText("职        称：");
                politics.setText("政治面貌：");
                year.setText("工作年限：");
                describe.setText("描        述：");
                name1.setText(entity.getUser_nicename());
                geyan1.setText(entity.getSignature());
                zhicheng1.setText(entity.getPosition());
                politics1.setText(entity.getPolitical());
                year1.setText(entity.getWorking());
                describe1.setText(entity.getDescribe());
            } else {
                ll_nodata_teacherdetail.setVisibility(View.GONE);
                rl_nodata_teacherdetail.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(entity);
        }

        @Override
        protected ClassTeacherDelail doInBackground(String... params) {
            ClassTeacherDelail entity = null;
            try {

                if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {
                    data = HttpDataUtil.getPublicData_POST(params[0], body);

                } else {
                    Log.i("HTTPData", "Detail_没网，傻啊，网打开..");
                }
                if (data!= null && !"".equals(data)) Log.i("Teacher", "加载数据"+data);
                Gson gson = new Gson();
                entity = gson.fromJson(data, ClassTeacherDelail.class);
            } catch (IOException e) {
//                e.printStackTrace();
                Log.i("HTTPData", "Detail_IOException" + e);
            }
            return entity;
        }
    }
}
