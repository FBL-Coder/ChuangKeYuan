package com.yitong.ChuangKeYuan.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.domain.PublicBean;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.ContentBody;
import internal.org.apache.http.entity.mime.content.FileBody;

/**
 * 作者：FBL  时间： 2016/6/6.
 *
 * Log日志上传服务器
 */
public class LogCollector {

    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String TAG = "UPLOG";
    private static int readTimeOut = 10 * 1000; // 读取超时
    private static int connectTimeout = 10 * 1000; // 超时时间
    /**
     * 请求使用多长时间
     */
    private static int requestTime = 0;
    // 设置编码
    private static final String CHARSET = "utf-8";

    public static void toUploadFile(File file, String fileKey, String RequestURL) {
        String result = null;
        requestTime = 0;

        long requestTime = System.currentTimeMillis();
        long responseTime = 0;

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";

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

            sb = null;
            params = null;
            sb = new StringBuffer();
            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition:form-data; name=\"" + fileKey
                    + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append("Content-Type:video/mp4" + LINE_END);
            sb.append("Content-Type:audio/mpeg" + LINE_END);
            //sb.append("Content-Type:application/octet-stream" + LINE_END);
            sb.append(LINE_END);
            params = sb.toString();
            sb = null;

            Log.i(TAG, file.getName() + "=" + params + "##");
            dos.write(params.getBytes());
            /**
             * 上传文件
             */
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            int curLen = 0;
            while ((len = is.read(bytes)) != -1) {
                curLen += len;
                dos.write(bytes, 0, len);
            }
            is.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            responseTime = System.currentTimeMillis();
            LogCollector.requestTime = (int) ((responseTime - requestTime) / 1000);
            Log.e(TAG, "response code:" + res);
            if (res == 200) {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();
                Gson gson = new Gson();
                PublicBean upResult = gson.fromJson(result,PublicBean.class);
                Log.e(TAG, "result : " + result);
                return;
            } else {
                Log.e(TAG, "request error");
                return;
            }
        } catch (MalformedURLException e) {
//            e.printStackTrace();
            return;
        } catch (IOException e) {
//            e.printStackTrace();
            return;
        }
    }





















    public static String upload(boolean isWifiOnly,String Filepath) throws IOException {

        HttpClient httpclient = new DefaultHttpClient();
        //设置通信协议版本
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

//        File Filepath= Environment.getExternalStorageDirectory(); //取得SD卡的路径

//        String pathToOurFile = Filepath.getPath()+File.separator+"ak.txt"; //uploadfile
        String urlServer = "";

        HttpPost httppost = new HttpPost(urlServer);
        File file = new File(Filepath);

        MultipartEntity mpEntity = new MultipartEntity(); //文件传输
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("userfile", cbFile); // <input type="file" name="userfile" />  对应的


        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();

        System.out.println(response.getStatusLine());//通信Ok
        String json="";
        String path="";
        if (resEntity != null) {
            //System.out.println(EntityUtils.toString(resEntity,"utf-8"));
            json= EntityUtils.toString(resEntity,"utf-8");
            JSONObject p=null;
            try{
                p=new JSONObject(json);
                path=(String) p.get("path");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }

        httpclient.getConnectionManager().shutdown();

        Log.i("UPlog","上传成功");
        return path;
    }

}
