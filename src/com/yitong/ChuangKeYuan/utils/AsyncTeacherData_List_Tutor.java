
package com.yitong.ChuangKeYuan.utils;

        import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
        import android.text.TextUtils;
        import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ClassTeacherlist;
import com.yitong.ChuangKeYuan.ui.TeactherDetail;
import com.yitong.ChuangKeYuan.widget.SuperSwipeRefreshLayout;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/7/13.
 * 老师数据加载；
 */
public class AsyncTeacherData_List_Tutor extends AsyncTask<String, Integer, ClassTeacherlist> {

    private MyTeacherAdapter_build build;
    private ListView mListView;
    private Context context;
    private int TAB = 0;
    private int count;
    private FormBody body;
    private String filename;
    private String data;
    private RelativeLayout rl_detail_pro;
    private SuperSwipeRefreshLayout RefreshLayout;
    private LinearLayout ll_nodata, ll_error;

    /**
     *
     * @param context 上下文
     * @param build 适配器
     * @param ll_nodata 没数据布局视图
     * @param ll_error 错误布局视图
     * @param listView
     * @param layout
     * @param RefreshLayout 可刷新布局视图
     * @param body 网络请求参数
     * @param filename 缓存文件名
     * @param count
     */
    public AsyncTeacherData_List_Tutor(Context context, MyTeacherAdapter_build build,LinearLayout ll_nodata, LinearLayout ll_error, ListView listView,RelativeLayout layout,SuperSwipeRefreshLayout RefreshLayout, FormBody body, String filename, int count) {

        this.context = context;
        this.build = build;
        this.ll_nodata = ll_nodata;
        this.ll_error = ll_error;
        this.body = body;
        this.filename = filename;
        this.count = count;
        this.RefreshLayout = RefreshLayout;
        this.mListView = listView;
        rl_detail_pro = layout;
    }

    @Override
    protected void onPreExecute() {
        rl_detail_pro.setVisibility(View.VISIBLE);
        RefreshLayout.setVisibility(View.GONE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final ClassTeacherlist result) {
        //刷新界面列表数据
        Log.i("TAG", TAB + "");
        rl_detail_pro.setVisibility(View.GONE);
        RefreshLayout.setVisibility(View.VISIBLE);
        RefreshLayout.setRefreshing(false);
        if (result != null && !"".equals(result) && Integer.parseInt(result.getCode()) == 0) {//判断数据是否加载成功
            mListView.setVisibility(View.VISIBLE);
            ll_nodata.setVisibility(View.GONE);
            ll_error.setVisibility(View.GONE);
            if (build == null) {
                build = new MyTeacherAdapter_build(result, MyApplication.getContext());
                mListView.setAdapter(build);
            } else {
                build.notifyDataSetChanged();
            }
        } else if (result != null && Integer.parseInt(result.getCode()) == 10101) { //数据为空
            Log.i("TAG", "没有数据  返回码是 10101");
            mListView.setVisibility(View.GONE);
            ll_nodata.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
            ll_error.setVisibility(View.VISIBLE);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,TeactherDetail.class);
                intent.putExtra("id",result.getList().get(position).getId());
                Log.i("Listid", "ListPostion" + position + "");
                context.startActivity(intent);
            }
        });
        super.onPostExecute(result);
    }

    @Override
    protected ClassTeacherlist doInBackground(String... params) {
        try {
            if (count == 0) {
                data = HttpDataUtil.getPublicData_POST(params[0], body);
                if (filename != null | !"".equals(filename)) {
                    if (data == null) {
                        data = Dtat_Cache.readFile(filename);
                    } else {
                        Dtat_Cache.writeFileToSD(data, filename);
                    }
                }
            } else {
                if (filename != null | !"".equals(filename)) {
                    if (HttpDataUtil.fileIsExists(filename)) {
                        data = Dtat_Cache.readFile(filename);
                    } else {
                        data = HttpDataUtil.getPublicData_POST(params[0], body);
                        Dtat_Cache.writeFileToSD(data, filename);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        Log.i("TAG", data);
        Gson gson = new Gson();
        ClassTeacherlist DaoShiList = gson.fromJson(data, ClassTeacherlist.class);
        return DaoShiList;
    }


    /**
     * 老师数据适配器
     */
    public class MyTeacherAdapter_build extends BaseAdapter {

        private ClassTeacherlist DaoShiList;
        private Context mContext;

        public MyTeacherAdapter_build(ClassTeacherlist DaoShiList, Context context) {
            this.DaoShiList = DaoShiList;
            mContext = context;
        }

        @Override
        public int getCount() {
            return DaoShiList.getList().size();
        }

        @Override
        public Object getItem(int position) {
            return DaoShiList.getList().get(position);
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
                convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.activity_build_item_item, null);
                //得到各个控件的对象
                holder.image = (ImageView) convertView.findViewById(R.id.iv_teach_listview);
                holder.title = (TextView) convertView.findViewById(R.id.tv_teach_listview);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.ll_hits_commnts = (LinearLayout) convertView.findViewById(R.id.ll_hits_commnts);
                //绑定ViewHolder对象
                convertView.setTag(holder);
            } else {
                //取出ViewHolder对象
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ll_hits_commnts.setVisibility(View.GONE);
            //设置TextView显示的内容，即我们存放在动态数组中的数据
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(DaoShiList.getList().get(position).getUser_nicename());
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setTextSize(12);
            holder.time.setText(DaoShiList.getList().get(position).getSignature());
            holder.time.setLines(2);
            holder.time.setTextColor(0xff808080);
            holder.time.setEllipsize(TextUtils.TruncateAt.END);
            //设置ImageView图片资源
            holder.image.setImageURI(Uri.parse(DaoShiList.getList().get(position).getThumb()));
            return convertView;
        }

        class ViewHolder {
            public ImageView image;
            public TextView title,time;
            public LinearLayout ll_hits_commnts;
        }
    }

}
