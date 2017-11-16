package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.yitong.ChuangKeYuan.Application.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者：FBL  时间： 2016/7/7.
 * 网络工具
 */
public class HttpDataUtil {

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 利用OKHTTP3进行POST请求；
     *
     * @param url
     * @param
     * @return
     * @throws IOException
     */
    public static String getPublicData_POST(String url, RequestBody formBody) throws IOException {
        try {
            if (isNetworkConnected(MyApplication.getContext())) {
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                Response response = MyApplication.getClient().newCall(request).execute();
                Log.i("NETWORK", response.code() + "");
                if (response.code() == 200) {
                    return response.body().string();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            ToastUtil.showToast(MyApplication.getContext(), "数据加载失败！网络服务器异常...");
                            Looper.loop();
                        }
                    }.start();
                    return "";
                }
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        new Thread() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                ToastUtil.showToast(MyApplication.getContext(), "请检查网络连接");
                                Looper.loop();
                            }
                        }.start();
                    }
                }.start();
                return "";
            }
        } catch (Exception e) {
            Log.e("OkHttp3--Exception", e + "");
            return "";
        }
    }

    /**
     * 异步进行下载文件；
     *
     * @param url
     * @param formBody
     * @param filename
     * @return
     * @throws IOException
     */

    public static boolean downLoadFile(String url, RequestBody formBody, Handler mHandler, String filename) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = MyApplication.getClient().newCall(request).execute();

        if (response.code() == 200) {
            InputStream is = null;
            byte[] buf = new byte[1024 * 5];
            int len = 0;
            FileOutputStream fos = null;
            try {
                is = response.body().byteStream();
                long total = response.body().contentLength();
                String savePath = "/sdcard/ckyDownload/";
                File file1 = new File(savePath);
                if (!file1.exists()) {
                    file1.mkdir();
                }

                File file = new File(savePath + filename);
                fos = new FileOutputStream(file);
                long sum = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    int progress = (int) (sum * 1.0f / total * 100);
                    Message msg = mHandler.obtainMessage();
                    msg.what = 11;
                    msg.arg1 = progress;
                    mHandler.sendMessage(msg);
                }
                fos.flush();
                Log.d("DOWN", "文件下载成功");

            } catch (Exception e) {
                Log.d("DOWN", "文件下载失败" + e);
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                }
                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                }
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    ToastUtil.showToast(MyApplication.getContext(), "附件下载失败！网络服务器异常...");
                    Looper.loop();
                }
            }).start();
        }
        return false;
    }

    /**
     * 查看网络状态
     *
     * @param context
     * @return
     */

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 查看缓存文件是否存在
     *
     * @param name
     * @return
     */

    public static boolean fileIsExists(String name) {
        try {
            File file = null;

            file = MyApplication.getContext().getFileStreamPath(name + ".txt");
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
}
