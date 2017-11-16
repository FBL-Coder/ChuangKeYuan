package com.yitong.ChuangKeYuan.fragement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ResearchData;
import com.yitong.ChuangKeYuan.ui.AddResearchActivity;
import com.yitong.ChuangKeYuan.ui.ResearchDetailActivity;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Dtat_Cache;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.yitong.ChuangKeYuan.utils.UiUtils;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * 调查问卷页面
 */
public class HomeFragment_research extends Fragment {

    /**
     * 定义的动态listview
     */
    private ListView mListView;
    private SimpleDraweeView sdv_teacherlist;
    /**
     * OKhttp  post上传所需参数；
     */
    private FormBody body;
    /**
     * 数据适配器；
     */
    private AsyncResearchData_List.MyResearchAdapter_build build;
    private ProgressBar mProgressBar;
    private ScrollView scrollView;
    /**
     * android 自带刷新控件；
     */
    private SuperSwipeRefreshLayout layout;
    /**
     * 自定义进度条；
     */
    private View headerView;
    private int count = 0;
    private View view;
    /**
     * 页面数据架子啊异常以及没有数据显示的默认资源
     */
    private LinearLayout ku_research_yichang, ku_research_nodata;
    private ImageView research_add;
    /**
     * 缓存文件命名；
     */
    private String filename = "ResearchData";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = UiUtils.inflateView(R.layout.fragment_home_research);
        //初始化ListView
        initView();
        //初始化标题栏
        initTitlebar();
        //初始化数据
        return view;
    }

    @Override
    public void onStart() {
        initData();
        super.onStart();
    }

    /**
     * 初始化ListView
     */
    private void initView() {
        mListView = (ListView) view.findViewById(R.id.lv_research);
        scrollView = (ScrollView) view.findViewById(R.id.scrollview_research);
        ku_research_yichang = (LinearLayout) view.findViewById(R.id.ku_research_yichang);
        ku_research_nodata = (LinearLayout) view.findViewById(R.id.ku_research_nodata);
        research_add = (ImageView) view.findViewById(R.id.research_add);
        //身份识别
        if ((MyApplication.getuserType() != null && "3".equals(MyApplication.getuserType()))
                | (MyApplication.getuserType() != null &&"1".equals(MyApplication.getuserType()))) {
            research_add.setVisibility(View.VISIBLE);
            research_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), AddResearchActivity.class));
                }
            });
        }

        //自定义进度条；
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.refreshhead, null);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.loading_process);
        Progressbar_Util.ProVisibility(mProgressBar, getActivity());

        //下拉组件初始化 并设置下拉事件
        layout = (SuperSwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout_research);
        layout.setHeaderView(headerView);
        layout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

            @Override
            public void onRefresh() {
                //TODO 开始刷新
                count = 0;
                initData();
                count++;
                layout.setRefreshing(false);
            }

            @Override
            public void onPullDistance(int distance) {
                //TODO 下拉距离
            }

            @Override
            public void onPullEnable(boolean enable) {
                //TODO 下拉过程中，下拉的距离是否足够出发刷新
            }
        });
        layout.setTargetScrollWithLayout(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        if (MyApplication.getToKen() == null || "".equals(MyApplication.getToKen())) {
            body = new FormBody.Builder().build();
        } else {
            body = new FormBody.Builder()
                    .add("username", MyApplication.getusername())
                    .add("password", MyApplication.getuserpassword())
                    .build();
        }
        new AsyncResearchData_List().execute(DatasUtil.URL_BASE + DatasUtil.URL_getQuestion);

        count += 5;
    }

    /**
     * 初始化标题栏
     */
    private void initTitlebar() {
        sdv_teacherlist = (SimpleDraweeView) view.findViewById(R.id.research_iv);

    }

    /**
     * 异步下载数据
     */
    public class AsyncResearchData_List extends AsyncTask<String, Integer, ResearchData> {

        int TAB = 0;

        @Override
        protected void onPostExecute(final ResearchData result) {
            //刷新界面列表数据
            Log.i("TAG", TAB + "");

            if (result != null && Integer.parseInt(result.getCode()) == 0) {
                ku_research_nodata.setVisibility(View.GONE);
                ku_research_yichang.setVisibility(View.GONE);

                build = new MyResearchAdapter_build(result, MyApplication.getContext());
                mListView.setAdapter(build);
                sdv_teacherlist.setVisibility(View.VISIBLE);
                try {
                    sdv_teacherlist.setImageURI(Uri.parse(MyApplication.getUtil().mListBeen.get(5).getBanner()));
                }catch (Exception e){}

            } else if (result != null && Integer.parseInt(result.getCode()) == 10101) {
                Log.i("TAG", "没有数据  返回码是 10101");
                ku_research_nodata.setVisibility(View.VISIBLE);
            } else {
                ku_research_yichang.setVisibility(View.VISIBLE);
            }
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), ResearchDetailActivity.class);
                    intent.putExtra("position", position);
                    Log.i("Listid", "ListPostion" + position + "");
                    getActivity().startActivity(intent);
                }
            });

            super.onPostExecute(result);
        }

        @Override
        protected ResearchData doInBackground(String... params) {
            String data = null;
            try {
                data = HttpDataUtil.getPublicData_POST(params[0], body);
                if (data == null || "".equals(data)) {
                    data = Dtat_Cache.readFile(filename);
                } else {
                    Dtat_Cache.writeFileToSD(data, filename);
                }
                if (data != null && !"".equals(data)) {
                    Log.i("TAG", data);
                }
            } catch (Exception e) {
                return null;
            }
            Gson gson = new Gson();
            ResearchData teactherList = gson.fromJson(data, ResearchData.class);
            return teactherList;
        }

        /**
         * 适配器
         */
        public class MyResearchAdapter_build extends BaseAdapter {

            ResearchData teactherList;
            Context mContext;

            public MyResearchAdapter_build(ResearchData teactherList, Context context) {
                this.teactherList = teactherList;
                mContext = context;
            }

            @Override
            public int getCount() {
                return teactherList.getList().size();
            }

            @Override
            public Object getItem(int position) {
                return teactherList.getList().get(position);
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
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_research_item, null);
                    //得到各个控件的对象
                    holder.name = (TextView) convertView.findViewById(R.id.research_name);
                    holder.man = (TextView) convertView.findViewById(R.id.research_man);
                    holder.time = (TextView) convertView.findViewById(R.id.research_time);
                    holder.pri = (TextView) convertView.findViewById(R.id.research_pri);
                    //绑定ViewHolder对象
                    convertView.setTag(holder);
                } else {
                    //取出ViewHolder对象
                    holder = (ViewHolder) convertView.getTag();
                }
                //设置TextView显示的内容，即我们存放在动态数组中的数据
                holder.name.setText(teactherList.getList().get(position).getName());
                holder.man.setText(teactherList.getList().get(position).getAuthor());
                holder.pri.setText(teactherList.getList().get(position).getPri());
                holder.time.setText(teactherList.getList().get(position).getCreatetime());
                return convertView;
            }

            class ViewHolder {
                public TextView name, time, man, pri;
            }
        }
    }
}
