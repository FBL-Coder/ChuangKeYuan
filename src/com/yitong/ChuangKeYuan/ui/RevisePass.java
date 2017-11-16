package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/9/20.
 * 修改密码
 */
public class RevisePass extends Activity implements View.OnClickListener {

    TextView title;
    ImageView back;
    EditText oldpass, newpass, agreenewpass;
    Button pass_ok;
    private ImageView search;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revisepass_activity);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.tv_teach_title);
        back = (ImageView) findViewById(R.id.iv_teach_back);
        search = (ImageView) findViewById(R.id.iv_teach_search);
        title.setText("修改密码");
        back.setOnClickListener(this);
        search.setVisibility(View.GONE);

        oldpass = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.newpass);
        agreenewpass = (EditText) findViewById(R.id.agreenewpass);
        pass_ok = (Button) findViewById(R.id.pass_ok);
        pass_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_teach_back:
                finish();
                break;
            case R.id.pass_ok:

                String oldpassstr = oldpass.getText().toString();
                String newpassstr = newpass.getText().toString();
                final String agreenewpassstr = agreenewpass.getText().toString();

                if ("".equals(oldpassstr) || oldpassstr == null ||
                        "".equals(newpassstr) || newpassstr == null ||
                        "".equals(agreenewpassstr) || agreenewpassstr == null) {
                    ToastUtil.showToast(RevisePass.this, "请依次填完");
                } else {
                    if (!oldpassstr.equals(MyApplication.getuserpassword())) {
                        ToastUtil.showToast(RevisePass.this, "旧密码不正确，请重新输入！");
                        oldpass.setText("");
                    } else {
                        if (!newpassstr.equals(agreenewpassstr)) {
                            ToastUtil.showToast(RevisePass.this, "新密码两次不一致！");
                            newpass.setText("");
                            agreenewpass.setText("");
                        } else {
                            final Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    ToastUtil.showToast(RevisePass.this, "修改成功");
                                    preferences = MyApplication.getContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("password", agreenewpassstr);
                                    finish();
                                    super.handleMessage(msg);
                                }
                            };
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    FormBody body = new FormBody.Builder()
                                            .add("username", MyApplication.getusername())
                                            .add("password", MyApplication.getuserpassword())
                                            .add("newpassword", agreenewpassstr).build();
                                    try {

                                        String data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_ChangePassword, body);

                                        if (data!= null &&!"".equals(data)){
                                            System.out.println(data);
                                        }
                                        Gson gson = new Gson();
                                        ReturnResult result = gson.fromJson(data, ReturnResult.class);

                                        if (Integer.parseInt(result.getCode()) == 0) {
                                            Message msg = handler.obtainMessage();
                                            handler.sendMessage(msg);
                                        } else {
                                            ToastUtil.showToast(RevisePass.this, "修改密码失败，请稍后再试！");
                                        }
                                    } catch (Exception e) {
                                        return;
                                    }
                                }
                            }).start();
                        }
                    }
                }
                break;
        }
    }
}
