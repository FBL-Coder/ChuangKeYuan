package com.yitong.ChuangKeYuan.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc.myricheditor.RichEditor;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ArticleDetail;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.FileSizeUtil;
import com.yitong.ChuangKeYuan.utils.FileUtils;
import com.yitong.ChuangKeYuan.utils.GetFilePath;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.utils.UploadUtil_Upload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

/**
 * 添加文章评论
 * <p/>
 * 详细注释请看UploadActivity.java
 */
@SuppressLint("SimpleDateFormat")
public class AddCommentActivity extends Activity implements OnClickListener, UploadUtil_Upload.OnUploadProcessListener {
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 200;
    //使用本地文件中的视频
    public static final int SELECT_VIDEO_BY_PICK_VIDEO = 8;
    //使用本地文件中的音频
    public static final int SELECT_AUDIO_BY_PICK_AUDIO = 10;
    //使用本地中的文件
    public static final int SELECT_FILE_BY_PICK_FILE = 11;

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
    private static String requestURL_File = requestURL + "/Base/uploadFile/";


    private final String TAG = "UploadActivity";
    private Context context;
    private EditText et_name;
    private TextView tv_title;
    private ProgressDialog progressDialog;
    private ImageView iv_back, iv_ok, img_video, img_audio, img_file, img_other, img_close;
    private File mCameraImageFile;// 照相机拍照得到的图片
    private FileUtils mFileUtils;
    private LinearLayout mFen, ll_detail_data, ku_task_yichang, comment_ll_1,
            comment_ll_2, comment_ll_3, comment_ll_4, comment_ll_5;
    private RichEditor mEditor;
    private RatingBar mRatingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comment_activity);
        context = this;
        add_ric();
        init();

    }

    private int Font = 1;

    private void add_ric() {
        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorFontSize(16);
        mEditor.setEditorHeight(this.getWindowManager().getDefaultDisplay().getHeight() - 100);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setEditorWidth(this.getWindowManager().getDefaultDisplay().getWidth());
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("     请输入评论");
        /*mPreview = (TextView) findViewById(R.id.preview);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {
                mPreview.setText(text);
            }
        });*/

        findViewById(R.id.action_bold).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();

            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        /*findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });*/

        findViewById(R.id.action_strikethrough).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Font == 1)
                    return;
                switch (Font % 6) {
                    case 0:
                        mEditor.setHeading(2);
                        Font--;
                        break;
                    case 5:
                        mEditor.setHeading(3);
                        Font--;
                        break;
                    case 4:
                        mEditor.setHeading(4);
                        Font--;
                        break;
                    case 3:
                        mEditor.setHeading(5);
                        Font--;
                        break;
                    case 2:
                        mEditor.setHeading(6);
                        Font--;
                        break;
                }
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Font == 6)
                    return;
                switch (Font % 6) {
                    case 0:
                        mEditor.setHeading(6);
                        Font++;
                        break;
                    case 1:
                        mEditor.setHeading(5);
                        Font++;
                        break;
                    case 2:
                        mEditor.setHeading(4);
                        Font++;
                        break;
                    case 3:
                        mEditor.setHeading(3);
                        Font++;
                        break;
                    case 4:
                        mEditor.setHeading(2);
                        Font++;
                        break;
                    case 5:
                        mEditor.setHeading(1);
                        Font++;
                        break;
                }
            }
        });

        /* findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });
        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });*/

        /**
         * 字体颜色
         */
        final TextView text_color = (TextView) findViewById(R.id.action_txt_color);
        text_color.setOnClickListener(new OnClickListener() {
            private int Color_INT = 1;

            @Override
            public void onClick(View v) {

                switch (Color_INT % 8) {
                    case 0:
                        mEditor.setTextColor(Color.DKGRAY);
                        text_color.setTextColor(Color.DKGRAY);
                        Color_INT++;
                        break;
                    case 1:
                        mEditor.setTextColor(Color.BLACK);
                        text_color.setTextColor(Color.BLACK);
                        Color_INT++;
                        break;
                    case 2:
                        mEditor.setTextColor(Color.RED);
                        text_color.setTextColor(Color.RED);
                        Color_INT++;
                        break;
                    case 3:
                        mEditor.setTextColor(Color.GREEN);
                        text_color.setTextColor(Color.GREEN);
                        Color_INT++;
                        break;
                    case 4:
                        mEditor.setTextColor(Color.YELLOW);
                        text_color.setTextColor(Color.YELLOW);
                        Color_INT++;
                        break;
                    case 5:
                        mEditor.setTextColor(Color.BLUE);
                        text_color.setTextColor(Color.BLUE);
                        Color_INT++;
                        break;
                    case 6:
                        mEditor.setTextColor(Color.MAGENTA);
                        text_color.setTextColor(Color.MAGENTA);
                        Color_INT++;
                        break;
                    case 7:
                        mEditor.setTextColor(Color.CYAN);
                        text_color.setTextColor(Color.CYAN);
                        Color_INT++;
                        break;
                }
            }
        });

       /* findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private int Color_INT = 1;

            @Override
            public void onClick(View v) {

                switch (Color_INT % 8) {
                    case 0:
                        mEditor.setBackgroundColor(Color.WHITE);
                        Color_INT++;
                        break;
                    case 1:
                        mEditor.setBackgroundColor(Color.BLACK);
                        Color_INT++;
                        break;
                    case 2:
                        mEditor.setBackgroundColor(Color.RED);
                        Color_INT++;
                        break;
                    case 3:
                        mEditor.setBackgroundColor(Color.GREEN);
                        Color_INT++;
                        break;
                    case 4:
                        mEditor.setBackgroundColor(Color.YELLOW);
                        Color_INT++;
                        break;
                    case 5:
                        mEditor.setBackgroundColor(Color.BLUE);
                        Color_INT++;
                        break;
                    case 6:
                        mEditor.setBackgroundColor(Color.MAGENTA);
                        Color_INT++;
                        break;
                    case 7:
                        mEditor.setBackgroundColor(Color.CYAN);
                        Color_INT++;
                        break;
                }
            }
        });*/

        findViewById(R.id.action_indent).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        /*findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });*/

       /* findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        })*/
        ;

        findViewById(R.id.action_insert_image).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开系统相册
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });

        findViewById(R.id.phone_camera).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开相机
                openCamera();
            }
        });

        /*findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });*/
    }

    private void init() {

        mFileUtils = new FileUtils(context);

        lastIntent = getIntent();
        progressDialog = new ProgressDialog(this);

        iv_back = (ImageView) findViewById(R.id.iv_teach_back);
        iv_back.setOnClickListener(this);
        iv_ok = (ImageView) findViewById(R.id.iv_teach_search);
        iv_ok.setImageResource(R.drawable.up_upload);
        iv_ok.setOnClickListener(this);
        img_video = (ImageView) findViewById(R.id.img_video);
        img_video.setOnClickListener(this);

        img_audio = (ImageView) findViewById(R.id.img_audio);
        img_audio.setOnClickListener(this);

        img_file = (ImageView) findViewById(R.id.img_file);
        img_file.setOnClickListener(this);

        tv_title = (TextView) findViewById(R.id.tv_teach_title);
        et_name = (EditText) findViewById(R.id.et_name);
        tv_title.setText("文章评论");

        comment_ll_1 = (LinearLayout) findViewById(R.id.comment_ll_1);
        comment_ll_2 = (LinearLayout) findViewById(R.id.comment_ll_2);
        comment_ll_3 = (LinearLayout) findViewById(R.id.comment_ll_3);
        comment_ll_4 = (LinearLayout) findViewById(R.id.comment_ll_4);
        comment_ll_5 = (LinearLayout) findViewById(R.id.comment_ll_5);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);

        comment_ll_1.setOnClickListener(this);
        comment_ll_2.setOnClickListener(this);
        comment_ll_3.setOnClickListener(this);
        comment_ll_4.setOnClickListener(this);
        comment_ll_5.setOnClickListener(this);
    }


    private void openCamera() {
        try {
            File PHOTO_DIR = new File(mFileUtils.getStorageDirectory());
            if (!PHOTO_DIR.exists())
                PHOTO_DIR.mkdirs();// 创建照片的存储目录

            mCameraImageFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCameraImageFile);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
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

    /**
     * 上传后的回调
     */
    @SuppressLint("HandlerLeak")
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
                        if (loact == 0) {
                            mEditor.insertImage(url, "图片");
                        } else if (loact == 1) {
                            mEditor.insertVideo(DatasUtil.URL_video_phone, url, "视频");
                        } else if (loact == 2) {
                            mEditor.insertAudio(DatasUtil.URL_audio_phone, url, "音乐");
                        } else if (loact == 3) {
                            String url_pdf = url.substring(url.lastIndexOf("/") + 1);
                            url_pdf = url_pdf.substring(0, url_pdf.lastIndexOf("."));
                            url_pdf = "/data/upload/pdf/" + url_pdf + ".pdf";
                            Log.e("URL", "PDF地址:" + url_pdf + "----------------附件下载地址 :" + url);
                            mEditor.insertFile(url_pdf, url, "文件");
                        }
                    } else {//上传失败
                        ToastUtil.showToast(AddCommentActivity.this, "连接超时,上传失败!");
                    }
                    Log.i("ABC", "上传返回值:" + msg.arg1);
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

        if (picPath == null && audioPath == null && filePath == null) {
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
        } else if (picPath == null && audioPath == null && videoPath == null) {
            String fileKey_file = "file";
            uploadUtil.uploadFile(filePath, fileKey_file, requestURL_File, params, TAG);
            filePath = null;
        }
    }

    //获取到的图片路径
    private String picPath;
    private Uri photoUri;
    //获取到的视频路径
    private String videoPath;
    private Uri videoUri;
    //获取到的音频路径
    private String audioPath;
    private Uri audioUri;
    //获取到的文件路径
    private String filePath;
    private Intent lastIntent;
    private String size;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {//图片
            Uri uri = data.getData();
            picPath = GetFilePath.getPath(this, data.getData());
            if (picPath == null)
                return;
            upload(size, null, BitmapFactory.decodeFile(picPath), 0);
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {//拍照
            picPath = mCameraImageFile.getAbsolutePath();
            if (picPath == null)
                return;
            upload(size, null, BitmapFactory.decodeFile(picPath), 0);
        } else if (requestCode == SELECT_VIDEO_BY_PICK_VIDEO) {//视频
            if (!doVideo(requestCode, data)) {
                return;
            }
            try {
                size = (String.valueOf(FileSizeUtil.getFileOrFilesSize(videoPath, 2)));
            } catch (Exception e) {
                size = "6000";
            }
            upload(size, img_video, getVideoThumbnail(videoPath), 1);

        } else if (requestCode == SELECT_AUDIO_BY_PICK_AUDIO) {//音频
            if (!doAudio_pick(requestCode, data)) {
                return;
            }
            File f = new File(audioPath);
            try {
                size = String.valueOf(FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f), FileSizeUtil.SIZETYPE_KB));
            } catch (Exception e) {
                size = "6000";
            }
            upload(size, img_audio, createAlbumArt(audioPath), 2);
        } else if (requestCode == SELECT_FILE_BY_PICK_FILE) {//文件
            if ("meizu".equals(MyApplication.getSIGN()))
                filePath = String.valueOf(data.getData());
            else
                filePath = GetFilePath.getPath(this, data.getData());
            if (filePath.isEmpty())
                return;
            if (filePath.endsWith("txt") || filePath.endsWith("doc") || filePath.endsWith("docx") ||
                    filePath.endsWith("wps") || filePath.endsWith("wpt") ||
                    filePath.endsWith("ppt") || filePath.endsWith("pptx") ||
                    filePath.endsWith("xls") || filePath.endsWith("xlsx") ||
                    filePath.endsWith("TXT") || filePath.endsWith("DOC") || filePath.endsWith("DOCX") ||
                    filePath.endsWith("WPS") || filePath.endsWith("WPT") ||
                    filePath.endsWith("PPT") || filePath.endsWith("PPTX") ||
                    filePath.endsWith("XLS") || filePath.endsWith("XLSX") || filePath.endsWith("pdf") || filePath.endsWith("PDF")) {
            } else {
                ToastUtil.showToast(AddCommentActivity.this, "选择文件类型不正确！");
                return;
            }
            File file = new File(filePath);
            try {
                size = String.valueOf(FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(file), FileSizeUtil.SIZETYPE_KB));
            } catch (Exception e) {
                Log.i("Exception", e + "");
                return;
            }
            upload(size, img_file, null, 3);
        }
    }

    /**
     * 上传图片，音频，视频和文件的方法
     */
    private void upload(String view, ImageView view1, Bitmap bitmap, int tag) {
        if (tag == 0) {
            Message message = Message.obtain();
            message.arg2 = tag;
            message.what = TO_UPLOAD_FILE;
            handler.sendMessage(message);
            return;
        }
        Double d = Double.valueOf(view);
        Double maxSize = Double.valueOf(50 * 1024);//限制上传附件大小在50M之内

        if (view1.getTag() != null && Integer.parseInt(view1.getTag().toString()) == 0) {
            maxSize = Double.valueOf(50 * 1024);
        }
        if (videoPath != null || audioPath != null || filePath != null) {
            if (maxSize >= d) {
                if (d >= 1) {
                    if (d > 10) {
                        ToastUtil.showToast(AddCommentActivity.this, "文件较大，上传较慢！");
                    }
                    Message message = Message.obtain();
                    message.arg2 = tag;
                    message.what = TO_UPLOAD_FILE;
                    handler.sendMessage(message);
                } else {
                    ToastUtil.showToast(AddCommentActivity.this, "对不起，您选择的文件太小，或者路径异常，请重新选择！");
                }
            } else {
                ToastUtil.showToast(AddCommentActivity.this, "对不起，您选择的文件过大！请重新选择..");
            }
        } else {
            ToastUtil.showToast(this, "请先选择您要上传的文件");
        }
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
                Toast.makeText(this, "选择音频文件出错", Toast.LENGTH_LONG).show();
                return false;
            }
            audioUri = data.getData();
            if (audioUri == null) {
                Toast.makeText(this, "选择音频文件出错", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        audioPath = GetFilePath.getPath(this, data.getData());
        if (audioPath != null && (audioPath.endsWith(".mp3") || audioPath.endsWith(".amr") || audioPath.endsWith(".flac")
                || audioPath.endsWith(".ape") || audioPath.endsWith(".aac") ||
                audioPath.endsWith(".MP3") || audioPath.endsWith(".AMR") || audioPath.endsWith(".FLAC")
                || audioPath.endsWith(".APE") || audioPath.endsWith(".AAC"))) {
            lastIntent.putExtra("audio", audioPath);
            setResult(Activity.RESULT_OK, lastIntent);
            return true;
        } else {
            return false;
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
        videoPath = GetFilePath.getPath(this, data.getData());
        if (videoPath != null && (videoPath.endsWith(".mp4") || videoPath.endsWith(".3gp") ||
                videoPath.endsWith(".wav") || videoPath.endsWith(".rm") || videoPath.endsWith(".rmvb") ||
                videoPath.endsWith(".MP4") || videoPath.endsWith(".3GP") ||
                videoPath.endsWith(".WMV") || videoPath.endsWith(".wmv") ||
                videoPath.endsWith(".WAV") || videoPath.endsWith(".RM") || videoPath.endsWith(".RMVB"))) {
            lastIntent.putExtra("video", videoPath);
            setResult(Activity.RESULT_OK, lastIntent);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEditor.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEditor.onPause();
    }

    @Override
    protected void onDestroy() {
        mEditor.loadUrl("about:blank");
        mFileUtils.deleteRichTextImage();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_search:
                progressDialog = new ProgressDialog(AddCommentActivity.this);
                progressDialog.setMessage("正在保存，请稍后...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                @SuppressLint("HandlerLeak") Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog.dismiss();
                        if (msg.what == 2) {
                            finish();
                        }
                        super.handleMessage(msg);
                    }
                };
                String content = mEditor.getHtml();
                UpData(content, mHandler);
                progressDialog.show();

                break;
            case R.id.iv_teach_back:
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                finish();
                break;
            case R.id.img_video:
                // 打开视频
                pickVideo();
                break;
            case R.id.img_audio:
                // 打开录音
                pickAudio();
                break;
            case R.id.img_file:
                // 打开文件
                if ("meizu".equals(MyApplication.getSIGN())) {
                    pickFile_meizu();
                } else {
                    pickFile();
                }
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
        }
    }

    /**
     * 魅族手机本地获取文件
     */
    public void pickFile_meizu() {
        Intent intent = new Intent(AddCommentActivity.this, AllFileActivity.class);
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

    /***
     * 从本地取视频
     */
    private void pickVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, SELECT_VIDEO_BY_PICK_VIDEO);
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
     * 获取视频缩略图的方法
     */
    public Bitmap getVideoThumbnail(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(videoPath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
        } catch (RuntimeException e) {
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
                return bitmap;
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
                return null;
            }
        }
    }

    /**
     * 最后的保存
     *
     * @param content
     * @param mHandler
     */
    public void UpData(final String content, final Handler mHandler) {
        int numrating = (int) mRatingBar.getRating();
        final String numstar = String.valueOf(numrating);
        Log.i("JSON", content);

        new Thread() {
            @Override
            public void run() {
                String data = "";
                FormBody body = new FormBody.Builder()
                        .add("username", MyApplication.getusername())
                        .add("password", MyApplication.getuserpassword())
                        .add("aid", getIntent().getStringExtra("aid"))
                        .add("starval", numstar)
                        .add("content", content)
                        .build();
                Log.i("HTTP", content);
                Message message = mHandler.obtainMessage();
                Looper.prepare();
                if (body != null) {
                    try {
                        data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_AddComment
                                , body);
                        Gson gson = new Gson();
                        ReturnResult returnResult = gson.fromJson(data, ReturnResult.class);
                        Log.i("FIND", data);

                        if (Integer.parseInt(returnResult.getCode()) != 0) {
                            mHandler.sendMessage(message);
                            ToastUtil.showToast(AddCommentActivity.this, "保存失败，请稍后再试...");
                        } else {
                            MyApplication.setFLAG(1001);
                            ToastUtil.showToast(AddCommentActivity.this, "保存成功");
                            message.what = 2;
                            mHandler.sendMessage(message);
                        }

                    } catch (Exception e) {
                        mHandler.sendMessage(message);
                        ToastUtil.showToast(AddCommentActivity.this, "保存失败，请稍后再试...");
                        return;
                    }
                } else {
                    mHandler.sendMessage(message);
                    ToastUtil.showToast(AddCommentActivity.this, "保存失败，请稍后再试...");
                }
                Looper.loop();
            }
        }.start();
    }
}
