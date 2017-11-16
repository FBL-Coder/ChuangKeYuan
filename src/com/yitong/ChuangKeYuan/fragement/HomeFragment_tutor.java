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
import com.yitong.ChuangKeYuan.ui.ParentsActivity;
import com.yitong.ChuangKeYuan.ui.TeacherActivity;
import com.yitong.ChuangKeYuan.utils.UiUtils;

/**
 * 创客导师
 */
public class HomeFragment_tutor extends Fragment implements OnClickListener {

    private RelativeLayout mTeacher, mParent;
    private ImageView mIvTutor, mIvTutor1, mIvTutor2;
    private TextView mTvTutor1, mTvTutor2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = UiUtils.inflateView(R.layout.fragment_home_tutor);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        mTeacher = (RelativeLayout) view.findViewById(R.id.rl_tutor_teacher);
        mParent = (RelativeLayout) view.findViewById(R.id.rl_tutor_parent);
        mIvTutor = (ImageView) view.findViewById(R.id.iv_tutor);
        mIvTutor1 = (ImageView) view.findViewById(R.id.iv_tutor1);
        mIvTutor2 = (ImageView) view.findViewById(R.id.iv_tutor2);

        mTvTutor1 = (TextView) view.findViewById(R.id.tv_tutor1);
        mTvTutor2 = (TextView) view.findViewById(R.id.tv_tutor2);

        try {
            mIvTutor.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(1).getBanner()));
        } catch (Exception e) {
            mIvTutor.setImageResource(R.drawable.noimage);
        }
        try {
            mIvTutor1.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(1).getParent().get(0).getSmeta()));
        } catch (Exception e) {
            mIvTutor1.setImageResource(R.drawable.noimage);
        }
        try {
            mIvTutor2.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(1).getParent().get(1).getSmeta()));
        } catch (Exception e) {
            mIvTutor2.setImageResource(R.drawable.noimage);
        }
        try {
            mTvTutor1.setText(MyApplication.getUtil().mListBeen.get(1).getParent().get(0).getName());
        } catch (Exception e) {
        }
        try {
            mTvTutor2.setText(MyApplication.getUtil().mListBeen.get(1).getParent().get(1).getName());
        } catch (Exception e) {
        }

        mTeacher.setOnClickListener(this);
        mParent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //创客教师介绍
            case R.id.rl_tutor_teacher:
                startActivity(new Intent(getActivity().getApplicationContext(), TeacherActivity.class));
                break;
            //创客家长介绍
            case R.id.rl_tutor_parent:
                startActivity(new Intent(getActivity().getApplicationContext(), ParentsActivity.class));
                break;
        }
    }
}
