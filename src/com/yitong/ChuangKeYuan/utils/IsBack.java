package com.yitong.ChuangKeYuan.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Say GoBay on 2016/7/26.
 * 判断程序是在前台还是后台的工具类
 */
public  class IsBack {

    /**
     * 判断程序是否在后台
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
}
