package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.domain.TeacherDerailEntity;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/6/7.
 * 教师详情页面
 */

public class TeactherDetail extends Activity {

    private ImageView mBack, mSearch, iv_firstteacher, delet, edit;
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

    @Override
    protected void onRestart() {
        if (MyApplication.getFLAG() == 1001) {//判断页面是否刷新
            initData();
            MyApplication.setFLAG(1010);//刷新后赋值
        }
        super.onRestart();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        System.out.println(getIntent().getStringExtra("id"));
//        if (getIntent().getStringExtra("id") != null || "".equals(getIntent().getStringExtra("id")))
        body = new FormBody.Builder().add("id", getIntent().getStringExtra("id")).build();

        new AsyncLoaderData_Detail().execute(DatasUtil.URL_BASE + DatasUtil.URL_TutorteacherList);
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {

        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        iv_firstteacher = (ImageView) findViewById(R.id.iv_firstteacher);
        mTitle = (TextView) findViewById(R.id.tv_teach_title);

        delet = (ImageView) findViewById(R.id.iv_teach_cancel);
        edit = (ImageView) findViewById(R.id.iv_teach_edit);

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

    ProgressDialog progressDialog;

    /**
     * 异步加载数据
     */
    class AsyncLoaderData_Detail extends AsyncTask<String, Void, TeacherDerailEntity> {
        private String data;

        @Override
        protected void onPostExecute(final TeacherDerailEntity entity) {

            if (entity != null && Integer.parseInt(entity.getCode()) == 0) {

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
                zhicheng1.setText(entity.getProfession());
                politics1.setText(entity.getPolitics_status());
                year1.setText(entity.getYear_working());
                describe1.setText(entity.getDesc());


                if ((entity.getUser_login() != null && !"".equals(entity)
                        && entity.getUser_login().equals(MyApplication.getusername()))
                        | (MyApplication.getuserType() != null && "1".equals(MyApplication.getuserType()))) {
                    edit.setVisibility(View.VISIBLE);
                    progressDialog = new ProgressDialog(TeactherDetail.this);
                    delet.setVisibility(View.VISIBLE);
                    final Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            ReturnResult result = (ReturnResult) msg.obj;
                            if (result.getCode() != null &&
                                    !"".equals(result.getCode()) &&
                                    Integer.parseInt(result.getCode()) == 0) {
                                progressDialog.dismiss();
                                ToastUtil.showToast(TeactherDetail.this, "删除成功");
                                finish();
                                MyApplication.setFLAG(1010);
                            } else {
                                ToastUtil.showToast(TeactherDetail.this, "删除失败，请重试...");
                            }
                            super.handleMessage(msg);
                        }
                    };
                    delet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(TeactherDetail.this);
                            builder.setTitle("亲，您确定要删除吗？");
                            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressDialog.setMessage("删除中...");
                                            progressDialog.show();
                                            progressDialog.setCancelable(false);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    FormBody body = new FormBody.Builder()
                                                            .add("username", MyApplication.getusername())
                                                            .add("password", MyApplication.getuserpassword())
                                                            .add("aid", entity.getId())
                                                            .build();
                                                    try {
                                                        String data = HttpDataUtil
                                                                .getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_article_del, body);
                                                        ReturnResult result = new Gson().fromJson(data, ReturnResult.class);
                                                        if (data != null && !"".equals(data)) {
                                                            Log.i("DEL", getIntent().getStringExtra("aid") + data);
                                                        }
                                                        Message message = mHandler.obtainMessage();
                                                        message.obj = result;
                                                        mHandler.sendMessage(message);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();
                                            dialog.dismiss();
                                        }
                                    }
                            );
                            builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }
                            );
                            builder.create().show();
                        }
                    });
                }


                edit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("parent", entity);

                        startActivity(new Intent(TeactherDetail.this, EditTeacher.class)
                                .putExtra("bun", bundle));
                    }
                });

            } else {
                ll_nodata_teacherdetail.setVisibility(View.GONE);
                rl_nodata_teacherdetail.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(entity);
        }

        @Override
        protected TeacherDerailEntity doInBackground(String... params) {
            TeacherDerailEntity entity = null;
            try {

                if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {
                    data = HttpDataUtil.getPublicData_POST(params[0], body);
                } else {
                    Log.i("HTTPData", "Detail_没网，傻啊，网打开..");
                }
                if (data != null && !"".equals(data)) Log.i("Teacher", "加载数据" + data);
                Gson gson = new Gson();
                entity = gson.fromJson(data, TeacherDerailEntity.class);
            } catch (IOException e) {
//                e.printStackTrace();
                Log.i("HTTPData", "Detail_IOException" + e);
            }
            return entity;
        }
    }
}
