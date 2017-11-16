package com.yitong.ChuangKeYuan.friendscircle.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;

import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentItem;
import com.yitong.ChuangKeYuan.friendscircle.mvp.presenter.CirclePresenter;
import com.yitong.ChuangKeYuan.friendscircle.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * @author yiw
 * @ClassName: CommentDialog
 * @Description: 评论长按对话框，保护复制和删除
 * @date 2015-12-28 下午3:36:39
 */
public class CommentDialog extends Dialog implements
        android.view.View.OnClickListener {

    private Context mContext;
    private CirclePresenter mPresenter;
    private CommentItem mCommentItem;
    private int mCirclePosition;
    private int commentposition;

    public CommentDialog(Context context, CirclePresenter presenter,
                         CommentItem commentItem, int circlePosition, int commentposition) {
        super(context, R.style.comment_dialog);
        mContext = context;
        this.mPresenter = presenter;
        this.mCommentItem = commentItem;
        this.mCirclePosition = circlePosition;
        this.commentposition = commentposition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        initWindowParams();
        initView();
    }

    private void initWindowParams() {
        Window dialogWindow = getWindow();
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        TextView copyTv = (TextView) findViewById(R.id.copyTv);
        copyTv.setOnClickListener(this);
        TextView deleteTv = (TextView) findViewById(R.id.deleteTv);
        if (mCommentItem != null
                && DatasUtil.curUser.getId().equals(
                mCommentItem.getUser().getId())) {
            deleteTv.setVisibility(View.VISIBLE);
        } else {
            deleteTv.setVisibility(View.GONE);
        }
        deleteTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyTv:
                if (mCommentItem != null) {
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(mCommentItem.getContent());
                }
                dismiss();
                break;
            case R.id.deleteTv:
                if (mPresenter != null && mCommentItem != null) {

                    Log.i("Comment", "传递过来的圈子id:" + mCirclePosition + "拿到的圈子id:" + DatasUtil.getItemdatas().get(mCirclePosition).getId());
                    Log.i("Comment", "评论数目:" + DatasUtil.getItemdatas().get(mCirclePosition).getComments().size() + "");
                    Log.i("Comment", "删除评论id:" + DatasUtil.getItemdatas().get(mCirclePosition).getComments().get(commentposition).getId());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FormBody body = new FormBody.Builder()
                                    .add("username", MyApplication.getusername())
                                    .add("password", MyApplication.getuserpassword())
                                    .add("id", DatasUtil.getItemdatas().get(mCirclePosition).getComments().get(commentposition).getId())
                                    .build();
                            try {
                                String data = HttpDataUtil.getPublicData_POST(com.yitong.ChuangKeYuan.utils.DatasUtil.URL_BASE +
                                        com.yitong.ChuangKeYuan.utils.DatasUtil.URL_delComments, body);
                                if (!("".equals(data)) && data != null) {
                                    Log.i("DELCOMMENT", " 返回信息" + data);
                                    Gson gson = new Gson();
                                    ReturnResult result = gson.fromJson(data, ReturnResult.class);
                                    if (Integer.parseInt(result.getCode()) == 0) {
                                        mPresenter.deleteComment(mCirclePosition, mCommentItem.getId());
                                    }else{
                                        Looper.prepare();
                                        ToastUtil.showToast(mContext, "对不起，删除评论失败，请稍后再试...");
                                    }
                                }
                            } catch (IOException e) {
                                //e.printStackTrace();
                            }
                        }
                    }).start();
                }
                dismiss();
                break;
            default:
                break;
        }
    }
}
