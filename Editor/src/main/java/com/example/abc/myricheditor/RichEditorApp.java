package com.example.abc.myricheditor;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * 作者：FBL  时间： 2017/4/20.
 */
public class RichEditorApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initTbs();
    }
    private void initTbs() {

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                Log.i("onViewInitFinished is " ,""+ arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };

        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.i("onDownloadFinish", ""+i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.i("onInstallFinish", ""+i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.i("onDownloadProgress:" , ""+i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);

    }
}
