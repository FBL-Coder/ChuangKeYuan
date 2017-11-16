package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.ResearchUpData;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：FBL  时间： 2016/9/22.
 * 添加调查问卷的子项；
 *
 */
public class AddResearch_Item extends Activity implements View.OnClickListener {

    private TextView title;
    private ImageView back, mSearch, mHelp;
    private LinearLayout select_show, select_answer, answer_show;
    private RadioGroup add_item_answer_type;
    private RadioButton add_item_select, add_item_explain;
    private EditText select_question, edit_answer;
    private Button delete_answer, add_answer, add_answer_save, add_answer_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_research_item);

        initView();

        event();
    }

    /**
     * 初始化事件
     */
    private void event() {

        add_item_answer_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {//根据点击判断显示内容
                    case R.id.add_item_select://选择题
                        answer_show.setVisibility(View.GONE);
                        select_show.setVisibility(View.VISIBLE);
                        delete_answer.setOnClickListener(AddResearch_Item.this);
                        add_answer.setOnClickListener(AddResearch_Item.this);
                        break;
                    case R.id.add_item_explain://解答题
                        select_show.setVisibility(View.GONE);
                        answer_show.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    /**
     * 初始化组件
     */
    private void initView() {
        title = (TextView) findViewById(R.id.tv_teach_title);
        back = (ImageView) findViewById(R.id.iv_teach_back);
        mSearch = (ImageView) findViewById(R.id.iv_teach_search);
        mHelp = (ImageView) findViewById(R.id.iv_teach_help);
        select_show = (LinearLayout) findViewById(R.id.select_show);
        select_answer = (LinearLayout) findViewById(R.id.select_answer);
        answer_show = (LinearLayout) findViewById(R.id.answer_show);
        add_item_answer_type = (RadioGroup) findViewById(R.id.add_item_answer_type);
        add_item_select = (RadioButton) findViewById(R.id.add_item_select);
        add_item_explain = (RadioButton) findViewById(R.id.add_item_explain);
        select_question = (EditText) findViewById(R.id.select_question);
        edit_answer = (EditText) findViewById(R.id.edit_answer);
        delete_answer = (Button) findViewById(R.id.delete_answer);
        add_answer = (Button) findViewById(R.id.add_answer);
        add_answer_save = (Button) findViewById(R.id.add_answer_save);
        add_answer_cancel = (Button) findViewById(R.id.add_answer_cancel);
        title.setText("添加调查问卷题目");
        mSearch.setVisibility(View.GONE);
        mHelp.setVisibility(View.VISIBLE);
        mHelp.setOnClickListener(this);

        add_answer_save.setOnClickListener(AddResearch_Item.this);
        add_answer_cancel.setOnClickListener(AddResearch_Item.this);
        back.setOnClickListener(AddResearch_Item.this);
        list_et = new ArrayList<EditText>();
        list_ll = new ArrayList<LinearLayout>();
    }

    List<EditText> list_et;
    List<LinearLayout> list_ll;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_answer:
                if (list_ll.size() < 4) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//定义一个LayoutParams
                    layoutParams.setMargins(10, 5, 10, 5);//4个参数按顺序分别是左上右下
                    TextView tv = new TextView(this);
                    tv.setText("请输入选项 : ");
                    tv.setTextSize(16);
                    tv.setLayoutParams(layoutParams);
                    EditText et = new EditText(this);
                    et.setTextSize(16);
                    et.setLayoutParams(layoutParams);
                    et.setBackgroundResource(R.drawable.rect_edittext);
                    list_et.add(et);
                    LinearLayout ll = new LinearLayout(this);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.addView(tv);
                    ll.addView(et);
                    list_ll.add(ll);
                    select_answer.addView(ll);
                } else {
                    ToastUtil.showToast(this, "只能有4个选项");
                }
                break;
            case R.id.delete_answer:
                if (list_ll.size() > 0) {
                    select_answer.removeViewAt(list_ll.size() - 1);
                    list_ll.remove(list_ll.size() - 1);
                    list_et.remove(list_et.size() - 1);
                } else {
                    ToastUtil.showToast(this, "已经没有啦");
                }
                break;
            case R.id.add_answer_save:
                ResearchUpData data = new ResearchUpData();

                if (select_show.getVisibility() == View.VISIBLE) {
                    String question = select_question.getText().toString();
                    if (question != null && !"".equals(question)) {
                        if (list_et.size() >= 2) {

                            List<ResearchUpData.Quesition_up> list_up = new ArrayList<ResearchUpData.Quesition_up>();

                            for (int i = 0; i < list_et.size(); i++) {
                                String answer = list_et.get(i).getText().toString();
                                if (answer != null && !"".equals(answer)) {
                                    ResearchUpData.Quesition_up up = data.new Quesition_up();
                                    up.setOption(list_et.get(i).getText().toString());
                                    list_up.add(up);
                                } else {
                                    ToastUtil.showToast(this, "请把添加的答案填写完整");
                                    return;
                                }
                            }

                            data.setQuesition_data(question);
                            data.setUp(list_up);

                        } else {
                            ToastUtil.showToast(this, "请添加答案选项");
                            return;
                        }
                    } else {
                        ToastUtil.showToast(this, "请填写问题题目");
                        return;
                    }

                } else if (answer_show.getVisibility() == View.VISIBLE) {
                    data.setQuesition_data(edit_answer.getText().toString());
                } else {
                    ToastUtil.showToast(this, "请选择问卷类型");
                    return;
                }

                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);
                intent.putExtras(bundle);
                // 设置该Activity结果码，并设置结束之后退回的Activity
                setResult(Activity.RESULT_OK, intent);
                // 结束Activity
                finish();

                break;
            case R.id.add_answer_cancel:
                finish();
                break;
            case R.id.iv_teach_back:
                finish();
                break;
            case R.id.iv_teach_help:
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(AddResearch_Item.this);
                builder.setTitle("操作说明：");
                builder.setMessage("    题目类型必选，为选择题或作答题中的一种。\n    选择添加选择题，您依次输入问题，添加答案选项，如果您对答案不满意，可点击删除答案选项，最多只能添加4个答案选项，点击保存则添加题目成功，取消则返回上一页。");
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
