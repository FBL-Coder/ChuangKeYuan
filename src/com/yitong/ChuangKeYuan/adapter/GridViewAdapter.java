package com.yitong.ChuangKeYuan.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.Recommend;

import java.util.ArrayList;

/**
 * Created by Say GoBay on 2016/6/3.
 * 精彩推荐页面的GridView适配器
 */
public class GridViewAdapter extends BaseAdapter {
    /**
     * 页面上下文；
     */
    private Context mContext;
    /**
     * 数据源
     */
    private ArrayList<Recommend> mList;

    /**
     * 构造器
     * @param context
     * @param list
     */
    public GridViewAdapter(Context context, ArrayList<Recommend> list) {
        super();
        this.mContext = context;
        this.mList = list;
    }
    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return this.mList.size();
        }
    }
    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.gridview_item, null, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.gridview_item_button);
            holder.textView = (TextView) convertView.findViewById(R.id.gridview_item_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (this.mList != null) {
            if (holder.imageView != null) {
                holder.imageView.setImageURI(Uri.parse(mList.get(position).getImage()));
                holder.textView.setText(mList.get(position).getTitle());
            }
        }
        return convertView;
    }
    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
