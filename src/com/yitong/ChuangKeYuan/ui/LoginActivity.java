/**
 * Copyright (C) 2013-2014 yitong Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.Helper.MyHelper;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.UserLogin;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Dtat_Cache;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity {
    private final String TAG = "LoginActivity";
    public final int REQUEST_CODE_SETNICK = 1;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private boolean progressShow;
    private boolean autoLogin = false;

    private String currentUsername;
    private String currentPassword;
    private FormBody body;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果登录成功过，直接进入主页面
        if (MyHelper.getInstance().isLoggedIn()) {
            Log.i("login", "环信自动登陆成功");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.em_activity_login);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (MyHelper.getInstance().getCurrentUsernName() != null) {
            usernameEditText.setText(MyHelper.getInstance().getCurrentUsernName());
        }
    }

    /**
     * 登录
     *
     * @param view
     */
    public void login(View view) {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        progressShow = true;
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();
        final long start = System.currentTimeMillis();


        body = new FormBody.Builder()
                .add("username", currentUsername)
                .add("password", currentPassword)
                .build();

//        Log.i("HTTPData", currentUsername + "*****" + currentPassword);

        new AsyncLogin().execute(DatasUtil.URL_BASE + DatasUtil.URL_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }

    /**
     * 异步登陆自己服务器和环信服务器
     */
    class AsyncLogin extends AsyncTask<String, Void, UserLogin> {
        UserLogin login;

        @Override
        protected void onPostExecute(final UserLogin userLogin) {
            if (userLogin != null) {
                Timer tExit = new Timer();
                switch (Integer.parseInt(userLogin.getCode())) {
                    case -1:
                        if (!LoginActivity.this.isFinishing() && !pd.isShowing()) {
                            pd.setMessage("发生未知错误,请稍后再试!");
                            pd.show();
                        } else if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                            pd.dismiss();
                            pd.setMessage("发生未知错误,请稍后再试!");
                            pd.show();
                        }
                        tExit.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                pd.dismiss();  // 取消退出
                            }
                        }, 1500);
                        break;
                    case 0:
                        EMChatManager.getInstance().login(currentUsername, "etsoft", new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                Log.i("login", "环信用户登陆成功");
                                if (!progressShow) {
                                    return;
                                }
                                // 登陆成功，保存用户名
                                MyHelper.getInstance().setCurrentUserName(currentUsername);
                                // 注册群组和联系人监听
                                MyHelper.getInstance().registerGroupAndContactListener();

                                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                                // ** manually load all local groups and
                                EMGroupManager.getInstance().loadAllGroups();
                                EMChatManager.getInstance().loadAllConversations();

                                //异步获取当前用户的昵称和头像(从自己服务器获取，这里使用的一个第三方服务)
                                MyHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                                    pd.dismiss();
                                }

                                //登录成功后 存储用户信息
                                SharedPreferences preferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("username", currentUsername);
                                editor.putString("password", currentPassword);
                                editor.putString("token", userLogin.getToken());
                                editor.putString("name", userLogin.getNicename());
                                editor.putString("headimg", userLogin.getAvatar());
                                editor.putString("id", userLogin.getId());
                                editor.putString("type", userLogin.getType());
                                editor.putString("classname", userLogin.getClassname());
                                editor.commit();

                                finish();
                            }

                            @Override
                            public void onProgress(int progress, String status) {
                            }

                            @Override
                            public void onError(final int code, final String message) {
                                if (!progressShow) {
                                    return;
                                }
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                        break;
                    case 10003:
                        if (!LoginActivity.this.isFinishing() && !pd.isShowing()) {
                            pd.setMessage("密码错误，请重试！");
                            pd.show();
                        } else if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                            pd.dismiss();
                            pd.setMessage("密码错误，请重试！");
                            pd.show();
                        }
                        tExit.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                pd.dismiss();  // 取消退出
                            }
                        }, 1500);
                        break;
                    case 10004:
                        if (!LoginActivity.this.isFinishing() && !pd.isShowing()) {
                            pd.setMessage("登陆用户不存在");
                            pd.show();
                        } else if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                            pd.dismiss();
                            pd.setMessage("登陆用户不存在");
                            pd.show();
                        }
                        tExit.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                pd.dismiss();  // 取消退出
                            }
                        }, 1500);
                        break;
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.Login_failed),
                        Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(userLogin);
        }

        @Override
        protected UserLogin doInBackground(String... params) {
            String data = null;
            try {

                if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {
                    data = HttpDataUtil.getPublicData_POST(params[0], body);
                    Dtat_Cache.writeFileToSD(data, "UserData");
                } else {
                    Log.i("HTTPData", "Detail_没网，网打开..");
                    data = Dtat_Cache.readFile("UserData");
                }
                Log.i("HTTPData", "UserData-----" + data);

                Gson gson = new Gson();
                login = gson.fromJson(data, UserLogin.class);

//                Log.i("HTTPData", "UserData_ok" + data.length() + "----" + login.getCode());

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("HTTPData", "Detail_IOException" + e);
            }
            return login;
        }
    }
}
