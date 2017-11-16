package com.yitong.ChuangKeYuan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.domain.DataPublicBean_Index;
import com.yitong.ChuangKeYuan.domain.DataPublicBean_Qita;
import com.yitong.ChuangKeYuan.ui.GuideActivity;
import com.yitong.ChuangKeYuan.ui.HomeActivity;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static com.yitong.ChuangKeYuan.utils.UiUtils.runOnUiThread;

/**
 * 作者：FBL  时间： 2016/7/7.
 * <p/>
 * 异步查询缓存文件及加载数据；
 */
public class AsyncQueryData extends AsyncTask<String, Void, Integer> {

    private int NONETWOR = 1;
    private int SERVEREXCEPTION = 2;
    private int SUCCEED = 0;
    private Activity SplashActivity;
    private String serverVersion;
    private boolean isfirst_enter;
    private Context context;
    private Handler mHandler;

    /**
     * 构造器
     *
     * @param SplashActivity 欢迎页对象
     * @param context        上下文
     */
    public AsyncQueryData(Activity SplashActivity, Context context) {
        this.context = context;
        this.SplashActivity = SplashActivity;
    }

    @Override
    protected void onPreExecute() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                DatasUtil util = new DatasUtil();
                MyApplication.setUtil(util);
                final Handler DtatHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        if (msg.what == 1) {
                            MyApplication.getUtil().Been_index = (DataPublicBean_Index) msg.obj;
                        } else if (msg.what == 2) {
                            MyApplication.getUtil().mListBeen = (List<DataPublicBean_Qita.ListBean>) msg.obj;
                        }

                        if (MyApplication.getUtil().Been_index != null && MyApplication.getUtil().mListBeen != null) {
                            Log.i("Thread", "页面跳转时间:" + System.currentTimeMillis());
                            isfirst_enter = PreferenceManager.getBoolean(UiUtils.getContext(), Constant.FIRST_ENTER, true);

                            if (isfirst_enter) {
                                //跳转引导界面
                                context.startActivity(new Intent(context, GuideActivity.class));
                            } else {
                                //跳转主界面
                                Intent intent = new Intent(context, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                context.startActivity(intent);

                            }
                            SplashActivity.finish();
                        }
                        super.handleMessage(msg);
                    }
                };

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Thread", "Time 主页开始时间：" + System.currentTimeMillis());
                        DataPublicBean_Index Been = null;
                        String data = null;
                        try {

                            if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {

                                data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_index, new FormBody.Builder().build());

                                if (!("".equals(data)) && data != null) {
                                    Dtat_Cache.writeFileToSD(data, "Data_index");
                                }
                            }
                            if (data == null | "".equals(data)) {
                                data = Dtat_Cache.readFile("Data_index");
                            }
                            Log.i("HTTPData", "Index_-----" + data);

                            if (!"".equals(data)) {
                                Gson gson = new Gson();
                                Been = gson.fromJson(data, DataPublicBean_Index.class);

                                if (Integer.parseInt(Been.getCode()) == 0 && Been != null) {

                                    Message message = DtatHandler.obtainMessage();
                                    message.obj = Been;
                                    message.what = 1;
                                    DtatHandler.sendMessage(message);

                                    Log.i("Thread", "Time 主页结束时间：" + System.currentTimeMillis());
                                } else {
                                    Looper.prepare();
                                    ToastUtil.showToast(MyApplication.getContext(), "服务器异常,马上退出！");
                                    System.exit(0);
                                    Looper.loop();
                                }
                            }
                        } catch (Exception e) {
                            Log.i("HTTPData", "Index_IOException" + e);
                            Looper.prepare();
                            ToastUtil.showToast(MyApplication.getContext(), "服务器连接超时,马上退出！");
                            System.exit(0);
                            Looper.loop();

                        }
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<DataPublicBean_Qita.ListBean> listBeen = null;
                        String data = null;
                        try {
                            Log.i("Thread", "ThreadID:其他线程开始:" + Thread.currentThread().getId());
                            if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {
                                data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_qita,
                                        new FormBody.Builder().build());

                                if (!("".equals(data)) && data != null) {
                                    Dtat_Cache.writeFileToSD(data, "Data_qita");
                                }
                            }
                            if (data == null | "".equals(data)) {
                                data = Dtat_Cache.readFile("Data_qita");
                            }
                            Log.i("HTTPData", "Qita_-----" + data);

                            if (!("".equals(data)) && data != null) {
                                Gson gson = new Gson();
                                DataPublicBean_Qita bean = gson.fromJson(data, DataPublicBean_Qita.class);

                                if (bean != null && Integer.parseInt(bean.getCode()) == 0) {
                                    listBeen = bean.getList();

                                    Message message = DtatHandler.obtainMessage();
                                    message.obj = listBeen;
                                    message.what = 2;
                                    DtatHandler.sendMessage(message);
                                    Log.i("Thread", "Time 其他线程结束时间：" + System.currentTimeMillis());
                                } else {
                                    Looper.prepare();
                                    ToastUtil.showToast(MyApplication.getContext(), "服务器异常,马上退出！");
                                    System.exit(0);
                                    Looper.loop();
                                }
                            }
                        } catch (Exception e) {
                            Log.i("HTTPData", "Qita_IOException" + e);
                            Looper.prepare();
                            ToastUtil.showToast(MyApplication.getContext(), "服务器连接超时,马上退出！");
                            System.exit(0);
                            Looper.loop();
                        }
                    }
                }).start();

                super.handleMessage(msg);
            }
        };
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer Result) {
        if (Result == 1) {//否则弹出对话框，要求打开网络连接；

            CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(context);
            builder.setTitle("亲，您没有打开网络，是否现在打开？");
            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SplashActivity.finish();
                }
            });
            builder.create().show();

        } else if (Result == 2) {//服务器返回异常就是  返回码不是200；

            CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(context);
            builder.setTitle("亲，实在是对不住啊，服务器出现异常，无法正常连接...");
            builder.setNeutralButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SplashActivity.finish();
                        }
                    }
            );
            builder.create().show();
        }
        super.onPostExecute(Result);

    }

    UpdateManager mUpdateManager;

    @Override
    protected Integer doInBackground(String... params) {

        //判断是否有网络和缓存文件
        if (!HttpDataUtil.isNetworkConnected(MyApplication.getContext()) &&
                !HttpDataUtil.fileIsExists("Data_index") && !HttpDataUtil.fileIsExists("Data_qita")) {
            //没有网络和缓存的话，返回false;
            Log.i("HTTP", "没有网络，缓存文件Dtat_index也不存在");
            return NONETWOR;
        } else if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {
            Request request = new Request.Builder()
                    .url(DatasUtil.URL_BASE + DatasUtil.URL_getversions)
                    .post(new FormBody.Builder().build())
                    .build();
            try {
                Response response = MyApplication.getClient().newCall(request).execute();
                if (response.code() == 200) {
                    serverVersion = response.body().string() + "";
                    if ("NULL".equals(serverVersion) || "NULL\n".equals(serverVersion) || "null\n".equals(serverVersion) || "".equals(serverVersion))
                        serverVersion = "0";

                    if ("nulls\n".equals(serverVersion) || "".equals(serverVersion) || "NULL\n".equals(serverVersion))
                        serverVersion = "1";
                    Log.i("VER", "版本号及版本名:" + serverVersion);
                    new Thread() {
                        public void run() {
                            //这儿是耗时操作，完成之后更新UI；
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //这里来检测版本是否需要更新
                                    Log.i("VER", "版本号:" + serverVersion);
                                    if (serverVersion.length() > 0) {
                                        mUpdateManager = new UpdateManager(context, serverVersion);
                                        mUpdateManager.showNoticeDialog(mHandler);
                                    }
                                }
                            });
                        }
                    }.start();
                    Log.i("UpAPP", "获取到的版本号:" + serverVersion);
                    return SUCCEED;
                } else {
                    return SERVEREXCEPTION;
                }
            } catch (Exception e) {
                mUpdateManager = new UpdateManager(context, "1");
                return SUCCEED;
            }
        } else {
            return NONETWOR;
        }
    }
}
