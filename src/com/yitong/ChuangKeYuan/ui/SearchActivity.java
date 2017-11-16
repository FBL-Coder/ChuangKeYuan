package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.adapter.MyItmeAdapter_build;
import com.yitong.ChuangKeYuan.domain.ArticleList;
import com.yitong.ChuangKeYuan.domain.ArticleSearch;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.Progressbar_Util;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.utils.ToastUtil;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Say GoBay on 2016/5/19.
 * 搜索页面
 */
public class SearchActivity extends Activity {
    //搜索结果列表view
    private ListView lvResults;
    private ImageView iv_search_clear, search_btn_back;
    private EditText search_edt;
    private MySearchKeyAdapter Keyadapter;
    private MyItmeAdapter_build searchAdapter;
    private Button btn_search_ok;
    private LinearLayout error_search;
    private RelativeLayout rl_search_pro;
    private ProgressBar pb_search_pro;
    private AsyncSearchKeyword list1;
    private AsyncSearchKeyword list2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //初始化视图
        initViews();

        //初始化数据
//        initData();
    }

    /**
     * 初始化视图
     */
    private void initViews() {

        lvResults = (ListView) findViewById(R.id.lv_search_data);
        iv_search_clear = (ImageView) findViewById(R.id.iv_search_clear);
        search_btn_back = (ImageView) findViewById(R.id.btn_search_back);
        search_edt = (EditText) findViewById(R.id.search_edt);
        btn_search_ok = (Button) findViewById(R.id.btn_search_ok);
        error_search = (LinearLayout) findViewById(R.id.error_search);
        rl_search_pro = (RelativeLayout) findViewById(R.id.rl_search_pro);
        pb_search_pro = (ProgressBar) findViewById(R.id.pb_search_pro);

        Progressbar_Util.ProVisibility(pb_search_pro, this);

        iv_search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list1 = new AsyncSearchKeyword();
                list1.execute();
                search_edt.setText("");
                error_search.setVisibility(View.GONE);
                lvResults.setVisibility(View.VISIBLE);
            }
        });

        search_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list1 != null){
                    list1.cancel(true);
                }
                if (list2 != null){
                    list2.cancel(true);
                }
                finish();
            }
        });

        btn_search_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(search_edt.getText().toString())) {
                    ToastUtil.showToast(SearchActivity.this, "请输入关键字！");
                } else {
                    error_search.setVisibility(View.GONE);
                    lvResults.setVisibility(View.GONE);
                    rl_search_pro.setVisibility(View.VISIBLE);
                    FormBody body = new FormBody.Builder()
                            .add("search", search_edt.getText().toString())
                            .build();
                    new AsyncArticleSearch(body).execute();
                    Log.i("BTN", search_edt.getText().toString());
                }


            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {

        search_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 0) {
                    iv_search_clear.setVisibility(View.VISIBLE);
                } else {
                    iv_search_clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        list2 = new AsyncSearchKeyword();
        list2.execute();
    }

    /**
     * 搜索关键字排行数据
     */

    class AsyncSearchKeyword extends AsyncTask<Void, Void, ArticleSearch> {

        private String data;

        @Override
        protected void onPostExecute(final ArticleSearch articleSearch) {
            if (articleSearch != null && Integer.parseInt(articleSearch.getCode()) == 0) {
//                if (Keyadapter == null) {
                Keyadapter = new MySearchKeyAdapter(articleSearch);
                lvResults.setAdapter(Keyadapter);
//                } else {
//                    Keyadapter.notifyDataSetChanged();
//                }
            }else {
                error_search.setVisibility(View.VISIBLE);
            }
            lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    search_edt.setText(articleSearch.getList().get(position).getKeyword());
                }
            });
            super.onPostExecute(articleSearch);
        }

        @Override
        protected ArticleSearch doInBackground(Void... params) {

            try {
                if (HttpDataUtil.isNetworkConnected(MyApplication.getContext())) {

                    data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_getSearchKeyword, new FormBody.Builder().build());
                    Log.i("HTTPData", "Search-----" + data);

                    Gson gson = new Gson();
                    ArticleSearch search = gson.fromJson(data, ArticleSearch.class);

                    if (search.getCode().equals("0")) {
                    } else {
                        Looper.prepare();
                        ToastUtil.showToast(MyApplication.getContext(), "亲，对不起，服务器异常,请稍后再试！！");
                        Looper.loop();
                    }
                    Log.i("HTTPData", search.getMsg());
                    return search;

                }
            } catch (IOException e) {
                Log.i("HTTPData", "Index_IOException" + e);
            }
            return null;
        }
    }

    /**
     * 搜索数据
     */
    class AsyncArticleSearch extends AsyncTask<Void, Void, ArticleList> {

        private String data;
        private FormBody body;

        public AsyncArticleSearch(FormBody body) {
            this.body = body;
        }

        @Override
        protected void onPostExecute(final ArticleList articleList) {
            rl_search_pro.setVisibility(View.GONE);
            if (articleList != null) {
                lvResults.setVisibility(View.VISIBLE);
                searchAdapter = new MyItmeAdapter_build(articleList, SearchActivity.this, true);
                lvResults.setAdapter(searchAdapter);
                lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SearchActivity.this, ArticleDetailActivity.class);
                        intent.putExtra("aid", articleList.getList().get(position).getId());
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                error_search.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected ArticleList doInBackground(Void... params) {
            ArticleList articleList = null;
            try {
                data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_articleSearch, body);
                Log.i("HTTPData", "Search-----" + data);
                Gson gson = new Gson();
                articleList = gson.fromJson(data, ArticleList.class);

                if (articleList != null && Integer.parseInt(articleList.getCode())== 0) {
                    Log.i("HTTPData", articleList.getMsg() + "----" + articleList.getCode());
                    return articleList;
                }
            } catch (IOException e) {
                Log.i("HTTPData", "Index_IOException" + e);
            }
            return null;
        }
    }

    /**
     * 搜索关键字排行数据适配器
     */
    class MySearchKeyAdapter extends BaseAdapter {

        ArticleSearch articleSearch;

        public MySearchKeyAdapter(ArticleSearch articleSearch) {
            this.articleSearch = articleSearch;
        }

        @Override
        public int getCount() {
            return articleSearch.getList().size();
        }

        @Override
        public Object getItem(int position) {
            return articleSearch.getList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {

                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.search_key, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.search_key_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            int num = position + 1;

            viewHolder.tv.setText("热搜排行榜第" + num + " :" + articleSearch.getList().get(position).getKeyword());

            return convertView;
        }

        class ViewHolder {
            TextView tv;
        }
    }
}
