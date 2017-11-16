package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.adapter.MyClassItmeAdapter;
import com.yitong.ChuangKeYuan.adapter.MyItmeAdapter_build;
import com.yitong.ChuangKeYuan.domain.ArticleList;
import com.yitong.ChuangKeYuan.ui.ArticleDetailActivity;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/7/13.
 * 次页面数据加载；
 */
public class AsyncLoaderData_List extends AsyncTask<String, Integer, ArticleList> {

    private MyItmeAdapter_build build;
    private MyClassItmeAdapter adapter;
    private boolean ratingBar;
    private ListView mListView;
    private ImageView tit;
    private Context context;
    private int TAB = 0;
    private int count;
    private FormBody body;
    private String filename;
    private String data;
    private LinearLayout ll_nodata, ll_error;
    int id_id = -1;
    long isShow = 0;
    private SuperSwipeRefreshLayout layout;

    /**
     * 构造器
     * @param context 上下文
     * @param build 适配器
     * @param ll_nodata 没数据布局视图
     * @param ll_error 错误布局视图
     * @param listView
     * @param body  网络请求参数
     * @param filename 缓存文件名
     * @param count 刷新int标记
     * @param ratingBar 是否星级显示
     * @param isShow 是否显示
     * @param layout 布局
     */
    public AsyncLoaderData_List(Context context, MyItmeAdapter_build build, LinearLayout ll_nodata,
                                LinearLayout ll_error, ListView listView, FormBody body, String filename,
                                int count, boolean ratingBar, long isShow,SuperSwipeRefreshLayout layout) {

        this.context = context;
        this.ll_nodata = ll_nodata;
        this.ll_error = ll_error;
        this.build = build;
        this.body = body;
        this.filename = filename;
        this.count = count;
        this.ratingBar = ratingBar;
        this.mListView = listView;
        this.isShow = isShow;
        this.layout = layout;
    }


    /**
     * 构造器
     * @param context 上下文
     * @param build 适配器
     * @param ll_nodata 没数据布局视图
     * @param ll_error 错误布局视图
     * @param listView
     * @param body  网络请求参数
     * @param filename 缓存文件名
     * @param count 刷新int标记
     * @param ratingBar 是否星级显示
     * @param layout 布局
     */
    public AsyncLoaderData_List(Context context, MyItmeAdapter_build build, LinearLayout ll_nodata,
                                LinearLayout ll_error, ListView listView, FormBody body, String filename,
                                int count, boolean ratingBar,SuperSwipeRefreshLayout layout) {

        this.context = context;
        this.ll_nodata = ll_nodata;
        this.ll_error = ll_error;
        this.build = build;
        this.body = body;
        this.filename = filename;
        this.count = count;
        this.ratingBar = ratingBar;
        this.mListView = listView;
        this.layout = layout;
    }

    /**
     * 构造器
     * @param context 上下文
     * @param build 适配器
     * @param ll_nodata 没数据布局视图
     * @param ll_error 错误布局视图
     * @param listView
     * @param body  网络请求参数
     * @param filename 缓存文件名
     * @param count 刷新int标记
     * @param ratingBar 是否星级显示
     * @param  id 文章id
     * @param layout 布局
     */
    public AsyncLoaderData_List(Context context, MyItmeAdapter_build build, LinearLayout ll_nodata,
                                LinearLayout ll_error, ListView listView, FormBody body, String filename,
                                int count, boolean ratingBar, int id,SuperSwipeRefreshLayout layout) {
        this.context = context;
        this.ll_nodata = ll_nodata;
        this.ll_error = ll_error;
        this.build = build;
        this.body = body;
        this.filename = filename;
        this.count = count;
        this.mListView = listView;
        this.ratingBar = ratingBar;
        this.id_id = id;
        this.layout = layout;
    }
    /**
     * 构造器
     * @param context 上下文
     * @param ll_nodata 没数据布局视图
     * @param ll_error 错误布局视图
     * @param listView
     * @param body  网络请求参数
     * @param filename 缓存文件名
     * @param count 刷新int标记
     * @param ratingBar 是否星级显示
     * @param TAB int标记
     * @param layout 布局
     */
    public AsyncLoaderData_List(Context context, MyClassItmeAdapter adapter,
                                LinearLayout ll_nodata, LinearLayout ll_error, ListView listView,
                                ImageView tit, FormBody body, String filename, int count, boolean ratingBar,
                                int TAB,SuperSwipeRefreshLayout layout) {
        this.tit = tit;
        this.context = context;
        this.ll_nodata = ll_nodata;
        this.ll_error = ll_error;
        this.adapter = adapter;
        this.body = body;
        this.filename = filename;
        this.count = count;
        this.ratingBar = ratingBar;
        this.TAB = TAB;
        this.mListView = listView;
        this.layout = layout;
    }

    @Override
    protected void onPostExecute(final ArticleList result) {
        //刷新界面列表数据
        Log.i("TAG", TAB + "");
        layout.setRefreshing(false);
        if (result != null && !("".equals(result)) && Integer.parseInt(result.getCode()) == 0) {//判断数据是否加载成功
            mListView.setVisibility(View.VISIBLE);
            ll_nodata.setVisibility(View.GONE);
            ll_error.setVisibility(View.GONE);

            if (TAB == -1) {//根据标记判断跳转
                if (adapter == null) {
                    adapter = new MyClassItmeAdapter(result, tit, MyApplication.getContext(), ratingBar);
                    mListView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, ArticleDetailActivity.class);

                        intent.putExtra("aid", result.getList().get(position).getId());
                        intent.putExtra("author", result.getList().get(position).getAuthor());
                        Log.i("Listid", "ListPostion" + position + "");
                        context.startActivity(intent);
                    }
                });
            } else {
                if (build == null) {
                    build = new MyItmeAdapter_build(result, MyApplication.getContext(), ratingBar, id_id);
                    mListView.setAdapter(build);
                } else {
                    build.notifyDataSetChanged();
                }
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, ArticleDetailActivity.class);
                        intent.putExtra("isShow", isShow);
                        intent.putExtra("aid", result.getList().get(position).getId());
                        intent.putExtra("author", result.getList().get(position).getAuthor());
                        if (id_id != -1 && (id_id == 4 || id_id == 5 || id_id == 6)) {
                            intent.putExtra("isBuild", true);
                            intent.putExtra("jianshe",true);
                        }else if (id_id != -1 && id_id == 28) {
                                intent.putExtra("video",true);
                        }
                        Log.i("Listid", "ListPostion" + position + "");
                        context.startActivity(intent);
                    }
                });
            }
        } else if (result != null && Integer.parseInt(result.getCode()) == 10101) {//数据为空
            mListView.setVisibility(View.GONE);
            ll_nodata.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
            ll_error.setVisibility(View.VISIBLE);
        }
        super.onPostExecute(result);
    }

    @Override
    protected ArticleList doInBackground(String... params) {
        try {
            data = HttpDataUtil.getPublicData_POST(params[0], body);
            if (filename != null && !("".equals(filename))) {
                if (data == null | "".equals(data)) {
                    data = Dtat_Cache.readFile(filename);
                } else {
                    Dtat_Cache.writeFileToSD(data, filename);
                }
            }

        } catch (Exception e) {
            return null;
        }
        if (data != null && !("".equals(data))) {
            Log.i("TAG", data);
            Gson gson = new Gson();
            ArticleList articleList = gson.fromJson(data, ArticleList.class);
            return articleList;
        }
        return null;
    }
}
