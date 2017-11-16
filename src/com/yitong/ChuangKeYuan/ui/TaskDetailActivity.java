package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.domain.TaskDetailEntity;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/7/28.
 * 任务详情
 */
public class TaskDetailActivity extends Activity implements View.OnClickListener {

    private WebView webView;
    private LinearLayout bodylayout, ll_data_task, ku_task_yichang;
    private ImageView back, search, delete, iv_teach_edit;
    private TextView title;
    private FormBody body;
    private RelativeLayout rl_detail_pro;
    private ProgressBar mProgressBar;
    private Button task_upload;
    private int type;
    /**
     * 数据源
     */
    private AsyncTaskData_Detail list;
    private String savePath = "/sdcard/ckyDownload/";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetail);
    }

    @Override
    protected void onStart() {
        if (bodylayout != null) {
            bodylayout.removeAllViews();
        }
        //初始化组件；
        initView();
        //初始化数据；
        initData();
        super.onStart();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        type = getIntent().getIntExtra("type", -1);
        iv_teach_edit = (ImageView) findViewById(R.id.iv_teach_edit);
        delete = (ImageView) findViewById(R.id.iv_teach_cancel);
        webView = (WebView) findViewById(R.id.task_wv);
        bodylayout = (LinearLayout) findViewById(R.id.taskdetail_download);
        back = (ImageView) findViewById(R.id.iv_teach_back);
        search = (ImageView) findViewById(R.id.iv_teach_search);
        title = (TextView) findViewById(R.id.tv_teach_title);
        ll_data_task = (LinearLayout) findViewById(R.id.ll_data_task);
        ku_task_yichang = (LinearLayout) findViewById(R.id.ku_task_yichang);
        task_upload = (Button) findViewById(R.id.task_upload);

        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setUseWideViewPort(false);//将图片调整到适合webview的大小
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
//设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webView.requestFocusFromTouch();//允许获取焦点

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
            }
        });//webview加载视频

        //初始化进度条；
        rl_detail_pro = (RelativeLayout) findViewById(R.id.rl_detail_pro);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_detail_pro);
        Progressbar_Util.ProVisibility(mProgressBar, this);

        search.setVisibility(View.GONE);
        back.setOnClickListener(this);

        //根据type类型决定视图显示；
        ku_task_yichang.setVisibility(View.GONE);
        if (MyApplication.getuserType() != null &&
                ("1".equals(MyApplication.getuserType()) || "3".equals(MyApplication.getuserType()))) {
            task_upload.setVisibility(View.GONE);
        } else {
            task_upload.setVisibility(View.VISIBLE);
        }
        if (type == 1) {
            bodylayout.setVisibility(View.VISIBLE);
        }
    }

    ProgressDialog progressDialog;
    @Override
    protected void onStop() {
        super.onStop();
        onClose();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        body = new FormBody.Builder()
                .add("username", MyApplication.getusername())
                .add("password", MyApplication.getuserpassword())
                .add("aid", getIntent().getStringExtra("aid"))
                .build();
        list = new AsyncTaskData_Detail();
        list.execute(DatasUtil.URL_BASE + DatasUtil.URL_Taskinfo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                if (list != null) {
                    list.cancel(true);
                }
                onClose();
                break;
        }
    }

    /**
     * 获取详情数据异步加载网络请求；
     */

    List<TaskDetailEntity.WorkingBean> EntityList;

    /**
     * 异步加载数据
     */
    class AsyncTaskData_Detail extends AsyncTask<String, Void, TaskDetailEntity> {

        private String data;

        @Override
        protected void onPreExecute() {
            ll_data_task.setVisibility(View.GONE);
            rl_detail_pro.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final TaskDetailEntity articleDetail) {

            try {
                if (articleDetail != null && Integer.parseInt(articleDetail.getCode()) == 0) {

                    if (MyApplication.getusername().equals(getIntent().getStringExtra("login"))
                            | (MyApplication.getuserType() != null && "1".equals(MyApplication.getuserType()))) {

                        //判断是否结束
                        if (getIntent().getIntExtra("isover", 1) == 0) {
                            iv_teach_edit.setVisibility(View.VISIBLE);

                            iv_teach_edit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("detail", articleDetail);
                                    startActivity(new Intent(TaskDetailActivity.this, EditTaskDetailActivity.class)
                                            .putExtra("key", bundle));
                                }
                            });
                        }
                        progressDialog = new ProgressDialog(TaskDetailActivity.this);
                        delete.setVisibility(View.VISIBLE);
                        final Handler mHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                ReturnResult result = (ReturnResult) msg.obj;
                                if (result.getCode() != null &&
                                        !"".equals(result.getCode()) &&
                                        Integer.parseInt(result.getCode()) == 0) {
                                    progressDialog.dismiss();
                                    ToastUtil.showToast(TaskDetailActivity.this, "删除成功");
                                    MyApplication.setFLAG_TASK(-1);
                                    onClose();
                                } else {
                                    progressDialog.dismiss();
                                    ToastUtil.showToast(TaskDetailActivity.this, "删除失败，请重试...");
                                }
                                super.handleMessage(msg);
                            }
                        };
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(TaskDetailActivity.this);
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

                    ku_task_yichang.setVisibility(View.GONE);
                    id = articleDetail.getArticle().getId();

                    webView.loadUrl(articleDetail.getArticle().getContent_url());

                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });

                    task_upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //判断是否结束
                            if (getIntent().getIntExtra("isover", -1) == 0) {

                                if (MyApplication.getToKen() != null && !"".equals(MyApplication.getToKen())) {
                                    Intent intent = new Intent(TaskDetailActivity.this, UploadTaskActivity.class);
                                    intent.putExtra("aid", id);
                                    startActivity(intent);
                                } else {
                                    ToastUtil.showToast(TaskDetailActivity.this, "请先登录！");
                                }
                            } else {
                                ToastUtil.showToast(TaskDetailActivity.this, "对不起，任务已过期！");
                            }
                        }
                    });
                    final ProgressDialog progressDialog = new ProgressDialog(TaskDetailActivity.this);

                    //判断数据是否加载成功
                    if (articleDetail != null && Integer.parseInt(articleDetail.getCode()) == 0) {
                        if (type == 1) {//判断类型
                            EntityList = articleDetail.getWorking();
                            Log.i("TASK", "总附件个数：" + articleDetail.getWorking().size());

                            //动态添加组件   点击下载附件
                            for (int i = 0; i < EntityList.size(); i++) {

                                final View view = LayoutInflater.from(TaskDetailActivity.this).inflate(R.layout.task_filedownload_item, null);
                                ImageView iv = (ImageView) view.findViewById(R.id.task_iv_down);
                                final TextView tv = (TextView) view.findViewById(R.id.task_tv_down);
                                final TextView tv_url = (TextView) view.findViewById(R.id.task_down_url);
                                TextView tv_id = (TextView) view.findViewById(R.id.task_down_id);

                                final TextView tv_ok = (TextView) view.findViewById(R.id.task_down_ok);
                                tv_ok.setText("正在查询");
                                final ProgressBar progress_down = (ProgressBar) view.findViewById(R.id.progress_down);

                                //判断附件格式 并作相应的显示
                                if ("txt".equals(EntityList.get(i).getType())) {
                                    iv.setImageResource(R.drawable.pic_txt);
                                    tv.setText(EntityList.get(i).getTitle() + ".txt");
                                    tv_url.setText(EntityList.get(i).getUrl());
                                    tv_id.setText(EntityList.get(i).getId());
                                } else if ("image".equals(EntityList.get(i).getType())) {
                                    iv.setImageResource(R.drawable.pic_photo);
                                    tv.setText(EntityList.get(i).getTitle()
                                            + EntityList.get(i).getUrl().substring(EntityList.get(i).getUrl().lastIndexOf("."))
                                    );
                                    tv_url.setText(EntityList.get(i).getUrl());
                                    tv_id.setText(EntityList.get(i).getId());
                                } else if ("video".equals(EntityList.get(i).getType())) {
                                    iv.setImageResource(R.drawable.pic_video);
                                    tv.setText(EntityList.get(i).getTitle()
                                            + EntityList.get(i).getUrl().substring(EntityList.get(i).getUrl().lastIndexOf("."))
                                    );
                                    tv_url.setText(EntityList.get(i).getUrl());
                                    tv_id.setText(EntityList.get(i).getId());
                                } else if ("audio".equals(EntityList.get(i).getType())) {
                                    iv.setImageResource(R.drawable.dialog_takeaudio);
                                    tv.setText(EntityList.get(i).getTitle()
                                            + EntityList.get(i).getUrl().substring(EntityList.get(i).getUrl().lastIndexOf("."))
                                    );
                                    tv_url.setText(EntityList.get(i).getUrl());
                                    tv_id.setText(EntityList.get(i).getId());
                                } else if ("word".equals(EntityList.get(i).getType())) {
                                    iv.setImageResource(R.drawable.pic_word);
                                    tv.setText(EntityList.get(i).getTitle()
                                            + EntityList.get(i).getUrl().substring(EntityList.get(i).getUrl().lastIndexOf("."))
                                    );
                                    tv_url.setText(EntityList.get(i).getUrl());
                                    tv_id.setText(EntityList.get(i).getId());
                                } else if ("ppt".equals(EntityList.get(i).getType())) {
                                    iv.setImageResource(R.drawable.pic_ppt);
                                    tv.setText(EntityList.get(i).getTitle()
                                            + EntityList.get(i).getUrl().substring(EntityList.get(i).getUrl().lastIndexOf("."))
                                    );
                                    tv_url.setText(EntityList.get(i).getUrl());
                                    tv_id.setText(EntityList.get(i).getId());
                                } else if ("excel".equals(EntityList.get(i).getType())) {
                                    iv.setImageResource(R.drawable.pic_excel);
                                    tv.setText(EntityList.get(i).getTitle()
                                            + EntityList.get(i).getUrl().substring(EntityList.get(i).getUrl().lastIndexOf("."))
                                    );
                                    tv_url.setText(EntityList.get(i).getUrl());
                                    tv_id.setText(EntityList.get(i).getId());
                                } else if ("zip".equals(EntityList.get(i).getType())) {
                                    iv.setImageResource(R.drawable.pic_zip);
                                    tv.setText(EntityList.get(i).getTitle()
                                            + EntityList.get(i).getUrl().substring(EntityList.get(i).getUrl().lastIndexOf("."))
                                    );
                                    tv_url.setText(EntityList.get(i).getUrl());
                                    tv_id.setText(EntityList.get(i).getId());
                                } else {
                                    iv.setImageResource(R.drawable.pic_file);
                                    tv.setText(EntityList.get(i).getTitle()
                                            + EntityList.get(i).getUrl().substring(EntityList.get(i).getUrl().lastIndexOf("."))
                                    );
                                    tv_url.setText(EntityList.get(i).getUrl());
                                    tv_id.setText(EntityList.get(i).getId());
                                }

                                final Handler mHandler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        if (msg.arg1 == 1) {//判断本地是否已存在，存在的话就显示已下载，不存在则执行下载
                                            tv_ok.setText("已下载");
                                            view.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        Intent intent = new Intent();
                                                        File file = new File(savePath + tv.getText().toString());
                                                        Log.i("OPEN", guessMimeType(savePath + tv.getText().toString()));
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.setAction(Intent.ACTION_VIEW);
                                                        intent.setDataAndType(Uri.fromFile(file), guessMimeType(savePath + tv.getText().toString()));
                                                        startActivity(intent);
                                                    } catch (Exception e) {
                                                        ToastUtil.showToast(TaskDetailActivity.this, "加载失败");
                                                    } catch (Error error) {
                                                        ToastUtil.showToast(TaskDetailActivity.this, "加载失败");
                                                    }
                                                }
                                            });
                                        } else {
                                            tv_ok.setText("点击下载");
                                            view.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(final View v) {
                                                    final TextView view_url = (TextView) v.findViewById(R.id.task_down_url);
                                                    final TextView tv_title = (TextView) v.findViewById(R.id.task_tv_down);
                                                    CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(TaskDetailActivity.this);
                                                    builder.setTitle("提示 :");
                                                    builder.setMessage("你是否要下载此附件？(为了减少流量使用，请确认是否为WIFI连接！)");
                                                    builder.setNegativeButton("不了", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });

                                                    builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            progress_down.setVisibility(View.VISIBLE);
                                                            final Handler mHandler = new Handler() {
                                                                @Override
                                                                public void handleMessage(Message msg) {
                                                                    switch (msg.what) {
                                                                        case 11:
                                                                            int progress = msg.arg1;
                                                                            progress_down.setProgress(progress);
                                                                            if (progress == 100) {
                                                                                progress_down.setVisibility(View.GONE);
                                                                                progress_down.setProgress(0);
                                                                                tv_ok.setText("已下载");
                                                                                view.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        try {
                                                                                            Intent intent = new Intent();
                                                                                            File file = new File(savePath + tv.getText().toString());
                                                                                            Log.i("OPEN", guessMimeType(savePath + tv.getText().toString()));
                                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                            intent.setAction(Intent.ACTION_VIEW);
                                                                                            intent.setDataAndType(Uri.fromFile(file), guessMimeType(savePath + tv.getText().toString()));
                                                                                            startActivity(intent);
                                                                                        } catch (Exception e) {
                                                                                            ToastUtil.showToast(TaskDetailActivity.this, "加载失败");
                                                                                        } catch (Error error) {
                                                                                            ToastUtil.showToast(TaskDetailActivity.this, "加载失败");
                                                                                        }
                                                                                    }
                                                                                });
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
                                                                        String filename = tv_title.getText().toString();
                                                                        Log.i("DOWN", "下载网址 :" + view_url.getText().toString());
                                                                        Log.i("DOWN", "保存文件名 :" + filename);
                                                                        boolean isdown = HttpDataUtil.downLoadFile(view_url.getText().toString(),
                                                                                new FormBody.Builder().build(), mHandler, filename);

                                                                    } catch (IOException e) {
                                                                        Looper.prepare();
                                                                        ToastUtil.showToast(TaskDetailActivity.this, "对不起，下载丢了...");
                                                                        Looper.loop();
                                                                    }
                                                                }
                                                            }).start();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    builder.create().show();
                                                }
                                            });
                                        }
                                    }
                                };

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                                        String filename = tv.getText().toString();
                                        Message message = mHandler.obtainMessage();
                                        try {
                                            ;
                                            File f = new File(savePath + filename);
                                            if (f.exists()) {
                                                message.arg1 = 1;
                                                mHandler.sendMessage(message);
                                            } else {
                                                message.arg1 = 0;
                                                mHandler.sendMessage(message);
                                            }

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            message.arg1 = 0;
                                            mHandler.sendMessage(message);
                                        }
                                    }
                                }).start();


                                view.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {

                                        final TextView tv_view_id = (TextView) v.findViewById(R.id.task_down_id);
                                        final Handler mHandler;
                                        CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(TaskDetailActivity.this);
                                        builder.setTitle("提示 :");
                                        builder.setMessage("你是否要从服务器删除此附件？");
                                        builder.setNegativeButton("不了", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                        mHandler = new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                view.setVisibility(View.GONE);
                                                Toast.makeText(TaskDetailActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                super.handleMessage(msg);
                                            }
                                        };
                                        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                progressDialog.setMessage("正在删除...");
                                                progressDialog.setCancelable(false);
                                                progressDialog.show();
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {

                                                            FormBody body = new FormBody.Builder()
                                                                    .add("username", MyApplication.getusername())
                                                                    .add("password", MyApplication.getuserpassword())
                                                                    .add("id", tv_view_id.getText().toString())
                                                                    .build();

                                                            String data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_TaskFinishDelete, body);
                                                            Gson gson = new Gson();
                                                            ReturnResult result = gson.fromJson(data, ReturnResult.class);

                                                            if (Integer.parseInt(result.getCode()) == 0) {
                                                                Message msg = mHandler.obtainMessage();
                                                                mHandler.sendMessage(msg);
                                                                Log.i("Delete", "删除成功 :" + tv_view_id.getText().toString());
                                                                Log.i("Delete", "删除成功 :" + data);
                                                            }
                                                        } catch (IOException e) {
                                                            Looper.prepare();
                                                            ToastUtil.showToast(TaskDetailActivity.this, "对不起，删除失败.");
                                                            Looper.loop();

                                                        }
                                                    }
                                                }).start();
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create().show();
                                        return true;
                                    }
                                });
                                bodylayout.addView(view);
                            }
                        }
                        title.setText(articleDetail.getArticle().getTitle());
                        ll_data_task.setVisibility(View.VISIBLE);
                        rl_detail_pro.setVisibility(View.GONE);
                    } else if (articleDetail != null && Integer.parseInt(articleDetail.getCode()) == 10101) {
                        rl_detail_pro.setVisibility(View.GONE);
                        ll_data_task.setVisibility(View.GONE);
                        ku_task_yichang.setVisibility(View.VISIBLE);
                        TextView textView = (TextView) ku_task_yichang.findViewById(R.id.ku_tv);
                        textView.setText("对不起，没有找到数据...");
                    } else if (articleDetail != null && Integer.parseInt(articleDetail.getCode()) == 10001) {
                        rl_detail_pro.setVisibility(View.GONE);
                        ll_data_task.setVisibility(View.GONE);
                        ku_task_yichang.setVisibility(View.VISIBLE);
                        TextView textView = (TextView) ku_task_yichang.findViewById(R.id.ku_tv);
                        textView.setText("对不起，这里需要登陆...");
                    } else if (articleDetail != null && Integer.parseInt(articleDetail.getCode()) == 10007) {
                        rl_detail_pro.setVisibility(View.GONE);
                        ll_data_task.setVisibility(View.GONE);
                        ku_task_yichang.setVisibility(View.VISIBLE);
                        TextView textView = (TextView) ku_task_yichang.findViewById(R.id.ku_tv);
                        textView.setText("登录时间过长，请重新登录...");
                    } else {
                        rl_detail_pro.setVisibility(View.GONE);
                        ll_data_task.setVisibility(View.GONE);
                        ku_task_yichang.setVisibility(View.VISIBLE);
                    }
                } else {
                    rl_detail_pro.setVisibility(View.GONE);
                    ll_data_task.setVisibility(View.GONE);
                    ku_task_yichang.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                System.out.println("异常" + e);
            }

            super.onPostExecute(articleDetail);
        }

        @Override
        protected TaskDetailEntity doInBackground(String... params) {
            TaskDetailEntity entity = null;
            try {
                if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {
                    data = HttpDataUtil.getPublicData_POST(params[0], body);

                    Log.i("TASK", "任务详情 :" + data);

                    Gson gson = new Gson();

                    entity = gson.fromJson(data, TaskDetailEntity.class);
                } else {
                    Log.i("HTTPData", "Detail_没网，傻啊，网打开..");
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.i("HTTPData", "Detail_IOException" + e);
            }
            return entity;
        }
    }

    private void onClose() {//在退出页面的时候  webview置空
        webView.loadUrl("about:blank");
        finish();
    }

    /**
     * 根据路径判断资源的类型
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

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
