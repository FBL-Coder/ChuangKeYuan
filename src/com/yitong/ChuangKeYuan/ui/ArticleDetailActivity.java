package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.codec.binary.Base64;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ArticleDetail;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.AsyncAddCommemt;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/6/7.
 * 文章详情页面
 */

public class ArticleDetailActivity extends Activity implements OnClickListener {

    private ImageView mSearch, shoucang, mBack, cancel, iv_teach_edit;
    private TextView mTitle;
    private ImageView comment_iv;
    private LinearLayout mFen, ll_detail_data, ku_task_yichang, comment_ll_1,
            comment_ll_2, comment_ll_3, comment_ll_4, comment_ll_5;
    private EditText mEdittext;
    private TextView mCancel;
    private Button mButton_comment;
    private RatingBar mRatingBar;
    private ProgressBar progressBar;
    /**
     * 主要内容显示组件
     */
    private WebView webView;
    private FormBody body;
    /**
     * 评论区域
     */
    private RelativeLayout rl_detail_pro, detail_comment;
    private ProgressBar mProgressBar;
    private int Click_Count = 0;
    /**
     * 数据源
     */
    private ArticleDetail articleDetail_Detail;
    private Handler mHandler;
    /**
     * 异步加载数据类
     */
    private AsyncLoaderData_Detail list1;
    private AsyncLoaderData_Detail list2;
    /**
     * 缓存文件路径
     */
    private String savePath = "/sdcard/ckyDownload/";
    /**
     * 缓存文件名
     */
    private String filename;
    private String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workdetail);

        //初始化标题栏
        initTitlebar();
        //初始化控件
        initView();
        //得到数据后，设置数据；
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    /**
     * 98
     * 初始化数据
     */
    private void initData() {
        if (HttpDataUtil.isNetworkConnected(this)) {//判断网络
            body = new FormBody.Builder()
                    .add("aid", getIntent().getStringExtra("aid"))
                    .add("username", MyApplication.getusername())
                    .add("password", MyApplication.getuserpassword())
                    .build();
            Log.i("SHOU", getIntent().getStringExtra("aid"));
            list1 = new AsyncLoaderData_Detail();
            list1.execute(DatasUtil.URL_BASE + DatasUtil.URL_article_details);//加载文章详情数据
        } else {
            //没有网络 显示视图
            rl_detail_pro.setVisibility(View.GONE);
            ll_detail_data.setVisibility(View.GONE);
            ku_task_yichang.setVisibility(View.VISIBLE);
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 12) {
                    //刷新
                    body = new FormBody.Builder()
                            .add("aid", getIntent().getStringExtra("aid"))
                            .add("username", MyApplication.getusername())
                            .add("password", MyApplication.getuserpassword())
                            .build();
                    list2 = new AsyncLoaderData_Detail();
                    list2.execute(DatasUtil.URL_BASE + DatasUtil.URL_article_details);
                }
                super.handleMessage(msg);
            }
        };
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        //标题栏
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        iv_teach_edit = (ImageView) findViewById(R.id.iv_teach_edit);
        shoucang = (ImageView) findViewById(R.id.iv_shoucang);
        webView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.wv_detail_webView);
        cancel = (ImageView) findViewById(R.id.iv_teach_cancel);
        mSearch.setVisibility(View.GONE);

        //进度条
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d("jiejie", "ProgressChanged++  " + newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }

                if (newProgress == 100) {
                    Log.i("打印日志", "加载完成");
                }
            }
        });

//      wevView的下载时间需要续写！
        webView.setDownloadListener(new MyWebViewDownLoadListener());

        mBack.setOnClickListener(this);
        shoucang.setOnClickListener(this);
    }

    /**
     * 自定义webview下载监听
     */
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(final String url, String userAgent, String contentDisposition, final String mimetype,
                                    long contentLength) {
            String name = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            name = name.replace('(', '/');
            name = name.replace('_', '=');
            name = name.replace('-', '+');
            filename = new String(Base64.decodeBase64(name));
            final Handler handler_dialog = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.arg1 == 1) {
                        down_ok_dialog();
                    } else if (msg.arg1 == 0) {
                        Download(url);
                        Log.i("URL", url);
                    }
                }
            };
            //开启线程，写入文件到本地
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = handler_dialog.obtainMessage();
                    try {
                        File f = new File(savePath + filename);
                        if (f.exists()) {
                            message.arg1 = 1;
                        } else {
                            message.arg1 = 0;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        message.arg1 = 0;
                    }
                    handler_dialog.sendMessage(message);
                }
            }).start();
        }

    }


    private void down_ok_dialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        // 为确认按钮添加事件,执行退出应用操作
        TextView mes = (TextView) window.findViewById(R.id.dialog_mes);
        mes.setText("下载已完成，您是否打开？(文件存储在: 根目录下的ckyDownload文件夹中！)");
        mes.setTextSize(14);
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        ok.setText("打开");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //确定逻辑
                try {
                    Intent intent = new Intent();
                    File file = new File(savePath + filename);
//                                                Log.i("OPEN", guessMimeType(savePath + tv.getText().toString()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), guessMimeType(savePath + filename));
                    startActivity(intent);
                } catch (Exception e) {
                    ToastUtil.showToast(ArticleDetailActivity.this, "本地无法打开此文件");
                } catch (Error error) {
                    ToastUtil.showToast(ArticleDetailActivity.this, "本地无法打开此文件");
                }
                dlg.dismiss();
            }
        });

        // 关闭alert对话框架
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //取消
                dlg.cancel();
            }
        });
    }


    /**
     * 下载线程
     *
     * @param url 网址
     */
    public void Download(final String url) {

        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        // 为确认按钮添加事件,执行退出应用操作
        TextView mes = (TextView) window.findViewById(R.id.dialog_mes);
        mes.setText("是否下载此文件？（请注意网络状态，建议WIFI下载）");
        mes.setTextSize(14);
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        ok.setText("下载");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //确定逻辑
                dlg.dismiss();
                final ProgressDialog Progressdialog = new ProgressDialog(ArticleDetailActivity.this);
                Progressdialog.setMessage("正在下载，请稍后...");
                Progressdialog.setMax(100);
                Progressdialog.setProgressStyle(Progressdialog.STYLE_HORIZONTAL);
                Progressdialog.show();

                final Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 11:
                                int progress = msg.arg1;
                                Progressdialog.setProgress(progress);
                                if (progress == 100) {
                                    Progressdialog.dismiss();
                                    Progressdialog.setProgress(0);
                                    down_ok_dialog();
                                }
                                break;
                            default:
                                break;
                        }
                        super.handleMessage(msg);
                    }
                };

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean isdown = HttpDataUtil.downLoadFile(url,
                                    new FormBody.Builder().build(), mHandler, filename);
                        } catch (IOException e) {
                            Looper.prepare();
                            ToastUtil.showToast(ArticleDetailActivity.this, "对不起，文件丢了...");
                            Looper.loop();
                        }
                    }
                }).start();

            }
        });

        // 关闭alert对话框架
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //取消
                dlg.cancel();
            }
        });
    }

    /**
     * 根据路径判断类型
     *
     * @param path
     * @return
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        } else if ("audio/amr".equals(contentTypeFor)) {
            contentTypeFor = "audio/*";
        } else if ("text/plain".equals(contentTypeFor)) {
            contentTypeFor = "text/plain";
        } else if ("image/jpeg".equals(contentTypeFor)) {
            contentTypeFor = "image/*";
        }
        return contentTypeFor;
    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }


    private void onClose() {//在退出页面的时候  webview置空
        webView.loadUrl("about:blank");
        finish();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (MyApplication.getFLAG() == 1001) {//判断页面是否需要刷新
            initData();
            MyApplication.setFLAG(0);//刷新后置0
        }
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        webView.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {//判断点击的实体键是不是音量-
            return super.onKeyDown(keyCode, event);
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {//判断点击的实体键是不是音量+
            return super.onKeyDown(keyCode, event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {//判断webView是否为首页
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webView.goBack();
                return false;
            } else {
                onClose();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progress_video);
        comment_iv = (ImageView) findViewById(R.id.comment_iv);
        mEdittext = (EditText) findViewById(R.id.edittext);
        mFen = (LinearLayout) findViewById(R.id.fen);
        ll_detail_data = (LinearLayout) findViewById(R.id.ll_detail_data);
        ku_task_yichang = (LinearLayout) findViewById(R.id.ku_task_yichang);
        rl_detail_pro = (RelativeLayout) findViewById(R.id.rl_detail_pro);
        detail_comment = (RelativeLayout) findViewById(R.id.detail_comment);
        comment_ll_1 = (LinearLayout) findViewById(R.id.comment_ll_1);
        comment_ll_2 = (LinearLayout) findViewById(R.id.comment_ll_2);
        comment_ll_3 = (LinearLayout) findViewById(R.id.comment_ll_3);
        comment_ll_4 = (LinearLayout) findViewById(R.id.comment_ll_4);
        comment_ll_5 = (LinearLayout) findViewById(R.id.comment_ll_5);
        if (getIntent().getBooleanExtra("isBuild", false)) {
            detail_comment.setVisibility(View.GONE);
        }
        mCancel = (TextView) findViewById(R.id.cancel);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mButton_comment = (Button) findViewById(R.id.btn_tijiao_comment);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_detail_pro);


        Progressbar_Util.ProVisibility(mProgressBar, this);
        detail_comment.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mButton_comment.setOnClickListener(this);
        comment_ll_1.setOnClickListener(this);
        comment_ll_2.setOnClickListener(this);
        comment_ll_3.setOnClickListener(this);
        comment_ll_4.setOnClickListener(this);
        comment_ll_5.setOnClickListener(this);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        mRatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back://退出页面的额时候  置空网络请求
                if (list1 != null) {
                    list1.cancel(true);
                }
                if (list2 != null) {
                    list2.cancel(true);
                }
                onClose();
                finish();
                break;
            case R.id.detail_comment:
                //判断是否登录
                if (MyApplication.getToKen() != null && !("".equals(MyApplication.getToKen()))) {
                    //身份识别
                    if (MyApplication.getuserType() != null && "3".equals(MyApplication.getuserType())) {
                        if (getIntent().getBooleanExtra("IsHome", false)) {
                            Intent intent = new Intent(this, TeacherCommentActivitv.class);
                            intent.putExtra("aid", getIntent().getStringExtra("aid"));
                            startActivityForResult(intent, 0);
                        } else {
                            mFen.setVisibility(View.VISIBLE);
                            comment_iv.setVisibility(View.GONE);
                            mCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mFen.setVisibility(View.VISIBLE);
                        comment_iv.setVisibility(View.GONE);
                        mCancel.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(ArticleDetailActivity.this, "对不起，您还没有登录...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ArticleDetailActivity.this, LoginActivity.class));
                }
                break;
            case R.id.cancel:
                mFen.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
                comment_iv.setVisibility(View.VISIBLE);
                break;
            case R.id.comment_ll_1:
                mRatingBar.setRating(5);
                break;
            case R.id.comment_ll_2:
                mRatingBar.setRating(4);
                break;
            case R.id.comment_ll_3:
                mRatingBar.setRating(3);
                break;
            case R.id.comment_ll_4:
                mRatingBar.setRating(2);
                break;
            case R.id.comment_ll_5:
                mRatingBar.setRating(1);
                break;
            case R.id.btn_tijiao_comment:
                comment();//提交评论
                break;
            case R.id.iv_shoucang://添加收藏按钮事件以及网络请求；
                new Thread() {
                    @Override
                    public void run() {
                        FormBody bodyshoucang = new FormBody.Builder()
                                .add("username", MyApplication.getusername())
                                .add("password", MyApplication.getuserpassword())
                                .add("id", getIntent().getStringExtra("aid"))
                                .build();
                        try {
                            String data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_Articlefavorites, bodyshoucang);
                            Log.i("SHOUCang", data);
                        } catch (IOException e) {
                            ToastUtil.showToast(ArticleDetailActivity.this, "收藏失败，请稍后再试...");
                        }
                    }
                }.start();

                if (articleDetail_Detail != null) {
                    if (Integer.parseInt(articleDetail_Detail.getIs_favorites()) == 2) {
                        if (Click_Count % 2 == 0) { //收藏按钮，判断是否收藏
                            shoucang.setImageResource(R.drawable.shoucang_ok);
                        } else {
                            shoucang.setImageResource(R.drawable.shoucang);
                        }
                    } else {
                        if (Click_Count % 2 == 0) {
                            shoucang.setImageResource(R.drawable.shoucang);
                        } else {
                            shoucang.setImageResource(R.drawable.shoucang_ok);
                        }
                    }
                    Click_Count++;
                }
                break;
        }
    }

    /**
     * 提交添加评论事件以及网络请求；
     */
    public void comment() {

        int numrating = (int) mRatingBar.getRating();
        String content = mEdittext.getText().toString();
        String numstar = String.valueOf(numrating);
        mEdittext.setText("");
        mRatingBar.setRating(0);

        if (content.length() < 1 && "0".equals(numstar)) {
            ToastUtil.showToast(ArticleDetailActivity.this, "亲，不能为空哦~");
            return;
        }

        FormBody comment_boby = new FormBody.Builder()
                .add("username", MyApplication.getusername())
                .add("password", MyApplication.getuserpassword())
                .add("aid", getIntent().getStringExtra("aid"))
                .add("starval", numstar)
                .add("content", content)
                .build();

        new AsyncAddCommemt(ArticleDetailActivity.this, comment_boby, mHandler).execute();

        mFen.setVisibility(View.GONE);
        mCancel.setVisibility(View.GONE);
        comment_iv.setVisibility(View.VISIBLE);
    }

    /**
     * 获取详情数据异步加载网络请求；
     */
    ProgressDialog progressDialog;

    /**
     * 异步下载数据
     */
    public class AsyncLoaderData_Detail extends AsyncTask<String, Void, ArticleDetail> {
        private String data;

        @Override
        protected void onPreExecute() {
            ll_detail_data.setVisibility(View.GONE);
            rl_detail_pro.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final ArticleDetail articleDetail) {
            if (articleDetail != null && Integer.parseInt(articleDetail.getCode()) == 0) {
                articleDetail_Detail = articleDetail;
                mTitle.setText("文章详情");
                Title = articleDetail.getTitle();

                ll_detail_data.setVisibility(View.VISIBLE);
                Log.i("TIME_DETAIL", System.currentTimeMillis() + "");
                Log.i("URL", articleDetail.getContent_url());

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webView.loadUrl(articleDetail.getContent_url());
//                webView.loadUrl("https://mozilla.github.io/pdf.js/web/viewer.html");
//                webView.loadUrl("192.168.1.19");

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });


                if (!"".equals(getIntent().getStringExtra("author")) &&
                        getIntent().getStringExtra("author") != null &&
                        MyApplication.getToKen() != null &&
                        !"".equals(MyApplication.getToKen())) {//判断是否已登录

                    if (getIntent().getStringExtra("author").equalsIgnoreCase(MyApplication.getusername())//判断用户类型
                            || "1".equals(MyApplication.getuserType())) {

                        progressDialog = new ProgressDialog(ArticleDetailActivity.this);


                        if (getIntent().getLongExtra("isShow", 0) == 1) {//创客秀中的数据不可编辑，不可删除
                        } else {

                            iv_teach_edit.setVisibility(View.VISIBLE);

                            iv_teach_edit.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("detail", articleDetail);
                                    startActivity(new Intent(ArticleDetailActivity.this, EditDetailActivity.class)
                                            .putExtra("key", bundle));
                                }
                            });

                            cancel.setVisibility(View.VISIBLE);
                            final Handler mHandler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    ReturnResult result = (ReturnResult) msg.obj;
                                    progressDialog.dismiss();
                                    if (result.getCode() != null &&
                                            !"".equals(result.getCode()) &&
                                            Integer.parseInt(result.getCode()) == 0) {
                                        ToastUtil.showToast(ArticleDetailActivity.this, "删除成功");
                                        MyApplication.setFLAG(1010);
                                        finish();
                                    } else {
                                        ToastUtil.showToast(ArticleDetailActivity.this, "删除失败，请重试...");
                                    }
                                    super.handleMessage(msg);
                                }
                            };
                            cancel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {//删除点击事件，以及数据交互

                                    CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(ArticleDetailActivity.this);
                                    builder.setTitle("亲，您确定要删除吗？");
                                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    progressDialog.setMessage("删除中...");
                                                    progressDialog.show();
                                                    progressDialog.setCancelable(false);
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            FormBody body = new FormBody.Builder()
                                                                    .add("username", MyApplication.getusername())
                                                                    .add("password", MyApplication.getuserpassword())
                                                                    .add("aid", getIntent().getStringExtra("aid"))
                                                                    .build();
                                                            try {
                                                                String data = HttpDataUtil
                                                                        .getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_article_del, body);
                                                                ReturnResult result = new Gson().fromJson(data, ReturnResult.class);
                                                                if (data != null && !"".equals(data)) {
                                                                    Log.i("DEL", getIntent().getStringExtra("aid") + data);
                                                                }
                                                                Message message = mHandler.obtainMessage();
                                                                message.obj = result;
                                                                mHandler.sendMessage(message);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).start();
                                                    dialog.dismiss();
                                                }
                                            }
                                    );
                                    builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }
                                    );
                                    builder.create().show();
                                }
                            });
                        }
                    }
                }


                Log.i("TIME_DETAIL", System.currentTimeMillis() + "");
                rl_detail_pro.setVisibility(View.GONE);
                if (MyApplication.getToKen() != null && !("".equals(MyApplication.getToKen()))) {
                    shoucang.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(articleDetail.getIs_favorites()) == 1) {
                        shoucang.setImageResource(R.drawable.shoucang_ok);
                    } else {
                        shoucang.setImageResource(R.drawable.shoucang);
                    }
                }

            } else if (articleDetail != null && Integer.parseInt(articleDetail.getCode()) == 10101) {
                rl_detail_pro.setVisibility(View.GONE);
                ll_detail_data.setVisibility(View.GONE);
                ku_task_yichang.setVisibility(View.VISIBLE);
                TextView textView = (TextView) ku_task_yichang.findViewById(R.id.ku_tv);
                textView.setText("亲，没有找到相应数据...");
            } else {
                rl_detail_pro.setVisibility(View.GONE);
                ll_detail_data.setVisibility(View.GONE);
                ku_task_yichang.setVisibility(View.VISIBLE);
                TextView textView = (TextView) ku_task_yichang.findViewById(R.id.ku_tv);
                textView.setText("对不起，服务器异常，请求数据未成功...");
            }
            super.onPostExecute(articleDetail);
        }

        @Override
        protected ArticleDetail doInBackground(String... params) {
            ArticleDetail articleDetail = null;
            try {
                Log.i("TIME_DEtail", System.currentTimeMillis() + "");
                if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {
                    data = HttpDataUtil.getPublicData_POST(params[0], body);

                    Log.i("SHOU", data);
                    Gson gson = new Gson();
                    articleDetail = gson.fromJson(data, ArticleDetail.class);
                } else {
                    Log.i("HTTPData", "Detail_没网，傻啊，网打开..");
                }

            } catch (IOException e) {
//                e.printStackTrace();
                Log.i("HTTPData", "Detail_IOException" + e);
            }
            return articleDetail;
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
