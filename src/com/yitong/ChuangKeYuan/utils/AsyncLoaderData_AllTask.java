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

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.adapter.MyTaskAdapter;
import com.yitong.ChuangKeYuan.domain.Task_List;
import com.yitong.ChuangKeYuan.ui.TaskDetailActivity;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/7/13.
 * 所有任务模块；
 */
public class AsyncLoaderData_AllTask extends AsyncTask<String, Integer, Task_List> {

    private SuperSwipeRefreshLayout layout;
    private MyTaskAdapter build;
    private ListView mListView;
    private Context context;
    private int count;
    private FormBody body;
    private String filename;
    private String data;
    private LinearLayout ku_task_yichang, ku_task_nodata;
    private RelativeLayout rl_detail_pro;

    /**
     * 构造器
     * @param context
     * @param build
     * @param rl_detail_pro
     * @param ku_task_nodata
     * @param ku_task_yichang
     * @param listView
     * @param body
     * @param filename
     * @param count
     * @param layout
     */
    public AsyncLoaderData_AllTask(Context context, MyTaskAdapter build, RelativeLayout rl_detail_pro,
                                   LinearLayout ku_task_nodata, LinearLayout ku_task_yichang,
                                   ListView listView, FormBody body, String filename, int count,SuperSwipeRefreshLayout layout) {
        this.context = context;
        this.build = build;
        this.ku_task_yichang = ku_task_yichang;
        this.ku_task_nodata = ku_task_nodata;
        this.body = body;
        this.filename = filename;
        this.count = count;
        this.mListView = listView;
        this.rl_detail_pro = rl_detail_pro;
        this.layout = layout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        rl_detail_pro.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }

    @Override
    protected void onPostExecute(final Task_List result) {
        //刷新界面列表数据
        rl_detail_pro.setVisibility(View.GONE);
        layout.setRefreshing(false);
        if (result != null && !("".equals(result)) && Integer.parseInt(result.getCode()) == 0) {
            mListView.setVisibility(View.VISIBLE);
            ku_task_nodata.setVisibility(View.GONE);
            ku_task_yichang.setVisibility(View.GONE);
            if (build == null) {
                build = new MyTaskAdapter(result, MyApplication.getContext());
                mListView.setAdapter(build);
            } else {
                build.notifyDataSetChanged();
            }
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(context, TaskDetailActivity.class);
                    intent.putExtra("aid", result.getList().get(position).getId());
                    intent.putExtra("type", Integer.parseInt(result.getList().get(position).getIs_work()));
                    intent.putExtra("isover", Integer.parseInt(result.getList().get(position).getIs_overdue()));
                    intent.putExtra("login", result.getList().get(position).getUser_login());

//                    Log.i("AAAAAAA", result.getList().get(position).getId() + "////"
//                            + result.getList().get(position).getIs_work() + "////" +
//                            result.getList().get(position).getIs_overdue());

                    context.startActivity(intent);
                }
            });
        } else if (result != null && Integer.parseInt(result.getCode()) == 10101) {
            ku_task_nodata.setVisibility(View.VISIBLE);
        } else if (result != null && Integer.parseInt(result.getCode()) == 10007) {
            ToastUtil.showToast(context, "对不起，登录时间过长，请重新登录...");
            ku_task_nodata.setVisibility(View.VISIBLE);
        } else {
            ku_task_yichang.setVisibility(View.VISIBLE);
        }
        super.onPostExecute(result);
    }

    @Override
    protected Task_List doInBackground(String... params) {
        try {
            data = HttpDataUtil.getPublicData_POST(params[0], body);
            Log.i("TASK", data);
            if (data == null || "".equals(data)) {
                data = Dtat_Cache.readFile(filename);
            } else {
                Dtat_Cache.writeFileToSD(data, filename);
            }
            Gson gson = new Gson();
            Task_List task_all = gson.fromJson(data, Task_List.class);
            return task_all;
        } catch (Exception e) {
            Log.i("", "" + e);
            return null;
        }
    }
}
