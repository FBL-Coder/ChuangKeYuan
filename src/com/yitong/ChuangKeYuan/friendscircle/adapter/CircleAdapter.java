package com.yitong.ChuangKeYuan.friendscircle.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.friendscircle.ImagePagerActivity;
import com.yitong.ChuangKeYuan.friendscircle.bean.ActionItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.CircleItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentConfig;
import com.yitong.ChuangKeYuan.friendscircle.bean.CommentItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.FavortItem;
import com.yitong.ChuangKeYuan.friendscircle.mvp.presenter.CirclePresenter;
import com.yitong.ChuangKeYuan.friendscircle.spannable.ISpanClick;
import com.yitong.ChuangKeYuan.friendscircle.utils.DatasUtil;
import com.yitong.ChuangKeYuan.friendscircle.utils.GlideCircleTransform;
import com.yitong.ChuangKeYuan.friendscircle.utils.UrlUtils;
import com.yitong.ChuangKeYuan.friendscircle.widgets.CircleVideoView;
import com.yitong.ChuangKeYuan.friendscircle.widgets.CommentListView;
import com.yitong.ChuangKeYuan.friendscircle.widgets.FavortListView;
import com.yitong.ChuangKeYuan.friendscircle.widgets.MagicTextView;
import com.yitong.ChuangKeYuan.friendscircle.widgets.MultiImageView;
import com.yitong.ChuangKeYuan.friendscircle.widgets.SnsPopupWindow;
import com.yitong.ChuangKeYuan.friendscircle.widgets.dialog.CommentDialog;
import com.yitong.ChuangKeYuan.friendscircle.widgets.videolist.model.VideoLoadMvpView;
import com.yitong.ChuangKeYuan.friendscircle.widgets.videolist.widget.TextureVideoView;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;
import com.yitong.ChuangKeYuan.widget.CustomDialog_upload;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {

    public final static int TYPE_HEAD = 0;
    public final static int TYPE_URL = 1;
    public final static int TYPE_IMAGE = 2;
    public final static int TYPE_VIDEO = 3;

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private int videoState = STATE_IDLE;
    public static int HEADVIEW_SIZE = 1;

    int curPlayIndex = -1;

    private CirclePresenter presenter;
    private Context context;

    public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }

    public CircleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }

        int itemType = 0;
        CircleItem item = (CircleItem) datas.get(position - 1);
        if (CircleItem.TYPE_URL.equals(item.getType())) {
            itemType = TYPE_URL;
        } else if (CircleItem.TYPE_IMG.equals(item.getType())) {
            itemType = TYPE_IMAGE;
        } else if (CircleItem.TYPE_VIDEO.equals(item.getType())) {
            itemType = TYPE_VIDEO;
        }
        return itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == TYPE_HEAD) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_circle, parent, false);
            ImageView back = (ImageView) headView.findViewById(R.id.findbackground);
            ImageView headicon = (ImageView) headView.findViewById(R.id.headicon);
            back.setImageURI(Uri.parse(DatasUtil.getFindData().getBanner()));
            headicon.setImageURI(Uri.parse(DatasUtil.getFindData().getUser().getHeadUrl()));

            viewHolder = new HeaderViewHolder(headView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);
            viewHolder = new CircleViewHolder(view, viewType);
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (getItemViewType(position) == TYPE_HEAD) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
        } else {

            final int circlePosition = position - HEADVIEW_SIZE;
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;
            CircleItem circleItem = (CircleItem) datas.get(circlePosition);
            final String circleId = circleItem.getId();
            String name = circleItem.getUser().getName();
            String headImg = circleItem.getUser().getHeadUrl();
            final String content = circleItem.getContent();
            String createTime = circleItem.getCreateTime();
            final List<FavortItem> favortDatas = circleItem.getFavorters();
            final List<CommentItem> commentsDatas = circleItem.getComments();
            boolean hasFavort = circleItem.hasFavort();
            boolean hasComment = circleItem.hasComment();

            Glide.with(context).load(headImg).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.color_eeeeee).transform(new GlideCircleTransform(context)).into(holder.headIv);

            holder.nameTv.setText(name);
            holder.timeTv.setText(createTime);

            if (!TextUtils.isEmpty(content)) {
                holder.contentTv.setText(UrlUtils.formatUrlString(content));
            }
            holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

            if (DatasUtil.curUser.getId().equals(circleItem.getUser().getId())) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
            } else {
                holder.deleteBtn.setVisibility(View.GONE);
            }


            final ProgressDialog progressDialog = new ProgressDialog(context);

            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    progressDialog.dismiss();
                }
            };
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(context);
                    builder.setTitle("亲，您确定要删除吗？");
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressDialog.setMessage("正在删除...");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    //删除
                                    if (presenter != null) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                FormBody body = new FormBody.Builder()
                                                        .add("username", MyApplication.getusername())
                                                        .add("password", MyApplication.getuserpassword())
                                                        .add("id", DatasUtil.getItemdatas().get(circlePosition).getId())
                                                        .build();

                                                Message message = mHandler.obtainMessage();
                                                try {
                                                    String data = HttpDataUtil
                                                            .getPublicData_POST(com.yitong.ChuangKeYuan.utils.DatasUtil.URL_BASE + com.yitong.ChuangKeYuan.utils.DatasUtil.URL_delPublish, body);
                                                    if (!("".equals(data)) && data != null) {
                                                        Log.i("DEL", "删除文章id" + circlePosition + DatasUtil.getItemdatas().get(circlePosition).getId() + " 返回信息" + data);
                                                        Gson gson = new Gson();
                                                        ReturnResult result = gson.fromJson(data, ReturnResult.class);
                                                        if (Integer.parseInt(result.getCode()) == 0) {
                                                            presenter.deleteCircle(circleId);
                                                            DatasUtil.getItemdatas().remove(circlePosition);
                                                            progressDialog.dismiss();
                                                            Looper.prepare();
                                                            ToastUtil.showToast(context, "删除成功");
                                                            Looper.loop();

                                                        } else {
                                                            progressDialog.dismiss();
                                                            Looper.prepare();
                                                            ToastUtil.showToast(context, "对不起，删除失败，请重试...");
                                                            Looper.loop();
                                                        }
                                                    }
                                                } catch (IOException e) {
                                                    progressDialog.dismiss();
                                                    Looper.prepare();
                                                    ToastUtil.showToast(context, "对不起，服务器异常，请求数据未成功...");
                                                    Looper.loop();
                                                    return;
                                                }
                                            }
                                        }).start();
                                    }
                                    dialog.dismiss();
                                }
                            }
                    );
                    builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    );
                    builder.create().show();

                }
            });
            if (hasFavort || hasComment) {
                if (hasFavort) {//处理点赞列表
                    holder.favortListTv.setSpanClickListener(new ISpanClick() {
                        @Override
                        public void onClick(int position) {
//                            String userName = favortDatas.get(position).getUser().getName();
//                            String userId = favortDatas.get(position).getUser().getId();
//                            Toast.makeText(MyApplication.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
                        }
                    });
                    holder.favortListAdapter.setDatas(favortDatas);
                    holder.favortListAdapter.notifyDataSetChanged();
                    holder.favortListTv.setVisibility(View.VISIBLE);
                } else {
                    holder.favortListTv.setVisibility(View.GONE);
                }

                if (hasComment) {//处理评论列表
                    holder.commentList.setOnItemClick(new CommentListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int commentPosition) {
                            CommentItem commentItem = commentsDatas.get(commentPosition);
                            if (DatasUtil.curUser.getId().equals(commentItem.getUser().getId())) {//复制或者删除自己的评论

                                CommentDialog dialog = new CommentDialog(context, presenter, commentItem, circlePosition, commentPosition);
                                dialog.show();
                            } else {//回复别人的评论
                                if (presenter != null) {
                                    CommentConfig config = new CommentConfig();
                                    config.circlePosition = circlePosition;
                                    config.commentPosition = commentPosition;
                                    config.commentType = CommentConfig.Type.REPLY;
                                    config.replyUser = commentItem.getUser();
                                    presenter.showEditTextBody(config);
                                }
                            }
                        }
                    });
                    holder.commentList.setOnItemLongClick(new CommentListView.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(int commentPosition) {
                            //长按进行复制或者删除
                            CommentItem commentItem = commentsDatas.get(commentPosition);
                            CommentDialog dialog = new CommentDialog(context, presenter, commentItem, circlePosition, commentPosition);
                            dialog.show();
                        }
                    });
                    holder.commentAdapter.setDatas(commentsDatas);
                    holder.commentAdapter.notifyDataSetChanged();
                    holder.commentList.setVisibility(View.VISIBLE);

                } else {

                    holder.commentList.setVisibility(View.GONE);
                }
                holder.digCommentBody.setVisibility(View.VISIBLE);
            } else {
                holder.digCommentBody.setVisibility(View.GONE);
            }

            holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

            final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
            //判断是否已点赞
            String curUserFavortId = circleItem.getCurUserFavortId(DatasUtil.curUser.getId());
            if (!TextUtils.isEmpty(curUserFavortId)) {
                snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
            } else {
                snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
            }
            snsPopupWindow.update();
            snsPopupWindow.setmItemClickListener(new PopupItemClickListener(circlePosition, circleItem, curUserFavortId));
            holder.snsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出popupwindow
                    snsPopupWindow.showPopupWindow(view);
                }
            });

            holder.urlTipTv.setVisibility(View.GONE);
            switch (holder.viewType) {
                case TYPE_URL:// 处理链接动态的链接内容和和图片
//                    String linkImg = circleItem.getLinkImg();
//                    String linkTitle = circleItem.getLinkTitle();
//                    Glide.with(context).load(linkImg).into(holder.urlImageIv);
//                    holder.urlContentTv.setText(linkTitle);
//                    holder.urlBody.setVisibility(View.VISIBLE);
//                    holder.urlTipTv.setVisibility(View.VISIBLE);
                    break;
                case TYPE_IMAGE:// 处理图片
                    final List<String> photos = circleItem.getPhotos();
                    if (photos != null && photos.size() > 0) {
                        holder.multiImageView.setVisibility(View.VISIBLE);
                        holder.multiImageView.setList(photos);
                        holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //imagesize是作为loading时的图片size
                                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                                ImagePagerActivity.startImagePagerActivity(context, photos, position, imageSize);
                            }
                        });
                    } else {
                        holder.multiImageView.setVisibility(View.GONE);
                    }
                    break;
                case TYPE_VIDEO:
                    holder.videoView.setVideoUrl(circleItem.getVideoUrl());
                    holder.videoView.setPostion(position);
                    holder.videoView.setOnPlayClickListener(new CircleVideoView.OnPlayClickListener() {
                        @Override
                        public void onPlayClick(int pos) {
                            curPlayIndex = pos;
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;//有head需要加1
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CircleViewHolder extends RecyclerView.ViewHolder implements VideoLoadMvpView {
        public int viewType;

        public ImageView headIv;
        public TextView nameTv;
        public TextView urlTipTv;
        /**
         * 动态的内容
         */
        public MagicTextView contentTv;
        public TextView timeTv;
        public TextView deleteBtn;
        public ImageView snsBtn;
        /**
         * 点赞列表
         */
        public FavortListView favortListTv;

        public LinearLayout urlBody;
        public LinearLayout digCommentBody;
        public View digLine;

        /**
         * 评论列表
         */
        public CommentListView commentList;
        /**
         * 链接的图片
         */
        public ImageView urlImageIv;
        /**
         * 链接的标题
         */
        public TextView urlContentTv;
        /**
         * 图片
         */
        public MultiImageView multiImageView;

        public CircleVideoView videoView;
        // ===========================
        public FavortListAdapter favortListAdapter;
        public CommentAdapter commentAdapter;
        public SnsPopupWindow snsPopupWindow;

        public CircleViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);
            switch (viewType) {
                case TYPE_URL:// 链接view
                    viewStub.setLayoutResource(R.layout.viewstub_urlbody);
                    viewStub.inflate();
//                    LinearLayout urlBodyView = (LinearLayout) itemView.findViewById(R.id.urlBody);
//                    if(urlBodyView != null){
//                        urlBody = urlBodyView;
//                        urlImageIv = (ImageView) itemView.findViewById(R.id.urlImageIv);
//                        urlContentTv = (TextView) itemView.findViewById(R.id.urlContentTv);
//                    }
                    break;
                case TYPE_IMAGE:// 图片view
                    viewStub.setLayoutResource(R.layout.viewstub_imgbody);
                    viewStub.inflate();
                    MultiImageView multiImageView = (MultiImageView) itemView.findViewById(R.id.multiImagView);
                    if (multiImageView != null) {
                        this.multiImageView = multiImageView;
                    }
                    break;
                case TYPE_VIDEO:
                    viewStub.setLayoutResource(R.layout.viewstub_videobody);
                    viewStub.inflate();

                    CircleVideoView videoBody = (CircleVideoView) itemView.findViewById(R.id.videoView);
                    if (videoBody != null) {
                        this.videoView = videoBody;
                    }
                    break;
                default:
                    break;
            }
            headIv = (ImageView) itemView.findViewById(R.id.headIv);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            digLine = itemView.findViewById(R.id.lin_dig);

            contentTv = (MagicTextView) itemView.findViewById(R.id.contentTv);
            urlTipTv = (TextView) itemView.findViewById(R.id.urlTipTv);
            timeTv = (TextView) itemView.findViewById(R.id.timeTv);
            deleteBtn = (TextView) itemView.findViewById(R.id.deleteBtn);
            snsBtn = (ImageView) itemView.findViewById(R.id.snsBtn);
            favortListTv = (FavortListView) itemView.findViewById(R.id.favortListTv);

            digCommentBody = (LinearLayout) itemView.findViewById(R.id.digCommentBody);

            commentList = (CommentListView) itemView.findViewById(R.id.commentList);
            commentAdapter = new CommentAdapter(itemView.getContext());
            favortListAdapter = new FavortListAdapter();

            favortListTv.setAdapter(favortListAdapter);
            commentList.setAdapter(commentAdapter);

            snsPopupWindow = new SnsPopupWindow(itemView.getContext());

        }

        @Override
        public TextureVideoView getVideoView() {
            return null;
        }

        @Override
        public void videoBeginning() {

        }

        @Override
        public void videoStopped() {

        }

        @Override
        public void videoPrepared(MediaPlayer player) {

        }

        @Override
        public void videoResourceReady(String videoPath) {

        }
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private String mFavorId;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private CircleItem mCircleItem;

        public PopupItemClickListener(int circlePosition, CircleItem circleItem, String favorId) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem = circleItem;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (presenter != null) {
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            presenter.addFavort(mCirclePosition);
                        } else {//取消点赞
                            presenter.deleteFavort(mCirclePosition, mFavorId);
                        }
                    }
                    break;
                case 1://发布评论
                    if (presenter != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        presenter.showEditTextBody(config);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
