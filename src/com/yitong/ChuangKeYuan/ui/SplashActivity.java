package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.utils.AsyncQueryData;

/**
 * 欢迎页面
 */
public class SplashActivity extends Activity {

    private RelativeLayout mRoot;
    private TextView mVersion;
    private Activity SplashActivity;
    private AsyncQueryData data;
    private EditText setip;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        //初始化控件
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        SplashActivity = this;

        mRoot = (RelativeLayout) findViewById(R.id.rel_splash_root);
        mVersion = (TextView) findViewById(R.id.app_version);
        try {
            mVersion.setText("当前版本号是：" + getVersionName());//从清单文件中获取版本号并显示
        } catch (Exception e) {
            mVersion.setVisibility(View.GONE);
        }

//        setip = (EditText) findViewById(R.id.setip);
//        ok = (Button) findViewById(R.id.ok);
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatasUtil.setUrlBase("http://" + setip.getText().toString() + "/index.php/Webapi/");
//                data = new AsyncQueryData(SplashActivity, SplashActivity.this);
//                data.execute();
//            }
//        });

        data = new AsyncQueryData(SplashActivity, SplashActivity.this);//实例化异步线程
        data.execute();
    }
    /**
     * 获取应用当前版本号；
     *
     * @return 版本号
     * @throws Exception
     */
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}
