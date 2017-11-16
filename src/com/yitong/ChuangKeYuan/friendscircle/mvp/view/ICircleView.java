package com.yitong.ChuangKeYuan.friendscircle.mvp.view;

import com.yitong.ChuangKeYuan.friendscircle.bean.CircleItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentConfig;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.FavortItem;

import java.util.List;


/**
 * 
* @ClassName: ICircleViewUpdateListener 
* @Description: view,服务器响应后更新界面 
* @author yiw
* @date 2015-12-28 下午4:13:04 
*
 */
public interface ICircleView extends BaseView{

	public void update2DeleteCircle(String circleId);
	public void update2AddFavorite(int circlePosition, FavortItem addItem);
	public void update2DeleteFavort(int circlePosition, String favortId);
	public void update2AddComment(int circlePosition, CommentItem addItem);
	public void update2DeleteComment(int circlePosition, String commentId);

	public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);

	void update2loadData(int loadType, List<CircleItem> datas);
}


