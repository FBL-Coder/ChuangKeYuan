package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.widget.ProgressBar;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.widget.ChromeFloatingCirclesDrawable;

/**
 * 作者：FBL  时间： 2016/7/12.
 * 自定义进度条
 */
public class Progressbar_Util {

    public static void ProVisibility(ProgressBar mProgressBar,Context context){
        Rect bounds = mProgressBar.getIndeterminateDrawable().getBounds();
        mProgressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(context)
                .colors(getProgressDrawableColors(context))
                .build());
        mProgressBar.getIndeterminateDrawable().setBounds(bounds);
    }
    private static int[] getProgressDrawableColors(Context context) {
        int[] colors = new int[4];
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        colors[0] = prefs.getInt(MyApplication.getContext().getString(R.string.firstcolor_pref_key), MyApplication.getContext().getResources().getColor(R.color.colorAccent));
        colors[1] = prefs.getInt(MyApplication.getContext().getString(R.string.secondcolor_pref_key), MyApplication.getContext().getResources().getColor(R.color.btn_login_normal));
        colors[2] = prefs.getInt(MyApplication.getContext().getString(R.string.thirdcolor_pref_key), MyApplication.getContext().getResources().getColor(R.color.possible_result_points));
        colors[3] = prefs.getInt(MyApplication.getContext().getString(R.string.fourthcolor_pref_key), MyApplication.getContext().getResources().getColor(R.color.result_points));
        return colors;
    }
}
