package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jacksen.taggroup.ITag;
import com.jacksen.taggroup.ITagBean;
import com.jacksen.taggroup.OnTagClickListener;
import com.jacksen.taggroup.SuperTagGroup;
import com.jacksen.taggroup.SuperTagUtil;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.WaveProgress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Author：FBL  Time： 2017/8/31.
 * 老师评价
 */

public class TeacherCommentActivitv extends Activity {

    private WaveProgress progress;
    private SuperTagGroup group;
    private View view;
    private List<String> Tags;
    private List<String> SelectTags;
    private int[] nums;
    private int[] num_Sum;
    private int sum;
    private TextView mTeacherCommemtBtn;
    private EditText mTeacherCommentEdittv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_comment);
        initData();
        initView();
    }

    private void initData() {
        nums = new int[]{12, 8, 7, 9, 9, 9, 6, 4, 4, 7, 15, 10};
        Tags = new ArrayList<>();
        SelectTags = new ArrayList<>();
        Tags.add("整体设计");
        Tags.add("细节功能");
        Tags.add("结构设计");
        Tags.add("材料功能");
        Tags.add("创意实现");
        Tags.add("作品设计");
        Tags.add("艺术表现");
        Tags.add("设计规范");
        Tags.add("制作规范");
        Tags.add("完成作品");
        Tags.add("团队展示");
        Tags.add("分工协作");
    }

    private void initView() {
        progress = (WaveProgress) findViewById(R.id.wave_progress_bar);
        group = (SuperTagGroup) findViewById(R.id.tag_group);
        view = findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        group.setMaxSelectedNum(Tags.size());
        progress.setMaxValue(100);
        progress.setValue(0);
        ITagBean.Builder builder = new ITagBean.Builder();
        for (int i = 0; i < Tags.size(); i++) {
            ITagBean tagBean = builder.setTag(Tags.get(i))
                    .setCornerRadius(SuperTagUtil.dp2px(this, 7.0f))
                    .setHorizontalPadding(SuperTagUtil.dp2px(this, 10))
                    .setBgColor(Color.parseColor("#4ebcd3"))
                    .setTextColor(Color.BLACK)
                    .setTextSize(14)
                    .setCheckedBgColor(0xffffbb33)
                    .create();
            group.appendTag(tagBean);
        }
        group.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public boolean onTagClick(int position, ITag tag) {
                return false;
            }

            @Override
            public void onSelected(SparseArray<View> selectedViews) {
                sum = 0;
                SelectTags.clear();
                for (int i = 0; i < selectedViews.size(); i++) {
                    sum += nums[selectedViews.keyAt(i)];
                    SelectTags.add(Tags.get(selectedViews.keyAt(i)));
                }
                if (sum > 98)
                    sum = 100;
                progress.setValue(sum);
            }
        });

        mTeacherCommemtBtn = (TextView) findViewById(R.id.teacher_commemt_btn);
        mTeacherCommentEdittv = (EditText) findViewById(R.id.teacher_comment_edittv);
        mTeacherCommemtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edittv = mTeacherCommentEdittv.getText().toString().trim();
                if (edittv == null)
                    edittv = "";
                String UpTags = "";
                for (int i = 0; i < SelectTags.size(); i++) {
                    UpTags += SelectTags.get(i) + ",";
                }
                if (sum == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TeacherCommentActivitv.this);
                    builder.setTitle("提示");
                    builder.setMessage("您还没有对作品进行评分，是否继续提交？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final String finalUpTags = UpTags;
                    final String edittv_final = edittv;
                    builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            upComment(edittv_final, finalUpTags);
                        }
                    });
                    builder.create().show();
                } else {
                    upComment(edittv, UpTags);
                }
            }
        });
    }

    private void upComment(String edittv, String upTags) {
        final FormBody comment_boby = new FormBody.Builder()
                .add("username", MyApplication.getusername())
                .add("password", MyApplication.getuserpassword())
                .add("aid", getIntent().getStringExtra("aid"))
                .add("tag", upTags)
                .add("content", edittv)
                .add("value", sum + "")
                .build();
//        Log.i(TAG, "upComment: "+upTags);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ReturnResult result = (ReturnResult) msg.obj;
                if ("0".equals(result.getCode())) {
                    ToastUtil.showToast(TeacherCommentActivitv.this, "评价成功");
                    MyApplication.setFLAG(1001);
                    finish();
                } else if ("10009".equals(result.getCode())) {
                    ToastUtil.showToast(TeacherCommentActivitv.this, "不可重复评价!");
                } else
                    ToastUtil.showToast(TeacherCommentActivitv.this, "评价失败");

            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data;
                ReturnResult result;
                try {
                    data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_AddTeacrherComment, comment_boby);
                    Gson gson = new Gson();
                    result = gson.fromJson(data, ReturnResult.class);
                    Log.i("Comment", data.toString());

                    Message message = handler.obtainMessage();
                    message.obj = result;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
