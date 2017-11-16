package com.yitong.ChuangKeYuan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.Recommend;
import com.yitong.ChuangKeYuan.ui.ParentsDetail;
import com.yitong.ChuangKeYuan.ui.TeactherDetail;
import com.yitong.ChuangKeYuan.ui.ArticleDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Say GoBay on 2016/6/3.
 * 精彩推荐页面的ListView适配器；
 */
public class ListViewAdapter extends BaseAdapter {
    /**
     * 数据源
     */
    private List<ArrayList<Recommend>> mList;
    /**
     * 页面上下文
     */
    private Context mContext;
    /**
     * 类型表题
     */
    private String[] title = {"建设方案", "创客导师", "创客空间", "创客做中学", "调查问卷"};

    /**
     * 构造器
     * @param list
     * @param context
     */
    public ListViewAdapter(ArrayList<ArrayList<Recommend>> list, Context context) {
        this.mList = list;
        mContext = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.listview_item, null, false);
            holder.textView = (TextView) convertView.findViewById(R.id.listview_item_textview);
            holder.gridView = (GridView) convertView.findViewById(R.id.listview_item_gridview);
            holder.imageView = (ImageView) convertView.findViewById(R.id.listview_item_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (this.mList != null) {
            if (holder.textView != null) {
                holder.textView.setText(title[position]);
            }
            if (holder.gridView != null) {
                GridViewAdapter gridViewAdapter = new GridViewAdapter(mContext, mList.get(position));
                holder.gridView.setAdapter(gridViewAdapter);
                //给gridview设置状态选择器
                holder.gridView.setSelector(R.drawable.selector_gridview_item);

                final int Position = position;
                holder.gridView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        switch (Position) {
                            case 0:
                                intent.setClass(mContext, ArticleDetailActivity.class);
                                intent.putExtra("aid", MyApplication.getUtil().Been_index.getJianshe().get(position).getId());
                                intent.putExtra("isBuild", true);
                                break;
                            case 1:
                                switch (position) {
                                    case 0:
                                        intent.setClass(mContext, TeactherDetail.class);
                                        break;
                                    case 1:
                                        intent.setClass(mContext, ParentsDetail.class);
                                        break;
                                }
                                intent.putExtra("id", MyApplication.getUtil().Been_index.getDaoshi().get(position).getId());
                                break;
                            case 2:
                                intent.setClass(mContext, ArticleDetailActivity.class);
                                intent.putExtra("aid", MyApplication.getUtil().Been_index.getKongjian().get(position).getId());
                                break;
                            case 3:
                                intent.setClass(mContext, ArticleDetailActivity.class);
                                intent.putExtra("aid", MyApplication.getUtil().Been_index.getZuozhongxue().get(position).getId());
                                break;
                        }
                        mContext.startActivity(intent);
                    }
                });
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        GridView gridView;
        ImageView imageView;
    }
}
