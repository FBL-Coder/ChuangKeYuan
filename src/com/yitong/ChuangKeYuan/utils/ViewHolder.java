package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Say GoBay on 2016/5/30.
 * 搜索页面的ViewHolder
 */
public class  ViewHolder {
    private SparseArray<View> mViews;
    private Context mContext;
    private View mConvertView;
    private int mPosition;

    /**
     * 初始化 holder
     */
    public ViewHolder(Context context, int layoutId, ViewGroup parent, int position) {
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mViews = new SparseArray<View>();
        mPosition = position;
        mConvertView.setTag(this);
    }

    /**
     *  获取viewHolder
     */
    public static ViewHolder getHolder(Context context, View convertView,
                                       int layoutId, ViewGroup parent, int position) {
        if(convertView == null){
            return new ViewHolder(context,layoutId,parent,position);
        }else{
            ViewHolder holder = (ViewHolder)convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }
    public View getConvertView(){
        return mConvertView;
    }

    /**
     * 获取View
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T)view;
    }
    /**
     * 设置文字
     */
    public ViewHolder setText(int viewId, String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     *  设置图片resId
     */
    public ViewHolder setImageResource(int viewId,int resId){
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     *  设置图片
     */
    public ViewHolder setImageBitmap(int viewId,Bitmap bitmap){
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }
}
