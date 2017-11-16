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
import com.yitong.ChuangKeYuan.domain.Collect;

/**
 * 作者：Fbl  时间： 2016/5/25.
 * 教学视频，日程安排等页面的适配器；
 */
public class MyItmeAdapter extends BaseAdapter {


    /**
     * 数据源
     */
    Collect collect;
    /**
     * 页面上下文；
     */
    Context mContext;
    /**
     * 是否显示星级评论
     */
    boolean mRatingBar;

    /**
     * 构造器
     *
     * @param collect
     * @param context
     * @param ratingBar
     */
    public MyItmeAdapter(Collect collect, Context context, boolean ratingBar) {
        this.collect = collect;
        mContext = context;
        mRatingBar = ratingBar;
    }

    @Override
    public int getCount() {
        if (collect.getList() != null && collect.getList().size() > 0) {
            return collect.getList().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (collect.getList() != null && collect.getList().size() > 0) {
            return collect.getList().get(position);
        } else {
            return null;
        }
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
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_ratingbar);
            holder.ll_hits_commnts = (LinearLayout) convertView.findViewById(R.id.ll_hits_commnts);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.zonghe);
            //绑定ViewHolder对象
            convertView.setTag(holder);
        } else {
            //取出ViewHolder对象
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ll_hits_commnts.setVisibility(View.GONE);
        holder.time.setVisibility(View.VISIBLE);

        if (mRatingBar) {
            holder.linearLayout.setVisibility(View.VISIBLE);
        }

        if (collect.getList().size() > 0) {
            holder.time.setText("收藏时间 : \n" + collect.getList().get(position).getCreatetime());
            //设置TextView显示的内容，即我们存放在动态数组中的数据
            holder.title.setText(collect.getList().get(position).getTitle());
            //设置ImageView图片资源
            holder.image.setImageURI(Uri.parse(collect.getList().get(position).getThumb()));
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView image;
        LinearLayout linearLayout, ll_hits_commnts;
        public TextView title, time;
        RatingBar ratingBar;
    }
}
