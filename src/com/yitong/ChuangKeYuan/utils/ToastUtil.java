package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Say GoBay on 2016/8/15.
 */
public class ToastUtil {
    private static Toast toast;
    static long lastClick;
    public static void showToast(Context context, String content) {

        if (System.currentTimeMillis() - lastClick <= 1000) {
            return;
        }
        lastClick = System.currentTimeMillis();
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
