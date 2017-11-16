package com.yitong.ChuangKeYuan.friendscircle.utils;

import android.os.Looper;
import android.util.Log;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.friendscircle.bean.CircleItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.FavortItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.FindData;
import com.yitong.ChuangKeYuan.friendscircle.bean.User;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * @author yiw
 * @ClassName: DatasUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午4:16:21
 */
public class DatasUtil {

    public static User curUser;

    public static FindData data;

    public static List<CircleItem> Itemdatas = new ArrayList<CircleItem>();

    public static FindData getFindData() {
        return data;
    }

    public static void setFindData(FindData data) {
        DatasUtil.data = data;
    }

    public static List<CircleItem> getItemdatas() {
        return Itemdatas;
    }

    public static void setItemdatas(List<CircleItem> itemdatas) {
        Itemdatas.addAll(itemdatas) ;
    }

    public static List<CircleItem> createCircleDatas(FindData result) {
        curUser = new User(result.getUser().getId(),result.getUser().getName(),result.getUser().getHeadUrl());
        List<CircleItem> circleDatas = new ArrayList<CircleItem>();
        for (int i = 0; i < result.getList().size(); i++) {
            CircleItem item = new CircleItem();
            User user = result.getList().get(i).getUser();
            item.setId(result.getList().get(i).getId());
            item.setUser(user);
            item.setContent(result.getList().get(i).getContent());
            item.setCreateTime(result.getList().get(i).getCreateTime());
            item.setFavorters(result.getList().get(i).getFavorters());
            item.setComments(result.getList().get(i).getComments());
            int type = Integer.parseInt(result.getList().get(i).getType());
            if (type == 1) {
                item.setType("1");// 链接
                item.setLinkImg("https://www.baidu.com/img/bd_logo1.png");
                item.setLinkTitle("百度一下，你就知道");
            } else if (type == 2) {
                item.setType("2");// 图片
                item.setPhotos(result.getList().get(i).getPhotos());
            } else {
                item.setType("3");// 图片
                String videoUrl = result.getList().get(i).getVideoUrl();
                item.setVideoUrl(videoUrl);
            }
            circleDatas.add(item);
        }
        return circleDatas;
    }

    public static FavortItem createCurUserFavortItem(final int circlePosition) {
        FavortItem item = new FavortItem();
        item.setId(DatasUtil.getItemdatas().get(circlePosition).getId());
        item.setUser(curUser);

        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody body = new FormBody.Builder()
                        .add("username", MyApplication.getusername())
                        .add("password", MyApplication.getuserpassword())
                        .add("id", DatasUtil.getItemdatas().get(circlePosition).getId())
                        .build();
                try {
                    String data = HttpDataUtil.getPublicData_POST(com.yitong.ChuangKeYuan.utils.DatasUtil.URL_BASE +
                            com.yitong.ChuangKeYuan.utils.DatasUtil.URL_Addpraise, body);
                    if (data != null) Log.i("FAVORT", "点赞文章id"+DatasUtil.getItemdatas().get(circlePosition).getId()+" 返回信息"+data);
                } catch (IOException e) {
                    Looper.prepare();
                    ToastUtil.showToast(MyApplication.getContext(), "对不起，点赞异常，请稍后再试...");
                    Looper.loop();
                    return;
                }
            }
        }).start();

        return item;
    }

    /**
     * 创建发布评论
     *
     * @return
     */
    public static CommentItem createPublicComment(final String content, String id) {
        CommentItem item = new CommentItem();
        item.setId(String.valueOf(id));
        item.setContent(content);
        item.setUser(curUser);
        return item;
    }

    /**
     * 创建回复评论
     *
     * @return
     */
    public static CommentItem createReplyComment(final User replyUser, final String content,String id) {
        CommentItem item = new CommentItem();
        item.setId(String.valueOf(id));
        item.setContent(content);
        item.setUser(curUser);
        item.setToReplyUser(replyUser);
        return item;
    }
}
