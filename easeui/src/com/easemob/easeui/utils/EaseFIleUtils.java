package com.easemob.easeui.utils;

import android.content.Context;
import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 作者：傅博龍  时间： 2016/8/5.
 */
public class EaseFIleUtils {

    public static boolean fileIsExists(String name, Context context) {
        try {
            File file = null;

            file = context.getFileStreamPath(name + ".txt");
            Log.i("000000", file.toURI().toString());

            if (!file.exists()) {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    public static String readFile(String name,Context context) throws IOException {
        String data = "";
        try {
            FileInputStream fin = context.openFileInput(name+".txt");
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            data = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
            Log.i("HTTPData", "读取成功");
        } catch (Exception e) {
//            e.printStackTrace();
            Log.i("HTTPData", "读取失败");
        }
        return data;
    }
}
