package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.FileSizeUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.utils.UploadUtil_Upload;
import com.yitong.ChuangKeYuan.widget.CustomDialog_upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/9/19.
 * 上传家长
 *
 */
public class UploadParentsActivity extends Activity implements View.OnClickListener, UploadUtil_Upload.OnUploadProcessListener {
    private ImageView mBack, mSearch, mAddPic, iv1;
    private TextView mTitle,upload_title, upload_des1, upload_btn, uploadResult_1;
    private EditText pname, motto, baby_name, age, job, describle;
    private LinearLayout ll1;
    private static final String TAG_PHOTO = "uploadImage";
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
    private Button upload;
    private ProgressDialog progressDialog;
    //获取到的图片路径
    private String picPath;
    private Intent lastIntent;
    private Uri photoUri;
    private String photo = "";
    //使用相册中的图片
    public static final int SELECT_PIC_BY_PICK_PHOTO = 5;
    //从Intent获取图片路径的KEY
    public static final String KEY_PHOTO_PATH = "photo_path";
    private static final String TAG1 = "SelectPic";
    private Spinner mSpinner;
    private Context context;
    private ArrayAdapter<String> adapter1;
    private String classname;
    private String[] babyclass = new String[]{"玫瑰班", "百合班", "蔷薇班", "茉莉班", "绿萝班", "紫藤班", "银杏班", "梧桐班", "豆豆班", "苗苗班", "绿绿班", "芽芽班"};
    //修剪后的返回
    private int REQUESTCODE_CUTTING = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parents);
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
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        mTitle.setText("上传家长信息");
        mSearch.setVisibility(View.GONE);
        mBack.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        pname = (EditText) findViewById(R.id.parentsname);
        motto = (EditText) findViewById(R.id.geyan);
        baby_name = (EditText) findViewById(R.id.babyname);
        age = (EditText) findViewById(R.id.parentsage);
        job = (EditText) findViewById(R.id.job);
        describle = (EditText) findViewById(R.id.parentsdescribe);
        mAddPic = (ImageView) findViewById(R.id.add_pic);
        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(this);
        mAddPic.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        lastIntent = getIntent();

        context = this;
        adapter1 = new ArrayAdapter<String>(this,  R.layout.spinner_custom_item, R.id.spinner_textView, babyclass);
        mSpinner = (Spinner) findViewById(R.id.spinner);

        mSpinner.setAdapter(adapter1);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classname = babyclass[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                classname = babyclass[0];
            }
        });

    }


    CustomDialog_upload dialog;

    /**
     * 初始化Dialog
     */
    public void getDialog() {
        dialog = new CustomDialog_upload(this, R.style.customDialog, R.layout.dialog_parents_layout);
        dialog.show();
        upload_title = (TextView) dialog.findViewById(R.id.upload_title);
        upload_des1 = (TextView) dialog.findViewById(R.id.upload_des1);
        upload_btn = (TextView) dialog.findViewById(R.id.upload_btn);
        iv1 = (ImageView) dialog.findViewById(R.id.iv1);
        ll1 = (LinearLayout) dialog.findViewById(R.id.ll1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                finish();
                break;
            case R.id.add_pic:
                getDialog();
                upload_title.setText("请选择要上传的照片");
                iv1.setImageResource(R.drawable.dialog_pickphoto);
                upload_des1.setText("从本地相册选择");
                ll1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickPhoto();
                        dialog.dismiss();
                    }
                });
                upload_btn.setText("取消");
                upload_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.upload:
                final String parentname = pname.getText().toString();
                final String parentmotto = motto.getText().toString();
                final String babyname = baby_name.getText().toString();
                final String parentage = age.getText().toString();
                final String parentiob = job.getText().toString();
                final String parentdescrible = describle.getText().toString();

                if (parentname != null && !"".equals(parentname)) {
                    progressDialog = new ProgressDialog(UploadParentsActivity.this);
                    progressDialog.setMessage("正在上传，请稍后...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    final Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            ReturnResult result = (ReturnResult) msg.obj;

                            if (Integer.parseInt(result.getCode()) == 0) {
                                ToastUtil.showToast(UploadParentsActivity.this, "上传成功");
                                MyApplication.setFLAG(1010);
                                finish();
                            } else {
                                ToastUtil.showToast(UploadParentsActivity.this, "对不起，上传失败");
                            }
                            progressDialog.dismiss();
                            super.handleMessage(msg);
                        }
                    };


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FormBody body = new FormBody.Builder()
                                        .add("username", MyApplication.getusername())
                                        .add("password", MyApplication.getuserpassword())
                                        .add("name", parentname)
                                        .add("signature", parentmotto)
                                        .add("profession", parentiob)
                                        .add("babyName", babyname)
                                        .add("className", classname)
                                        .add("desc", parentdescrible)
                                        .add("thumb", photo)
                                        .add("age", parentage).build();
                                String data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_Addparents, body);

                                Log.i("ABC", data);
                                Gson gson = new Gson();
                                ReturnResult result = gson.fromJson(data, ReturnResult.class);


                                Message message = mHandler.obtainMessage();
                                message.obj = result;
                                mHandler.sendMessage(message);

                            } catch (Exception e) {
                                System.out.println("ABC"+e.toString());
                                progressDialog.dismiss();
                                return;
                            }
                        }
                    }).start();
                } else {
                    ToastUtil.showToast(UploadParentsActivity.this, "请完善内容");
                }
                break;
        }

    }

    /**
     * 上传图片，
     */
    private void upload(ImageView view1, Bitmap bitmap, String name, Double size) {
        Double d = size;
        Double maxSize = Double.valueOf(20 * 1024);//限制上传文件大小在50M以内
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") ||
                picPath.endsWith(".jpg") || picPath.endsWith(".JPG") || picPath.endsWith(".gif") ||
                picPath.endsWith(".GIF") || picPath.endsWith(".jpeg") || picPath.endsWith(".JPEG"))) {
            maxSize = Double.valueOf(20 * 1024);
        }
        if (picPath != null) {
            if (maxSize >= d) {
                if (d >= 1) {
                    if (bitmap != null) {
                        view1.setImageBitmap(bitmap);
                    } else {
                        view1.setImageResource(R.drawable.add_audio);
                    }
                    toUploadFile(1);
                } else {
                    view1.setImageResource(R.drawable.tianjia);
                    ToastUtil.showToast(UploadParentsActivity.this, "对不起，您选择的文件太小，或者路径异常，请重新选择！");
                }
            } else {
                view1.setImageResource(R.drawable.tianjia);
                ToastUtil.showToast(UploadParentsActivity.this, "对不起，您选择的文件过大！请重新选择..");
            }
        } else {
            ToastUtil.showToast(this, "请先选择您要上传的文件");
        }
    }

    /**
     * 上传照片
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

    private void Show_pic(int requestCode, int resultCode, Intent data, ImageView mAddPic) {

//        if (resultCode == Activity.RESULT_OK && requestCode == REQUESTCODE_CUTTING) {
            if (data != null) {
                if (!doPhoto(requestCode, data)) {
                    return;
                }
                Log.i(TAG_PHOTO, "最终选择的图片是 ：" + picPath);
                BitmapFactory.Options myoptions = new BitmapFactory.Options();
                myoptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picPath, myoptions);
                myoptions.inJustDecodeBounds = false;
                myoptions.inSampleSize = 1;
                myoptions.inPurgeable = true;
                myoptions.inInputShareable = true;
                myoptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bm = BitmapFactory.decodeFile(picPath, myoptions);
                picPath = saveCroppedImage(bm);
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
                upload( mAddPic, softReference.get(), name, size);
            } else {
                return;
            }
        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
//            startPhotoZoom(data.getData());
//        } else {
            Show_pic(requestCode, resultCode, data, mAddPic);
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", true);
//        // aspectX aspectY 是宽高的比例
        intent.putExtra("inputX", 1.3);
        intent.putExtra("inputY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 230);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
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

        if (picPath != null) {
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
                case UPLOAD_FILE_DONE://上传完成后的回调操作
                    if (msg.arg1 == 1) {
                        String result = msg.obj.toString();
                        if (Integer.parseInt(result.substring(0, 1)) == 1) {

                            photo = result.substring(1);
                            Log.i("ABC", photo);
                        }
                    } else {
                        if (msg.obj.toString() != null && !"".equals(msg.obj.toString())) {
                            int tag = -1;
                            try {
                                tag = Integer.parseInt(msg.obj.toString().substring(0, 1));
                            } catch (Exception e) {
                                tag = -1;
                            }
                            if (tag == 1) {
                                mAddPic.setImageResource(R.drawable.tianjia);
                            }
                        }
                        ToastUtil.showToast(UploadParentsActivity.this, "连接超时,上传失败!");
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
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return false;
            }
//        Bundle extras = data.getExtras();
//        // 取得SDCard图片路径做显示
//        Bitmap photo = extras.getParcelable("data");
//        picPath =saveCroppedImage(photo);
        photoUri = data.getData();
        if (photoUri == null) {
            Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
            return false;
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
     * 保存bitmap
     *
     * @param bmp
     */
    private String saveCroppedImage(Bitmap bmp) {
        File file = new File("/sdcard/myFolder");
        if (!file.exists())
            file.mkdir();

        file = new File("/sdcard/temp.jpg".trim());
        String fileName = file.getName();
        String mName = fileName.substring(0, fileName.lastIndexOf("."));
        String sName = fileName.substring(fileName.lastIndexOf("."));

        // /sdcard/myFolder/temp_cropped.jpg
        String newFilePath = "/sdcard/myFolder" + "/" + mName + "_cropped" + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newFilePath;
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
