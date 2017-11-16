package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.CommentActivityAdapter;
import com.yitong.ChuangKeYuan.domain.CommentBean;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Author：FBL  Time： 2017/9/12.
 * 综合评价
 */

public class CommentActivity extends Activity {
    public TextView mDes;
    public ImageView mBack, iv_teach_search;
    private ListView listView;
    private FormBody body;
    private int time = 0;
    private LinearLayout mCommentItemLl;
    private TextView mCommentItemMore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initData();
    }


    private void initData() {
        body = new FormBody.Builder()
                .add("username", MyApplication.getusername())
                .add("password", MyApplication.getuserpassword())
                .build();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                CommentBean bean = (CommentBean) msg.obj;

                TextView mCommentItemContent = (TextView) findViewById(R.id.comment_item_content);
                TextView mCommentItemNum = (TextView) findViewById(R.id.comment_item_num);
                TextView mCommentItemTime = (TextView) findViewById(R.id.comment_item_time);

                mCommentItemContent.setText("    " + bean.getList().get(0).getGood() + "\n    " + bean.getList().get(0).getBad());
                mCommentItemTime.setText(bean.getList().get(0).getTitle());
                mCommentItemNum.setText(bean.getList().get(0).getLevel());
                listView.setAdapter(new CommentActivityAdapter(bean, CommentActivity.this));
                mCommentItemMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCommentItemLl.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                time++;
                try {
                    String data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_Comment, body);
                    Gson gson = new Gson();
                    CommentBean bean = gson.fromJson(data, CommentBean.class);
                    if ("0".equals(bean.getCode())) {
                        Message mess = handler.obtainMessage();
                        mess.obj = bean;
                        handler.sendMessage(mess);
                    } else {
                        if (time < 3)
                            initData();
                        else
                            ToastUtil.showToast(CommentActivity.this, "请求数据失败");
                    }
                } catch (IOException e) {
                    ToastUtil.showToast(CommentActivity.this, "请求数据失败");
                }
            }
        }).start();
    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        iv_teach_search = (ImageView) findViewById(R.id.iv_teach_search);
        mDes = (TextView) findViewById(R.id.tv_teach_title);
        listView = (ListView) findViewById(R.id.Comment_lv);
        iv_teach_search.setVisibility(View.GONE);
        mDes.setText("综合评价");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mCommentItemLl = (LinearLayout) findViewById(R.id.comment_item_ll);
        mCommentItemMore = (TextView) findViewById(R.id.comment_item_more);
    }
}
