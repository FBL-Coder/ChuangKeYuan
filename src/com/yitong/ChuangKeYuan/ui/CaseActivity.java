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
 * 创客园课程,案例页面
 */
public class CaseActivity extends Activity implements OnClickListener {

    public TextView mDes, mTvClassTitle, mTvClass, mTvTeachNoteTitle, mTvTeachNote, mTvCaseTitle, mTvCase;
    public ImageView mBack, mSearch, mIvClass, mIvTeachNote, mIvCase, mIvCaseTit;
    private RelativeLayout mCourse, mNote, mCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
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
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mCourse = (RelativeLayout) findViewById(R.id.rl_case_course);
        mNote = (RelativeLayout) findViewById(R.id.rl_case_note);
        mCase = (RelativeLayout) findViewById(R.id.rl_case_case);

        mTvClassTitle = (TextView) findViewById(R.id.tv_class_title);
        mTvClass = (TextView) findViewById(R.id.tv_class);
        mTvTeachNoteTitle = (TextView) findViewById(R.id.tv_teachnote_title);
        mTvTeachNote = (TextView) findViewById(R.id.tv_teachnote);
        mTvCaseTitle = (TextView) findViewById(R.id.tv_case_title);
        mTvCase = (TextView) findViewById(R.id.tv_case);

        try {
            mDes.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getName());
        } catch (Exception e) {
        }
        try {
            mTvClassTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(0).getName());
        } catch (Exception e) {
        }
        try {
            mTvClass.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(0).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvTeachNoteTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(1).getName());
        } catch (Exception e) {
        }
        try {
            mTvCaseTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(2).getName());
        } catch (Exception e) {
        }
        try {
            mTvCaseTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(2).getName());
        } catch (Exception e) {
        }
        try {
            mTvCase.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(2).getDescription());
        } catch (Exception e) {
        }


        mIvClass = (ImageView) findViewById(R.id.iv_class);
        mIvTeachNote = (ImageView) findViewById(R.id.iv_teachnote);
        mIvCase = (ImageView) findViewById(R.id.iv_case);
        mIvCaseTit = (ImageView) findViewById(R.id.iv_case_tit);
        try {
            mIvClass.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(0).getSmeta()));
        } catch (Exception e) {
            mIvClass.setImageResource(R.drawable.noimage);
        }
        try {
            mIvTeachNote.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(1).getSmeta()));
        } catch (Exception e) {
            mIvTeachNote.setImageResource(R.drawable.noimage);
        }
        try {
            mIvCase.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(2).getSmeta()));
        } catch (Exception e) {
            mIvCase.setImageResource(R.drawable.noimage);
        }
        try {
            mIvCaseTit.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getBanner()));
        } catch (Exception e) {
            mIvCaseTit.setImageResource(R.drawable.noimage);
        }

        mCourse.setOnClickListener(this);
        mNote.setOnClickListener(this);
        mCase.setOnClickListener(this);

        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
    }

    String ID = "";
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CaseActivity.this, CaseItemActivity_Cass.class);
        switch (v.getId()) {
            //课本课程
            case R.id.rl_case_course:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(0).getTerm_id();
                } catch (Exception e) {
                    ID = "30";
                }
                intent.putExtra("aid", ID);
                intent.putExtra("index", 0);
                startActivity(intent);
                break;
            //教学笔记
            case R.id.rl_case_note:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(1).getTerm_id();
                } catch (Exception e) {
                    ID = "31";
                }
                intent.putExtra("aid", ID);
                intent.putExtra("index", 1);
                startActivity(intent);
                break;
            //案例集锦
            case R.id.rl_case_case:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getParent_ParentBean().get(2).getTerm_id();
                } catch (Exception e) {
                    ID = "32";
                }
                intent.putExtra("aid", ID);
                intent.putExtra("index", 2);
                startActivity(intent);
                break;
            //返回
            case R.id.iv_teach_back:
                finish();
                break;
        }
    }
}
