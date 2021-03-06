package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.DataPublicBean_Qita;
import com.yitong.ChuangKeYuan.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Say GoBay on 2016/5/24.
 * 家庭工作坊
 */
public class HomeClassActivity extends Activity implements OnClickListener {

    public TextView mDes;
    public ImageView mBack;
    public ImageView mSearch;
    private NoScrollGridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        //初始化控件
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //标题栏
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mDes = (TextView) findViewById(R.id.tv_teach_title);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        gridView = (NoScrollGridView) findViewById(R.id.gridview_class);
        mDes.setText("班级分类");
        mBack.setOnClickListener(this);
        mSearch.setVisibility(View.GONE);

        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(new GridViewAdapterClass());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeClassActivity.this, HomeAddWorkActivity.class);
                intent.putExtra("classname", MyApplication.getUtil().mListBeen.get(2).getParent().get(2).getParent_ParentBean().get(position).getName());
                intent.putExtra("termid", MyApplication.getUtil().mListBeen.get(2).getParent().get(2).getParent_ParentBean().get(position).getTerm_id());
                intent.putExtra("IsHome", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                finish();
                break;
        }
    }

    /**
     * Gridview适配器
     */
    class GridViewAdapterClass extends BaseAdapter {

        List<DataPublicBean_Qita.ListBean.ParentBean.Parent_ParentBean> mList;

        public GridViewAdapterClass() {
            try {
                mList = MyApplication.getUtil().mListBeen.get(2).getParent().get(2).getParent_ParentBean();
            } catch (Exception e) {
                mList = new ArrayList<DataPublicBean_Qita.ListBean.ParentBean.Parent_ParentBean>();
            }
        }

        @Override
        public int getCount() {
            if (mList == null)
                return 0;
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            if (mList == null)
                return null;
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoider viewHoider;
            if (convertView == null) {
                viewHoider = new ViewHoider();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.gridview_item_class, null);
                viewHoider.sdv = (SimpleDraweeView) convertView.findViewById(R.id.sdv_gv_item_class);
                convertView.setTag(viewHoider);
            } else {
                viewHoider = (ViewHoider) convertView.getTag();
            }
            viewHoider.sdv.setImageURI(mList.get(position).getSmeta());
            return convertView;
        }

        class ViewHoider {
            SimpleDraweeView sdv;
        }
    }
}
