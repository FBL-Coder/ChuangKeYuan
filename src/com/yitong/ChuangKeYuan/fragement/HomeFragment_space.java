package com.yitong.ChuangKeYuan.fragement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.yitong.ChuangKeYuan.ui.ClassActivity;
import com.yitong.ChuangKeYuan.ui.HomeClassActivity;
import com.yitong.ChuangKeYuan.ui.TaskActivity;
import com.yitong.ChuangKeYuan.ui.WorkRoomItemActivity;
import com.yitong.ChuangKeYuan.utils.UiUtils;

/**
 * 创客空间页面
 */
public class HomeFragment_space extends Fragment implements OnItemClickListener {
    //GridView
    private GridView mGridView;
    private ImageView mTutor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = UiUtils.inflateView(R.layout.fragment_home_space);
        //初始化GridView
        initGridView(view);
        return view;
    }

    /**
     * 初始化GridView
     */
    private void initGridView(View view) {
        mGridView = (GridView) view.findViewById(R.id.gv);
        mTutor = (ImageView) view.findViewById(R.id.iv_tutor);

        try {
            mTutor.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(2).getBanner()));
        }catch (Exception e){
            
        }
        mGridView.setAdapter(new SpaceGridViewAdapter());
        mGridView.setSelector(R.drawable.selector_gridview_item);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            //我的任务单
            case 0:
                startActivity(new Intent(getActivity().getApplicationContext(), TaskActivity.class));
                break;
            //班级小创客
            case 1:
                startActivity(new Intent(getActivity().getApplicationContext(), ClassActivity.class));
                break;
            //家庭工作坊
            case 2:
                startActivity(new Intent(getActivity().getApplicationContext(), HomeClassActivity.class));
                break;
            //创客工作室
            case 3:
                startActivity(new Intent(getActivity().getApplicationContext(), WorkRoomItemActivity.class));
                break;
        }
    }


    /**
     * GirdView适配器
     */
    class SpaceGridViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            try {
                return MyApplication.getUtil().mListBeen.get(2).getParent().size();
            }catch (Exception e){
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            try {
                return MyApplication.getUtil().mListBeen.get(2).getParent().get(position);
            }catch (Exception e){
                return null;
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
                        (getActivity()).inflate(R.layout.fragment_gridview_item, null, false);
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                holder.textView = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (MyApplication.getUtil().mListBeen.get(2).getParent() != null) {
                if (holder.imageView != null) {
                    try {
                        holder.imageView.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(2).getParent().get(position).getSmeta()));
                    }catch (Exception e){
                        holder.imageView.setImageResource(R.drawable.noimage);
                    }
                    holder.textView.setText(MyApplication.getUtil().mListBeen.get(2).getParent().get(position).getName());
                }
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }
}
