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
import com.yitong.ChuangKeYuan.ui.DesignActivity;
import com.yitong.ChuangKeYuan.ui.PlanActivity;
import com.yitong.ChuangKeYuan.ui.TeachActivity;
import com.yitong.ChuangKeYuan.utils.UiUtils;

/**
 * Created by Say GoBay on 2016/5/23.
 * 建设方案页面
 */
public class HomeFragment_build extends Fragment implements OnClickListener {

    //页面声明控件；
    private RelativeLayout mTeach, mPlan, mDesign;
    private ImageView mIvBuild, mIvBuildTeach, mIvBuildPlan, mIvBuildDesign;
    private TextView mTvBuildTeachTitle, mTvBuildTeach, mTvBuildPlanTitle, mTvBuildPlan, mTvBuildDesignTitle, mTvBuildDesign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = UiUtils.inflateView(R.layout.fragment_home_build);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        mTeach = (RelativeLayout) view.findViewById(R.id.rl_build_teach);
        mPlan = (RelativeLayout) view.findViewById(R.id.rl_build_plan);
        mDesign = (RelativeLayout) view.findViewById(R.id.rl_build_design);

        mIvBuild = (ImageView) view.findViewById(R.id.iv_build);
        mIvBuildTeach = (ImageView) view.findViewById(R.id.iv_build_teach);
        mIvBuildPlan = (ImageView) view.findViewById(R.id.iv_build_plan);
        mIvBuildDesign = (ImageView) view.findViewById(R.id.iv_build_design);

        mTvBuildTeachTitle = (TextView) view.findViewById(R.id.tv_build_teach_title);
        mTvBuildTeach = (TextView) view.findViewById(R.id.tv_build_teach);
        mTvBuildPlanTitle = (TextView) view.findViewById(R.id.tv_build_plan_title);
        mTvBuildPlan = (TextView) view.findViewById(R.id.tv_build_plan);
        mTvBuildDesignTitle = (TextView) view.findViewById(R.id.tv_build_design_title);
        mTvBuildDesign = (TextView) view.findViewById(R.id.tv_build_design);

        try {
            mIvBuild.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(0).getBanner()));
        } catch (Exception e) {
            mIvBuild.setImageResource(R.drawable.noimage);

        }
        try {
            mIvBuildTeach.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(0).getParent().get(0).getSmeta()));
        } catch (Exception e) {
            mIvBuildTeach.setImageResource(R.drawable.noimage);

        }
        try {
            mIvBuildPlan.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(0).getParent().get(1).getSmeta()));
        } catch (Exception e) {
            mIvBuildPlan.setImageResource(R.drawable.noimage);

        }
        try {
            mIvBuildDesign.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(0).getParent().get(2).getSmeta()));
        } catch (Exception e) {
            mIvBuildDesign.setImageResource(R.drawable.noimage);
        }


        try {
            mTvBuildTeachTitle.setText(MyApplication.getUtil().mListBeen.get(0).getParent().get(0).getName());
        } catch (Exception e) {}
        try {
            mTvBuildTeach.setText(MyApplication.getUtil().mListBeen.get(0).getParent().get(0).getDescription());
        } catch (Exception e) {}
        try {
            mTvBuildPlanTitle.setText(MyApplication.getUtil().mListBeen.get(0).getParent().get(1).getName());
        } catch (Exception e) {}
        try {
            mTvBuildPlan.setText(MyApplication.getUtil().mListBeen.get(0).getParent().get(1).getDescription());
        } catch (Exception e) {}
        try {
            mTvBuildDesignTitle.setText(MyApplication.getUtil().mListBeen.get(0).getParent().get(2).getName());
        } catch (Exception e) {}
        try {
            mTvBuildDesign.setText(MyApplication.getUtil().mListBeen.get(0).getParent().get(2).getDescription());
        } catch (Exception e) {}

        mTeach.setOnClickListener(this);
        mPlan.setOnClickListener(this);
        mDesign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //幼儿园创客教育方案
            case R.id.rl_build_teach:
                startActivity(new Intent(getActivity().getApplicationContext(), TeachActivity.class));
                break;
            //班级项目有策划方案
            case R.id.rl_build_plan:
                startActivity(new Intent(getActivity().getApplicationContext(), PlanActivity.class));
                break;
            //创客微项目设计方案
            case R.id.rl_build_design:
                startActivity(new Intent(getActivity().getApplicationContext(), DesignActivity.class));
                break;
        }
    }
}

