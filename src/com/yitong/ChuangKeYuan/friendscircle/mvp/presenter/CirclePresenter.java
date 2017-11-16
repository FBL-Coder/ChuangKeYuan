package com.yitong.ChuangKeYuan.friendscircle.mvp.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.fragement.FindFragment;
import com.yitong.ChuangKeYuan.friendscircle.adapter.CircleAdapter;
import com.yitong.ChuangKeYuan.fragement.FindFragment;
import com.yitong.ChuangKeYuan.friendscircle.bean.CircleItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentConfig;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.FavortItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.FindData;
import com.yitong.ChuangKeYuan.friendscircle.mvp.modle.CircleModel;
import com.yitong.ChuangKeYuan.friendscircle.mvp.modle.IDataRequestListener;
import com.yitong.ChuangKeYuan.friendscircle.mvp.view.ICircleView;
import com.yitong.ChuangKeYuan.friendscircle.utils.AsyncFindData;
import com.yitong.ChuangKeYuan.friendscircle.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;

/**
 * @author yiw
 * @ClassName: CirclePresenter
 * @Description: 通知model请求服务器和通知view更新
 * @date 2015-12-28 下午4:06:03
 */
public class CirclePresenter extends BasePresenter<ICircleView> {
    private CircleModel mCircleModel;
    private FindFragment findFragment;

    public CirclePresenter(FindFragment findFragment) {
        mCircleModel = new CircleModel();
        this.findFragment = findFragment;
    }

    public void loadData(final int loadType) {
        List<CircleItem> datas = null;
        findFragment.update2loadData(loadType, datas);
    }

    /**
     * @param circleId
     * @return void    返回类型
     * @throws
     * @Title: deleteCircle
     * @Description: 删除动态
     */
    public void deleteCircle(final String circleId) {
        mCircleModel.deleteCircle(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                getView().update2DeleteCircle(circleId);
            }
        });
    }

    /**
     * @param circlePosition
     * @return void    返回类型
     * @throws
     * @Title: addFavort
     * @Description: 点赞
     */
    public void addFavort(final int circlePosition) {
        mCircleModel.addFavort(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                FavortItem item = DatasUtil.createCurUserFavortItem(circlePosition);
                getView().update2AddFavorite(circlePosition, item);
            }
        });
    }

    /**
     * @param @param circlePosition
     * @param @param favortId
     * @return void    返回类型
     * @throws
     * @Title: deleteFavort
     * @Description: 取消点赞
     */
    public void deleteFavort(final int circlePosition, final String favortId) {
        mCircleModel.deleteFavort(new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {
                getView().update2DeleteFavort(circlePosition, favortId);
            }
        });
        DatasUtil.createCurUserFavortItem(circlePosition);
    }

    /**
     * @param content
     * @param config  CommentConfig
     * @return void    返回类型
     * @throws
     * @Title: addComment
     * @Description: 增加评论
     */

    ReturnResult result;
    Gson gson = new Gson();
    CommentItem newItem = null;

    public void addComment(final String content, final CommentConfig config, final int page) {
        if (config == null) {
            return;
        }

        mCircleModel.addComment(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {


                final Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        if (msg.what == 1) {
                            result = (ReturnResult) msg.obj;
                            if (!("".equals(result)) && Integer.parseInt(result.getCode()) == 0) {
                                newItem = DatasUtil.createPublicComment(content, result.getData());
                            } else {
                                Looper.prepare();
                                Toast.makeText(MyApplication.getContext(), "对不起，评论失败，请重新评论...", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } else {
                            result = (ReturnResult) msg.obj;
                            if (!("".equals(result)) && Integer.parseInt(result.getCode()) == 0) {
                                newItem = DatasUtil.createReplyComment(config.replyUser, content, result.getData());
                            } else {
                                Looper.prepare();
                                Toast.makeText(MyApplication.getContext(), "对不起，评论失败，请重新评论...", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                        getView().update2AddComment(config.circlePosition, newItem);
                        super.handleMessage(msg);
                    }
                };
                Log.i("COMMENTS", "config : 本来的圈子id " + config.circlePosition);
                Log.i("COMMENTS", "config : 圈子数 :" + DatasUtil.getItemdatas().size());
                if (config.commentType == CommentConfig.Type.PUBLIC) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FormBody body = new FormBody.Builder()
                                    .add("username", MyApplication.getusername())
                                    .add("password", MyApplication.getuserpassword())
                                    .add("id", DatasUtil.getItemdatas().get(config.circlePosition).getId())
                                    .add("text", content)
                                    .build();
                            try {
                                String data = HttpDataUtil.getPublicData_POST(com.yitong.ChuangKeYuan.utils.DatasUtil.URL_BASE +
                                        com.yitong.ChuangKeYuan.utils.DatasUtil.URL_addComments, body);

                                if (data != null && !"".equals(data)) {
                                    result = gson.fromJson(data, ReturnResult.class);
                                    Log.i("COMMENTS", "评论文章id" + DatasUtil.getItemdatas().get(config.circlePosition).getId() + "  返回信息" + data);
                                    Message msg = mHandler.obtainMessage();
                                    msg.obj = result;
                                    msg.what = 1;
                                    mHandler.sendMessage(msg);
                                }
                            } catch (IOException e) {
                                Looper.prepare();
                                Toast.makeText(MyApplication.getContext(), "对不起，评论异常，请稍后再试...", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                return;
                            }
                        }
                    }).start();


                } else if (config.commentType == CommentConfig.Type.REPLY) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FormBody body = new FormBody.Builder()
                                    .add("username", MyApplication.getusername())
                                    .add("password", MyApplication.getuserpassword())
                                    .add("id", DatasUtil.getItemdatas().get(config.circlePosition).getId())
                                    .add("text", content)
                                    .add("touserid", config.replyUser.getId())
                                    .build();
                            try {
                                String data = HttpDataUtil.getPublicData_POST(com.yitong.ChuangKeYuan.utils.DatasUtil.URL_BASE +
                                        com.yitong.ChuangKeYuan.utils.DatasUtil.URL_addComments, body);
                                if (data != null && !"".equals(data)) {
                                    result = gson.fromJson(data, ReturnResult.class);
                                    Log.i("COMMENTS", "评论文章id" + DatasUtil.getItemdatas().get(config.circlePosition).getId()
                                            + "回复用户id" + config.replyUser.getId() + " 返回信息" + data);
                                    Message msg = mHandler.obtainMessage();
                                    msg.obj = result;
                                    msg.what = 2;
                                    mHandler.sendMessage(msg);
                                }
                            } catch (IOException e) {
                                Looper.prepare();
                                Toast.makeText(MyApplication.getContext(), "对不起，评论异常，请稍后再试...", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                return;
                            }
                        }
                    }).start();
                }
            }
        });
    }

    /**
     * @param @param circlePosition
     * @param @param commentId
     * @return void    返回类型
     * @throws
     * @Title: deleteComment
     * @Description: 删除评论
     */
    public void deleteComment(final int circlePosition, final String commentId) {
        mCircleModel.deleteComment(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                getView().update2DeleteComment(circlePosition, commentId);
            }

        });
    }

    /**
     * @param commentConfig
     */
    public void showEditTextBody(CommentConfig commentConfig) {
        getView().updateEditTextBodyVisible(View.VISIBLE, commentConfig);
    }

}
