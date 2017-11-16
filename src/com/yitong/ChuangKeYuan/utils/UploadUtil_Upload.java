package com.yitong.ChuangKeYuan.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.domain.PublicBean;

import java.io.File;
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
 * 文章上传 工具类
 */
public class UploadUtil_Upload {
    private static UploadUtil_Upload uploadUtil;
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

    private UploadUtil_Upload() {
    }

    /**
     * 单例模式获取上传工具类
     *
     * @return
     */
    public static UploadUtil_Upload getInstance() {
        if (null == uploadUtil) {
            uploadUtil = new UploadUtil_Upload();
        }
        return uploadUtil;
    }

    private static final String TAG = "UploadUtil_Upload";
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
    public void uploadFile(String filePath, String fileKey, String RequestURL,
                           Map<String, String> param, int TAG) {
        if (filePath == null) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
        }
        try {
            File file = new File(filePath);
            uploadFile(file, fileKey, RequestURL, param, TAG);
        } catch (Exception e) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
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
                           final String RequestURL, final Map<String, String> param, final int TAG) {
        if (file == null || (!file.exists())) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
        }
        Log.i("UploadUtil_Upload", "请求的URL=" + RequestURL);
        Log.i("UploadUtil_Upload", "请求的fileName=" + file.getName());


//
//        char[] charArray = s.toCharArray();
//        System.out.println(charArray.length);
//        for (int i = 0; i < charArray.length; i++) {
//            System.out.println((int) charArray[i]);
//            System.out.println(Integer.toBinaryString(charArray[i]));
//        }


        Log.i("UploadUtil_Upload", "请求的fileKey=" + fileKey);
        new Thread(new Runnable() { //开启线程上传文件
            @Override
            public void run() {
                toUploadFile(file, fileKey, RequestURL, param, TAG);
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
            String data = BitmapCompressor.compressBitmap(file.getPath(), 400, 500, 500);
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
            } catch (Exception e) {
                sendMessage(UPLOAD_SERVER_ERROR_CODE, Tag + "上传失败：error=" + e.getMessage());
                Log.e(TAG, "request error" + e);
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
                        return;
                    } else {
                        sendMessage(UPLOAD_SERVER_ERROR_CODE, Tag + "上传失败：code=" + request);
                        return;
                    }
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


//    /**
//     * 上传文件
//     *
//     * @param file
//     * @param fileKey
//     * @param RequestURL
//     * @param param
//     */
//    private void toUploadFile(File file, String fileKey, String RequestURL,
//                              Map<String, String> param, int Tag) {
//        String result = null;
//        requestTime = 0;
//
//        long requestTime = System.currentTimeMillis();
//        long responseTime = 0;
//
//        try {
//            URL url = new URL(RequestURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(readTimeOut);
//            conn.setConnectTimeout(connectTimeout);
//            conn.setDoInput(true); // 允许输入流
//            conn.setDoOutput(true); // 允许输出流
//            conn.setUseCaches(false); // 不允许使用缓存
//            conn.setRequestMethod("POST"); // 请求方式
//            conn.setRequestProperty("Charset", CHARSET); // 设置编码
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//            /**
//             * 当文件不为空，把文件包装并且上传
//             */
//            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//            StringBuffer sb = null;
//            String params = "";
//
//            /**
//             * 以下是用于上传参数
//             */
//            if (param != null && param.size() > 0) {
//                Iterator<String> it = param.keySet().iterator();
//                while (it.hasNext()) {
//                    sb = null;
//                    sb = new StringBuffer();
//                    String key = it.next();
//                    String value = param.get(key);
//                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
//                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
//                    sb.append(value).append(LINE_END);
//                    params = sb.toString();
//                    Log.i(TAG, key + "=" + params + "##");
//                    dos.write(params.getBytes());
//                }
//            }
//
//            sb = null;
//            params = null;
//            sb = new StringBuffer();
//            /**
//             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
//             * filename是文件的名字，包含后缀名的 比如:abc.png
//             */
//            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
//            sb.append("Content-Disposition:form-data; name=\"" + fileKey
//                    + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
//            sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
//            sb.append("Content-Type:video/mp4" + LINE_END);
//            sb.append("Content-Type:audio/mpeg" + LINE_END);
//            sb.append("Content-Type:multipart/form-data" + LINE_END);
//            //sb.append("Content-Type:application/octet-stream" + LINE_END);
//            sb.append(LINE_END);
//            params = sb.toString();
//            sb = null;
//
//            Log.i(TAG, file.getName() + "=" + params + "##");
//            dos.write(params.getBytes());
//            /**
//             * 上传文件
//             */
//            InputStream is = new FileInputStream(file);
//            onUploadProcessListener.initUpload((int) file.length());
//            byte[] bytes = new byte[1024];
//            int len = 0;
//            int curLen = 0;
//            while ((len = is.read(bytes)) != -1) {
//                curLen += len;
//                dos.write(bytes, 0, len);
//                onUploadProcessListener.onUploadProcess(curLen);
//            }
//            is.close();
//            dos.write(LINE_END.getBytes());
//            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
//            dos.write(end_data);
//            dos.flush();
//            /**
//             * 获取响应码 200=成功 当响应成功，获取响应的流
//             */
//            int res = conn.getResponseCode();
//            responseTime = System.currentTimeMillis();
//            this.requestTime = (int) ((responseTime - requestTime) / 1000);
//            Log.e(TAG, "response code:" + res);
//            if (res == 200) {
//                Log.e(TAG, "request success");
//                InputStream input = conn.getInputStream();
//                StringBuffer sb1 = new StringBuffer();
//                int ss;
//                while ((ss = input.read()) != -1) {
//                    sb1.append((char) ss);
//                }
//                result = sb1.toString();
//
//                Gson gson = new Gson();
//                UpResult upResult = gson.fromJson(result, UpResult.class);
//
////                if (Tag == 1){
////                    Log.e(TAG, "result :我是第一个添加按钮 " + result);
////                }else if (Tag == 2){
////                    Log.e(TAG, "result :我是第二个添加按钮 " + result);
////                }else if (Tag == 3){
////                    Log.e(TAG, "result :我是第三个添加按钮 " + result);
////                }
//                Log.e("AABC", "result : " + result);
//                if (upResult != null && Integer.parseInt(upResult.getCode()) == 0 && !"".equals(upResult.getUrl())) {
//                    sendMessage(UPLOAD_SUCCESS_CODE, Tag + upResult.getUrl().get(0));
//                } else {
//                    Log.i("AABC", "服务器数据库处理异常");
//                    sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：code=" + upResult.getCode());
//                }
//                return;
//            } else {
//                Log.e(TAG, "request error");
//                sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：code=" + res);
//                return;
//            }
//        } catch (MalformedURLException e) {
//            sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：error=" + e.getMessage());
//            return;
//        } catch (IOException e) {
//            sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：error=" + e.getMessage());
//            return;
//        }
//    }

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

    /**
     * 获取文件名称的方法
     *
     * @param Path eg: /internal/sdcard/code.png
     * @return
     */
    @Nullable
    public static String getFileName(String Path) {
        int start = Path.lastIndexOf("/");
        if (start != -1) {
            return Path.substring(start + 1);
        } else {
            return null;
        }
    }

    /**
     * 获取文件大小的方法
     *
     * @param str eg:94 kb
     * @return
     */
    public static String getStringSize(String str) {
        int end = str.lastIndexOf(" ");
        if (end != -1) {
            Log.i("ABC", str.substring(0, end));
            return str.substring(0, end);
        } else {
            return null;
        }
    }
}

