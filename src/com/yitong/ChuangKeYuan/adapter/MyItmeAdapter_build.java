package com.yitong.ChuangKeYuan.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ArticleList;

/**
 * 作者：FBL  时间： 2016/5/25.
 *
 * 多个布局通用适配器；
 */
public class MyItmeAdapter_build extends BaseAdapter {

    /**
     * 数据源
     */
    private ArticleList articleList;
    /**
     * 页面上下文
     */
    private Context mContext;
    /**
     * 是否显示星级评论
     */
    boolean mRatingBar;
    /**
     * 用来区分建设方案的文章列表，假设方案列表不显示评论图标；以及详情没有评论按钮；
     */
    private int ID = -1;

    /**
     * 构造器
     * @param articleList
     * @param context
     * @param ratingBar
     */
    public MyItmeAdapter_build(ArticleList articleList, Context context, boolean ratingBar) {
        this.articleList = articleList;
        mContext = context;
        mRatingBar = ratingBar;
    }

    public MyItmeAdapter_build(ArticleList articleList, Context context, boolean ratingBar, int ID) {
        this.articleList = articleList;
        mContext = context;
        mRatingBar = ratingBar;
        this.ID = ID;
    }

    @Override
    public int getCount() {
        if (articleList == null)
            return 0;
        return articleList.getList().size();
    }

    @Override
    public Object getItem(int position) {
        if (articleList == null)
            return null;
        return articleList.getList().get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_build_item_item, null);
            //得到各个控件的对象
            holder.image = (ImageView) convertView.findViewById(R.id.iv_teach_listview);
            holder.title = (TextView) convertView.findViewById(R.id.tv_teach_listview);
            holder.hits = (TextView) convertView.findViewById(R.id.detail_hits);
            holder.comments = (TextView) convertView.findViewById(R.id.detail_comments);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_ratingbar);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.zonghe);
            holder.comment_iv = (ImageView) convertView.findViewById(R.id.comment_iv);
            //绑定ViewHolder对象
            convertView.setTag(holder);
        } else {
            //取出ViewHolder对象
            holder = (ViewHolder) convertView.getTag();
        }

        //星级显示，mRatingBar，是个boolean值；
        if (mRatingBar) {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(Float.parseFloat(articleList.getList().get(position).getStar()));
        }

        //设置TextView显示的内容，即我们存放在动态数组中的数据
        holder.title.setText(articleList.getList().get(position).getTitle());
        holder.hits.setText(articleList.getList().get(position).getHits());
        holder.comments.setText(articleList.getList().get(position).getComment_count());
        //设置ImageView图片资源
        holder.image.setImageURI(Uri.parse(articleList.getList().get(position).getThumb()));
        if (ID != -1 && (ID == 4 || ID == 5 || ID == 6)) {
            holder.comment_iv.setVisibility(View.GONE);
            holder.comments.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView image, comment_iv;
        LinearLayout linearLayout;
        public TextView title, hits, comments;
        RatingBar ratingBar;
    }
}
