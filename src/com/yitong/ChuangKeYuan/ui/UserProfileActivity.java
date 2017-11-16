package com.yitong.ChuangKeYuan.ui;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.utils.EaseUserUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.Helper.MyHelper;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import okhttp3.FormBody;

public class UserProfileActivity extends BaseActivity implements OnClickListener {

    private static final int REQUESTCODE_PICK = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private int QR_WIDTH = 200, QR_HEIGHT = 200;
    private ImageView headAvatar;
    private ImageView headPhotoUpdate;
    //    private ImageView iconRightArrow;
    private ImageView myerweima;
    private Bitmap bitmap;

    private TextView tvNickName;
    private TextView tvUsername;
    private ProgressDialog dialog;
    private RelativeLayout rlNickName,revisepass;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_user_profile);
        initView();
        initListener();
        initErweima();
    }

    private void initErweima() {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();

            String text = tvUsername.getText().toString();

            if (text == null || "".equals(text) || text.length() < 1) {
                return;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }

            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

            myerweima.setImageBitmap(bitmap);
            try {
                saveMyBitmap(bitmap, "code");
            } catch (Exception e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            }
        } catch (WriterException e) {
//            e.printStackTrace();
        }
    }

    public void saveMyBitmap(Bitmap bm, String bitName) throws IOException {
        File f = new File("/mnt/sdcard/" + bitName + ".png");
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);

            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();

            fOut.close();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void initView() {
        headAvatar = (ImageView) findViewById(R.id.user_head_avatar);
        headPhotoUpdate = (ImageView) findViewById(R.id.user_head_headphoto_update);
        tvUsername = (TextView) findViewById(R.id.user_username);
        tvNickName = (TextView) findViewById(R.id.user_nickname);
        rlNickName = (RelativeLayout) findViewById(R.id.rl_nickname);
        revisepass = (RelativeLayout) findViewById(R.id.revisepass);
//        iconRightArrow = (ImageView) findViewById(R.id.ic_right_arrow);
        myerweima = (ImageView) findViewById(R.id.myerweima);
        headAvatar.setImageURI(Uri.parse(MyApplication.getHeadImg()));
        Log.i("HEAD", MyApplication.getHeadImg());
        revisepass.setOnClickListener(this);

    }

    private void initListener() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Log.i("USERNAME", username);
        boolean enableUpdate = intent.getBooleanExtra("setting", false);
        if (enableUpdate) {
            headPhotoUpdate.setVisibility(View.VISIBLE);
//            iconRightArrow.setVisibility(View.VISIBLE);
//            rlNickName.setOnClickListener(this);
            headAvatar.setOnClickListener(this);
        } else {
            headPhotoUpdate.setVisibility(View.GONE);
//            iconRightArrow.setVisibility(View.INVISIBLE);
        }

        if (username != null) {
            if (username.equals(EMChatManager.getInstance().getCurrentUser())) {
                tvUsername.setText(EMChatManager.getInstance().getCurrentUser());
                EaseUserUtils.setUserAvatarAddUserNick(this, username, headAvatar, tvNickName);
//                headAvatar.setImageURI(Uri.parse(MyApplication.getHeadImg()));
            } else {
                tvUsername.setText(username);
                EaseUserUtils.setUserAvatarAddUserNick(this, username, headAvatar, tvNickName);
//                headAvatar.setImageURI(Uri.parse(MyApplication.getHeadImg()));
//                asyncFetchUserInfo(username);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_head_avatar:
                uploadHeadPhoto();
                break;
            case R.id.revisepass:
//                final EditText editText = new EditText(this);
//                new AlertDialog.Builder(this).setTitle(R.string.setting_nickname).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
//                        .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String nickString = editText.getText().toString();
//                                if (TextUtils.isEmpty(nickString)) {
//                                    Toast.makeText(UserProfileActivity.this, getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                updateRemoteNick(nickString);
//                            }
//                        }).setNegativeButton(R.string.dl_cancel, null).show();

                startActivity(new Intent(UserProfileActivity.this,RevisePass.class));
                break;
            default:
                break;
        }

    }

    public void asyncFetchUserInfo(String username) {
        MyHelper.getInstance().getUserProfileManager().asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {

            @Override
            public void onSuccess(EaseUser user) {
                if (user != null) {
                    MyHelper.getInstance().saveContact(user);
                    if (isFinishing()) {
                        return;
                    }
//                    tvNickName.setText(user.getNick());
                    if (!TextUtils.isEmpty(user.getAvatar())) {
//                        headAvatar.setImageURI(Uri.parse(MyApplication.getHeadImg()));
                        Log.i("USERNAME", user.getAvatar());

                        Glide.with(UserProfileActivity.this).load(user.getAvatar()).placeholder(R.drawable.em_default_avatar).into(headAvatar);
                    } else {
//                        headAvatar.setImageURI(Uri.parse(MyApplication.getHeadImg()));
                        Glide.with(UserProfileActivity.this).load(R.drawable.em_default_avatar).into(headAvatar);
                    }
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
            }
        });
    }


    private void uploadHeadPhoto() {
        Builder builder = new Builder(this);
        builder.setTitle(R.string.dl_title_upload_photo);
        builder.setItems(new String[]{getString(R.string.dl_msg_local_upload)},
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    }
                }
        );
        builder.create().

                show();

    }


    private void updateRemoteNick(final String nickName) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_nick), getString(R.string.dl_waiting));
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean updatenick = MyHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(nickName);
                if (UserProfileActivity.this.isFinishing()) {
                    return;
                }
                if (!updatenick) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatenick_fail), Toast.LENGTH_SHORT)
                                    .show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatenick_success), Toast.LENGTH_SHORT)
                                    .show();
                            tvNickName.setText(nickName);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:
                if (data == null || data.getData() == null) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * save the picture data
     *
     * @param picdata
     */
    String pho;

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            headAvatar.setImageDrawable(drawable);
            uploadUserAvatar(Bitmap2Bytes(photo));
            pho = BitmapBase64(photo);
        }

    }

    private void uploadUserAvatar(final byte[] data) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_photo), getString(R.string.dl_waiting));
        new Thread(new Runnable() {

            @Override
            public void run() {
                final String avatarUrl = MyHelper.getInstance().getUserProfileManager().uploadUserAvatar(data);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        FormBody body = new FormBody.Builder()
                                .add("username", MyApplication.getusername())
                                .add("password", MyApplication.getuserpassword())
                                .add("data", pho).build();
                        try {
                            String code = HttpDataUtil.getPublicData_POST(
                                    DatasUtil.URL_BASE + DatasUtil.URL_upAvatar, body);
                            Log.i("HEAD", pho);
                            Log.i("HEAD", code);
                        } catch (IOException e) {
//                            e.printStackTrace();
                        }
                    }
                }).start();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (avatarUrl != null) {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_success),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }).start();

        dialog.show();
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public String BitmapBase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
