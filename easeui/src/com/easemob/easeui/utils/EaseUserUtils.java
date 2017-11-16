package com.easemob.easeui.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easemob.easeui.R;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.easemob.easeui.domain.AllUser;
import com.easemob.easeui.domain.EaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.google.gson.Gson;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;


    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * 根据username获取相应user
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUserAvatar(final Context context, final String username, final ImageView imageView) {
        final EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
//            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(avatarResId).into(imageView);
//            } catch (Exception e) {
            //正常的string路径
//            Log.i("USERNAME", user.getAvatar());
//            Log.i("USERNAME", user.getNick());
//            Log.i("USERNAME", user.getUsername());
//            Log.i("USERNAME", user.getEid());
//            Log.i("USERNAME", user.getInitialLetter());
            Log.i("USERNAME", "返回的" + username);
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    List<AllUser.ListBean> UserList = (List<AllUser.ListBean>) msg.obj;
                    for (int i = 0; i < UserList.size(); i++) {
                        if (username.equalsIgnoreCase(UserList.get(i).getUser_login())) {
                            Glide.with(context).load(UserList.get(i).getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.ease_default_avatar)
                                    .into(imageView);
                        }
                    }
                    super.handleMessage(msg);
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (EaseFIleUtils.fileIsExists("AllUser_IMG", context)) {
                            String data = EaseFIleUtils.readFile("AllUser_IMG", context);

                            if (!("".equals(data)) && data != null) {

                                JSONObject jsonObject = new JSONObject(data);

                                JSONArray jsonArray = jsonObject.getJSONArray("list");

                                List<AllUser.ListBean> UserList = new ArrayList<AllUser.ListBean>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject json = jsonArray.getJSONObject(i);

                                    AllUser.ListBean list = new AllUser.ListBean();
                                    list.setAvatar((String) json.get("avatar"));
                                    list.setId((String) json.get("id"));
                                    list.setUser_login((String) json.get("user_login"));
                                    list.setUser_nicename((String) json.get("user_nicename"));
                                    UserList.add(list);
                                }

                                Message message = mHandler.obtainMessage();
                                message.obj = UserList;
                                mHandler.sendMessage(message);
                            }
                        }
                    } catch (Exception e) {
                        return;
                    }
                }
            }).start();
        }

//            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
    }

    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username, TextView textView) {

        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {
                textView.setText(username);
            }
        }
    }

    public static void setUserAvatarAddUserNick(final Context context,
                                                final String username,
                                                final ImageView imageView,
                                                final TextView textView) {
        final EaseUser user = getUserInfo(username);
        if (user != null) {

            Log.i("USERNAME", "返回的" + username);
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    List<AllUser.ListBean> UserList = (List<AllUser.ListBean>) msg.obj;
                    for (int i = 0; i < UserList.size(); i++) {
                        if (username.equalsIgnoreCase(UserList.get(i).getUser_login())) {
                            if (imageView != null) {
                                Glide.with(context).load(UserList.get(i).getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.ease_default_avatar)
                                        .into(imageView);
                            }
                            if (textView != null) {
                                if (UserList.get(i).getUser_nicename() == null || "".equals(UserList.get(i).getUser_nicename()))
                                    textView.setText(username);
                                textView.setText(UserList.get(i).getUser_nicename());
                            }
                        }

                    }
                    super.handleMessage(msg);
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (EaseFIleUtils.fileIsExists("AllUser_IMG", context)) {
                            String data = EaseFIleUtils.readFile("AllUser_IMG", context);

                            if (!("".equals(data)) && data != null) {

                                JSONObject jsonObject = new JSONObject(data);

                                JSONArray jsonArray = jsonObject.getJSONArray("list");

                                List<AllUser.ListBean> UserList = new ArrayList<AllUser.ListBean>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject json = jsonArray.getJSONObject(i);

                                    AllUser.ListBean list = new AllUser.ListBean();
                                    list.setAvatar((String) json.get("avatar"));
                                    list.setId((String) json.get("id"));
                                    list.setUser_login((String) json.get("user_login"));
                                    list.setUser_nicename((String) json.get("user_nicename"));
                                    UserList.add(list);
                                }

                                Message message = mHandler.obtainMessage();
                                message.obj = UserList;
                                mHandler.sendMessage(message);
                            }
                        }
                    } catch (Exception e) {
                        return;
                    }
                }
            }).start();
        }
    }

}
