/**
 * Copyright (C) 2013-2014 yitong Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yitong.ChuangKeYuan.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.yitong.ChuangKeYuan.Helper.MyHelper;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.IsBack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;


public class MyApplication extends Application {

    /**
     * Application 本身对象；
     */
    private static MyApplication instance;
    /**
     * main线程
     */
    private static int MainThreadId;
    /**
     * OkHttp3的数据加载对象；单例模式。
     */
    private static OkHttpClient client;
    /**
     * 数据集合处理类，单例模式。
     */
    private static DatasUtil util;
    /**
     * 主线程handler；
     */
    static Handler handler;
    private static Context context;
    /**
     * 加载标记；在这里做保存；
     */
    private static int FLAG;
    /**
     * 同上；
     */
    private static int FLAG_TASK;
    /**
     * 手机品牌
     */
    private static String SIGN;
    /**
     * 首选项存储对象；
     */
    private static SharedPreferences preferences;

    /**
     * 首页的4个页面
     */
    private static List<Fragment> fragments = new ArrayList<Fragment>();
    /**
     * 是否退出，在异常处理中使用；
     */
    private static boolean ISEXIT = true;
    /**
     * 记录在线时长使用标记；
     */
    private static boolean TAG = false;

    @Override
    public void onCreate() {
        getBackTime();//获取开启应用的时间戳；
        super.onCreate();
        /**
         * 异常处理，异常拦截，保存本地，并上传服务器。
         */
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        /**
         * 腾讯异常处理
         */
        CrashReport.initCrashReport(getApplicationContext(), "a6779bf243", false);

        //初始化X5内核；
        initTbs();

        /**
         * 初始化imageLoader；
         */

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).imageDownloader(
                new BaseImageDownloader(getApplicationContext(),
                        10 * 1000, 20 * 1000))// connectTimeout 超时时间
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);

        /**
         * 手机型号以及品牌
         */
        MyApplication.SIGN = Build.MANUFACTURER.toLowerCase();//获取手机品牌及型号
        Log.i("SIGN", MyApplication.SIGN);
        /**
         * facebook 图片加载框架初始化
         */
        Fresco.initialize(this);
        /**
         *环信内部实例化；
         */
        MultiDex.install(this);
        /**
         * 初始化上下文；
         */
        MyApplication.context = getApplicationContext();

        /**
         * Application自己的对象实例化；
         */
        instance = MyApplication.this;
        /**
         * 网络请求实例化；
         */
        client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();
        /**
         * 初始化helper实例对象
         */
        MyHelper.getInstance().init(this);

        handler = new Handler();

        util = new DatasUtil();

        /**
         * 获取主线程的线程id  myTid()在哪个方法被调用，返回的就是那个方法所在的线程id
         */
        MainThreadId = android.os.Process.myTid();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static List<Fragment> getFragments() {
        if (fragments == null)
            return new ArrayList<Fragment>();
        return fragments;
    }

    public static void addFragment(Fragment fragment) {
        MyApplication.fragments.add(fragment);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {

        return MyApplication.context;
    }

    public static Handler getHandler() {

        return handler;
    }

    public static void setpreferences() {//清空用户数据；
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", "");
        editor.putString("password", "");
        editor.putString("token", "");
        editor.putString("name", "");
        editor.putString("headimg", "");
        editor.putString("id", "");
        editor.putString("type", "");
        editor.putString("classname", "");
        editor.commit();
    }

    public static int getMainThreadId() {
        return MainThreadId;
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static DatasUtil getUtil() {
        return MyApplication.util;
    }

    public static void setUtil(DatasUtil util) {
        MyApplication.util = util;
    }

    public static String getToKen() {
        preferences = MyApplication.getContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    public static String getName() {
        preferences = MyApplication.getContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        return preferences.getString("name", "");
    }

    public static String getHeadImg() {
        preferences = MyApplication.getContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        return preferences.getString("headimg", "");
    }

    public static String getuserid() {
        preferences = MyApplication.getContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        return preferences.getString("id", "");
    }

    public static String getusername() {
        preferences = MyApplication.getContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        return preferences.getString("username", "");
    }

    public static String getuserpassword() {
        preferences = MyApplication.getContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        return preferences.getString("password", "");
    }

    public static String getuserType() {
        preferences = MyApplication.getContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        return preferences.getString("type", "");
    }

    public static String getuserClassName() {
        preferences = MyApplication.getContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        return preferences.getString("classname", "");
    }

    public static int getFLAG() {
        return MyApplication.FLAG;
    }

    public static void setFLAG(int FLAG) {
        MyApplication.FLAG = FLAG;
    }

    public static int getFLAG_TASK() {
        return MyApplication.FLAG_TASK;
    }

    public static void setFLAG_TASK(int FLAG) {
        MyApplication.FLAG_TASK = FLAG;
    }

    public static String getSIGN() {
        return SIGN;
    }

    public static void setISEXIT(boolean ISEXIT) {
        MyApplication.ISEXIT = ISEXIT;
        MyApplication.TAG = true;
    }

    /**
     * 初始化X5内核
     */
    private void initTbs() {

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                Log.i("onViewInitFinished is ", "" + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };

        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.i("onDownloadFinish", "" + i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.i("onInstallFinish", "" + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.i("onDownloadProgress:", "" + i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);

    }


    /**
     * 获取系统当前时间的方法
     *
     * @return
     */

    public static long getTime() {
        long currenttime = System.currentTimeMillis() / 1000;
        return currenttime;
    }

    long time1;
    long time2;

    /**
     * 根据程序在前台后台记录时间戳
     */
    private boolean isClose = false;

    public void getBackTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (; ; ) {
                    try {
                        if (TAG) {
                            if (IsBack.isBackground(getApplicationContext())) {
                                time2 = getTime();
//                                Log.i("TIME", "这是进入后台的时间戳 :" + time2);
                                //上传记录时间
                                recordTime();
                                TAG = false;
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        for (int i = 0; i < 6; i++) {
//                                            try {
//                                                Thread.sleep(10000);
//                                                if (IsBack.isBackground(getApplicationContext()))
//                                                    continue;
//                                                else
//                                                    return;
//                                            } catch (InterruptedException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                        System.exit(0);
//                                    }
//                                }).start();
                            }
                        }
                        if (ISEXIT) {
                            if (!TAG) {
                                if (!IsBack.isBackground(getApplicationContext())) {
                                    time1 = getTime();
//                                    Log.i("TIME", "进入前台的时间戳 :" + time1);
                                    TAG = true;
                                }
                            }
                        } else {
                            return;
                        }
                        //每隔十秒进行判断并记录
                        Thread.sleep(10000);

                    } catch (InterruptedException e) {
                        Log.e("", e + "");

                    }
                }
            }
        }).start();
    }


    /**
     * 上传记录时间
     */
    FormBody body;

    private void recordTime() {
        if (!("".equals(MyApplication.getToKen())) && MyApplication.getToKen() != null) {

            String data = null;
            body = new FormBody.Builder()
                    .add("username", MyApplication.getusername())
                    .add("password", MyApplication.getuserpassword())
                    .add("onlinetime", time2 - time1 > 900 ? 900 + "" : String.valueOf(time2 - time1))
                    .build();
            Log.i("TIME-T", String.valueOf(time2 - time1));
            try {
                data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_recordTime, body);
            } catch (IOException e) {

            }
            Gson gson = new Gson();
            ReturnResult returnResult = gson.fromJson(data, ReturnResult.class);
            if (data != null && !"".equals(data))
                Log.i("FIND", data);
            Looper.prepare();
            if (returnResult != null && !"".equals(returnResult.getCode()) && Integer.parseInt(returnResult.getCode()) == 0) {
                Log.i("TIME", "记录在线时间成功");
            }
            Looper.loop();
        }
    }
}

