package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.FileUtils;
import com.yitong.ChuangKeYuan.utils.FileSizeUtil;
import com.yitong.ChuangKeYuan.utils.GetFilePath;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.utils.UploadUtil_Share;
import com.yitong.ChuangKeYuan.widget.CustomDialog_upload;

import java.io.File;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;

/**
 * 发现中的发布页面
 */

public class ShareActivity extends Activity implements OnClickListener, UploadUtil_Share.OnUploadProcessListener, View.OnTouchListener {
    private static final String TAG_PHOTO = "uploadImage";
    private static final String TAG_VIDEO = "uploadVideo";
    private static final String TAG_AUDIO = "uploadAudio";
    /**
     * 实例化的一个空集合；
     */
    private List Pic_Arr_url = new ArrayList();

    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2;
    /**
     * 上传初始化
     */

    private static final int UPLOAD_INIT_PROCESS = 3;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 4;
    /**
     * 这里的这个URL是服务器的环境URL
     */
    private static String requestURL = DatasUtil.URL_BASE;
    private static String requestURL_PHOTO = requestURL + "/Moments/uploadImg/";
    private static String requestURL_VIDEO = requestURL + "/Moments/uploadVideo/";
    private static String requestURL_AUDIO = requestURL + "/Moments/uploadAudio/";
    private Button btnShare;
    private ImageView mBack, mSearch, mShare1, mShare2, mShare3, mShare4, mShare5, mShare6, iv1, iv2, iv3, iv4;
    private TextView mTitle, uploadResult, upload_title, upload_des1, upload_des2, upload_des3, upload_des4, upload_btn;
    private View v1, v2, v3, v4;
    private LinearLayout ll1, ll2, ll3, ll4, image_two_ll;
    private EditText mDes;
    private ProgressDialog progressDialog;
    private int number = 0;
    private String mSize1, mSize, mSize2;
    /**
     * 获取到的图片路径
     */
    private String url;
    private Intent lastIntent;
    private Uri photoUri;
    /**
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 5;
    /**
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 6;
    /**
     * 从Intent获取图片路径的KEY
     */
    public static final String KEY_PHOTO_PATH = "photo_path";
    private static final String TAG1 = "SelectPic";
    private String[] strings = {"", "", "", "", "", "", "", "", ""};

    /**
     * 获取到的视频路径
     */
    private String videoPath;
    /**
     * 上传后的视频网址
     */
    private Uri videoUri;
    /**
     * 使用录影机录像获取视频
     */
    public static final int SELECT_VIDEO_BY_TACK_VIDEO = 7;
    /**
     * 使用本地文件中的视频
     */
    public static final int SELECT_VIDEO_BY_PICK_VIDEO = 8;
    /**
     * 从Intent获取视频路径的KEY
     */
    public static final String KEY_VIDEO_PATH = "video_path";
    private static final String TAG2 = "SelectVideo";
    private String result;
    FormBody body;
    private FileUtils mFileUtils;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        //初始化标题栏
        initTitlebar();
        //初始化控件
        initView();
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mTitle = (TextView) findViewById(R.id.tv_teach_title);

        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
        mTitle.setText("分享");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mFileUtils = new FileUtils(ShareActivity.this);
        mDes = (EditText) findViewById(R.id.des);
        mDes.setOnTouchListener(this); // 解决scrollView中嵌套EditText导致不能上下滑动的问题
        uploadResult = (TextView) findViewById(R.id.uploadResult);
        mShare1 = (ImageView) findViewById(R.id.share1);
        mShare2 = (ImageView) findViewById(R.id.share2);
        mShare3 = (ImageView) findViewById(R.id.share3);
        mShare4 = (ImageView) findViewById(R.id.share4);
        mShare5 = (ImageView) findViewById(R.id.share5);
        mShare6 = (ImageView) findViewById(R.id.share6);
        image_two_ll = (LinearLayout) findViewById(R.id.image_two_ll);

        btnShare = (Button) findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);

        mShare1.setOnClickListener(this);
        mShare2.setOnClickListener(this);
        mShare3.setOnClickListener(this);
        mShare4.setOnClickListener(this);
        mShare5.setOnClickListener(this);
        mShare6.setOnClickListener(this);
        uploadResult.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        lastIntent = getIntent();


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                finish();
                break;
            case R.id.share1:
                getDialog();
                upload_title.setText("请选择要分享的文件类型");
                iv1.setImageResource(R.drawable.dialog_photo);
                upload_des1.setText("图片");
                ll1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upload_title.setText("请选择图片");
                        iv1.setImageResource(R.drawable.dialog_tackphoto);
                        upload_des1.setText("拍照");
                        ll1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                takePhoto();
                                dialog.dismiss();
                            }
                        });
                        iv2.setImageResource(R.drawable.dialog_pickphoto);
                        upload_des2.setText("从相册选择");
                        ll2.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pickPhoto();
                                dialog.dismiss();
                            }
                        });
                        ll3.setVisibility(View.GONE);
                        ll4.setVisibility(View.GONE);
                        v3.setVisibility(View.GONE);
                        v4.setVisibility(View.GONE);
                        upload_btn.setText("取消");
                        upload_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                iv2.setImageResource(R.drawable.dialog_video);
                upload_des2.setText("视频");
                ll2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("meizu".equals(MyApplication.getSIGN())) {
                            upload_title.setText("请选择视频");
                            iv1.setImageResource(R.drawable.dialog_file);
                            upload_des1.setText("从本地文件选择");
                            ll1.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pickVideo_meizu();
                                    dialog.dismiss();
                                }
                            });
                            ll2.setVisibility(View.GONE);
                            ll3.setVisibility(View.GONE);
                            ll4.setVisibility(View.GONE);
                            v2.setVisibility(View.GONE);
                            v3.setVisibility(View.GONE);
                            v4.setVisibility(View.GONE);
                            upload_btn.setText("取消");
                            upload_btn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            upload_title.setText("请选择视频");
                            iv1.setImageResource(R.drawable.dialog_takevideo);
                            upload_des1.setText("录像");
                            ll1.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    takeVideo();
                                    dialog.dismiss();
                                }
                            });
                            iv2.setImageResource(R.drawable.dialog_file);
                            upload_des2.setText("从本地文件选择");
                            ll2.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pickVideo();
                                    dialog.dismiss();
                                }
                            });
                            ll3.setVisibility(View.GONE);
                            ll4.setVisibility(View.GONE);
                            v3.setVisibility(View.GONE);
                            v4.setVisibility(View.GONE);
                            upload_btn.setText("取消");
                            upload_btn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
                ll3.setVisibility(View.GONE);
                ll4.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                v4.setVisibility(View.GONE);
                upload_btn.setText("取消");
                upload_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                number = 1;
                break;
            case R.id.share2:
                getDialog();
                upload_title.setText("请选择图片");
                iv1.setImageResource(R.drawable.dialog_tackphoto);
                upload_des1.setText("拍照");
                ll1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto();
                        dialog.dismiss();
                    }
                });
                iv2.setImageResource(R.drawable.dialog_pickphoto);
                upload_des2.setText("从相册选择");
                ll2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickPhoto();
                        dialog.dismiss();
                    }
                });
                ll3.setVisibility(View.GONE);
                ll4.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                v4.setVisibility(View.GONE);
                upload_btn.setText("取消");
                upload_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                number = 2;
                break;
            case R.id.share3:
                getDialog();
                upload_title.setText("请选择图片");
                iv1.setImageResource(R.drawable.dialog_tackphoto);
                upload_des1.setText("拍照");
                ll1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto();
                        dialog.dismiss();
                    }
                });
                iv2.setImageResource(R.drawable.dialog_pickphoto);
                upload_des2.setText("从相册选择");
                ll2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickPhoto();
                        dialog.dismiss();
                    }
                });
                ll3.setVisibility(View.GONE);
                ll4.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                v4.setVisibility(View.GONE);
                upload_btn.setText("取消");
                upload_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                number = 3;
                break;
            case R.id.share4:
                getDialog();
                upload_title.setText("请选择图片");
                iv1.setImageResource(R.drawable.dialog_tackphoto);
                upload_des1.setText("拍照");
                ll1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto();
                        dialog.dismiss();
                    }
                });
                iv2.setImageResource(R.drawable.dialog_pickphoto);
                upload_des2.setText("从相册选择");
                ll2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickPhoto();
                        dialog.dismiss();
                    }
                });
                ll3.setVisibility(View.GONE);
                ll4.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                v4.setVisibility(View.GONE);
                upload_btn.setText("取消");
                upload_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                number = 4;
                break;
            case R.id.share5:
                getDialog();
                upload_title.setText("请选择图片");

                upload_des1.setText("拍照");
                iv1.setImageResource(R.drawable.dialog_tackphoto);
                ll1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto();
                        dialog.dismiss();
                    }
                });
                upload_des2.setText("从相册选择");
                iv2.setImageResource(R.drawable.dialog_pickphoto);
                ll2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickPhoto();
                        dialog.dismiss();
                    }
                });
                ll3.setVisibility(View.GONE);
                ll4.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                v4.setVisibility(View.GONE);
                upload_btn.setText("取消");
                upload_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                number = 5;
                break;
            case R.id.share6:
                getDialog();
                upload_title.setText("请选择图片");
                iv1.setImageResource(R.drawable.dialog_tackphoto);
                upload_des1.setText("拍照");
                ll1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto();
                        dialog.dismiss();
                    }
                });
                iv2.setImageResource(R.drawable.dialog_pickphoto);
                upload_des2.setText("从相册选择");
                ll2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickPhoto();
                        dialog.dismiss();
                    }
                });
                ll3.setVisibility(View.GONE);
                ll4.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                v4.setVisibility(View.GONE);
                upload_btn.setText("取消");
                upload_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                number = 6;
                break;
            case R.id.btn_share:

                progressDialog = new ProgressDialog(ShareActivity.this);
                progressDialog.setMessage("正在发布，请稍后...");
                progressDialog.setCancelable(false);
                Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog.dismiss();
                        finish();
                        super.handleMessage(msg);
                    }
                };


                final String content = mDes.getText().toString();
                if (content != null && !("".equals(content))) {
                    for (int i = 0; i < strings.length; i++) {
                        if (!("".endsWith(strings[i])) && strings != null) {
                            Pic_Arr_url.add(strings[i]);
                        }
                    }
                    uploadResult.setText(ToString(Pic_Arr_url));
                    Share(content, ToString(Pic_Arr_url), mHandler);
                    progressDialog.show();
                } else {
                    ToastUtil.showToast(ShareActivity.this, "请填写内容");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 最后的上传
     *
     * @param content  内容
     * @param url      资源网址
     * @param mhandler
     */
    public void Share(final String content, final String url, final Handler mhandler) {
        new Thread() {
            @Override
            public void run() {
                String data = "";
                Log.i("UPFIND", url);
                if (url.endsWith("png,") | url.endsWith(".PNG,") | url.endsWith(".jpg,")
                        | url.endsWith(".JPG,") | url.endsWith(".gif,") | url.endsWith(".GIF,")
                        | url.endsWith(".jpeg,") | url.endsWith(".JPEG,")) {
                    Log.i("AAAAAAAAA", url);
                    body = new FormBody.Builder()
                            .add("username", MyApplication.getusername())
                            .add("password", MyApplication.getuserpassword())
                            .add("text", content)
                            .add("image", url)
                            .build();
                } else if (url.endsWith(".mp3,") || url.endsWith(".amr,") || url.endsWith(".flac,")
                        || url.endsWith(".ape,") || url.endsWith(".aac,")) {
                    Log.i("1111111", url);
                    body = new FormBody.Builder()
                            .add("username", MyApplication.getusername())
                            .add("password", MyApplication.getuserpassword())
                            .add("text", content)
                            .add("audio", url)
                            .build();
                } else if (url.endsWith(".mp4,") || url.endsWith(".3gp,") ||
                        url.endsWith(".wav,") || url.endsWith(".rm,") || url.endsWith(".rmvb,")) {
                    Log.i("22222222222", url);
                    body = new FormBody.Builder()
                            .add("username", MyApplication.getusername())
                            .add("password", MyApplication.getuserpassword())
                            .add("text", content)
                            .add("video", url)
                            .build();
                } else {
                    Log.i("33333333", url);
                    body = new FormBody.Builder()
                            .add("username", MyApplication.getusername())
                            .add("password", MyApplication.getuserpassword())
                            .add("text", content)
                            .build();
                }
                Message message = mhandler.obtainMessage();
                if (body != null) {
                    try {
                        data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_Publish, body);
                        Gson gson = new Gson();
                        ReturnResult returnResult = gson.fromJson(data, ReturnResult.class);
                        Log.i("FIND", data);
                        Looper.prepare();
                        if (Integer.parseInt(returnResult.getCode()) != 0) {
                            mhandler.sendMessage(message);
                            ToastUtil.showToast(ShareActivity.this, "分享失败，请稍后再试...");
                        } else {
                            mhandler.sendMessage(message);
                            ToastUtil.showToast(ShareActivity.this, "分享成功");
                        }
                        Looper.loop();
                    } catch (Exception e) {
                        mhandler.sendMessage(message);
                        ToastUtil.showToast(ShareActivity.this, "分享失败，请稍后再试...");
                        return;
                    }
                } else {
                    mhandler.sendMessage(message);
                    ToastUtil.showToast(ShareActivity.this, "分享失败，请稍后再试...");
                }
            }
        }.start();
        MyApplication.setFLAG(666);

    }


    /**
     * 上传图片，音频，视频的方法
     */
    private void upload(String view, int TAG, ImageView view1, Bitmap bitmap, View view2) {
        Log.i("AABC", view);
        Double d = Double.valueOf(view);
        Double maxSize = Double.valueOf(10 * 1024);
        if (view1.getTag() != null && Integer.parseInt(view1.getTag().toString()) == 0) {
            maxSize = Double.valueOf(20 * 1024);
        }
        if (url != null || videoPath != null) {
            if (maxSize >= d) {
                view1.setImageBitmap(bitmap);
                if (view2 != null) {
                    view2.setVisibility(View.VISIBLE);
                }
                Message message = Message.obtain();
                message.arg2 = TAG;
                message.what = TO_UPLOAD_FILE;
                handler.sendMessage(message);
            } else {
                ToastUtil.showToast(ShareActivity.this, "对不起，您选择的文件过大！请重新选择..");
            }
        } else {
            ToastUtil.showToast(this, "请先选择您要上传的文件");
        }
    }

    /**
     * 上传第一条数据的方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param mShare1
     */

    private void Share1(int requestCode, int resultCode, Intent data, ImageView mShare1) {
        Log.i("AABC", requestCode + "");
        Log.i("AABC", resultCode + "");
        Map<String, SoftReference<Bitmap>> mImageCacheMap = new HashMap<String, SoftReference<Bitmap>>();

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_TACK_PHOTO) {
            if (!doPhoto(requestCode, data)) {
                return;
            }

            Log.i(TAG_PHOTO, "最终选择的图片是 ：" + url);
            BitmapFactory.Options myoptions = new BitmapFactory.Options();
            myoptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(url, myoptions);
            myoptions.inJustDecodeBounds = false;
            myoptions.inSampleSize = 4;
            myoptions.inPurgeable = true;
            myoptions.inInputShareable = true;
            myoptions.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bm = BitmapFactory.decodeFile(url, myoptions);
            SoftReference<Bitmap> d = new SoftReference<Bitmap>(bm);
            mImageCacheMap.put(url, d);
            SoftReference<Bitmap> softReference = mImageCacheMap.get(url);
            File f = new File(url);
            try {
                mSize1 = String.valueOf(FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB));
            } catch (Exception e) {
                Log.i("AABC", "异常 :" + e);
                mSize1 = "6000";
            }
            if (softReference.get() != null) {
                mShare1.setTag("0");
                upload(mSize1, 1, mShare1, softReference.get(), mShare2);
            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_PICK_PHOTO) {

            Log.i(TAG_PHOTO, "最终选择的图片是 ：" + data.getData().toString());

            if (data != null) {
                if ("xiaomi".equals(MyApplication.getSIGN())) {//手机品牌适配
                    url = data.getData().toString().substring(7);
                } else {
                    if (!doPhoto(requestCode, data)) {
                        return;
                    }
                }
                Log.i(TAG_PHOTO, "最终选择的图片是 ：" + url);
                BitmapFactory.Options myoptions = new BitmapFactory.Options();
                myoptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(url, myoptions);
                myoptions.inJustDecodeBounds = false;
                myoptions.inSampleSize = 4;
                myoptions.inPurgeable = true;
                myoptions.inInputShareable = true;
                myoptions.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bm = BitmapFactory.decodeFile(url, myoptions);
                SoftReference<Bitmap> d = new SoftReference<Bitmap>(bm);
                mImageCacheMap.put(url, d);
                SoftReference<Bitmap> softReference = mImageCacheMap.get(url);
                File f = new File(url);
                try {
                    mSize1 = String.valueOf(FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB));
                } catch (Exception e) {
                    Log.i("AABC", "异常 :" + e);
                    mSize1 = "6000";
                }
                if (softReference.get() != null) {
                    mShare1.setTag("0");
                    upload(mSize1, 1, mShare1, softReference.get(), mShare2);
                }
            } else {
                return;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_VIDEO_BY_TACK_VIDEO) {
            //录制视频，华为手机可以，平板路径有，但是获取不到大小！
            if (!doVideo(requestCode, data)) {
                return;
            }
            Log.i(TAG_VIDEO, "最终选择的视频是 ：" + videoPath);
            if (FileSizeUtil.getFileOrFilesSize(videoPath, 2) > 0) {
                try {
                    mSize2 = (String.valueOf(FileSizeUtil.getFileOrFilesSize(videoPath, 2)));
                } catch (Exception e) {
                    return;
                }
                upload(mSize2, 1, mShare1, getVideoThumbnail(videoPath), null);
            } else {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_VIDEO_BY_PICK_VIDEO) {
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
                if (FileSizeUtil.getFileOrFilesSize(videoPath, 2) > 0) {
                    try {
                        mSize2 = (String.valueOf(FileSizeUtil.getFileOrFilesSize(videoPath, 2)));
                    } catch (Exception e) {
                        mSize2 = "10000";
                    }
                    upload(mSize2, 1, mShare1, getVideoThumbnail(videoPath), null);
                } else {
                    ToastUtil.showToast(this, "文件不正确，请重新选择...");
                    return;
                }
            } else {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
        }
    }

    /**
     * 上传其它八张图片的方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param mShare
     */
    private void Share(int requestCode, int resultCode, Intent data, ImageView mShare) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_TACK_PHOTO) {

            if (!doPhoto(requestCode, data)) {
                return;
            }

            Log.i(TAG_PHOTO, "最终选择的图片是 ：" + url);
            BitmapFactory.Options myoptions = new BitmapFactory.Options();
            myoptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(url, myoptions);
            myoptions.inJustDecodeBounds = false;
            myoptions.inSampleSize = 4;
            myoptions.inPurgeable = true;
            myoptions.inInputShareable = true;
            myoptions.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bm = BitmapFactory.decodeFile(url, myoptions);
            File f = new File(url);
            try {
                mSize = String.valueOf(FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB));
            } catch (Exception e) {
                Toast.makeText(ShareActivity.this, "图片选择不合适，请重新选择...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mShare == mShare2 && bm != null) {
                mShare2.setTag("0");
                upload(mSize, 2, mShare2, bm, mShare3);
            } else if (mShare == mShare3 && bm != null) {
                mShare3.setTag("0");
                upload(mSize, 3, mShare3, bm, mShare4);
            } else if (mShare == mShare4 && bm != null) {
                mShare4.setTag("0");
                upload(mSize, 4, mShare4, bm, mShare5);
            } else if (mShare == mShare5 && bm != null) {
                mShare5.setTag("0");
                upload(mSize, 5, mShare5, bm, mShare6);
            } else if (mShare == mShare6 && bm != null) {
                mShare6.setTag("0");
                mShare6.setImageBitmap(bm);
                upload(mSize, 6, mShare6, bm, null);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (data != null) {
                if ("xiaomi".equals(MyApplication.getSIGN())) {
                    url = data.getData().toString().substring(7);
                } else {
                    if (!doPhoto(requestCode, data)) {
                        return;
                    }
                }
                Log.i(TAG_PHOTO, "最终选择的图片是 ：" + url);
                BitmapFactory.Options myoptions = new BitmapFactory.Options();
                myoptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(url, myoptions);
                myoptions.inJustDecodeBounds = false;
                myoptions.inSampleSize = 4;
                myoptions.inPurgeable = true;
                myoptions.inInputShareable = true;
                myoptions.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bm = BitmapFactory.decodeFile(url, myoptions);
                File f = new File(url);
                try {
                    mSize = String.valueOf(FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB));
                } catch (Exception e) {
                    mSize = "6000";
                }
                if (mShare == mShare2 && bm != null) {
                    upload(mSize, 2, mShare2, bm, mShare3);
                } else if (mShare == mShare3 && bm != null) {
                    upload(mSize, 3, mShare3, bm, mShare4);
                } else if (mShare == mShare4 && bm != null) {
                    upload(mSize, 4, mShare4, bm, mShare5);
                } else if (mShare == mShare5 && bm != null) {
                    upload(mSize, 5, mShare5, bm, mShare6);
                } else if (mShare == mShare6 && bm != null) {
                    mShare6.setImageBitmap(bm);
                    upload(mSize, 6, mShare6, bm, null);
                }
            } else {
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (number == 1) {
            Share1(requestCode, resultCode, data, mShare1);
        } else if (number == 2) {
            Share(requestCode, resultCode, data, mShare2);
        } else if (number == 3) {
            Share(requestCode, resultCode, data, mShare3);
        } else if (number == 4) {
            Share(requestCode, resultCode, data, mShare4);
        } else if (number == 5) {
            Share(requestCode, resultCode, data, mShare5);
        } else if (number == 6) {
            Share(requestCode, resultCode, data, mShare6);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取视频缩略图的方法
     */

    public Bitmap getVideoThumbnail(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(videoPath);
            bitmap = retriever.getFrameAtTime();
        } catch (Exception e) {
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
            }
        }
        return bitmap;
    }

    /**
     * @param audioPath 文件路径，like XXX/XXX/XX.mp3
     * @return 专辑封面bitmap
     * @Description 获取音频专辑封面的方法
     */
    public Bitmap createAlbumArt(String audioPath) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(audioPath); //设置数据源
            byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
            if (embedPic != null) {
                bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
            }
        } catch (Exception e) {
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
            }
        }
        return bitmap;
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

    private void toUploadFile(int TAG) {
        progressDialog.setMessage("正在上传文件...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        UploadUtil_Share uploadUtil = UploadUtil_Share.getInstance();
        uploadUtil.setOnUploadProcessListener(this); //设置监听器监听上传状态
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", "11111");

        if (url == null) {
            String fileKey_video = "video";
            uploadUtil.uploadFile(videoPath, fileKey_video, requestURL_VIDEO, params, TAG);
        } else if (videoPath == null) {
            String fileKey_photo = "image";
            uploadUtil.uploadFile(url, fileKey_photo, requestURL_PHOTO, params, TAG);
        }
    }

    /**
     * 上传完成后的回调
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
                case UPLOAD_FILE_DONE:
                    if (msg.arg1 == 1) {//上传成功
                        //返回值中是 位置+网址 （1http...）
                        result = msg.obj.toString();
                        Log.i("FIND", result);
                        if (Integer.parseInt(result.substring(0, 1))==3)
                            image_two_ll.setVisibility(View.VISIBLE);
                        strings[Integer.parseInt(result.substring(0, 1)) - 1] = result.substring(1);
                        Log.i("FIND", "数组的长度 ：" + strings.length);
                    } else {//上传失败
                        if (msg.obj.toString() != null && !"".equals(msg.obj.toString())) {
                            int tag = -1;
                            try {
                                tag = Integer.parseInt(msg.obj.toString().substring(0, 1));
                            } catch (Exception e) {
                                tag = -1;
                            }
                            if (tag == 1) {//上传失败后，此位置的imageview设置默认图片
                                mShare1.setImageResource(R.drawable.tianjia);
                            } else if (tag == 2) {
                                mShare2.setImageResource(R.drawable.tianjia);
                            } else if (tag == 3) {
                                mShare3.setImageResource(R.drawable.tianjia);
                            } else if (tag == 4) {
                                mShare4.setImageResource(R.drawable.tianjia);
                            } else if (tag == 5) {
                                mShare5.setImageResource(R.drawable.tianjia);
                            } else if (tag == 6) {
                                mShare6.setImageResource(R.drawable.tianjia);
                            }
                        }
                        Toast.makeText(ShareActivity.this, "连接超时,上传失败!", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("SHARE", "上传返回值:" + msg.arg1);
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

    /**
     * 拍照获取图片
     */
    private File mCameraImageFile;// 照相机拍照得到的图片

    private void takePhoto() {
        try {
            File PHOTO_DIR = new File(mFileUtils.getStorageDirectory());
            if (!PHOTO_DIR.exists())
                PHOTO_DIR.mkdirs();// 创建照片的存储目录

            mCameraImageFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCameraImageFile);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } catch (ActivityNotFoundException e) {
        }
    }

    private Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy_MM_dd_HH_mm_ss");
        return dateFormat.format(date) + ".jpg";
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
        if (requestCode == SELECT_PIC_BY_TACK_PHOTO)
            url = mCameraImageFile.getAbsolutePath();
        else {
            url = mFileUtils.getFilePathFromUri(data.getData());
        }
        Log.i(TAG1, "imagePath = " + url);
        if (url != null && (url.endsWith(".png") || url.endsWith(".PNG") || url.endsWith(".jpg") ||
                url.endsWith(".JPG") || url.endsWith(".gif") || url.endsWith(".GIF") || url.endsWith(".jpeg") ||
                url.endsWith(".JPEG"))) {
            lastIntent.putExtra(KEY_PHOTO_PATH, url);
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
        Intent intent = new Intent(ShareActivity.this, AllVideoActivity.class);
        startActivityForResult(intent, SELECT_VIDEO_BY_PICK_VIDEO);
    }

    /**
     * 录像获取视频
     */
    private void takeVideo() {
        //执行录像前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            ContentValues values = new ContentValues();
            videoUri = this.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(intent, SELECT_VIDEO_BY_TACK_VIDEO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
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
        videoPath = GetFilePath.getPath(this, data.getData());
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

    /**
     * 将list集合转成字符串
     *
     * @param strings
     * @return
     */
    public String ToString(List strings) {
        String str = "";
        for (int i = 0; i < strings.size(); i++) {
            str += strings.get(i) + ",";
        }
        return str;
    }
}
