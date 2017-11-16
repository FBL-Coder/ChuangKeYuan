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
 * 创客工作室子页面
 */
public class WorkRoomItemActivity extends Activity implements OnClickListener {

    private RelativeLayout mZuopin, mXieZhu, mGongXiang, mLunTan;

    private ImageView mBack, mSearch, mIvSchool, mIvMaterial, mivDiy, mIvCase, mIv_4;
    private TextView mDes, mTvMaterialTitle, mTvMaterial, mTvDiyTitle, mTvDiy, mTvCaseTitle, mTvCase, mTv_4, mTv_41;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workroomitem);
        initView();
        initBar();
    }

    /**
     * 初始化标题栏
     */
    private void initBar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mDes = (TextView) findViewById(R.id.tv_teach_title);
        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
        try {
            mDes.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getName());
        } catch (Exception e) {
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mZuopin = (RelativeLayout) findViewById(R.id.rl_school_material);
        mXieZhu = (RelativeLayout) findViewById(R.id.rl_school_diy);
        mGongXiang = (RelativeLayout) findViewById(R.id.rl_school_case);
        mLunTan = (RelativeLayout) findViewById(R.id.rl_school_4);

        mIvSchool = (ImageView) findViewById(R.id.iv_school);
        mIvMaterial = (ImageView) findViewById(R.id.iv_material);
        mivDiy = (ImageView) findViewById(R.id.iv_diy);
        mIvCase = (ImageView) findViewById(R.id.iv_case);
        mIv_4 = (ImageView) findViewById(R.id.iv_4);

        mTvMaterialTitle = (TextView) findViewById(R.id.tv_material);
        mTvMaterial = (TextView) findViewById(R.id.tv_material1);
        mTvDiyTitle = (TextView) findViewById(R.id.tv_diy);
        mTvDiy = (TextView) findViewById(R.id.tv_diy1);
        mTvCaseTitle = (TextView) findViewById(R.id.tv_case);
        mTvCase = (TextView) findViewById(R.id.tv_case1);
        mTv_4 = (TextView) findViewById(R.id.tv_4);
        mTv_41 = (TextView) findViewById(R.id.tv_41);

        try {
            mIvSchool.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getBanner()));
        } catch (Exception e) {
            mIvSchool.setImageResource(R.drawable.noimage);
        }
        try {
            mIvMaterial.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(0).getSmeta()));

        } catch (Exception e) {
            mIvMaterial.setImageResource(R.drawable.noimage);
        }
        try {
            mivDiy.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(1).getSmeta()));
        } catch (Exception e) {
            mivDiy.setImageResource(R.drawable.noimage);
        }
        try {
            mIvCase.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(2).getSmeta()));
        } catch (Exception e) {
            mIvCase.setImageResource(R.drawable.noimage);
        }
        try {
            mIv_4.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(3).getSmeta()));
        } catch (Exception e) {
            mIv_4.setImageResource(R.drawable.noimage);
        }


        try {
            mTvMaterialTitle.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(0).getName());
        } catch (Exception e) {
        }
        try {
            mTvMaterial.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(0).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvDiyTitle.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(1).getName());
        } catch (Exception e) {
        }
        try {
            mTvDiy.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(1).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvCaseTitle.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(2).getName());
        } catch (Exception e) {
        }
        try {
            mTvCase.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(0).getDescription());
        } catch (Exception e) {
        }
        try {
            mTv_4.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(3).getName());
        } catch (Exception e) {
        }
        try {
            mTv_41.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(3).getDescription());
        } catch (Exception e) {
        }

        mZuopin.setOnClickListener(this);
        mXieZhu.setOnClickListener(this);
        mGongXiang.setOnClickListener(this);
        mLunTan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(WorkRoomItemActivity.this, WorkRoomActivity.class);
        switch (v.getId()) {
            case R.id.iv_teach_back:
                finish();
                break;
            case R.id.rl_school_material:
                intent.putExtra("index", 0);
                intent.putExtra("termid"
                        , MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(0).getTerm_id());
                startActivity(intent);
                break;
            case R.id.rl_school_diy:
                intent.putExtra("index", 1);
                intent.putExtra("termid"
                        , MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(1).getTerm_id());
                startActivity(intent);
                break;
            case R.id.rl_school_case:
                intent.putExtra("index", 2);
                intent.putExtra("termid"
                        , MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(2).getTerm_id());
                startActivity(intent);
                break;
            case R.id.rl_school_4:
                intent.putExtra("index", 3);
                intent.putExtra("termid"
                        , MyApplication.getUtil().mListBeen.get(2).getParent().get(3).getParent_ParentBean().get(3).getTerm_id());
                startActivity(intent);
                break;
        }
    }
}
