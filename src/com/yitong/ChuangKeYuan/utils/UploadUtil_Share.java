package com.yitong.ChuangKeYuan.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.domain.PublicBean;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Say GoBay on 2016/7/12.
 *
 * 上传工具类 发现发布
 */
public class UploadUtil_Share {
    private static UploadUtil_Share uploadUtil;
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

    private UploadUtil_Share() {
    }

    /**
     * 单例模式获取上传工具类
     *
     * @return
     */
    public static UploadUtil_Share getInstance() {
        if (null == uploadUtil) {
            uploadUtil = new UploadUtil_Share();
        }
        return uploadUtil;
    }

    private static final String TAG = "SHARE";
    private int readTimeOut = 10 * 1000; // 读取超时
    private int connectTimeout = 15 * 1000; // 超时时间
    /**
     * 请求使用多长时间
     */
    private static int requestTime = 0;
    // 设置编码
    private static final String CHARSET = "utf-8";
    /**
     * 上传成功
     */
    public static final int UPLOAD_SUCCESS_CODE = 1;
    /**
     * 文件不存在
     */
    public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
    /**
     * 服务器出错
     */
    public static final int UPLOAD_SERVER_ERROR_CODE = 3;
    protected static final int WHAT_TO_UPLOAD = 1;
    protected static final int WHAT_UPLOAD_DONE = 2;

    /**
     * android上传文件到服务器
     *
     * @param filePath   需要上传的文件的路径
     * @param fileKey    在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL 请求的URL
     */
    public int uploadFile(String filePath, String fileKey, String RequestURL,
                          Map<String, String> param, int Tag) {
        if (filePath == null) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return 0;
        }
        try {
            File file = new File(filePath);
            if (file == null || (!file.exists())) {
                sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
                return 0;
            }
            uploadFile(file, fileKey, RequestURL, param, Tag);
            return 1;
        } catch (Exception e) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param fileKey    在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL 请求的URL
     */
    public void uploadFile(final File file, final String fileKey,
                           final String RequestURL, final Map<String, String> param, final int Tag) {
        Log.i(TAG, "请求的URL=" + RequestURL);
        Log.i(TAG, "请求的fileName=" + file.getName());
        Log.i(TAG, "请求的fileKey=" + fileKey);
        new Thread(new Runnable() { //开启线程上传文件
            @Override
            public void run() {
                toUploadFile(file, fileKey, RequestURL, param, Tag);
            }
        }).start();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 上传文件
     *
     * @param file
     * @param fileKey
     * @param RequestURL
     * @param param
     */
    private void toUploadFile(File file, String fileKey, String RequestURL,
                              Map<String, String> param, int Tag) {
        String result;

        if ("image".equals(fileKey)) {

            String data = BitmapCompressor.compressBitmap(file.getPath(), 200, 500, 500);
            if (data != null && !"".equals(data)) {
                FormBody body = new FormBody.Builder().add("data", data).build();
                try {
                    String request = HttpDataUtil.getPublicData_POST(RequestURL, body);

                    Log.i(TAG, request);
                    Gson gson = new Gson();
                    PublicBean upResult = gson.fromJson(request, PublicBean.class);

                    if (upResult.getCode() != null && Integer.parseInt(upResult.getCode()) == 0) {
                        sendMessage(UPLOAD_SUCCESS_CODE, Tag + upResult.getUrl().get(0));
                        return;
                    } else {
                        sendMessage(UPLOAD_SERVER_ERROR_CODE, Tag + "上传失败：code=" + request);
                        return;
                    }
                } catch (IOException e) {
                    sendMessage(UPLOAD_SERVER_ERROR_CODE, Tag + "上传失败：error=" + e.getMessage());
                    Log.e(TAG, "request error" + e);
                    return;
                }
            } else {
                sendMessage(UPLOAD_SERVER_ERROR_CODE, Tag + "");
                return;
            }
        } else {

            RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getPath()) + ";charset=utf-8"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("upload[0]", file.getPath(), fileBody)
                    .build();
            Request request = new Request.Builder()
                    .url(RequestURL)
                    .post(requestBody)
                    .build();
            try {

                Response response = MyApplication.getClient().newCall(request).execute();
                if (response.code() == 200) {
                    result = response.body().string();
                    Log.e(TAG, "result : " + result);
                    Gson gson = new Gson();
                    PublicBean upResult = gson.fromJson(result, PublicBean.class);
                    if (upResult.getCode() != null && Integer.parseInt(upResult.getCode()) == 0) {
                        sendMessage(UPLOAD_SUCCESS_CODE, Tag + upResult.getUrl().get(0));
                    }
                    return;
                } else {
                    Log.e(TAG, "request error");
                    sendMessage(UPLOAD_SERVER_ERROR_CODE, Tag + "上传失败：code=" + response.code());
                    return;
                }
            } catch (Exception e) {
                sendMessage(UPLOAD_SERVER_ERROR_CODE, Tag + "上传失败：error=" + e.getMessage());
                Log.e(TAG, "request error" + e);
                return;
            }
        }
    }

    /**
     * 发送上传结果
     *
     * @param responseCode
     * @param responseMessage
     */
    private void sendMessage(int responseCode, String responseMessage) {
        onUploadProcessListener.onUploadDone(responseCode, responseMessage);
    }

    /**
     * 下面是一个自定义的回调函数，用到回调上传文件是否完成
     */
    public static interface OnUploadProcessListener {
        /**
         * 上传响应
         *
         * @param responseCode
         * @param message
         */
        void onUploadDone(int responseCode, String message);

        /**
         * 上传中
         *
         * @param uploadSize
         */
        void onUploadProcess(int uploadSize);

        /**
         * 准备上传
         *
         * @param fileSize
         */
        void initUpload(int fileSize);
    }

    private OnUploadProcessListener onUploadProcessListener;

    public void setOnUploadProcessListener(
            OnUploadProcessListener onUploadProcessListener) {
        this.onUploadProcessListener = onUploadProcessListener;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 获取上传使用的时间
     *
     * @return
     */
    public static int getRequestTime() {
        return requestTime;
    }

    public static interface uploadProcessListener {

    }
}

