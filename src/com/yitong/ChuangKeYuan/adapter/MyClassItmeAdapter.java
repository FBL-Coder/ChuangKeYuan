package com.yitong.ChuangKeYuan.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ArticleList;

/**
 * 作者：FBL  时间： 2016/7/14.
 * 班级小创客GridView适配器；
 */
public class MyClassItmeAdapter extends BaseAdapter {

    /**
     * 数据源
     */
    ArticleList mList;
    /**
     * 页面上下文
     */
    Context mContext;
    /**
     * 是否有星级评价
     */
    boolean mRatingBar;
    /**
     * 评论图标
     */
    ImageView tit;

    /**
     * 构造器
     *
     * @param list
     * @param tit
     * @param context
     * @param ratingBar
     */
    public MyClassItmeAdapter(ArticleList list, ImageView tit, Context context, boolean ratingBar) {
        this.tit = tit;
        mList = list;
        mContext = context;
        mRatingBar = ratingBar;
    }

    @Override
    public int getCount() {
        if (mList == null || mList.getList() == null)
            return 0;
        return mList.getList().size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null || mList.getList() == null)
            return null;
        return mList.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_build_item_item_item, null);
            //得到各个控件的对象
            holder.image = (ImageView) convertView.findViewById(R.id.iv_teach_class_listview);
            holder.title = (TextView) convertView.findViewById(R.id.tv_teach_class_listview);
            holder.hits = (TextView) convertView.findViewById(R.id.detail_hits);
            holder.comments = (TextView) convertView.findViewById(R.id.detail_comments);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_class_ratingbar);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.class_zonghe);
            //绑定ViewHolder对象
            convertView.setTag(holder);
        } else {
            //取出ViewHolder对象
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            //星级显示，mRatingBar，是个boolean值；
            if (mRatingBar) {
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.ratingBar.setRating(Float.parseFloat(mList.getList().get(position).getStar()));
            }
            Log.i("TAG", mList.getList().get(position).getTitle());

            if (mList.getBanner() != null) {
                tit.setImageURI(Uri.parse(mList.getBanner()));
            } else {
                tit.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(2).getParent().get(1).getParent_ParentBean().get(position).getBanner()));
            }
            //设置TextView显示的内容，即我们存放在动态数组中的数据
            holder.title.setText(mList.getList().get(position).getTitle());
            holder.hits.setText(mList.getList().get(position).getHits());
            holder.comments.setText(mList.getList().get(position).getComment_count());
            //设置ImageView图片资源
            holder.image.setImageURI(Uri.parse(mList.getList().get(position).getThumb()));
        } catch (Exception e) {
            Log.e("Exception", e + "");
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView image;
        LinearLayout linearLayout;
        public TextView title, hits, comments;
        RatingBar ratingBar;
    }
}
