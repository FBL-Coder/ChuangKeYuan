package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.utils.Constant;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.PreferenceManager;

/**
 * Created by Say GoBay on 2016/5/16.
 * 引导页面
 */
public class GuideActivity extends Activity implements OnClickListener {

    private ViewPager mViewpager;
    private Button mGoHome,mGoHome1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
        //初始化控件
        initView();
        //设置显示信息
        setMessasge();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        mViewpager = (ViewPager) findViewById(R.id.vp_guid_viewpager);
        mGoHome = (Button) findViewById(R.id.btn_guid_goHome);
        mGoHome1 = (Button) findViewById(R.id.btn_guid_goHome1);

        SharedPreferences preferences = getSharedPreferences("token", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token","");
        editor.commit();
    }
    /**
     * 设置显示信息
     */
    private void setMessasge() {

        mViewpager.setAdapter(new Myadapter());
        // 监听viewpager界面切换操作
        mViewpager.setOnPageChangeListener(onPageChangeListener);
    }
    // viewpager界面切换监听
    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        // 当界面切换完成调用的方法
        // position : 切换界面的位置
        @Override
        public void onPageSelected(int position) {
            if (position == DatasUtil.getGuideData(GuideActivity.this).size() - 1) {
                mGoHome.setVisibility(View.VISIBLE);
                mGoHome.setOnClickListener(GuideActivity.this);
            } else {
                mGoHome.setVisibility(View.INVISIBLE);
                mGoHome.setOnClickListener(null);
            }
        }
        // 滑动的时候调用的方法
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        // 滑动状态改变的时候
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private class Myadapter extends PagerAdapter {
        // 设置条目的个数，imageview的个数
        @Override
        public int getCount() {
            return DatasUtil.getGuideData(GuideActivity.this).size();
        }

        // 判断viewpager的页面的view是否和object一致
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 添加viewpager的条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 根据条目的位置获取条目显示的相应的图片
            ImageView imageView = DatasUtil.getGuideData(GuideActivity.this).get(position);
            // 将imageview添加到viewpager中
            container.addView(imageView);
            // 返回的就是添加的view对象
            return imageView;
        }

        // 销毁viewpager的条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_guid_goHome:
                mGoHome.setVisibility(View.GONE);
                mGoHome1.setVisibility(View.VISIBLE);
                // 跳转主界面
                startActivity(new Intent(GuideActivity.this, HomeActivity.class));
                // 保存不是第一次进入的状态（获取是在splashactivity）
                PreferenceManager.saveBoolean(getApplicationContext(),
                        Constant.FIRST_ENTER, false);
                // 销毁界面
                finish();
                break;
        }
    }
}
