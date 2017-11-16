package com.yitong.ChuangKeYuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.CommentBean;

/**
 * Author：FBL  Time： 2017/9/12.
 * 综合评价适配器
 */

public class CommentActivityAdapter extends BaseAdapter {

    private CommentBean mBean;
    private Context mContext;


    public CommentActivityAdapter(CommentBean bean, Context context) {
        mBean = bean;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mBean.getList().size();
    }

    @Override
    public Object getItem(int i) {
        return mBean.getList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.comment_list_item, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else viewHolder = (ViewHolder) view.getTag();

        viewHolder.mCommentItemContent.setText("    " + mBean.getList().get(i).getGood() + "\n    " + mBean.getList().get(i).getBad());
        viewHolder.mCommentItemTime.setText(mBean.getList().get(i).getTitle());
        viewHolder.mCommentItemNum.setText(mBean.getList().get(i).getLevel());

        return view;
    }


    class ViewHolder {
        public ViewHolder(View view) {
            mCommentItemContent = (TextView) view.findViewById(R.id.comment_item_content);
            mCommentItemNum = (TextView) view.findViewById(R.id.comment_item_num);
            mCommentItemTime = (TextView) view.findViewById(R.id.comment_item_time);
        }

        TextView mCommentItemContent;
        TextView mCommentItemNum;
        TextView mCommentItemTime;
    }
}
