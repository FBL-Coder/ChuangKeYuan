package com.yitong.ChuangKeYuan.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liangmutian.mypicker.DataPickerDialog;
import com.google.gson.Gson;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ResearchUpData;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 作者：FBL  时间： 2016/9/22.
 * 添加调查问卷页面
 */
public class AddResearchActivity extends Activity implements View.OnClickListener {

    private ImageView back, mSearch, mHelp;
    private TextView title, class_select,authority_select_btn;
    private EditText name;
    private ExpandableListView data_num;
    private Button addResearch, updata;
    private List<ResearchUpData> list;
    private Handler handler;
    private ResearchAdapter adapter;
    private String json;
    private Dialog chooseDialog;
    /**
     * 权限对应数组；
     */
    private String[] power = new String[]{"全部", "园", "组", "班级"};
    private String[][] some = new String[][]{{"全部"}, {"绿叶园"}, {"小班组", "中班组", "大班组"}, {"玫瑰班", "百合班", "蔷薇班", "茉莉班", "绿萝班", "紫藤班", "银杏班", "梧桐班", "豆豆班", "苗苗班", "绿绿班", "芽芽班"}};
    private List<String> list_one = new ArrayList<String>();
    private List<String> list_two_1 = new ArrayList<String>();
    private List<String> list_two_2 = new ArrayList<String>();
    private List<String> list_two_3 = new ArrayList<String>();
    private List<String> list_two_4 = new ArrayList<String>();
    private Context context;

    @Override//URL_addQuestion
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_research);
        //初始化控件
        initView();
        //初始化数据；
        initData();
        //事件
        event();
    }
    /**
     * 初始化数据
     */
    private void initData() {
        adapter = new ResearchAdapter();
        data_num.setAdapter(adapter);
    }

    /**
     * 初始化事件
     */
    private void event() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                list.add((ResearchUpData) msg.obj);
//                adapter.notifyDataSetChanged();
                initData();
            }
        };
    }

    /**
     * 初始化组件
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {

        list = new ArrayList<ResearchUpData>();
        back = (ImageView) findViewById(R.id.iv_teach_back);
        title = (TextView) findViewById(R.id.tv_teach_title);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mHelp = (ImageView) findViewById(R.id.iv_teach_help);
        name = (EditText) findViewById(R.id.research_name);
        data_num = (ExpandableListView) findViewById(R.id.resrarch_data_num);
        addResearch = (Button) findViewById(R.id.add_research_item);
        updata = (Button) findViewById(R.id.add_research_up);
        class_select = (TextView) findViewById(R.id.class_select);
        authority_select_btn = (TextView) findViewById(R.id.authority_select_btn);
        authority_select_btn.setOnClickListener(this);
        title.setText("添加调查问卷");
        mSearch.setVisibility(View.GONE);
        mHelp.setVisibility(View.VISIBLE);
        mHelp.setOnClickListener(this);
        addResearch.setOnClickListener(this);
        back.setOnClickListener(this);
        updata.setOnClickListener(this);
        context = this;

        for (String str : power) {
            list_one.add(str);
        }
        for (int i = 0; i < some[0].length; i++) {
            list_two_1.add(some[0][i]);
        }
        for (int i = 0; i < some[1].length; i++) {
            list_two_2.add(some[1][i]);
        }
        for (int i = 0; i < some[2].length; i++) {
            list_two_3.add(some[2][i]);
        }
        for (int i = 0; i < some[3].length; i++) {
            list_two_4.add(some[3][i]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                finish();
                break;
            case R.id.add_research_item:
                startActivityForResult(new Intent(AddResearchActivity.this, AddResearch_Item.class), 24);
                break;
            case R.id.authority_select_btn:
                showChooseDialog(list_one);
                break;
            case R.id.add_research_up:

                //点击上传，进行数据打包post上传服务器；
                String namestr = name.getText().toString();
                if (namestr != null && !"".equals(namestr)) {
                    if (list.size() > 0) {
                        try {
                            JSONObject object = new JSONObject();
                            JSONArray array = new JSONArray();

                            for (int i = 0; i < list.size(); i++) {
                                JSONObject object1 = new JSONObject();


                                if (list.get(i).getUp() != null) {
                                    for (int j = 0; j < list.get(i).getUp().size(); j++) {
                                        if (j == 0)
                                            object1.put("A", list.get(i).getUp().get(j).getOption());
                                        if (j == 1)
                                            object1.put("B", list.get(i).getUp().get(j).getOption());
                                        if (j == 2)
                                            object1.put("C", list.get(i).getUp().get(j).getOption());
                                        if (j == 3)
                                            object1.put("D", list.get(i).getUp().get(j).getOption());
                                    }
                                    object1.put("num", list.get(i).getUp().size());
                                    object1.put("tit", list.get(i).getQuesition_data());
                                } else {
                                    object1.put("num", 0);
                                    object1.put("tit", list.get(i).getQuesition_data());
                                }
                                array.put(object1);
                            }
                            object.putOpt("data", array);
                            object.put("name", namestr);
                            object.put("pri", class_select.getText());
                            System.out.println(object.toString());
                            json = object.toString();

                            final Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (msg.arg1 == 0) {
                                        ToastUtil.showToast(AddResearchActivity.this, "添加成功");
                                        finish();
                                    } else {
                                        ToastUtil.showToast(AddResearchActivity.this, "添加失败");
                                        return;
                                    }
                                    super.handleMessage(msg);
                                }
                            };

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FormBody body = new FormBody.Builder()
                                            .add("username", MyApplication.getusername())
                                            .add("password", MyApplication.getuserpassword())
                                            .add("jsonData", json)
                                            .build();

                                    String data = HttpDataUtil.getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_addQuestion, body);

                                    if (data!= null && !"".equals(data)){
                                        Log.i("UPDATA",data);
                                    }
                                    Gson gson = new Gson();
                                    ReturnResult result = gson.fromJson(data, ReturnResult.class);

                                    Message message = handler.obtainMessage();
                                    message.arg1 = Integer.parseInt(result.getCode());
                                    handler.sendMessage(message);

                                    } catch (Exception e) {
                                        Log.e("Exception", "AddResearchActivity" + e.toString());
                                        return;
                                    }
                                }
                            }).start();


                    } catch (Exception e) {
                            System.out.println(e);
                            return;
                        }

                        list.clear();
                        name.setText("");
                        initData();
                    }else {
                        ToastUtil.showToast(this, "至少添加一个问题！");
                        return;
                    }
                } else {
                    ToastUtil.showToast(this, "请填写调查问卷名称");
                    return;
                }
                break;
            case R.id.iv_teach_help:
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(context);
                builder.setTitle("操作说明：");
                builder.setMessage("        问卷名称必填；权限可不选，不选默认为全部；至少添加一个题目，点击添加题目进入添加题目页面，题目添加完毕后，点击保存提交则调查问卷上传成功；如果想删除上传的调查问卷，进入问卷详情点击删除按钮即可删除，注意：用户只能删除自己上传的问卷。 ");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
    }

    /**
     * chooseDialog
     * 权限选择
     */
    private void showChooseDialog(List<String> mlist) {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        chooseDialog = builder.setData(mlist).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        class_select.setText(itemValue);
                        if (position == 1)
                            showClassDialog(list_two_2);
                        if (position == 2)
                            showClassDialog(list_two_3);
                        if (position == 3)
                            showClassDialog(list_two_4);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).create();

        chooseDialog.show();
    }
    /**
     * chooseDialog
     * 权限选择
     */
    private void showClassDialog(List<String> mlist) {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        chooseDialog = builder.setData(mlist).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue, int position) {
                        class_select.setText(itemValue);

                    }

                    @Override
                    public void onCancel() {

                    }
                }).create();

        chooseDialog.show();
    }
    /**
     * 添加调查问卷的页面是套页。所以数据时从内层页面穿透过来的额；
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 24 && resultCode == Activity.RESULT_OK) {

            final ResearchUpData upData = (ResearchUpData) data.getSerializableExtra("data");

            Log.i("Result", upData.getQuesition_data());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message mess = handler.obtainMessage();
                    mess.obj = upData;
                    handler.sendMessage(mess);
                }
            }).start();
        }
    }


    /**
     * 问卷的显示适配器；
     *     这个适配器是类似QQ好友分组的数据适配；
     */
    class ResearchAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (list.get(groupPosition).getUp() == null) {
                return 1;
            } else {
                return list.get(groupPosition).getUp().size();
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (list.get(groupPosition).getUp() == null) {
                return "这是个解答题！";
            } else {
                return list.get(groupPosition).getUp().get(childPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            if (list.get(groupPosition).getUp() == null) {
                return 0;
            } else {
                return childPosition;
            }
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AddResearchActivity.this).inflate(R.layout.research_list_item, null);
                holder.research_item_tv = (TextView) convertView.findViewById(R.id.research_item_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.research_item_tv.setText(groupPosition + 1 + " 、 " + list.get(groupPosition).getQuesition_data());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ViewHolder_child holder;
            if (convertView == null) {
                holder = new ViewHolder_child();
                convertView = LayoutInflater.from(AddResearchActivity.this).inflate(R.layout.research_list_item_child, null);
                holder.research_item_tv_cjild = (TextView) convertView.findViewById(R.id.research_item_tv_child);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder_child) convertView.getTag();
            }
            if (list.get(groupPosition).getUp() == null) {
                holder.research_item_tv_cjild.setText("这是个解答题！");
            } else {
                if (childPosition == 0)
                    holder.research_item_tv_cjild.setText("A 、 " + list.get(groupPosition).getUp().get(childPosition).getOption());
                if (childPosition == 1)
                    holder.research_item_tv_cjild.setText("B 、 " + list.get(groupPosition).getUp().get(childPosition).getOption());
                if (childPosition == 2)
                    holder.research_item_tv_cjild.setText("C 、 " + list.get(groupPosition).getUp().get(childPosition).getOption());
                if (childPosition == 3)
                    holder.research_item_tv_cjild.setText("D 、 " + list.get(groupPosition).getUp().get(childPosition).getOption());
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        class ViewHolder {

            TextView research_item_tv;
        }

        class ViewHolder_child {

            TextView research_item_tv_cjild;
        }
    }
    /**
     * 点击EditText之外的区域，键盘消失
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 是否隐藏键盘
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
