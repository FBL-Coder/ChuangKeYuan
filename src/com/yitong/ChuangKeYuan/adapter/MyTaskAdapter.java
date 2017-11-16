package com.yitong.ChuangKeYuan.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.Task_List;

/**
 * 作者：FBL  时间： 2016/5/25.
 * 我的任务适配器
 */
public class MyTaskAdapter extends BaseAdapter {

    /**
     * 数据源
     */
    private Task_List task_all;
    /**
     * 页面上下文
     */
    private Context mContext;

    /**
     * 构造器
     * @param task_all
     * @param context
     */
    public MyTaskAdapter(Task_List task_all, Context context) {
        this.task_all = task_all;
        mContext = context;
    }
    @Override
    public int getCount() {
        if (task_all == null)
            return 0;
        return task_all.getList().size();
    }

    @Override
    public Object getItem(int position) {
        if (task_all == null)
            return null;
        return task_all.getList().get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mytask_item, null);
            //得到各个控件的对象
            holder.image = (ImageView) convertView.findViewById(R.id.iv_mytask_item);
            holder.title = (TextView) convertView.findViewById(R.id.tv_mytask_title);
            holder.classid = (TextView) convertView.findViewById(R.id.tv_mytask_classid);
            holder.tv_task_label = (TextView) convertView.findViewById(R.id.tv_task_label);
            //绑定ViewHolder对象
            convertView.setTag(holder);
        } else {
            //取出ViewHolder对象
            holder = (ViewHolder) convertView.getTag();
        }

        //设置TextView显示的内容，即我们存放在动态数组中的数据
        holder.title.setText(task_all.getList().get(position).getTitle());
        //设置ImageView图片资源
        holder.image.setImageURI(Uri.parse(task_all.getList().get(position).getThumb()));

        if (Integer.parseInt(task_all.getList().get(position).getIs_overdue()) == 0){
            if (Integer.parseInt(task_all.getList().get(position).getIs_work()) == 0){
                holder.tv_task_label.setBackgroundResource(R.drawable.task_textbg_red);
                holder.tv_task_label.setText("未完成");
            }else {
                holder.tv_task_label.setBackgroundResource(R.drawable.task_textbg_red);
                holder.tv_task_label.setText("已完成");
            }
        }else {
            holder.tv_task_label.setBackgroundResource(R.drawable.task_textbg_blue);
            holder.tv_task_label.setText("已过期");
        }
        holder.classid.setText("参与班级 :"+task_all.getList().get(position).getApp_auth_txt());
        return convertView;
    }
    class ViewHolder {
        public ImageView image;
        public TextView title,classid,tv_task_label;
    }
}
