package com.yitong.ChuangKeYuan.fragement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.Helper.MyHelper;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.domain.TimeAddPostBean;
import com.yitong.ChuangKeYuan.ui.ChuangkeyuanActivity;
import com.yitong.ChuangKeYuan.ui.CollectActivity;
import com.yitong.ChuangKeYuan.ui.CommentActivity;
import com.yitong.ChuangKeYuan.ui.LoginActivity;
import com.yitong.ChuangKeYuan.ui.UserProfileActivity;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.utils.UiUtils;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/17.
 * 我的页面
 */
public class MyFragment extends Fragment implements OnClickListener {

    public View view;
    public TextView mTitle;
    public TextView mDes;
    public RelativeLayout mSearch, rel_collect, rel_comment, rel_setting, rel_chuangkeyuan;
    public TextView mTime, mPost, mPost1, mPostDefault, mTime1, mTimeDefault, mCollect, mTV_comment,
            mUser, mSetting, mChuangkeyuan, mVersions, mVersionsDefault, mVersions1, mLogout;
    private ImageView time, post, collect, iv_comment, setting, chuangkeyuan, versions;
    private FormBody body;
    private SimpleDraweeView mIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = UiUtils.inflateView(R.layout.fragment_my);
        //初始化标题栏
        initTitlebar(view);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        mTime = (TextView) view.findViewById(R.id.tv_time);
        mTime1 = (TextView) view.findViewById(R.id.tv_time1);
        mTimeDefault = (TextView) view.findViewById(R.id.tv_time_default);
        mPost = (TextView) view.findViewById(R.id.tv_post);
        mPost1 = (TextView) view.findViewById(R.id.tv_post1);
        mPostDefault = (TextView) view.findViewById(R.id.tv_post_dafault);
        mCollect = (TextView) view.findViewById(R.id.tv_collect);
        mSetting = (TextView) view.findViewById(R.id.tv_setting);
        mChuangkeyuan = (TextView) view.findViewById(R.id.tv_chuangkeyuan);
        mVersions = (TextView) view.findViewById(R.id.tv_versions);
        mVersionsDefault = (TextView) view.findViewById(R.id.tv_versions_dafault);
        mVersions1 = (TextView) view.findViewById(R.id.tv_versions1);
        mLogout = (TextView) view.findViewById(R.id.tv_logout);
        mIcon = (SimpleDraweeView) view.findViewById(R.id.iv_icon);
        mUser = (TextView) view.findViewById(R.id.tv_user);
        time = (ImageView) view.findViewById(R.id.iv_time);
        mTV_comment = (TextView) view.findViewById(R.id.tv_comment);
        post = (ImageView) view.findViewById(R.id.iv_post);
        collect = (ImageView) view.findViewById(R.id.iv_collect);
        setting = (ImageView) view.findViewById(R.id.iv_setting);
        chuangkeyuan = (ImageView) view.findViewById(R.id.iv_chuangkeyuan);
        iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
        versions = (ImageView) view.findViewById(R.id.iv_versions);
        rel_collect = (RelativeLayout) view.findViewById(R.id.rel_collect);
        rel_setting = (RelativeLayout) view.findViewById(R.id.rel_setting);
        rel_chuangkeyuan = (RelativeLayout) view.findViewById(R.id.rel_chuangkeyuan);
        rel_comment = (RelativeLayout) view.findViewById(R.id.rel_comment);

        time.setImageResource(R.drawable.time);
        post.setImageResource(R.drawable.post);
        collect.setImageResource(R.drawable.collect);
        setting.setImageResource(R.drawable.setting);
        chuangkeyuan.setImageResource(R.drawable.chuangkeyuanyuan);
        versions.setImageResource(R.drawable.versions);
        iv_comment.setImageResource(R.drawable.em_add_public_group);

        mTime.setText(DatasUtil.MY_TEXT[1]);
        mPost.setText(DatasUtil.MY_TEXT[2]);
        mCollect.setText(DatasUtil.MY_TEXT[3]);
        mSetting.setText(DatasUtil.MY_TEXT[4]);
        mChuangkeyuan.setText(DatasUtil.MY_TEXT[5]);
        mVersions.setText(DatasUtil.MY_TEXT[6]);
        mTV_comment.setText("综合评价");
        mIcon.setOnClickListener(this);
        mIcon.setOnClickListener(this);
        mTime.setOnClickListener(this);
        mPost.setOnClickListener(this);
        rel_collect.setOnClickListener(this);
        rel_setting.setOnClickListener(this);
        rel_chuangkeyuan.setOnClickListener(this);
        mLogout.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!"".equals(MyApplication.getToKen()) && MyApplication.getToKen() != null) {
            if ("2".equals(MyApplication.getuserType())) {
                rel_comment.setVisibility(View.VISIBLE);
                rel_comment.setOnClickListener(this);
            }else {
                rel_comment.setVisibility(View.GONE);
            }
        }
        if (!"".equals(MyApplication.getToKen()) && MyApplication.getToKen() != null) {
            mLogout.setText("退出登录");
            mIcon.setImageURI(MyApplication.getHeadImg());
            mUser.setText(MyApplication.getName());
            mTimeDefault.setVisibility(View.GONE);
            mPostDefault.setVisibility(View.GONE);
            mVersionsDefault.setVisibility(View.GONE);
            //加载论坛发帖数据
            initData_Post();
        } else {
            mLogout.setText("登  录");
            mIcon.setImageResource(DatasUtil.MY_PHOTO[0]);
            mTimeDefault.setVisibility(View.VISIBLE);
            mPostDefault.setVisibility(View.VISIBLE);
            mUser.setText("未登录");
            mVersionsDefault.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载论坛发帖数据
     */
    private void initData_Post() {
        body = new FormBody.Builder()
                .add("username", MyApplication.getusername())
                .add("password", MyApplication.getuserpassword())
                .build();
        new AsyncPostData().execute(DatasUtil.URL_BASE + DatasUtil.URL_Statistics);
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar(View view) {
        //标题栏
        mTitle = (TextView) view.findViewById(R.id.tv_titlebar_title);
        mDes = (TextView) view.findViewById(R.id.tv_titlebar_des);
        mSearch = (RelativeLayout) view.findViewById(R.id.tv_titlebar_search);
        mTitle.setText("我的");
        mSearch.setVisibility(View.GONE);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_collect:
                Intent intent2 = new Intent(getActivity().getApplicationContext(), CollectActivity.class);
                intent2.putExtra("title", DatasUtil.MY_TEXT[3]);
                startActivity(intent2);
                break;
            case R.id.rel_setting:
                if (MyApplication.getToKen() != null && !"".equals(MyApplication.getToKen())) {
                    startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("setting", true)
                            .putExtra("username", MyApplication.getusername()));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rel_chuangkeyuan:
                startActivity(new Intent(getActivity(), ChuangkeyuanActivity.class));
                break;
            case R.id.tv_logout:
                if (!"".equals(MyApplication.getToKen()) && MyApplication.getToKen() != null) {
                    logout();
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rel_comment:
                startActivity(new Intent(getActivity(),CommentActivity.class));
                break;
        }
    }

    /**
     * 加载论坛发帖数据
     */
    public class AsyncPostData extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPostExecute(final String result) {
            try {
                mVersions1.setVisibility(View.VISIBLE);
                try {
                    mVersions1.setText(getVersionName());
                } catch (Exception e) {
                    mVersions1.setText("最新版本");
                }
                Gson gson = new Gson();
                if (result != null) {
                    TimeAddPostBean postBean = gson.fromJson(result, TimeAddPostBean.class);
                    if (!"".equals(postBean.getCode()) && postBean.getCode() != null &&
                            Integer.parseInt(postBean.getCode()) == 0) {
                        mPost1.setVisibility(View.VISIBLE);
                        mTime1.setVisibility(View.VISIBLE);
                        mTime1.setText(postBean.getOnlinetimeStatistics_h());
                        mPost1.setText(postBean.getArticleStatistics());
                    } else {
                        mPostDefault.setVisibility(View.VISIBLE);
                    }
                } else {
                    mPostDefault.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... params) {
            String data = null;
            try {
                data = HttpDataUtil.getPublicData_POST(params[0], body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (data != null && !"".equals(data)) {
                Log.i("TAG", data);
            }
            return data;
        }
    }

    /**
     * 登陆/退出
     */
    void logout() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        MyHelper.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Handler mHandler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    pd.dismiss();
                                    if (msg.arg1 == 0) {
                                        ToastUtil.showToast(getActivity(), "退出成功");
                                        rel_comment.setVisibility(View.GONE);
                                        mLogout.setText("登  录");
                                        mIcon.setImageResource(DatasUtil.MY_PHOTO[0]);
                                        mUser.setText("未登录");
                                        mTime1.setVisibility(View.GONE);
                                        mPost1.setVisibility(View.GONE);
                                        mVersions1.setVisibility(View.GONE);
                                        mTimeDefault.setVisibility(View.VISIBLE);
                                        mPostDefault.setVisibility(View.VISIBLE);
                                        mVersionsDefault.setVisibility(View.VISIBLE);
                                        MyApplication.setpreferences();
                                    } else {
                                        ToastUtil.showToast(getActivity(), "退出失败，请稍后再试...");
                                    }
                                    super.handleMessage(msg);
                                }
                            };

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String data = null;
                                    try {
                                        data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_logout,
                                                new FormBody.Builder().add("uid", MyApplication.getuserid()).build());
                                        Gson gson = new Gson();

                                        ReturnResult result = gson.fromJson(data, ReturnResult.class);
                                        Log.i("LOGOUT", data);
                                        Message msg = mHandler.obtainMessage();
                                        msg.arg1 = Integer.parseInt(result.getCode());
                                        mHandler.sendMessage(msg);
                                    } catch (IOException e) {
                                        System.out.println(e);
                                    }
                                }
                            }).start();
                        } catch (Exception e) {
                            pd.dismiss();
                            Looper.prepare();
                            ToastUtil.showToast(getActivity(), "退出失败，请稍后再试...");
                            Looper.loop();
                        }
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, final String message) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Log.i("logout", "退出失败 " + message);

                    }
                });
            }
        });
    }

    /**
     * 获取版本号
     *
     * @return
     * @throws Exception
     */
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = MyApplication.getContext().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }
}
