package com.yitong.ChuangKeYuan.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.fragement.FindFragment;
import com.yitong.ChuangKeYuan.fragement.HomeFragment;
import com.yitong.ChuangKeYuan.fragement.MyFragment;
import com.yitong.ChuangKeYuan.fragement.TaskFragment_all;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/13.
 * 主页面
 */
public class HomeActivity extends FragmentActivity {

    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private ImageView message, Findmess;
    private Button Address;
    //创客园子栏目
    private Fragment homeFragment;
    //发现子栏目
    private Fragment findFragment;
    //我的子栏目
    private Fragment myFragment;
    //任务
    private Fragment alltaskFragment;

    private Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //初始化
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {

        message = (ImageView) findViewById(R.id.iv_address_mes);
        Findmess = (ImageView) findViewById(R.id.iv_find_mes);
        Address = (Button) findViewById(R.id.rbtn_homeactivity_address);

        fragmentManager = getSupportFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.rg_homeactivity_radiogroup);
        ((RadioButton) radioGroup.findViewById(R.id.rbtn_homeactivity_home)).setChecked(true);
        transaction = fragmentManager.beginTransaction();
        //加载数据
        initData();
        find_mess();
    }

    private void find_mess() {

        final FormBody boby = new FormBody.Builder()
                .add("userid", MyApplication.getuserid()).build();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Findmess.setVisibility(View.VISIBLE);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {

                        String data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_Message_Find, boby);
                        Gson gson = new Gson();
                        ReturnResult result = gson.fromJson(data, ReturnResult.class);
                        Log.i("HTTP", data);
                        if (Integer.parseInt(result.getCode()) == 1) {
                            Message mess = handler.obtainMessage();
                            handler.sendMessage(mess);
                        }
                        Thread.sleep(60000);
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 加载数据
     */
    private void initData() {

        homeFragment = new HomeFragment();
        findFragment = new FindFragment();
        myFragment = new MyFragment();
        alltaskFragment = new TaskFragment_all();
        MyApplication.addFragment(homeFragment);
        MyApplication.addFragment(findFragment);
        MyApplication.addFragment(myFragment);
        MyApplication.addFragment(alltaskFragment);
        transaction.replace(R.id.home, homeFragment).commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.rbtn_homeactivity_home:
                        transaction.replace(R.id.home, MyApplication.getFragments().get(0));
                        break;
                    case R.id.rbtn_homeactivity_find:
                        transaction.replace(R.id.home, MyApplication.getFragments().get(1));
                        Findmess.setVisibility(View.GONE);
                        break;
                    case R.id.rbtn_homeactivity_my:
                        transaction.replace(R.id.home, MyApplication.getFragments().get(2));
                        break;
                    case R.id.rbtn_homeactivity_task:
                        transaction.replace(R.id.home, MyApplication.getFragments().get(3));
                        break;
                }
                transaction.commit();
            }
        });

        Address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        refreshUIWithMessage();
        super.onStart();
    }

    /**
     * 获取消息
     */
    private void refreshUIWithMessage() {
        Log.i("message", "您有新的消息，请注意查收.****");
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取未读消息数
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果是返回键,直接返回到桌面
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            showExitGameAlert();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void showExitGameAlert() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.shrew_exit_dialog);
        // 为确认按钮添加事件,执行退出应用操作
        TextView mes = (TextView) window.findViewById(R.id.dialog_mes);
        mes.setText("是否退出创客园 ?");
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        ok.setText("退出");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0); // 退出应用...
            }
        });

        // 关闭alert对话框架
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
}
