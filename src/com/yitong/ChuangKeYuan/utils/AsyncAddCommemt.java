package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.domain.ReturnResult;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/7/15.
 *
 * 文章详情添加评论异步网络；
 */
public class AsyncAddCommemt extends AsyncTask<Void, Void, ReturnResult> {
    private FormBody comment_boby;
    private String data;
    private ReturnResult result;
    private Context context;
    private Handler mHandler;

    /**
     * 构造器
     * @param context
     * @param comment_boby
     * @param mHandler
     */
    public AsyncAddCommemt(Context context ,FormBody comment_boby,Handler mHandler) {
        this.comment_boby = comment_boby;
        this.context = context;
        this.mHandler = mHandler;
    }

    @Override
    protected void onPostExecute(ReturnResult resultl) {
        if (resultl != null && !("".equals(resultl)) && Integer.parseInt(resultl.getCode()) == 0){
            Message message = mHandler.obtainMessage();
            message.what = 12;
            mHandler.sendMessage(message);
            ToastUtil.showToast(context, "评论成功");
        }else if (resultl != null && !("".equals(resultl)) && Integer.parseInt(resultl.getCode()) == 10111){
            ToastUtil.showToast(context, "对不起，这个模块不允许评论...");
        }else{
            ToastUtil.showToast(context, "亲，服务器异常，请求数据未成功...");
        }
        super.onPostExecute(resultl);
    }

    @Override
    protected ReturnResult doInBackground(Void... params) {
        try {
            data =  HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE+DatasUtil.URL_AddComment,comment_boby);

            Gson gson = new Gson();
            result =  gson.fromJson(data,ReturnResult.class);
            Log.i("Comment",data.toString());

        } catch (Exception e) {
            return null;
        }
        return result;
    }
}
