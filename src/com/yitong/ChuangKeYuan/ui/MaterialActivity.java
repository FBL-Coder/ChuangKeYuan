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
 * 创客材料页面
 */
public class MaterialActivity extends Activity implements OnClickListener {

    public TextView mDes, mTvXueTitle, mTvXue, mTvXiTitle, mTvXi, mTvChuangTitle, mTvChuang;
    public ImageView mBack, mSearch, mIvXue, mIvXi, mIvChuang, mIvSchool;
    private RelativeLayout mXue, mXi, mChuang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        //初始化标题栏
        initTitlebar();
        //初始化控件
        initView();
    }
    /**
     * 初始化标题栏
     */
    private void initView() {

        mTvXueTitle = (TextView) findViewById(R.id.tv_xue_title);
        mTvXue = (TextView) findViewById(R.id.tv_xue);
        mTvXiTitle = (TextView) findViewById(R.id.tv_xi_title);
        mTvXi = (TextView) findViewById(R.id.tv_xi);
        mTvChuangTitle = (TextView) findViewById(R.id.tv_chuang_title);
        mTvChuang = (TextView) findViewById(R.id.tv_chuang);

        try {
            mTvXueTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(0).getName());
        } catch (Exception e) {
        }
        try {
            mTvXue.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(0).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvXiTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(1).getName());
        } catch (Exception e) {
        }
        try {
            mTvXi.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(1).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvChuangTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(2).getName());
        } catch (Exception e) {
        }
        try {
            mTvChuang.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(2).getDescription());
        } catch (Exception e) {
        }

        mIvXue = (ImageView) findViewById(R.id.iv_xue);
        mIvXi = (ImageView) findViewById(R.id.iv_xi);
        mIvChuang = (ImageView) findViewById(R.id.iv_chuang);
        mIvSchool = (ImageView) findViewById(R.id.iv_school);

        try {
            mIvXue.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(0).getSmeta()));
        } catch (Exception e) {
            mIvXue.setImageResource(R.drawable.noimage);
        }
        try {
            mIvXi.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(1).getSmeta()));

        } catch (Exception e) {
            mIvXi.setImageResource(R.drawable.noimage);
        }
        try {
            mIvChuang.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(2).getSmeta()));
        } catch (Exception e) {
            mIvChuang.setImageResource(R.drawable.noimage);
        }
        try {
            mIvSchool.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getBanner()));
        } catch (Exception e) {
            mIvSchool.setImageResource(R.drawable.noimage);
        }

        mXue = (RelativeLayout) findViewById(R.id.rl_material_xue);
        mXi = (RelativeLayout) findViewById(R.id.rl_material_xi);
        mChuang = (RelativeLayout) findViewById(R.id.rl_material_chuang);

        mXue.setOnClickListener(this);
        mXi.setOnClickListener(this);
        mChuang.setOnClickListener(this);

        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mDes = (TextView) findViewById(R.id.tv_teach_title);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        try {
            mDes.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getName());
        }catch (Exception e){}
    }

    String ID = "";
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_material_xue:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(0).getTerm_id();
                } catch (Exception e) {
                    ID = "24";
                }
                startActivity(new Intent(this, XueActivity.class).putExtra("id", ID));
                break;
            case R.id.rl_material_xi:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(1).getTerm_id();
                } catch (Exception e) {
                    ID = "25";
                }
                startActivity(new Intent(this, XiActivity.class).putExtra("id", ID));
                break;
            case R.id.rl_material_chuang:
                try {
                    ID = MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getParent_ParentBean().get(2).getTerm_id();
                } catch (Exception e) {
                    ID = "26";
                }
                startActivity(new Intent(this, ChuangActivity.class).putExtra("id", ID));
                break;
            case R.id.iv_teach_back:
                finish();
                break;
        }
    }
}
