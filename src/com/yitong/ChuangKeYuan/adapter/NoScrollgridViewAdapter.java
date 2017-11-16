package com.yitong.ChuangKeYuan.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yitong.ChuangKeYuan.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hankkin on 2015/6/30.
 */
@SuppressLint("HandlerLeak")
public class NoScrollgridViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private boolean shape;
    private List<String> listImage;

    public boolean isShape() {
        return shape;
    }

    private Activity context;

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public NoScrollgridViewAdapter(Activity context, List<String> listUrl_result) {
        this.context = context;
        listImage = new ArrayList<String>();
        for (int i = 0; i < listUrl_result.size(); i++) {
            listImage.add(listUrl_result.get(i));
        }
        if (listImage.size() < 9)
            listImage.add(null);

        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (listImage == null) return 0;
        return listImage.size();
    }

    public Object getItem(int arg0) {
        return 0;
    }

    public void notifyDataSetChanged(List<String> listUrl_result) {
        listImage = new ArrayList<String>();
        for (int i = 0; i < listUrl_result.size(); i++) {
            listImage.add(listUrl_result.get(i));
        }
        if (listUrl_result.size() < 9)
            listImage.add(null);
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged_imageok(List<String> listUrl_result) {
        listImage = new ArrayList<String>();
        for (int i = 0; i < listUrl_result.size(); i++) {
            listImage.add(listUrl_result.get(i));
        }
        super.notifyDataSetChanged();
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alltask_up_item,
                    parent, false);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.alltask_up_item_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (listImage.get(position) == null || "".equals(listImage.get(position))) {
            holder.image.setImageResource(R.drawable.em_smiley_add_btn_nor);
        } else if ("video".equals(listImage.get(position))) {
            holder.image.setImageResource(R.drawable.video_up_task);
        } else {
            holder.image.setImageURI(Uri.parse(listImage.get(position)));
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }
}
