package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jacksen.taggroup.ITagBean;
import com.jacksen.taggroup.SuperTagGroup;
import com.jacksen.taggroup.SuperTagUtil;
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
 * 家长详情
 */

public class ParentsDetail extends Activity implements View.OnClickListener {

    private ImageView mBack, mSearch, iv_firstteacher, delet, edit;
    private TextView mTitle, parent_name, baby_name, geyan, baby_class, age,
            job, parent_name1, baby_name1, geyan1, baby_class1, age1, job1,
            miaoshu, miaoshu1;
    private FormBody body;
    private LinearLayout ll_parentdetail, describe;
    private RelativeLayout nodata_parentdetail, ll_describe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentdetail);
        //初始化标题栏
        initTitlebar();
        //得到数据后，设置数据；
        initData();
    }

    @Override
    protected void onRestart() {
        //这里判断Appli中的FLAG，用来刷新页面数据
        if (MyApplication.getFLAG() == 1001) {
            initData();
            MyApplication.setFLAG(1010);
        }
        super.onRestart();
    }

    ProgressDialog progressDialog;

    /**
     * 初始化数据
     */
    private void initData() {
        body = new FormBody.Builder().add("id", getIntent().getStringExtra("id")).build();
        new AsyncLoaderData_Detail().execute(DatasUtil.URL_BASE + DatasUtil.URL_teacherInfo);
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        iv_firstteacher = (ImageView) findViewById(R.id.iv_parent);
        delet = (ImageView) findViewById(R.id.iv_teach_cancel);
        edit = (ImageView) findViewById(R.id.iv_teach_edit);

        parent_name = (TextView) findViewById(R.id.tv_parent_name);
        geyan = (TextView) findViewById(R.id.tv_parent_geyan);
        baby_name = (TextView) findViewById(R.id.tv_parent_baby_name);
        baby_class = (TextView) findViewById(R.id.tv_parent_class);
        age = (TextView) findViewById(R.id.tv_parent_age);
        job = (TextView) findViewById(R.id.tv_parent_job);
        parent_name1 = (TextView) findViewById(R.id.tv_parent_name1);
        geyan1 = (TextView) findViewById(R.id.tv_parent_geyan1);
        baby_name1 = (TextView) findViewById(R.id.tv_parent_baby_name1);
        baby_class1 = (TextView) findViewById(R.id.tv_parent_class1);
        age1 = (TextView) findViewById(R.id.tv_parent_age1);
        job1 = (TextView) findViewById(R.id.tv_parent_job1);
        miaoshu = (TextView) findViewById(R.id.tv_parent_miaoshu);
        miaoshu1 = (TextView) findViewById(R.id.tv_parent_miaoshu1);

        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        ll_parentdetail = (LinearLayout) findViewById(R.id.ll_parentdetail);
        nodata_parentdetail = (RelativeLayout) findViewById(R.id.nodata_parentdetail);


        mSearch.setVisibility(View.GONE);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                finish();
                break;
        }
    }

    /**
     * 数据加载异步类
     */
    class AsyncLoaderData_Detail extends AsyncTask<String, Void, TeacherDerailEntity> {
        private String data;

        @Override
        protected void onPostExecute(final TeacherDerailEntity entity) {

            if (entity != null && Integer.parseInt(entity.getCode()) == 0) {

                if ((entity.getUser_login() != null && !"".equals(entity)
                        && entity.getUser_login().equals(MyApplication.getusername()))
                        | (MyApplication.getuserType() != null && "1".equals(MyApplication.getuserType()))) {
                    edit.setVisibility(View.VISIBLE);

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println(parent_name1.getText().toString());

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("parent",entity);

                            startActivity(new Intent(ParentsDetail.this, EditParents.class)
                                    .putExtra("bun", bundle));
                        }
                    });

                    progressDialog = new ProgressDialog(ParentsDetail.this);
                    delet.setVisibility(View.VISIBLE);
                    final Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            ReturnResult result = (ReturnResult) msg.obj;
                            if (result.getCode() != null &&
                                    !"".equals(result.getCode()) &&
                                    Integer.parseInt(result.getCode()) == 0) {
                                progressDialog.dismiss();
                                ToastUtil.showToast(ParentsDetail.this, "删除成功");
                                MyApplication.setFLAG(1010);
                                finish();
                            } else {
                                ToastUtil.showToast(ParentsDetail.this, "删除失败，请重试...");
                            }
                            super.handleMessage(msg);
                        }
                    };
                    delet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(ParentsDetail.this);
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

                mTitle.setText(entity.getUser_nicename() + "详情");
                iv_firstteacher.setImageURI(Uri.parse(entity.getThumb()));
                parent_name.setText("家长姓名：");
                geyan.setText("育儿格言：");
                baby_name.setText("孩子姓名：");
                baby_class.setText("孩子班级：");
                age.setText("家长年龄： ");
                job.setText("职        业：");
                parent_name1.setText(entity.getUser_nicename());
                geyan1.setText(entity.getSignature());
                baby_name1.setText(entity.getBaby_name());
                baby_class1.setText(entity.getClass_name());
                age1.setText(entity.getAge());
                job1.setText(entity.getProfession());

                miaoshu.setText("描        述：");
                miaoshu1.setText(entity.getDesc());

            } else {
                ll_parentdetail.setVisibility(View.GONE);
                nodata_parentdetail.setVisibility(View.VISIBLE);
            }

            Log.i("TAG", "用户id" + MyApplication.getuserid());
            if (!("".equals(MyApplication.getuserid())) && MyApplication.getuserid() != null) {
                if (MyApplication.getuserid().equals(entity.getId())) {
                    describe.setVisibility(View.VISIBLE);
                }
            } else {
                Log.i("TAG", "登陆信息不合适，或者未登录..." + MyApplication.getuserid());
            }
            super.onPostExecute(entity);
        }

        @Override
        protected TeacherDerailEntity doInBackground(String... params) {
            TeacherDerailEntity entity = null;
            try {
                if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {
                    data = HttpDataUtil.getPublicData_POST(params[0], body);

                    Log.i("TAG", data);
                    Gson gson = new Gson();

                    entity = gson.fromJson(data, TeacherDerailEntity.class);
                } else {
                    Log.i("HTTPData", "Detail_没网，傻啊，网打开..");
                }
            } catch (IOException e) {
                Log.i("HTTPData", "Detail_IOException" + e);
                return null;

            }
            return entity;
        }
    }
}
