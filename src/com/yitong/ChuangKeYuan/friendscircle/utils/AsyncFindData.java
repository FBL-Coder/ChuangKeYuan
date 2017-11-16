package com.yitong.ChuangKeYuan.friendscircle.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.friendscircle.adapter.CircleAdapter;
import com.yitong.ChuangKeYuan.friendscircle.bean.CircleItem;
import com.yitong.ChuangKeYuan.friendscircle.bean.FindData;
import com.yitong.ChuangKeYuan.friendscircle.bean.User;
import com.yitong.ChuangKeYuan.friendscircle.mvp.presenter.CirclePresenter;
import com.yitong.ChuangKeYuan.utils.Dtat_Cache;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;

/**
 * 作者：傅博龍  时间： 2016/7/26.
 */
public class AsyncFindData extends AsyncTask<String, Integer, FindData> {


    private Context context;
    private CircleAdapter mAdapter;
    private CirclePresenter mPresenter;
    private RelativeLayout rl_find_pro, bodyLayout;
    private SuperRecyclerView recyclerView;
    private GetCircleItemListener getFindDataListener;
    private int loadType;
    private String data;
    public User curUser;
    private FormBody boby;


    public AsyncFindData(Context context, CirclePresenter mPresenter, CircleAdapter mAdapter
            , RelativeLayout rl_find_pro, RelativeLayout bodyLayout, SuperRecyclerView recyclerView, FormBody boby, int loadType) {

        this.context = context;
        this.mAdapter = mAdapter;
        this.mPresenter = mPresenter;
        this.rl_find_pro = rl_find_pro;
        this.recyclerView = recyclerView;
        this.loadType = loadType;
        this.boby = boby;
        this.bodyLayout = bodyLayout;

    }

    public void setGetFindDataListener(GetCircleItemListener getFindData) {
        getFindDataListener = getFindData;
    }


    @Override
    protected void onPostExecute(final FindData result) {
        //刷新界面列表数据

        if (result != null && !"".equals(result) && Integer.parseInt(result.getCode()) == 0) {
            Log.i("FIND", "发现数据长度" + result.getList().size() + "");
            getFindDataListener.getCircleItem(result.getList());
            getFindDataListener.getFindData(result);
            bodyLayout.setVisibility(View.VISIBLE);
            rl_find_pro.setVisibility(View.GONE);
            List<CircleItem> Itemdatas = DatasUtil.createCircleDatas(result);
            if (mAdapter == null) {
                mAdapter = new CircleAdapter(context);
                recyclerView.setAdapter(mAdapter);
            }
            if (loadType == 1) {
                mAdapter.setDatas(Itemdatas);

                Log.i("FIND", "下拉刷新中");

            } else if (loadType == 2) {
                mAdapter.getDatas().addAll(Itemdatas);
                Log.i("FIND", "上拉加载中");
            }
            mAdapter.notifyDataSetChanged();

        } else if (result != null && Integer.parseInt(result.getCode()) == 10101) {
            //对不起，暂无数据
            ToastUtil.showToast(context, "亲，没有更多数据了...");
        } else {
            ToastUtil.showToast(context, "对不起，服务器异常，请求数据未成功...");
        }
        super.onPostExecute(result);
    }

    @Override
    protected FindData doInBackground(String... params) {

        try {
            Log.i("TIME", ".......");
            data = HttpDataUtil.getPublicData_POST(params[0], boby);
            Log.i("TIME", ".......");
            if (data != null && !"".equals(data)) {
                Log.i("FIND", data);
                Dtat_Cache.writeFileToSD(data, "Data_Find");
            } else {
                data = Dtat_Cache.readFile("Data_Find");
            }
            Gson gson = new Gson();
            FindData item = gson.fromJson(data, FindData.class);

            return item;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface GetCircleItemListener {
        void getCircleItem(List<CircleItem> items);

        void getFindData(FindData data);
    }

}
