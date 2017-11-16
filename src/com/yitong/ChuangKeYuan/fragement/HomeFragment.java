package com.yitong.ChuangKeYuan.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.TabAdapter;
import com.yitong.ChuangKeYuan.ui.SearchActivity;
import com.yitong.ChuangKeYuan.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Say GoBay on 2016/5/17.
 * 主页面
 */
public class HomeFragment extends Fragment implements OnCheckedChangeListener, OnClickListener {

    /**
     * 页签
     */
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonRecommend, mRadioButtonBuild, mRadioButtonTutor, mRadioButtonSpace, mRadioButtonSchool, mRadioButtonShow, mRadioButtonResearch;
    //ViewPager
    private ViewPager mViewPager;
    //Fragment
    private Fragment mRecommend, mBulid, mTutor, mSpace, mSchool, mShow, mResearch;
    /**
     * Fragment的集合
     */
    private List<Fragment> mList;
    //ImageView
    private ImageView mImageView;
    /**
     * 当前被选中的RadioButton距离左侧的距离
     */
    private float mCurrentCheckedRadioLeft;
    /**
    *上面的水平滚动控件
     */
    private HorizontalScrollView mHorizontalScrollView;
    private TranslateAnimation TranslateAnimation;
    private AnimationSet AnimationSet;

    public View view;

    //PopupWindow
    private PopupWindow popupwindow;
    private ImageView imageView, search;
    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = UiUtils.inflateView(R.layout.fragment_home);
        //初始化控件
        initView(view);
        initListener();
        mRadioButtonRecommend.setChecked(true);
        mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
        return view;
    }

    /**
     * RadioGroup点击CheckedChanged监听
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        AnimationSet = new AnimationSet(true);
        AnimationSet.setFillBefore(false);
        AnimationSet.setFillAfter(true);
        AnimationSet.setDuration(100);
        //开始上面蓝色横条图片的动画切换
        mImageView.startAnimation(AnimationSet);
        if (checkedId == R.id.btn_recommend) {
            TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo1), 0f, 0f);
            AnimationSet.addAnimation(TranslateAnimation);
            mViewPager.setCurrentItem(0);// 选择某一页

        } else if (checkedId == R.id.btn_build) {
            TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo2), 0f, 0f);
            AnimationSet.addAnimation(TranslateAnimation);
            mViewPager.setCurrentItem(1);// 选择某一页

        } else if (checkedId == R.id.btn_tutor) {
            TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo3), 0f, 0f);
            AnimationSet.addAnimation(TranslateAnimation);
            mViewPager.setCurrentItem(2);// 选择某一页

        } else if (checkedId == R.id.btn_space) {
            TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo4), 0f, 0f);
            AnimationSet.addAnimation(TranslateAnimation);
            mViewPager.setCurrentItem(3);// 选择某一页

        } else if (checkedId == R.id.btn_school) {
            TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo5), 0f, 0f);
            AnimationSet.addAnimation(TranslateAnimation);
            mViewPager.setCurrentItem(4);// 选择某一页

        } else if (checkedId == R.id.btn_show) {
            TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo6), 0f, 0f);
            AnimationSet.addAnimation(TranslateAnimation);
            mViewPager.setCurrentItem(5);// 选择某一页
        } else if (checkedId == R.id.btn_research) {
            TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo7), 0f, 0f);
            AnimationSet.addAnimation(TranslateAnimation);
            mViewPager.setCurrentItem(6);// 选择某一页

        }

        //更新当前蓝色横条距离左边的距离
        mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();

        mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
    }

    /**
     * 获得当前被选中的RadioButton距离左侧的距离
     */
    private float getCurrentCheckedRadioLeft() {
        if (mRadioButtonRecommend.isChecked()) {
            return getResources().getDimension(R.dimen.rdo1);
        } else if (mRadioButtonBuild.isChecked()) {
            return getResources().getDimension(R.dimen.rdo2);
        } else if (mRadioButtonTutor.isChecked()) {
            return getResources().getDimension(R.dimen.rdo3);
        } else if (mRadioButtonSpace.isChecked()) {
            return getResources().getDimension(R.dimen.rdo4);
        } else if (mRadioButtonSchool.isChecked()) {
            return getResources().getDimension(R.dimen.rdo5);
        } else if (mRadioButtonShow.isChecked()) {
            return getResources().getDimension(R.dimen.rdo6);
        } else if (mRadioButtonResearch.isChecked()) {
            return getResources().getDimension(R.dimen.rdo7);
        }
        return 0f;
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
        mViewPager.setAdapter(new TabAdapter(getChildFragmentManager(), mList));
        mViewPager.setOnPageChangeListener(new TabOnPageChangeListener());
    }

    /**
     * 初始化组件
     * @param view
     */
    private void initView(View view) {
        //页签
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        mRadioButtonRecommend = (RadioButton) view.findViewById(R.id.btn_recommend);
        mRadioButtonBuild = (RadioButton) view.findViewById(R.id.btn_build);
        mRadioButtonTutor = (RadioButton) view.findViewById(R.id.btn_tutor);
        mRadioButtonSpace = (RadioButton) view.findViewById(R.id.btn_space);
        mRadioButtonSchool = (RadioButton) view.findViewById(R.id.btn_school);
        mRadioButtonShow = (RadioButton) view.findViewById(R.id.btn_show);
        mRadioButtonResearch = (RadioButton) view.findViewById(R.id.btn_research);
        mImageView = (ImageView) view.findViewById(R.id.img);
        search = (ImageView) view.findViewById(R.id.iv_search);
        mHorizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);

        imageView = (ImageView) view.findViewById(R.id.iv_home);
        imageView.setOnClickListener(this);
        search.setOnClickListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.vp_homefragment_viewpager);
        mList = new ArrayList<Fragment>();
        mRecommend = new HomeFragment_recommend();
        mBulid = new HomeFragment_build();
        mTutor = new HomeFragment_tutor();
        mSpace = new HomeFragment_space();
        mSchool = new HomeFragment_school();
        mShow = new HomeFragment_show();
        mResearch = new HomeFragment_research();

        //将子页面添加到集合中
        mList.add(mRecommend);
        mList.add(mBulid);
        mList.add(mTutor);
        mList.add(mSpace);
        mList.add(mSchool);
        mList.add(mShow);
        mList.add(mResearch);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                startActivity(new Intent(getActivity().getApplicationContext(), SearchActivity.class));
                break;
            case R.id.rb1:
                mViewPager.setCurrentItem(0);
                popupwindow.dismiss();
                break;
            case R.id.rb2:
                mViewPager.setCurrentItem(1);
                popupwindow.dismiss();
                break;
            case R.id.rb3:
                mViewPager.setCurrentItem(2);
                popupwindow.dismiss();
                break;
            case R.id.rb4:
                mViewPager.setCurrentItem(3);
                popupwindow.dismiss();
                break;
            case R.id.rb5:
                mViewPager.setCurrentItem(4);
                popupwindow.dismiss();
                break;
            case R.id.rb6:
                mViewPager.setCurrentItem(5);
                popupwindow.dismiss();
                break;
            case R.id.rb7:
                mViewPager.setCurrentItem(6);
                popupwindow.dismiss();
                break;
            case R.id.rb8:
                popupwindow.dismiss();
                break;
            case R.id.iv_home:
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                } else {
                    initPopupWindowView();
                    popupwindow.showAsDropDown(v, 0, 0);
                }
                break;
        }
    }

    /**
     * 初始化 PopupWindowView
     */
    private void initPopupWindowView() {
        //获取自定义布局文件pop.xml的视图
        final View customView = UiUtils.inflateView(R.layout.popview_item);
        // 创建PopupWindow实例
        popupwindow = new PopupWindow(customView,
                -1, -1
        );
        //popupwindow页面之外可点
        popupwindow.setOutsideTouchable(true);
        popupwindow.update();
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                return false;
            }
        });
        //popupwindow的radioButton
        rb1 = (RadioButton) customView.findViewById(R.id.rb1);
        rb2 = (RadioButton) customView.findViewById(R.id.rb2);
        rb3 = (RadioButton) customView.findViewById(R.id.rb3);
        rb4 = (RadioButton) customView.findViewById(R.id.rb4);
        rb5 = (RadioButton) customView.findViewById(R.id.rb5);
        rb6 = (RadioButton) customView.findViewById(R.id.rb6);
        rb7 = (RadioButton) customView.findViewById(R.id.rb7);
        rb8 = (RadioButton) customView.findViewById(R.id.rb8);

        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        rb3.setOnClickListener(this);
        rb4.setOnClickListener(this);
        rb5.setOnClickListener(this);
        rb6.setOnClickListener(this);
        rb7.setOnClickListener(this);
        rb8.setOnClickListener(this);
    }

    /**
     * 页卡滑动改变事件
     */
    public class TabOnPageChangeListener implements OnPageChangeListener {
        /**
         * 当滑动状态改变时调用
         * state=0的时候表示什么都没做，就是停在那
         * state=1的时候表示正在滑动
         * state==2的时候表示滑动完毕了
         */
        public void onPageScrollStateChanged(int state) {

        }

        /**
         * 当前页面被滑动时调用
         * position:当前页面
         * positionOffset:当前页面偏移的百分比
         * positionOffsetPixels:当前页面偏移的像素位置
         */
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //当新的页面被选中时调用
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mRadioButtonRecommend.setChecked(true);
                    break;
                case 1:
                    mRadioButtonBuild.setChecked(true);
                    break;
                case 2:
                    mRadioButtonTutor.setChecked(true);
                    break;
                case 3:
                    mRadioButtonSpace.setChecked(true);
                    break;
                case 4:
                    mRadioButtonSchool.setChecked(true);
                    break;
                case 5:
                    mRadioButtonShow.setChecked(true);
                    break;
                case 6:
                    mRadioButtonResearch.setChecked(true);
                    break;
            }
        }
    }
}
