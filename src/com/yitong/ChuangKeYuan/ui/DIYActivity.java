package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;

/**
 * Created by Say GoBay on 2016/5/27.
 * 创客DIY页面
 */
public class DIYActivity extends Activity implements OnClickListener {

    public TextView mDes, mTvScheduleTitle, mTvSchedule, mTvVideoTitle, mTvVideo, mTvNoteTitle, mTvNote, mTvInteractTitle, mTvInteract;
    public ImageView mBack, mSearch, mIvDiy, mIvSchedule, mIvVideo, mIvNote, mIvInteract;
    private RelativeLayout mSchedule, mVideo, mNote, mRlDiySchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy);
        //初始化标题栏
        initTitlebar();
        //初始化控件
        initView();
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mDes = (TextView) findViewById(R.id.tv_teach_title);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mDes.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getName());
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mSchedule = (RelativeLayout) findViewById(R.id.rl_diy_schedule);
        mVideo = (RelativeLayout) findViewById(R.id.rl_diy_video);
        mNote = (RelativeLayout) findViewById(R.id.rl_diy_note);
        mRlDiySchool = (RelativeLayout) findViewById(R.id.rl_diy_school);

        mTvScheduleTitle = (TextView) findViewById(R.id.tv_schedule_title);
        mTvSchedule = (TextView) findViewById(R.id.tv_schedule);
        mTvVideoTitle = (TextView) findViewById(R.id.tv_video_title);
        mTvVideo = (TextView) findViewById(R.id.tv_video);
        mTvInteractTitle = (TextView) findViewById(R.id.tv_interact_title);
        mTvInteract = (TextView) findViewById(R.id.tv_interact);
        mTvNoteTitle = (TextView) findViewById(R.id.tv_note_title);
        mTvNote = (TextView) findViewById(R.id.tv_note);

        try {
            mTvScheduleTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(0).getName());
        } catch (Exception e) {
        }
        try {
            mTvSchedule.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(0).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvNoteTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(1).getName());
        } catch (Exception e) {
        }
        try {
            mTvNote.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(1).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvVideoTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(2).getName());
        } catch (Exception e) {
        }
        try {
            mTvVideo.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(2).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvInteractTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(3).getName());
        } catch (Exception e) {
        }
        try {
            mTvInteract.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(3).getDescription());
        } catch (Exception e) {
        }


        mIvDiy = (ImageView) findViewById(R.id.iv_diy);
        mIvSchedule = (ImageView) findViewById(R.id.iv_schedule);
        mIvVideo = (ImageView) findViewById(R.id.iv_video);
        mIvNote = (ImageView) findViewById(R.id.iv_note);
        mIvInteract = (ImageView) findViewById(R.id.iv_interact);


        try {
            mIvDiy.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getBanner()));
        } catch (Exception e) {
            mIvDiy.setImageResource(R.drawable.noimage);
        }
        try {
            mIvSchedule.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(0).getSmeta()));
        } catch (Exception e) {
            mIvSchedule.setImageResource(R.drawable.noimage);
        }
        try {
            mIvVideo.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(1).getSmeta()));
        } catch (Exception e) {
            mIvVideo.setImageResource(R.drawable.noimage);
        }
        try {
            mIvNote.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(2).getSmeta()));
        } catch (Exception e) {
            mIvNote.setImageResource(R.drawable.noimage);
        }
        try {
            mIvInteract.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(3).getSmeta()));
        } catch (Exception e) {
            mIvInteract.setImageResource(R.drawable.noimage);
        }

        mSchedule.setOnClickListener(this);
        mVideo.setOnClickListener(this);
        mNote.setOnClickListener(this);
        mRlDiySchool.setOnClickListener(this);

        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
    }

    String ID = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //日程安排
            case R.id.rl_diy_schedule:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(0).getTerm_id();
                } catch (Exception e) {
                    ID = "27";
                }
                startActivity(new Intent(DIYActivity.this, ScheduleActivity.class).putExtra("id", ID));
                break;
            //教学笔记
            case R.id.rl_diy_note:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(1).getTerm_id();
                } catch (Exception e) {
                    ID = "28";
                }
                startActivity(new Intent(DIYActivity.this, NoteActivity.class).putExtra("id", ID));
                break;
            //教学微视频
            case R.id.rl_diy_video:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(2).getTerm_id();
                } catch (Exception e) {
                    ID = "29";
                }
                startActivity(new Intent(DIYActivity.this, VideoActivity.class).putExtra("id", ID));
                break;
            //创客做中学
            case R.id.rl_diy_school:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getParent_ParentBean().get(3).getTerm_id();
                } catch (Exception e) {
                    ID = "45";
                }
                startActivity(new Intent(DIYActivity.this, DoSchoolActivity.class).putExtra("id", ID));
                break;
            //返回
            case R.id.iv_teach_back:
                finish();
                break;
        }
    }
}
