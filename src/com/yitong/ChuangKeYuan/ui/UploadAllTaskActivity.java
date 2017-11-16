package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liangmutian.mypicker.DataPickerDialog;
import com.example.liangmutian.mypicker.DatePickerDialog;
import com.example.liangmutian.mypicker.DateUtil;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.NoScrollgridViewAdapter;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.FileSizeUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.utils.UploadUtil_Upload;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;
import com.yitong.ChuangKeYuan.widget.CustomDialog_upload;
import com.yitong.ChuangKeYuan.widget.NoScrollGridView;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/9/26.
 * 上传任务页面
 */
public class UploadAllTaskActivity extends Activity implements View.OnClickListener, UploadUtil_Upload.OnUploadProcessListener, View.OnTouchListener {
    private ImageView mBack, mSearch, mHelp;
    private TextView mTitle, upload_title, over_time, class_select, upload_des1,
            upload_btn, upload_des2, upload_des3,upload_des4, over_time_btn,authority_select_btn;
    private EditText title, content;
    private LinearLayout ll1, ll2, ll3, ll4;
    private ImageView iv1, iv2, iv3, iv4;
    private NoScrollGridView noScrollGridView;
    private NoScrollgridViewAdapter noScrollGridViewAdapter;
    private View v1, v2, v3, v4;
    private static final String TAG_PHOTO = "uploadImage";
    private static final String TAG_VIDEO = "uploadVideo";
    //去上传文件
    protected static final int TO_UPLOAD_FILE = 1;
    //上传文件响应
    protected static final int UPLOAD_FILE_DONE = 2;
    //上传初始化
    private static final int UPLOAD_INIT_PROCESS = 3;
    //上传中
    private static final int UPLOAD_IN_PROCESS = 4;
    //这里的这个URL是服务器的环境URL
    private static String requestURL = DatasUtil.URL_BASE;
    private static String requestURL_PHOTO = requestURL + "/Base/uploadImg/";
    private static String requestURL_VIDEO = requestURL + "/Base/uploadVideo/";
    private Button upload;
    private ProgressDialog progressDialog;
    //获取到的图片路径
    private String picPath;
    private Intent lastIntent;
    private Uri photoUri;
    //使用相册中的图片
    public static final int SELECT_PIC_BY_PICK_PHOTO = 6;
    //使用照相机拍照获取图片
    public static final int SELECT_PIC_BY_TACK_PHOTO = 5;
    //从Intent获取图片路径的KEY
    public static final String KEY_PHOTO_PATH = "photo_path";
    private static final String TAG1 = "SelectPic";
    //获取到的视频路径
    private String videoPath;
    private Uri videoUri;
    //使用录影机录像获取视频
    public static final int SELECT_VIDEO_BY_TACK_VIDEO = 7;
    //使用本地文件中的视频
    public static final int SELECT_VIDEO_BY_PICK_VIDEO = 8;
    //从Intent获取视频路径的KEY
    public static final String KEY_VIDEO_PATH = "video_path";
    private static final String TAG2 = "SelectVideo";
    private String[] power = new String[]{"全部", "园", "组", "班级"};
    private String[][] some = new String[][]{{"全部"}, {"绿叶园"}, {"小班组", "中班组", "大班组"}, {"玫瑰班", "百合班", "蔷薇班", "茉莉班", "绿萝班", "紫藤班", "银杏班", "梧桐班", "豆豆班", "苗苗班", "绿绿班", "芽芽班"}};
    private List<String> list_one = new ArrayList<String>();
    private List<String> list_two_1 = new ArrayList<String>();
    private List<String> list_two_2 = new ArrayList<String>();
    private List<String> list_two_3 = new ArrayList<String>();
    private List<String> list_two_4 = new ArrayList<String>();

    private Time time;
    private Dialog dateDialog, chooseDialog;

    /**
     * 存放之前页面中的拓片网址
     */
    private List<String> listUrl_result = null;
    /**
     * 存放之前页面中的视频网址
     */
    private String videoUrl_result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_alltask);
        //初始化标题栏
        initTitleBar();
        //初始化控件
        initView();
    }
    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mHelp = (ImageView) findViewById(R.id.iv_teach_help);
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        mTitle.setText("上传任务");
        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
        mHelp.setVisibility(View.VISIBLE);
        mHelp.setOnClickListener(this);
        listUrl_result = new ArrayList();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        time = new Time("GMT+8");
        time.setToNow();

        title = (EditText) findViewById(R.id.des);
        content = (EditText) findViewById(R.id.content_up);
        over_time = (TextView) findViewById(R.id.over_time);
        class_select = (TextView) findViewById(R.id.class_select);
        noScrollGridView = (NoScrollGridView) findViewById(R.id.noScrollGridView);
        upload = (Button) findViewById(R.id.upload);
        authority_select_btn = (TextView) findViewById(R.id.authority_select_btn);
        authority_select_btn.setOnClickListener(this);
        over_time_btn = (TextView) findViewById(R.id.over_time_btn);
        over_time_btn.setOnClickListener(this);
        upload.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        lastIntent = getIntent();
        for (String str : power) {
            list_one.add(str);
        }
        for (int i = 0; i < some[0].length; i++) {
            list_two_1.add(some[0][i]);
        }
        for (int i = 0; i < some[1].length; i++) {
            list_two_2.add(some[1][i]);
        }
        for (int i = 0; i < some[2].length; i++) {
            list_two_3.add(some[2][i]);
        }
        for (int i = 0; i < some[3].length; i++) {
            list_two_4.add(some[3][i]);
        }
        content.setOnTouchListener(this); // 解决scrollView中嵌套EditText导致不能上下滑动的问题
        initGordView();

    }

    /**
     * chooseDialog
     * 权限选择
     */
    private void showChooseDialog(List<String> mlist) {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        chooseDialog = builder.setData(mlist).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        class_select.setText(itemValue);
                        if (position == 1)
                            showClassDialog(list_two_2);
                        if (position == 2)
                            showClassDialog(list_two_3);
                        if (position == 3)
                            showClassDialog(list_two_4);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).create();

        chooseDialog.show();
    }

    /**
     * chooseDialog
     * 权限选择
     */
    private void showClassDialog(List<String> mlist) {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        chooseDialog = builder.setData(mlist).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        class_select.setText(itemValue);

                    }

                    @Override
                    public void onCancel() {

                    }
                }).create();

        chooseDialog.show();
    }

    //时间选择器
    private void showDateDialog(List<Integer> date) {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this);
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {

                over_time.setText(dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                        + (dates[2] > 9 ? dates[2] : ("0" + dates[2])));
                Log.e("DATA", dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                        + (dates[2] > 9 ? dates[2] : ("0" + dates[2])));
            }

            @Override
            public void onCancel() {

            }
        }).setSelectYear(date.get(0) - 1).setSelectMonth(date.get(1) - 1).setSelectDay(date.get(2) - 1);

        builder.setMinYear(DateUtil.getYear());
        builder.setMinMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        builder.setMinDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        dateDialog = builder.create();
        dateDialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 解决scrollView中嵌套EditText导致不能上下滑动的问题
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
    }


    CustomDialog_upload dialog;

    /**
     * 初始化Dialog
     */
    public void getDialog() {
        dialog = new CustomDialog_upload(this, R.style.customDialog, R.layout.dialog_find_layout);
        dialog.show();
        upload_title = (TextView) dialog.findViewById(R.id.upload_title);
        upload_des1 = (TextView) dialog.findViewById(R.id.upload_des1);
        upload_des2 = (TextView) dialog.findViewById(R.id.upload_des2);
        upload_des3 = (TextView) dialog.findViewById(R.id.upload_des3);
        upload_des4 = (TextView) dialog.findViewById(R.id.upload_des4);
        upload_btn = (TextView) dialog.findViewById(R.id.upload_btn);
        ll1 = (LinearLayout) dialog.findViewById(R.id.ll1);
        ll2 = (LinearLayout) dialog.findViewById(R.id.ll2);
        ll3 = (LinearLayout) dialog.findViewById(R.id.ll3);
        ll4 = (LinearLayout) dialog.findViewById(R.id.ll4);
        v1 = dialog.findViewById(R.id.v1);
        v2 = dialog.findViewById(R.id.v2);
        v3 = dialog.findViewById(R.id.v3);
        v4 = dialog.findViewById(R.id.v4);
        iv1 = (ImageView) dialog.findViewById(R.id.iv1);
        iv2 = (ImageView) dialog.findViewById(R.id.iv2);
        iv3 = (ImageView) dialog.findViewById(R.id.iv3);
        iv4 = (ImageView) dialog.findViewById(R.id.iv4);
    }

    /**
     * 点击GordView 条目后的事件
     *
     * @param
     */

    public void initGordView() {

        noScrollGridViewAdapter = new NoScrollgridViewAdapter(this, listUrl_result);

        noScrollGridView.setAdapter(noScrollGridViewAdapter);

        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == listUrl_result.size()) {
                    getDialog();
                    upload_title.setText("请选择上传类型");
                    iv1.setImageResource(R.drawable.dialog_tackphoto);
                    upload_des1.setText("拍照");
                    ll1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            takePhoto();
                            dialog.dismiss();
                        }
                    });
                    iv2.setImageResource(R.drawable.dialog_pickphoto);
                    upload_des2.setText("从相册选择");
                    ll2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pickPhoto();
                            dialog.dismiss();
                        }
                    });
                    if (videoUrl_result == null || "".equals(videoUrl_result)) {
                        iv3.setImageResource(R.drawable.dialog_video);
                        upload_des3.setText("从本地选择视频");
                        ll3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if ("meizu".equals(MyApplication.getSIGN())) {
                                    pickVideo_meizu();
                                } else {
                                    pickVideo();
                                }
                                dialog.dismiss();
                            }
                        });
                    } else {
                        ll3.setVisibility(View.GONE);
                        v3.setVisibility(View.GONE);
                    }
                    ll4.setVisibility(View.GONE);
                    v4.setVisibility(View.GONE);
                    upload_btn.setText("取消");
                    upload_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    if ("".equals(videoUrl_result) || videoUrl_result == null)
                        listUrl_result.remove(position);
                    else {
                        listUrl_result.remove(position);
                        videoUrl_result = "";
                    }
                    noScrollGridViewAdapter.notifyDataSetChanged(listUrl_result);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                finish();
                break;
            case R.id.over_time_btn:
                showDateDialog(DateUtil.getDateForString("2016-01-01"));
                break;
            case R.id.authority_select_btn:
                showChooseDialog(list_one);
                break;
            case R.id.upload:
                final String overtime = over_time.getText().toString();
                final String tasktitle = title.getText().toString();
                final String taskcontent = content.getText().toString();
                final String Privilege = class_select.getText().toString();

                if ("".equals(tasktitle) || "".equals(overtime) || "".equals(taskcontent) || "".equals(Privilege)) {
                    ToastUtil.showToast(UploadAllTaskActivity.this, "标题，内容，结束时间,权限不能为空");
                } else {
                    progressDialog = new ProgressDialog(UploadAllTaskActivity.this);
                    progressDialog.setMessage("正在上传，请稍后...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.arg1 == 0) {
                                ToastUtil.showToast(UploadAllTaskActivity.this, "上传成功");
                                MyApplication.setFLAG_TASK(-1);
                                finish();
                            } else {
                                ToastUtil.showToast(UploadAllTaskActivity.this, "上传失败");
                            }
                            progressDialog.dismiss();
                            super.handleMessage(msg);
                        }
                    };

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String data = "";
                            String lastUrl = "";
                            if (listUrl_result.size() > 0) {
                                for (int i = 0; i < listUrl_result.size(); i++) {
                                    if ("video".equals(listUrl_result.get(i))){
                                        continue;
                                    }
                                    if (listUrl_result.get(i) != null || "".equals(listUrl_result.get(i))) {
                                        lastUrl += listUrl_result.get(i) + ",";
                                    }
                                }
                            }
                            if (!"".equals(videoUrl_result))
                                lastUrl += videoUrl_result + ",";

                            try {
                                FormBody body = new FormBody.Builder()
                                        .add("username", MyApplication.getusername())
                                        .add("password", MyApplication.getuserpassword())
                                        .add("termid", "13")
                                        .add("time", overtime)
                                        .add("title", tasktitle)
                                        .add("content", taskcontent)
                                        .add("pri", Privilege)
                                        .add("fbl", lastUrl).build();
                                data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_addTAsk, body);

                                Log.i("ABC", data);
                                Gson gson = new Gson();
                                ReturnResult result = gson.fromJson(data, ReturnResult.class);

                                Message message = handler.obtainMessage();
                                message.arg1 = Integer.parseInt(result.getCode());
                                handler.sendMessage(message);

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(e.toString());
                                progressDialog.dismiss();
                                return;
                            }
                        }
                    }).start();
                }
                break;
            case R.id.iv_teach_help:
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(UploadAllTaskActivity.this);
                builder.setTitle("操作说明：");
                builder.setMessage("    作业标题必填；结束时间必填；作业详情必填；权限可选择，如果没有选择则默认为全部；图片可添加可不添加。");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
    }

    /**
     * 上传图片，
     */

    private void upload(String view, int TAG) {
        Double d = Double.valueOf(view);
        Double maxSize = Double.valueOf(50 * 1024);//限制上传文件大小在50M以内
        if (picPath != null || videoPath != null) {
            if (maxSize >= d) {
                if (d >= 1) {
                    Message message = Message.obtain();
                    message.arg2 = TAG;
                    message.what = TO_UPLOAD_FILE;
                    handler.sendMessage(message);
//                    toUploadFile(1);
                } else {
                    ToastUtil.showToast(UploadAllTaskActivity.this, "对不起，您选择的图片太小，或者路径异常，请重新选择！");
                }
            } else {
                ToastUtil.showToast(UploadAllTaskActivity.this, "对不起，您选择的图片过大！请重新选择..");
            }
        } else {
            ToastUtil.showToast(this, "请先选择您要上传的图片");
        }
    }

    Map<String, SoftReference<Bitmap>> mImageCacheMap = new HashMap<String, SoftReference<Bitmap>>();
    String size;

    /**
     * 选择图片后的显示
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param
     */
    private void ShowImage(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (data != null) {
                if (!doPhoto(requestCode, data)) {
                    return;
                }
                Log.i(TAG_PHOTO, "最终选择的图片是 ：" + picPath);
                File f = new File(picPath);
                try {
                    size = String.valueOf(FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB));
                } catch (Exception e) {
                    size = "6000";
                }
                upload(size, 2);
            } else {
                return;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_TACK_PHOTO) {
            if (!doPhoto(requestCode, data)) {
                return;
            }
            Log.i(TAG_PHOTO, "最终选择的图片是 ：" + picPath);
//            BitmapFactory.Options myoptions = new BitmapFactory.Options();
//            myoptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(picPath, myoptions);
//            myoptions.inJustDecodeBounds = false;
//            myoptions.inSampleSize = 6;
//            myoptions.inPurgeable = true;
//            myoptions.inInputShareable = true;
//            myoptions.inPreferredConfig = Bitmap.Config.RGB_565;
//            Bitmap bm = BitmapFactory.decodeFile(picPath, myoptions);
//            SoftReference<Bitmap> d = new SoftReference<Bitmap>(bm);
//            mImageCacheMap.put(picPath, d);
//            SoftReference<Bitmap> softReference = mImageCacheMap.get(picPath);
            File f = new File(picPath);
            try {
                size = String.valueOf(FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB));
            } catch (Exception e) {
                size = "6000";
            }
            upload(size, 2);
        }
    }

    /**
     * 点击视频后的显示
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param
     */
    private void ShowVideo(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_VIDEO_BY_PICK_VIDEO) {
            if (data.getData().toString() != null && !("".equals(data.getData().toString()))) {
                if ("xiaomi".equals(MyApplication.getSIGN())) {
                    videoPath = data.getData().toString().substring(7);
                } else if ("meizu".equals(MyApplication.getSIGN())) {
                    videoPath = data.getData().toString();
                } else {
                    if (!doVideo(requestCode, data)) {
                        return;
                    }
                }
                Log.i(TAG_VIDEO, "最终选择的视频是 ：" + videoPath);
                try {
                    size = (String.valueOf(FileSizeUtil.getFileOrFilesSize(videoPath, 2)));
                } catch (Exception e) {
                    size = "6000";
                }
//                upload(size, 1, getVideoThumbnail(videoPath), 1);
                upload(size, 1);
            } else {
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_VIDEO_BY_PICK_VIDEO) {
            ShowVideo(requestCode, resultCode, data);
            return;
        }
        ShowImage(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传服务器响应回调
     */
    @Override
    public void onUploadDone(int responseCode, String message) {
        progressDialog.dismiss();
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    /**
     * 上传文件
     *
     * @param TAG
     */
    private void toUploadFile(int TAG) {
        progressDialog.setMessage("正在上传文件...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        UploadUtil_Upload uploadUtil = UploadUtil_Upload.getInstance();
        uploadUtil.setOnUploadProcessListener(this); //设置监听器监听上传状态
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", "11111");

        if (picPath == null) {
            String fileKey_video = "video";
            uploadUtil.uploadFile(videoPath, fileKey_video, requestURL_VIDEO, params, TAG);
            videoPath = null;
        } else if (videoPath == null) {
            String fileKey_photo = "image";
            uploadUtil.uploadFile(picPath, fileKey_photo, requestURL_PHOTO, params, TAG);
            picPath = null;
        }
    }

    /**
     * 完成上传后的回调
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    toUploadFile(msg.arg2);
                    break;
                case UPLOAD_INIT_PROCESS:
                    break;
                case UPLOAD_IN_PROCESS:
                    break;
                case UPLOAD_FILE_DONE://上传完成后的回调操作
                    if (msg.arg1 == 1) {//上传成功
                        String result = msg.obj.toString();
                        int loact = Integer.parseInt(result.substring(0, result.indexOf("h")));
                        String url = result.substring(result.indexOf("h"));
                        Log.i("ABC", result);
                        if (url.endsWith("png") | url.endsWith(".PNG") | url.endsWith(".jpg")
                                | url.endsWith(".JPG") | url.endsWith(".gif") | url.endsWith(".GIF")
                                | url.endsWith(".jpeg") | url.endsWith(".JPEG")) {
                            listUrl_result.add(url);
                            if (listUrl_result.size() == 9)
                                noScrollGridViewAdapter.notifyDataSetChanged_imageok(listUrl_result);
                            noScrollGridViewAdapter.notifyDataSetChanged(listUrl_result);
                        } else if (url.endsWith(".mp4") || url.endsWith(".3gp") ||
                                url.endsWith(".wav") || url.endsWith(".rm") || url.endsWith(".rmvb")) {
                            videoUrl_result = url;
                            listUrl_result.add("video");
                            if (listUrl_result.size() == 9)
                                noScrollGridViewAdapter.notifyDataSetChanged_imageok(listUrl_result);
                            noScrollGridViewAdapter.notifyDataSetChanged(listUrl_result);
                        }
                    } else {//上传失败
                        ToastUtil.showToast(UploadAllTaskActivity.this, "连接超时,上传失败!");
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        handler.sendMessage(msg);
    }

    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        handler.sendMessage(msg);
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    private boolean doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return false;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
        }
        Log.i(TAG1, "imagePath = " + picPath);
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") ||
                picPath.endsWith(".jpg") || picPath.endsWith(".JPG") || picPath.endsWith(".gif") ||
                picPath.endsWith(".GIF") || picPath.endsWith(".jpeg") || picPath.endsWith(".JPEG"))) {
            lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
            setResult(Activity.RESULT_OK, lastIntent);
            return true;
        } else {
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * 魅族手机本地获取视频
     */
    public void pickVideo_meizu() {
        Intent intent = new Intent(UploadAllTaskActivity.this, AllVideoActivity.class);
        startActivityForResult(intent, SELECT_VIDEO_BY_PICK_VIDEO);
    }

    /***
     * 从本地取视频
     */
    private void pickVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, SELECT_VIDEO_BY_PICK_VIDEO);
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            //"android.media.action.IMAGE_CAPTURE"
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 选择视频后，获取视频的路径
     *
     * @param requestCode
     * @param data
     */
    private boolean doVideo(int requestCode, Intent data) {
        if (requestCode == SELECT_VIDEO_BY_PICK_VIDEO) {
            if (data == null) {
                Toast.makeText(this, "选择视频文件出错", Toast.LENGTH_LONG).show();
                return false;
            }
            videoUri = data.getData();
            if (videoUri == null) {
                Toast.makeText(this, "选择视频文件出错", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        String[] pojo = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(videoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            videoPath = cursor.getString(columnIndex);
        }
        Log.i(TAG2, "videoPath = " + videoPath);
        if (videoPath != null && (videoPath.endsWith(".mp4") || videoPath.endsWith(".3gp") ||
                videoPath.endsWith(".wav") || videoPath.endsWith(".rm") || videoPath.endsWith(".rmvb") ||
                videoPath.endsWith(".MP4") || videoPath.endsWith(".3GP") ||
                videoPath.endsWith(".WAV") || videoPath.endsWith(".RM") || videoPath.endsWith(".RMVB"))) {
            lastIntent.putExtra(KEY_VIDEO_PATH, videoPath);
            setResult(Activity.RESULT_OK, lastIntent);
            return true;
        } else {
            Toast.makeText(this, "选择视频文件不正确", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * 点击EditText之外的区域，键盘消失
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 是否隐藏键盘
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
