package com.yitong.ChuangKeYuan.fragement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.ui.CaseActivity;
import com.yitong.ChuangKeYuan.ui.DIYActivity;
import com.yitong.ChuangKeYuan.ui.MaterialActivity;
import com.yitong.ChuangKeYuan.utils.UiUtils;

/**
 * 创客中学页面
 */
public class HomeFragment_school extends Fragment implements OnClickListener {

    private RelativeLayout mMaterial;
    private RelativeLayout mDiy;
    private RelativeLayout mCase;

    private ImageView mIvSchool, mIvMaterial, mivDiy, mIvCase;
    private TextView mTvMaterialTitle, mTvMaterial, mTvDiyTitle, mTvDiy, mTvCaseTitle, mTvCase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = UiUtils.inflateView(R.layout.fragment_home_school);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        mMaterial = (RelativeLayout) view.findViewById(R.id.rl_school_material);
        mDiy = (RelativeLayout) view.findViewById(R.id.rl_school_diy);
        mCase = (RelativeLayout) view.findViewById(R.id.rl_school_case);

        mIvSchool = (ImageView) view.findViewById(R.id.iv_school);
        mIvMaterial = (ImageView) view.findViewById(R.id.iv_material);
        mivDiy = (ImageView) view.findViewById(R.id.iv_diy);
        mIvCase = (ImageView) view.findViewById(R.id.iv_case);

        mTvMaterialTitle = (TextView) view.findViewById(R.id.tv_material);
        mTvMaterial = (TextView) view.findViewById(R.id.tv_material1);
        mTvDiyTitle = (TextView) view.findViewById(R.id.tv_diy);
        mTvDiy = (TextView) view.findViewById(R.id.tv_diy1);
        mTvCaseTitle = (TextView) view.findViewById(R.id.tv_case);
        mTvCase = (TextView) view.findViewById(R.id.tv_case1);

        try {
            mIvSchool.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getBanner()));
        } catch (Exception e) {
            mIvSchool.setImageResource(R.drawable.noimage);
        }
        try {
            mIvMaterial.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getSmeta()));
        } catch (Exception e) {
            mIvMaterial.setImageResource(R.drawable.noimage);
        }
        try {
            mivDiy.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getSmeta()));
        } catch (Exception e) {
            mivDiy.setImageResource(R.drawable.noimage);
        }
        try {
            mIvCase.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getSmeta()));
        } catch (Exception e) {
            mIvCase.setImageResource(R.drawable.noimage);
        }
        try {
            mTvMaterialTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getName());
        } catch (Exception e) {
        }
        try {
            mTvMaterial.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(0).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvDiyTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getName());
        } catch (Exception e) {
        }
        try {
            mTvDiy.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(1).getDescription());
        } catch (Exception e) {
        }
        try {
            mTvCaseTitle.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getName());
        } catch (Exception e) {
        }
        try {
            mTvCase.setText(MyApplication.getUtil().mListBeen.get(3).getParent().get(2).getDescription());
        } catch (Exception e) {
        }

        mMaterial.setOnClickListener(this);
        mDiy.setOnClickListener(this);
        mCase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //典型的创客材料
            case R.id.rl_school_material:
                startActivity(new Intent(getActivity().getApplicationContext(), MaterialActivity.class));
                break;
            //创客DIY
            case R.id.rl_school_diy:
                startActivity(new Intent(getActivity().getApplicationContext(), DIYActivity.class));
                break;
            //创客园课程、案例
            case R.id.rl_school_case:
                startActivity(new Intent(getActivity().getApplicationContext(), CaseActivity.class));
                break;
        }
    }
}
