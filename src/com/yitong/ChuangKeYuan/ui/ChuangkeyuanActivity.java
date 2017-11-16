package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.utils.DatasUtil;

/**
 * Created by Say GoBay on 2016/8/16.
 * 关于创客园
 */
public class ChuangkeyuanActivity extends Activity implements View.OnClickListener{
    private ImageView mBack,mSearch;
    private TextView mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuangkeyuan);
        initTitlebar();
    }

    /**
     * 初始化标题栏以及组件
     */
    private void initTitlebar() {
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mSearch.setVisibility(View.GONE);
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        mTitle.setText(DatasUtil.MY_TEXT[5]);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.iv_teach_back:
               finish();
               break;
       }
    }
}
