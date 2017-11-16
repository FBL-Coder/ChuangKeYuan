package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.yitong.ChuangKeYuan.ui.ClassTeactherDetail;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/7/13.
 * 次页面数据加载；
 */
public class AsyncTeacherData_List extends AsyncTask<String, Integer, ClassTeacherlist> {

    private MyTeacherAdapter_build build;
    private ListView mListView;
    private Context context;
    private int TAB = 0;
    private int count;
    private FormBody body;
    private String filename;
    private String data;
    private RelativeLayout rl_detail_pro;

    /**
     *
     * @param context 上下文
     * @param build 适配器
     * @param listView
     * @param layout 布局
     * @param body 网络请求参数
     * @param filename 缓存文件名
     * @param count int标记
     */
    public AsyncTeacherData_List(Context context, MyTeacherAdapter_build build, ListView listView,RelativeLayout layout,
                                 FormBody body, String filename, int count) {

        this.context = context;
        this.build = build;
        this.body = body;
        this.filename = filename;
        this.count = count;
        this.mListView = listView;
        rl_detail_pro = layout;
    }

    @Override
    protected void onPreExecute() {
        rl_detail_pro.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final ClassTeacherlist result) {
        //刷新界面列表数据
        Log.i("TAG", TAB + "");
        rl_detail_pro.setVisibility(View.GONE);
        if (result != null && !"".equals(result) && Integer.parseInt(result.getCode()) == 0) {

            if (build == null) {
                build = new MyTeacherAdapter_build(result, MyApplication.getContext());
                mListView.setAdapter(build);
            } else {
                build.notifyDataSetChanged();
            }


        } else if (result != null && Integer.parseInt(result.getCode()) == 10101) {//  数据为空
            Log.i("TAG", "没有数据  返回码是 10101");
            ToastUtil.showToast(context, "亲，没有找到相应数据...");
        } else {
            ToastUtil.showToast(context, "对不起，服务器异常，请求数据未成功...");
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,ClassTeactherDetail.class);
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
            holder.title.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(DaoShiList.getList().get(position).getUser_nicename());
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
