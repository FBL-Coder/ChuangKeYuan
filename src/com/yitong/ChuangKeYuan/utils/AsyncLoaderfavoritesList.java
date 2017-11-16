package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.adapter.MyItmeAdapter;
import com.yitong.ChuangKeYuan.domain.Collect;
import com.yitong.ChuangKeYuan.ui.ArticleDetailActivity;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/7/7.
 * 异步加载收藏列表接口数据
 */
public class AsyncLoaderfavoritesList extends AsyncTask<String, Void, Collect> {

    private MyItmeAdapter build;
    private ListView mListView;
    private Context context;
    private LinearLayout ku_collect;
    private LinearLayout ku_collect_yichang;
    private RelativeLayout rl_detail_pro;
    private int count;
    private String data;

    /**
     *
     * @param context 上下文
     * @param build 适配器
     * @param listView
     * @param ku_collect 没数据布局视图
     * @param rl_detail_pro 进度布局视图
     * @param ku_collect_yichang 异常布局视图
     * @param count int标记
     */
    public AsyncLoaderfavoritesList(Context context, MyItmeAdapter build, ListView listView
            , LinearLayout ku_collect, RelativeLayout rl_detail_pro, LinearLayout ku_collect_yichang, int count) {
        this.context = context;
        this.build = build;
        this.count = count;
        this.ku_collect = ku_collect;
        this.ku_collect_yichang = ku_collect_yichang;
        this.rl_detail_pro = rl_detail_pro;
        this.mListView = listView;
    }

    @Override
    protected void onPreExecute() {
        rl_detail_pro.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final Collect collect) {
        rl_detail_pro.setVisibility(View.GONE);
        if (collect != null && !"".equals(collect) && Integer.parseInt(collect.getCode()) == 0) {//判断数据是否加载成功
            if (build == null) {
                build = new MyItmeAdapter(collect, context, false);
                mListView.setAdapter(build);
            } else {
                build.notifyDataSetChanged();
            }
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(context, ArticleDetailActivity.class);
                    intent.putExtra("aid", collect.getList().get(position).getArticle_id());
                    context.startActivity(intent);
                }
            });
        } else if (collect != null && Integer.parseInt(collect.getCode()) == 10101) {//数据为空
            mListView.setVisibility(View.GONE);
            ku_collect.setVisibility(View.VISIBLE);
        } else if (collect != null && Integer.parseInt(collect.getCode()) == 10007) {
            mListView.setVisibility(View.GONE);
            ku_collect_yichang.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
            ku_collect_yichang.setVisibility(View.VISIBLE);
        }
        super.onPostExecute(collect);
    }

    @Override
    protected Collect doInBackground(String... params) {
        Collect collect = null;
        try {
            if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {
                data = HttpDataUtil.getPublicData_POST(params[0], new FormBody.Builder().add("token", MyApplication.getToKen()).build());

                data = HttpDataUtil.getPublicData_POST(params[0], new FormBody.Builder()
                        .add("username", MyApplication.getusername())
                        .add("password", MyApplication.getuserpassword())
                        .add("pagesize","10000")
                        .build());
                if (data != null && !"".equals(data)) {
                    Dtat_Cache.writeFileToSD(data, "Data_Shoucang");
                }
            } else {
                Log.i("HTTPData", "没网，傻啊，网打开..");
            }
            if (data == null | "".equals(data)) {
                data = Dtat_Cache.readFile("Data_Shoucang");
            }
            Log.i("HTTPData", "Collect-----" + data);
            if (data != null && !"".equals(data)) {
                Log.i("HTTPData", "Collect-----" + data);
            }
            Gson gson = new Gson();
            collect = gson.fromJson(data, Collect.class);
        } catch (Exception e) {
            Log.i("HTTPData", "Index_IOException" + e);
            return null;
        }
        return collect;
    }
}
