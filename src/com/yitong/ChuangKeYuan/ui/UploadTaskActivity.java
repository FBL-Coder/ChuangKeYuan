package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.FileSizeUtil;
import com.yitong.ChuangKeYuan.utils.GetFilePath;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.utils.UploadUtil_Upload;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;
import com.yitong.ChuangKeYuan.widget.CustomDialog_upload;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/6/30.
 * 上传任务
 */
public class UploadTaskActivity extends Activity implements OnClickListener, UploadUtil_Upload.OnUploadProcessListener, View.OnTouchListener {

    private static final String TAG_PHOTO = "uploadImage";
    private static final String TAG_VIDEO = "uploadVideo";
    private static final String TAG_AUDIO = "uploadAudio";
    private static final String TAG_FILE = "uploadfile";

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
    private static String requestURL_AUDIO = requestURL + "/Base/uploadAudio/";
    private static String requestURL_File = requestURL + "/Base/uploadfile/";
    private Button upload;
    private ImageView mSearch, mBack, mAddPic, mAddAudio, mHelp;
    private TextView mTitle, mName1, mSize1, mName2, mSize2, uploadResult_1, uploadResult_2,
            url_class_1, url_class_2, upload_title, upload_des1, upload_des2, upload_des3, upload_des4, upload_btn;
    private View v1, v2, v3, v4;
    private LinearLayout ll1, ll2, ll3, ll4;
    private ImageView iv1, iv2, iv3, iv4;
    private EditText mDes, content_up;
    private ProgressDialog progressDialog, mProgressDialog;
    private RelativeLayout mRelPic, mRelAudio;
    private int number = 0;
    //获取到的图片路径
    private String picPath;
    private Intent lastIntent;
    private Uri photoUri;
    //使用照相机拍照获取图片
    public static final int SELECT_PIC_BY_TACK_PHOTO = 5;
    //使用相册中的图片
    public static final int SELECT_PIC_BY_PICK_PHOTO = 6;
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

    //获取到的音频路径
    private String audioPath;
    private Uri audioUri;
    //使用录音机录音获取音频
    public static final int SELECT_AUDIO_BY_TACK_AUDIO = 9;
    //使用本地文件中的音频
    public static final int SELECT_AUDIO_BY_PICK_AUDIO = 10;
    //从Intent获取音频路径的KEY
    public static final String KEY_AUDIO_PATH = "audio_path";
    private static final String TAG3 = "SelectAudio";

    //获取到的文件路径
    private String filePath;
    //使用本地中的文件
    public static final int SELECT_FILE_BY_PICK_FILE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadtask);
        //初始化标题栏
        initTitlebar();
        //初始化控件
        initView();
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mHelp = (ImageView) findViewById(R.id.iv_teach_help);
        mBack = (ImageView) findViewById(R.id.iv_teach_back);

        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);
        mHelp.setVisibility(View.VISIBLE);
        mHelp.setOnClickListener(this);
        mTitle.setText("上传附件");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mDes = (EditText) findViewById(R.id.des);
        content_up = (EditText) findViewById(R.id.content_up);
        content_up.setOnTouchListener(this); // 解决scrollView中嵌套EditText导致不能上下滑动的问题

        mAddPic = (ImageView) findViewById(R.id.add_pic);
        mAddAudio = (ImageView) findViewById(R.id.add_audio);

        mRelPic = (RelativeLayout) findViewById(R.id.rel_pic);
        mRelAudio = (RelativeLayout) findViewById(R.id.rel_audio);

        upload = (Button) findViewById(R.id.upload);
        mName1 = (TextView) findViewById(R.id.name1);
        mSize1 = (TextView) findViewById(R.id.size1);
        mName2 = (TextView) findViewById(R.id.name2);
        mSize2 = (TextView) findViewById(R.id.size2);

        uploadResult_1 = (TextView) findViewById(R.id.uploadResult_1);
        uploadResult_2 = (TextView) findViewById(R.id.uploadResult_2);

        url_class_1 = (TextView) findViewById(R.id.url_class_1);
        url_class_2 = (TextView) findViewById(R.id.url_class_2);

        upload.setOnClickListener(this);
        mAddPic.setOnClickListener(this);
        mAddAudio.setOnClickListener(this);
        mRelPic.setOnClickListener(this);
        mRelAudio.setOnClickListener(this);

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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressDialog.dismiss();
            if (msg.arg1 == 1) {
                finish();
            }

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                finish();
                break;
            case R.id.add_pic:
                getDialog();
                upload_title.setText("请选择要上传的附件类型");
                iv1.setImageResource(R.drawable.dialog_photo);
                upload_des1.setText("图片");
                ll1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_class_1.setText("" + 1 + 1);
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
                iv2.setImageResource(R.drawable.dialog_audio);
                upload_des2.setText("音频");
                ll2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_class_1.setText("" + 2 + 1);
                        if ("meizu".equals(MyApplication.getSIGN()) || "xiaomi".equals(MyApplication.getSIGN())) {
                            upload_title.setText("请选择音频");
                            iv1.setImageResource(R.drawable.dialog_file);
                            upload_des1.setText("从本地文件选择");
                            ll1.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pickAudio();
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
                            upload_title.setText("请选择音频");
                            iv1.setImageResource(R.drawable.dialog_takeaudio);
                            upload_des1.setText("录音");
                            ll1.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    takeAudio();
                                    dialog.dismiss();
                                }
                            });
                            iv2.setImageResource(R.drawable.dialog_file);
                            upload_des2.setText("从本地文件选择");
                            ll2.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pickAudio();
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
                iv3.setImageResource(R.drawable.dialog_video);
                upload_des3.setText("视频");
                ll3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_class_1.setText("" + 3 + 1);
                        upload_title.setText("请选择视频");
                        iv1.setImageResource(R.drawable.dialog_file);
                        upload_des1.setText("从本地文件选择");
                        ll1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ("meizu".equals(MyApplication.getSIGN())) {
                                    pickVideo_meizu();
                                } else {
                                    pickVideo();
                                }
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
                    }
                });
                iv4.setImageResource(R.drawable.dialog_file);
                upload_des4.setText("文件");
                ll4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_class_1.setText("" + 4 + 1);
                        upload_title.setText("请选择文件");
                        iv1.setImageResource(R.drawable.dialog_file);
                        upload_des1.setText("从本地文件选择");
                        ll1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ("meizu".equals(MyApplication.getSIGN())) {
                                    pickFile_meizu();
                                } else {
                                    pickFile();
                                }
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
                    }
                });
                upload_btn.setText("取消");
                upload_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                number = 1;
                break;
            case R.id.add_audio:
                getDialog();
                upload_title.setText("请选择要上传的附件类型");
                iv1.setImageResource(R.drawable.dialog_photo);
                upload_des1.setText("图片");
                ll1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_class_2.setText("" + 1 + 2);
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
                iv2.setImageResource(R.drawable.dialog_audio);
                upload_des2.setText("音频");
                ll2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_class_2.setText("" + 2 + 2);
                        if ("meizu".equals(MyApplication.getSIGN()) || "xiaomi".equals(MyApplication.getSIGN())) {
                            upload_title.setText("请选择音频");
                            iv1.setImageResource(R.drawable.dialog_file);
                            upload_des1.setText("从本地文件选择");
                            ll1.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pickAudio();
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
                            upload_title.setText("请选择音频");
                            iv1.setImageResource(R.drawable.dialog_takeaudio);
                            upload_des1.setText("录音");
                            ll1.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    takeAudio();
                                    dialog.dismiss();
                                }
                            });
                            iv2.setImageResource(R.drawable.dialog_file);
                            upload_des2.setText("从本地文件选择");
                            ll2.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pickAudio();
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
                iv3.setImageResource(R.drawable.dialog_video);
                upload_des3.setText("视频");
                ll3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_class_2.setText("" + 3 + 2);
                        upload_title.setText("请选择视频");
                        iv1.setImageResource(R.drawable.dialog_file);
                        upload_des1.setText("从本地文件选择");
                        ll1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ("meizu".equals(MyApplication.getSIGN())) {
                                    pickVideo_meizu();
                                } else {
                                    pickVideo();
                                }
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
                    }
                });
                iv4.setImageResource(R.drawable.dialog_file);
                upload_des4.setText("文件");
                ll4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url_class_2.setText("" + 4 + 2);
                        upload_title.setText("请选择文件");
                        iv1.setImageResource(R.drawable.dialog_file);
                        upload_des1.setText("从本地文件选择");
                        ll1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ("meizu".equals(MyApplication.getSIGN())) {
                                    pickFile_meizu();
                                } else {
                                    pickFile();
                                }
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
                    }
                });
                upload_btn.setText("取消");
                upload_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                number = 2;
                break;
            case R.id.upload:

                mProgressDialog = new ProgressDialog(UploadTaskActivity.this);
                mProgressDialog.setMessage("正在上传，请稍后...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();


                //存放返回网址以及类型的组件，并在这些组件中获取返回的数据
                final String title = mDes.getText().toString();
                final String text = content_up.getText().toString();
                String one = uploadResult_1.getText().toString();
                String two = uploadResult_2.getText().toString();
                String one_class = url_class_1.getText().toString();
                String two_class = url_class_2.getText().toString();
                String jsonUp = null;

                if (title != null && !("".equals(title))) {//判断标题是否为空
                    try {
                        if (two != null && !("".equals(two))) {//判断第二个位置是否为空
                            if ("11".equals(one_class)) {//判断类型
                                if ("21".equals(two_class)) {//判断类型
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", one);
                                    JSONObject object1 = new JSONObject();
                                    object.put("title", title);
                                    object1.put("url", two);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);

                                } else if ("22".equals(two_class)) {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", one);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", two);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);

                                } else if ("23".equals(two_class)) {

                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", one);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", two);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);

                                } else {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", one);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", two);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);

                                }
                            } else if ("12".equals(one_class)) {
                                if ("21".equals(two_class)) {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);
                                } else if ("22".equals(two_class)) {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);

                                } else if ("23".equals(two_class)) {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);
                                } else {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);
                                }
                            } else if ("13".equals(one_class)) {
                                if ("21".equals(two_class)) {

                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);
                                } else if ("22".equals(two_class)) {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);

                                } else if ("23".equals(two_class)) {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);
                                } else {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);
                                }
                            } else {
                                if ("21".equals(two_class)) {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);

                                } else if ("22".equals(two_class)) {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);
                                } else if ("23".equals(two_class)) {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);
                                } else {
                                    JSONObject object = new JSONObject();
                                    object.put("title", title);
                                    object.put("url", two);
                                    JSONObject object1 = new JSONObject();
                                    object1.put("title", title);
                                    object1.put("url", one);
                                    JSONArray array = new JSONArray();
                                    array.put(object);
                                    array.put(object1);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("attachment", array);
                                    jsonUp = jsonObject.toString();
                                    UpData(title, text, jsonUp, mHandler);
                                }
                            }
                        } else if (one != null && !("".equals(one))) {
                            if ("11".equals(one_class)) {
                                JSONObject object = new JSONObject();
                                object.put("title", title);
                                object.put("url", one);
                                JSONArray array = new JSONArray();
                                array.put(object);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("attachment", array);
                                jsonUp = jsonObject.toString();
                                UpData(title, text, jsonUp, mHandler);
                            } else if ("12".equals(one_class)) {
                                JSONObject object = new JSONObject();
                                object.put("title", title);
                                object.put("url", one);
                                JSONArray array = new JSONArray();
                                array.put(object);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("attachment", array);
                                jsonUp = jsonObject.toString();
                                UpData(title, text, jsonUp, mHandler);
                            } else if ("13".equals(one_class)) {
                                JSONObject object = new JSONObject();
                                object.put("title", title);
                                object.put("url", one);
                                JSONArray array = new JSONArray();
                                array.put(object);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("attachment", array);
                                jsonUp = jsonObject.toString();
                                UpData(title, text, jsonUp, mHandler);
                            } else {
                                JSONObject object = new JSONObject();
                                object.put("url", one);
                                object.put("title", title);
                                JSONArray array = new JSONArray();
                                array.put(object);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("attachment", array);
                                jsonUp = jsonObject.toString();
                                UpData(title, text, jsonUp, mHandler);
                            }
                        } else {
                            if (text == null | "".equals(text)) {
                                mProgressDialog.dismiss();
                                ToastUtil.showToast(UploadTaskActivity.this, "正文和附件必填一个！");
                            } else {
                                UpData(title, text, "", mHandler);
                            }
                        }
                    } catch (Exception e) {
                        mProgressDialog.dismiss();
                        ToastUtil.showToast(UploadTaskActivity.this, "对不起，数据处理异常，请稍后再试...");
                        return;
                    }
                } else {
                    mProgressDialog.dismiss();
                    ToastUtil.showToast(UploadTaskActivity.this, "标题为空");
                    return;
                }
                break;
            case R.id.iv_teach_help:
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(UploadTaskActivity.this);
                builder.setTitle("说明：");
                builder.setMessage("        标题必填；正文填写后将生成为.txt文件；附件可添加可不添加；作业上传成功后，点击条目则进行下载作业操作，长按条目则进行删除作业操作。");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            default:
                break;
        }
    }

    /**
     * 将选择的附件跟标题一起上传到文章列表
     *
     * @param
     * @param
     * @param jsonUp
     */
    public void UpData(final String title, final String text, final String jsonUp, final Handler mHandler) {

        new Thread() {
            @Override
            public void run() {

                String data = null;
                FormBody body = new FormBody.Builder()
                        .add("username", MyApplication.getusername())
                        .add("password", MyApplication.getuserpassword())
                        .add("aid", getIntent().getStringExtra("aid"))
                        .add("smeta", jsonUp)
                        .add("title", title)
                        .add("text", text)
                        .build();
                Log.i("AABC", "上传的JSON :" + jsonUp);

                Message message = mHandler.obtainMessage();
                try {
                    data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_addHomework, body);
                    Log.i("AABC", "上传完后返回的数据 :" + data);

                    Gson gson = new Gson();
                    ReturnResult returnResult = gson.fromJson(data, ReturnResult.class);
                    Looper.prepare();
                    if (Integer.parseInt(returnResult.getCode()) == 0) {
                        message.arg1 = 1;
                        mHandler.sendMessage(message);
                        ToastUtil.showToast(UploadTaskActivity.this, "上传成功");
                        MyApplication.setFLAG(1010);
                        finish();
                    } else if (Integer.parseInt(returnResult.getCode()) == 10013) {
                        mHandler.sendMessage(message);
                        ToastUtil.showToast(UploadTaskActivity.this, "请在自己所属班级内上传...");
                    } else if (Integer.parseInt(returnResult.getCode()) == 10012) {
                        mHandler.sendMessage(message);
                        ToastUtil.showToast(UploadTaskActivity.this, "标题已存在，请更改标题...");
                    } else {
                        mHandler.sendMessage(message);
                        ToastUtil.showToast(UploadTaskActivity.this, "上传失败，请稍后再试！");
                    }
                    Looper.loop();
                } catch (Exception e) {
                    mHandler.sendMessage(message);
                    Looper.prepare();
                    ToastUtil.showToast(UploadTaskActivity.this, "对不起，数据处理异常，请稍后再试...");
                    Looper.loop();
                }
            }
        }.start();
    }

    /**
     * 上传图片，音频，视频和文件的方法
     */
    private void upload(TextView view, int TAG, ImageView view1, Bitmap bitmap, View view2, int tag, TextView view3, String name, Double size) {
        Double d = size;

        Double maxSize = Double.valueOf(50 * 1024);//限制上传文件大小在50M以内
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") ||
                picPath.endsWith(".jpg") || picPath.endsWith(".JPG") || picPath.endsWith(".gif") ||
                picPath.endsWith(".GIF") || picPath.endsWith(".jpeg") || picPath.endsWith(".JPEG"))) {
            maxSize = Double.valueOf(50 * 1024);
        }
        if (picPath != null || videoPath != null || audioPath != null || filePath != null) {
            if (maxSize >= d) {
//                handler.sendEmptyMessage(TO_UPLOAD_FILE);
                //*************************************************
                if (d >= 1) {
                    if (bitmap != null) {
                        view1.setImageBitmap(bitmap);
                    } else {
                        if (tag == 0) {
                            view1.setImageResource(R.drawable.add_pic);
                        } else if (tag == 1) {
                            view1.setImageResource(R.drawable.add_video);
                        } else if (tag == 2) {
                            view1.setImageResource(R.drawable.add_audio);
                        } else {
                            view1.setImageResource(R.drawable.add_file);
                        }
                    }
                    view3.setText(name);
                    view.setText(size + "KB");
                    if (view2 != null) {
                        view2.setVisibility(View.VISIBLE);
                    }
                    Message message = Message.obtain();
                    message.arg2 = TAG;
                    message.what = TO_UPLOAD_FILE;
                    handler.sendMessage(message);
                } else {
                    view3.setText("");
                    view.setText("");
                    view1.setImageResource(R.drawable.tianjia);
                    ToastUtil.showToast(UploadTaskActivity.this, "对不起，您选择的文件太小，或者路径异常，请重新选择！");
                }
            } else {
                view3.setText("");
                view.setText("");
                view1.setImageResource(R.drawable.tianjia);
                ToastUtil.showToast(UploadTaskActivity.this, "对不起，您选择的文件过大！请重新选择..");
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
     * @param mAddPic
     * @param mName1
     * @param mSize1
     */
    Map<String, SoftReference<Bitmap>> mImageCacheMap = new HashMap<String, SoftReference<Bitmap>>();
    String name;
    Double size;

    private void Show_pic(int requestCode, int resultCode, Intent data, ImageView mAddPic, TextView mName1, TextView mSize1) {

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_TACK_PHOTO) {

            if (!doPhoto(requestCode, data)) {
                return;
            }

            Log.i(TAG_PHOTO, "最终选择的图片是 ：" + picPath);

            BitmapFactory.Options myoptions = new BitmapFactory.Options();
            myoptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picPath, myoptions);
            myoptions.inJustDecodeBounds = false;
            myoptions.inSampleSize = 6;
            myoptions.inPurgeable = true;
            myoptions.inInputShareable = true;
            myoptions.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bm = BitmapFactory.decodeFile(picPath, myoptions);
            SoftReference<Bitmap> d = new SoftReference<Bitmap>(bm);
            mImageCacheMap.put(picPath, d);
            SoftReference<Bitmap> softReference = mImageCacheMap.get(picPath);

            name = UploadUtil_Upload.getFileName(picPath);
            File f = new File(picPath);
            try {
                size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
            } catch (Exception e) {
                size = Double.valueOf(10000);
            }
            upload(mSize1, 1, mAddPic, softReference.get(), mRelAudio, 0, mName1, name, size);

        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (data != null) {
                if (!doPhoto(requestCode, data)) {
                    return;
                }
                Log.i(TAG_PHOTO, "最终选择的图片是 ：" + picPath);

                BitmapFactory.Options myoptions = new BitmapFactory.Options();
                myoptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picPath, myoptions);
                myoptions.inJustDecodeBounds = false;
                myoptions.inSampleSize = 6;
                myoptions.inPurgeable = true;
                myoptions.inInputShareable = true;
                myoptions.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bm = BitmapFactory.decodeFile(picPath, myoptions);
                SoftReference<Bitmap> d = new SoftReference<Bitmap>(bm);
                mImageCacheMap.put(picPath, d);
                SoftReference<Bitmap> softReference = mImageCacheMap.get(picPath);

                name = UploadUtil_Upload.getFileName(picPath);
                File f = new File(picPath);
                try {
                    size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
                } catch (Exception e) {
                    size = Double.valueOf(10000);
                }
                upload(mSize1, 1, mAddPic, softReference.get(), mRelAudio, 0, mName1, name, size);
            } else {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_VIDEO_BY_PICK_VIDEO) {

            if (data.getData().toString() != null && !("".equals(data.getData().toString()))) {

                    if (!doVideo(requestCode, data)) {
                        return;
                    }
                Log.i(TAG_VIDEO, "最终选择的视频是 ：" + videoPath);
                name = UploadUtil_Upload.getFileName(videoPath);
                if (FileSizeUtil.getFileOrFilesSize(videoPath, 2) > 0) {
                    try {
                        size = FileSizeUtil.getFileOrFilesSize(videoPath, 2);
                    } catch (Exception e) {
                        size = Double.valueOf(10000);
                    }
                    upload(mSize1, 1, mAddPic, getVideoThumbnail(videoPath), mRelAudio, 1, mName1, name, size);
                } else {
                    ToastUtil.showToast(this, "文件不正确，请重新选择...");
                    return;
                }
            } else {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_AUDIO_BY_PICK_AUDIO) {
            Log.i(TAG_AUDIO, "最终选择的音频是 ：" + data.getData().getPath());
            Log.i(TAG_VIDEO, "最终选择的音频是 ：" + data.getData().toString());

            if (doAudio_pick(requestCode, data)) {
                Log.i(TAG_AUDIO, "最终选择的音频是 ：" + audioPath);
                name = UploadUtil_Upload.getFileName(audioPath);
                File f = new File(audioPath);
                try {
                    if (FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f),
                            FileSizeUtil.SIZETYPE_KB) > 0) {
                        size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
                    } else {
                        ToastUtil.showToast(this, "文件不正确，请重新选择...");
                        return;
                    }
                } catch (Exception e) {
                    ToastUtil.showToast(this, "文件不正确，请重新选择...");
                    return;
                }
                upload(mSize1, 1, mAddPic, createAlbumArt(audioPath), mRelAudio, 2, mName1, name, size);
            } else {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_AUDIO_BY_TACK_AUDIO) {
            Log.i(TAG_AUDIO, "最终选择的音频是 ：" + data.getData());
            if (!doAudio_tack(requestCode, data)) {
                return;
            }

            Log.i(TAG_AUDIO, "最终选择的音频是 ：" + audioPath);
            name = UploadUtil_Upload.getFileName(audioPath);
            File f = new File(audioPath);
            try {
                if (FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f),
                        FileSizeUtil.SIZETYPE_KB) > 0) {
                    size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
                } else {
                    ToastUtil.showToast(this, "文件不正确，请重新选择...");
                    return;
                }
            } catch (Exception e) {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
            upload(mSize1, 1, mAddPic, createAlbumArt(audioPath), mRelAudio, 2, mName1, name, size);
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE_BY_PICK_FILE) {
            if ("meizu".equals(MyApplication.getSIGN()))
                filePath = String.valueOf(data.getData());
            else
                filePath = GetFilePath.getPath(this, data.getData());
            if (filePath == null)
                return;
            Log.i(TAG_FILE, "最终选择的文件是 ：" + filePath);
            name = UploadUtil_Upload.getFileName(filePath);
            File f = new File(filePath);
            try {
                size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
            } catch (Exception e) {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
            upload(mSize1, 1, mAddPic, null, mRelAudio, 3, mName1, name, size);
        }

    }

    /**
     * 上传第二条数据的方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param mAddAudio
     * @param mName2
     * @param mSize2
     */

    private void Show_audio(int requestCode, int resultCode, Intent data, ImageView mAddAudio, TextView mName2, TextView mSize2) {

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (data != null) {
                if (!doPhoto(requestCode, data)) {
                    return;
                }
                Log.i(TAG_PHOTO, "最终选择的图片是 ：" + picPath);

                BitmapFactory.Options myoptions = new BitmapFactory.Options();
                myoptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picPath, myoptions);
                myoptions.inJustDecodeBounds = false;
                myoptions.inSampleSize = 6;
                myoptions.inPurgeable = true;
                myoptions.inInputShareable = true;
                myoptions.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bm = BitmapFactory.decodeFile(picPath, myoptions);
                SoftReference<Bitmap> d = new SoftReference<Bitmap>(bm);
                mImageCacheMap.put(picPath, d);
                SoftReference<Bitmap> softReference = mImageCacheMap.get(picPath);

                name = UploadUtil_Upload.getFileName(picPath);
                File f = new File(picPath);
                try {
                    size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
                } catch (Exception e) {
                    size = Double.valueOf(10000);
                }
                upload(mSize2, 2, mAddAudio, softReference.get(), null, 0, mName2, name, size);
            } else {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PIC_BY_TACK_PHOTO) {
            if (!doPhoto(requestCode, data)) {
                return;
            }
            Log.i(TAG_PHOTO, "最终选择的图片是 ：" + picPath);

            BitmapFactory.Options myoptions = new BitmapFactory.Options();
            myoptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picPath, myoptions);
            myoptions.inJustDecodeBounds = false;
            myoptions.inSampleSize = 6;
            myoptions.inPurgeable = true;
            myoptions.inInputShareable = true;
            myoptions.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bm = BitmapFactory.decodeFile(picPath, myoptions);
            SoftReference<Bitmap> d = new SoftReference<Bitmap>(bm);
            mImageCacheMap.put(picPath, d);
            SoftReference<Bitmap> softReference = mImageCacheMap.get(picPath);

            name = UploadUtil_Upload.getFileName(picPath);
            File f = new File(picPath);
            try {
                size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
            } catch (Exception e) {
                size = Double.valueOf(10000);
            }
            upload(mSize2, 2, mAddAudio, softReference.get(), null, 0, mName2, name, size);

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
                name = UploadUtil_Upload.getFileName(videoPath);
                if (FileSizeUtil.getFileOrFilesSize(videoPath, 2) > 0) {
                    try {
                        size = FileSizeUtil.getFileOrFilesSize(videoPath, 2);
                    } catch (Exception e) {
                        size = Double.valueOf(10000);
                    }
                    upload(mSize2, 2, mAddAudio, getVideoThumbnail(videoPath), null, 1, mName2, name, size);
                } else {
                    ToastUtil.showToast(this, "文件不正确，请重新选择...");
                }
            } else {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_AUDIO_BY_PICK_AUDIO) {
            if (!doAudio_pick(requestCode, data)) {
                return;
            }
            Log.i(TAG_AUDIO, "最终选择的音频是 ：" + audioPath);
            name = UploadUtil_Upload.getFileName(audioPath);
            File f = new File(audioPath);
            try {
                if (FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f),
                        FileSizeUtil.SIZETYPE_KB) > 0) {
                    size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
                } else {
                    ToastUtil.showToast(this, "文件不正确，请重新选择...");
                    return;
                }
            } catch (Exception e) {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
            upload(mSize2, 2, mAddAudio, createAlbumArt(audioPath), null, 2, mName2, name, size);
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_AUDIO_BY_TACK_AUDIO) {
            Log.i(TAG_AUDIO, "最终选择的音频是 ：" + data.getData().getPath());
            if (!doAudio_tack(requestCode, data)) {
                return;
            }
            Log.i(TAG_AUDIO, "最终选择的音频是 ：" + audioPath);
            name = UploadUtil_Upload.getFileName(audioPath);
            File f = new File(audioPath);
            try {
                if (FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f),
                        FileSizeUtil.SIZETYPE_KB) > 0) {
                    size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
                } else {
                    ToastUtil.showToast(this, "文件不正确，请重新选择...");
                    return;
                }
            } catch (Exception e) {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
            upload(mSize2, 2, mAddAudio, createAlbumArt(audioPath), null, 2, mName2, name, size);
        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE_BY_PICK_FILE) {
            if ("meizu".equals(MyApplication.getSIGN()))
                filePath = String.valueOf(data.getData());
            else
                filePath = GetFilePath.getPath(this, data.getData());
            if (filePath == null)
                return;
            Log.i(TAG_FILE, "最终选择的文件是 ：" + filePath);
            name = UploadUtil_Upload.getFileName(filePath);
            File f = new File(filePath);
            try {
                size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB);
            } catch (Exception e) {
                ToastUtil.showToast(this, "文件不正确，请重新选择...");
                return;
            }
            upload(mSize2, 2, mAddAudio, null, null, 3, mName2, name, size);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (number == 1) {
            Show_pic(requestCode, resultCode, data, mAddPic, mName1, mSize1);
        } else if (number == 2) {
            Show_audio(requestCode, resultCode, data, mAddAudio, mName2, mSize2);
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

        if (picPath == null && audioPath == null && videoPath == null) {
            String fileKey_file = "file";
            uploadUtil.uploadFile(filePath, fileKey_file, requestURL_File, params, TAG);
            filePath = null;
        } else if (picPath == null && audioPath == null && filePath == null) {
            String fileKey_video = "video";
            uploadUtil.uploadFile(videoPath, fileKey_video, requestURL_VIDEO, params, TAG);
            videoPath = null;
        } else if (picPath == null && videoPath == null && filePath == null) {
            String fileKey_audio = "audio";
            uploadUtil.uploadFile(audioPath, fileKey_audio, requestURL_AUDIO, params, TAG);
            audioPath = null;
        } else if (audioPath == null && videoPath == null && filePath == null) {
            String fileKey_photo = "image";
            uploadUtil.uploadFile(picPath, fileKey_photo, requestURL_PHOTO, params, TAG);
            picPath = null;
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
                //上传完成后的回调操作
                case UPLOAD_FILE_DONE:
                    if (msg.arg1 == 1) {//上传成功
                        String result = msg.obj.toString();
                        if (Integer.parseInt(result.substring(0, 1)) == 1) {
                            uploadResult_1.setText(result.substring(1));
                        } else if (Integer.parseInt(result.substring(0, 1)) == 2) {
                            uploadResult_2.setText(result.substring(1));
                        }
                        Log.i("ABC", result);
                    } else {//上传失败
                        if (msg.obj.toString() != null && !"".equals(msg.obj.toString())) {
                            int tag = -1;
                            try {
                                tag = Integer.parseInt(msg.obj.toString().substring(0, 1));
                            } catch (Exception e) {
                                tag = -1;
                            }
                            if (tag == 1) {//判断位置  置空该位置的文件名、大小的组件
                                mName1.setText("");
                                mSize1.setText("");
                                mAddPic.setImageResource(R.drawable.tianjia);
                            } else if (tag == 2) {
                                mName2.setText("");
                                mSize2.setText("");
                                mAddAudio.setImageResource(R.drawable.tianjia);
                            }
                        }
                        ToastUtil.showToast(UploadTaskActivity.this, "连接超时,上传失败!");
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
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
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG") || picPath.endsWith(".gif") || picPath.endsWith(".GIF") || picPath.endsWith(".jpeg") || picPath.endsWith(".JPEG"))) {
            lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
            setResult(Activity.RESULT_OK, lastIntent);
            return true;
        } else {
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * 录音获取音频
     */
    private void takeAudio() {
        //执行录音前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(intent, SELECT_AUDIO_BY_TACK_AUDIO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从本地文件中获取音频
     */
    private void pickAudio() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, SELECT_AUDIO_BY_PICK_AUDIO);
    }

    /**
     * 从本地选择音频后，获取音频的路径
     *
     * @param requestCode
     * @param data
     */
    private boolean doAudio_pick(int requestCode, Intent data) {
        if (requestCode == SELECT_AUDIO_BY_PICK_AUDIO) {
            if (data == null) {
                ToastUtil.showToast(this, "选择音频文件出错");
                return false;
            }
            audioUri = data.getData();
            if (audioUri == null) {
                ToastUtil.showToast(this, "选择音频文件出错");
                return false;
            }
        }
        audioPath = GetFilePath.getPath(this, data.getData());
        Log.i(TAG3, "audioPath = " + audioPath);
        if (audioPath != null && (audioPath.endsWith(".mp3") || audioPath.endsWith(".amr") ||
                audioPath.endsWith(".flac") || audioPath.endsWith(".ape") || audioPath.endsWith(".aac") ||
                audioPath.endsWith(".MP3") || audioPath.endsWith(".AMR") ||
                audioPath.endsWith(".FLAC") || audioPath.endsWith(".APE") || audioPath.endsWith(".AAC"))) {
            lastIntent.putExtra(KEY_AUDIO_PATH, audioPath);
            setResult(Activity.RESULT_OK, lastIntent);
            return true;
        } else {
            ToastUtil.showToast(this, "选择音频文件不正确");
            return false;
        }
    }

    /**
     * 录音机录制音频后，获取音频的路径
     *
     * @param requestCode
     * @param data
     */
    private boolean doAudio_tack(int requestCode, Intent data) {
        Log.i("AUDIO", "audioPath = " + data.getData().toString());
        if (requestCode == SELECT_AUDIO_BY_TACK_AUDIO) {
            if (data == null) {
                ToastUtil.showToast(this, "选择音频文件出错");
                return false;
            }
            audioUri = Uri.parse(Uri.decode(data.getData().getPath()));
            if (audioUri == null) {
                ToastUtil.showToast(this, "选择音频文件出错");
                return false;
            }
        }
        audioPath = getAudioFilePath(data);
        Log.i(TAG3, "audioPath = " + audioPath);
        if (audioPath != null && (audioPath.endsWith(".mp3") || audioPath.endsWith(".amr") ||
                audioPath.endsWith(".flac") || audioPath.endsWith(".ape") || audioPath.endsWith(".aac") ||
                audioPath.endsWith(".MP3") || audioPath.endsWith(".AMR") ||
                audioPath.endsWith(".FLAC") || audioPath.endsWith(".APE") || audioPath.endsWith(".AAC")
                || audioPath.endsWith(".m4a")|| audioPath.endsWith(".M4A"))) {
            lastIntent.putExtra(KEY_AUDIO_PATH, audioPath);
            setResult(Activity.RESULT_OK, lastIntent);
            return true;
        } else {
            ToastUtil.showToast(this, "选择音频文件不正确");
            return false;
        }
    }

    /**
     * 录制音频后获取音频路径的方法
     *
     * @param data
     * @return
     */
    private String getAudioFilePath(Intent data) {
        Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        return cursor.getString(index);
    }

    /**
     * 魅族手机本地获取视频
     */
    public void pickVideo_meizu() {
        Intent intent = new Intent(UploadTaskActivity.this, AllVideoActivity.class);
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

//      {act=android.intent.action.VIEW dat=file:///storage/emulated/0/DCIM/Video/V60809-114910.mp4 typ=video/ext-mp4
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
                ToastUtil.showToast(this, "选择视频文件出错");
                return false;
            }
            videoUri = data.getData();
            if (videoUri == null) {
                ToastUtil.showToast(this, "选择视频文件出错");
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
            ToastUtil.showToast(this, "选择视频文件格式不正确");
            return false;
        }
    }

    /**
     * 魅族手机本地获取文件
     */
    public void pickFile_meizu() {
        Intent intent = new Intent(UploadTaskActivity.this, AllFileActivity.class);
        startActivityForResult(intent, SELECT_FILE_BY_PICK_FILE);
    }

    /***
     * 从本地获取文件
     */
    private void pickFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_FILE_BY_PICK_FILE);
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


