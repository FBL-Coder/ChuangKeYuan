package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.yitong.ChuangKeYuan.Application.MyApplication;

/**
 * Created by Say GoBay on 2016/5/12.
 * ui操作处理的工具类
 */
public class UiUtils {

    //获取context对象
    public static Context getContext() {
        return MyApplication.getContext();
    }

    //获取handler对象
    public static Handler getMainThreadHandler() {
        return MyApplication.getHandler();
    }

    //获取主线程的线程id
    public static int getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    //获取字符串资源
    public static String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    //获取字符串数组资源
    public static String[] getStringArray(int resId) {
        return getContext().getResources().getStringArray(resId);
    }

    //获取drawable
    public static Drawable getDrawable(int resId) {
        return getContext().getResources().getDrawable(resId);
    }

    //获取color
    public static int getColor(int resId) {
        return getContext().getResources().getColor(resId);
    }
    //获取颜色的状态选择器
    public static ColorStateList getColorStateList(int resId) {
        return getContext().getResources().getColorStateList(resId);
    }

    //获取dimen下的值
    public static int getDimen(int resId) {
        return getContext().getResources().getDimensionPixelSize(resId);
    }

    // dp--px
    public static int dip2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;//获取屏幕密度
        return (int) (dp*density + 0.5);
    }

    //px--dp
    public static int px2dip(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;//获取屏幕密度
        return (int) (px/density + 0.5);
    }

    //判断当前线程是否是主线程
    public static boolean isRunOnUiThread() {
        //获取主线程的线程id
        int mainThreadId = getMainThreadId();
        //获取当前线程的id
        int currentThreadId = android.os.Process.myTid();

        return mainThreadId == currentThreadId;
    }
    //保证r一定运行在主线程中
    public static void runOnUiThread(Runnable r) {
        if(isRunOnUiThread()) {
            //在主线程
            r.run();
        } else {
            //不在主线程  将r丢到主线程的消息队列里面
            getMainThreadHandler().post(r);
        }
    }

    //加载布局
    public static View inflateView(int resId) {
        return View.inflate(getContext(), resId, null);
    }
}
