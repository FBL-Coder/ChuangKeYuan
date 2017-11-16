package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.Answer;
import com.yitong.ChuangKeYuan.domain.Page;
import com.yitong.ChuangKeYuan.domain.Quesition;
import com.yitong.ChuangKeYuan.domain.ResearchData;
import com.yitong.ChuangKeYuan.domain.ReturnResult;
import com.yitong.ChuangKeYuan.utils.DatasUtil;
import com.yitong.ChuangKeYuan.utils.Dtat_Cache;
import com.yitong.ChuangKeYuan.utils.HttpDataUtil;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.CustomDialog_comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 调查问卷详情页面
 */
public class ResearchDetailActivity extends Activity implements View.OnClickListener {
    private LinearLayout test_layout;
    private Page the_page;
    /**
     * 答案列表
     */
    private ArrayList<Answer> the_answer_list;
    /**
     * 问题列表
     */
    private ArrayList<Quesition> the_quesition_list;
    /**
     * 问题所在的View
     */
    private View que_view;
    /**
     * 答案所在的View
     */
    private View ans_view;
    private LayoutInflater xInflater;
    private RelativeLayout ku_research;
    private LinearLayout ll_research_data;
    private Page page;
    /**
     * 下面这两个list是为了实现点击的时候改变图片，因为单选多选时情况不一样，为了方便控制
     * 存每个问题下的imageview
     */
    private ArrayList<ArrayList<ImageView>> imglist = new ArrayList<ArrayList<ImageView>>();
    /**
     * 存每个答案的imageview
     */
    private ArrayList<ImageView> imglist2;
    private ImageView mBack, iv_teach_search, delete, mHelp;
    private TextView mTitle;
    private WebView webView_research;
    private AsybcLoaderResearchData list;
    private String filename = "ResearchData";
    private int position;
    private List<EditText> AskAnswers;
    private ResearchData researchData;
    private ProgressDialog progressDialog;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        xInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //假数据
        initDate();
        //提交按钮
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        webView_research = (WebView) findViewById(R.id.webView_research);
        ll_research_data = (LinearLayout) findViewById(R.id.ll_research_data);
        ll_research_data.setVisibility(View.GONE);

        position = getIntent().getIntExtra("position", -1);
        AskAnswers = new ArrayList<EditText>();
        ku_research = (RelativeLayout) findViewById(R.id.ku_research);
        mBack = (ImageView) findViewById(R.id.iv_teach_back);
        iv_teach_search = (ImageView) findViewById(R.id.iv_teach_search);
//        mHelp = (ImageView) findViewById(R.id.iv_teach_help);
        delete = (ImageView) findViewById(R.id.iv_teach_cancel);
        mTitle = (TextView) findViewById(R.id.tv_teach_title);
        button = (Button) findViewById(R.id.submit);

        WebSettings webSettings = webView_research.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setUseWideViewPort(false);//将图片调整到适合webview的大小
        mBack.setOnClickListener(this);

        iv_teach_search.setVisibility(View.GONE);
//        mHelp.setVisibility(View.VISIBLE);
//        mHelp.setOnClickListener(this);
        list = new AsybcLoaderResearchData();
        list.execute();

    }

    /**
     * 数据格式化
     * @param aVoid
     */
    public void setData(ResearchData aVoid) {

        //假数据
        // TODO Auto-generated method stub

        ArrayList<Quesition> quesitions = new ArrayList<Quesition>();
        for (int i = 0; i < aVoid.getList().get(position).getQuestions().size(); i++) {
            Quesition q_one = new Quesition();

            ArrayList<Answer> answers_one = new ArrayList<Answer>();
            for (int j = 0; j < aVoid.getList().get(position).getQuestions().get(i).getItems().size(); j++) {
                Answer a_one = new Answer();

                if (j == 0) {
                    a_one.setAnswerId("A");
                    a_one.setAnswer_content(String.valueOf(aVoid.getList().get(position).getQuestions().get(i).getItems().get(j).getA()));
                }
                if (j == 1) {
                    a_one.setAnswerId("B");
                    a_one.setAnswer_content(String.valueOf(aVoid.getList().get(position).getQuestions().get(i).getItems().get(j).getB()));
                }
                if (j == 2) {
                    a_one.setAnswerId("C");
                    a_one.setAnswer_content(String.valueOf(aVoid.getList().get(position).getQuestions().get(i).getItems().get(j).getC()));
                }
                if (j == 3) {
                    a_one.setAnswerId("D");
                    a_one.setAnswer_content(String.valueOf(aVoid.getList().get(position).getQuestions().get(i).getItems().get(j).getD()));
                }
                a_one.setAns_state(0);
                answers_one.add(a_one);
            }

            q_one.setAnswers(answers_one);
            q_one.setQuesitionId(aVoid.getList().get(position).getQuestions().get(i).getId());
            q_one.setContent(i + 1 + "、 " + aVoid.getList().get(position).getQuestions().get(i).getQuestion());
            q_one.setQue_state(0);
            if (aVoid.getList().get(position).getQuestions().get(i).getItems().size() < 2) {
                q_one.setIsAsk(true);
            } else {
                q_one.setIsAsk(false);
            }
            quesitions.add(q_one);
        }

        page = new Page();
        page.setPageId(aVoid.getList().get(position).getId());
        page.setStatus("0");
        page.setTitle(aVoid.getList().get(position).getName());
        page.setQuesitions(quesitions);
        //加载布局
        initView(page);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_teach_back:
                if (list != null) {
                    list.cancel(true);
                }
                finish();
        }
    }

    /**
     * 异步加载数据
     */
    class AsybcLoaderResearchData extends AsyncTask<Void, Void, ResearchData> {
        String data;

        @Override
        protected void onPostExecute(ResearchData aVoid) {
            if (aVoid != null && Integer.parseInt(aVoid.getCode()) == 0) {
                researchData = aVoid;
                mTitle.setText(aVoid.getList().get(position).getName());
                ku_research.setVisibility(View.GONE);
                String URL = aVoid.getList().get(position).getContent_url();

                //  如果webview有值  就直接进入WEView
                //  最好用用户类型和是否是上传者即  loginname  和usertype  来判断；

                if (URL != null && "".equals(URL)) {
                    ll_research_data.setVisibility(View.VISIBLE);
                    setData(aVoid);
                } else {
                    webView_research.setVisibility(View.VISIBLE);
                    webView_research.loadUrl(aVoid.getList().get(position).getContent_url());
                    //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
                    webView_research.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            // TODO Auto-generated method stub
                            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                            view.loadUrl(url);
                            return true;
                        }
                    });
                }

                if (aVoid.getList().get(position).getLoginname().equalsIgnoreCase(MyApplication.getusername())
                        | (MyApplication.getuserType() != null && "1".equals(MyApplication.getuserType()))) {
                    if (!(MyApplication.getuserType() != null && "1".equals(MyApplication.getuserType()))) {
                        button.setVisibility(View.GONE);
                    }
                    progressDialog = new ProgressDialog(ResearchDetailActivity.this);
                    delete.setVisibility(View.VISIBLE);
                    final Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            ReturnResult result = (ReturnResult) msg.obj;
                            if (result.getCode() != null &&
                                    !"".equals(result.getCode()) &&
                                    Integer.parseInt(result.getCode()) == 0) {
                                progressDialog.dismiss();
                                ToastUtil.showToast(ResearchDetailActivity.this, "删除成功");
                                finish();
                            } else {
                                ToastUtil.showToast(ResearchDetailActivity.this, "删除失败，请重试...");
                            }
                            super.handleMessage(msg);
                        }
                    };
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(ResearchDetailActivity.this);
                            builder.setTitle("亲，您确定要删除吗？");
                            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressDialog.setMessage("删除中...");
                                            progressDialog.show();
                                            progressDialog.setCancelable(false);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    FormBody body = new FormBody.Builder()
                                                            .add("username", MyApplication.getusername())
                                                            .add("password", MyApplication.getuserpassword())
                                                            .add("id", researchData.getList().get(position).getId())
                                                            .build();
                                                    try {
                                                        String data = HttpDataUtil
                                                                .getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_delAnswer, body);
                                                        ReturnResult result = new Gson().fromJson(data, ReturnResult.class);
                                                        if (data != null && !"".equals(data)) {
                                                            Log.i("DEL", getIntent().getStringExtra("aid") + data);
                                                        }
                                                        Message message = mHandler.obtainMessage();
                                                        message.obj = result;
                                                        mHandler.sendMessage(message);
                                                    } catch (Exception e) {
                                                        return;
                                                    }
                                                }
                                            }).start();
                                            dialog.dismiss();
                                        }
                                    }
                            );
                            builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }
                            );
                            builder.create().show();
                        }
                    });
                }
            } else if (aVoid != null && Integer.parseInt(aVoid.getCode()) == 10101) {
                ll_research_data.setVisibility(View.GONE);
                ku_research.setVisibility(View.VISIBLE);
                TextView textView = (TextView) ku_research.findViewById(R.id.ku_tv);
                textView.setText("对不起，没有找到相应数据...");
            } else {
                ll_research_data.setVisibility(View.GONE);
                ku_research.setVisibility(View.VISIBLE);

            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected ResearchData doInBackground(Void... params) {
            try {
                data = Dtat_Cache.readFile(filename);

                if (data != null && !"".equals(data)) {
                    Log.i("HTTPData", "ResearchData" + data);
                }
            } catch (Exception e) {
                Log.i("HTTPData", "Qita_IOException" + e);
                return null;
            }
            Gson gson = new Gson();
            ResearchData researchData = gson.fromJson(data, ResearchData.class);
            return researchData;
        }
    }

    /**
     * 初始化组件
     * @param page
     */
    private void initView(Page page) {
        // TODO Auto-generated method stub

        button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new submitOnClickListener(page));
        //这是要把问题的动态布局加入的布局
        test_layout = (LinearLayout) findViewById(R.id.lly_test);
        //获得问题即第二层的数据
        the_quesition_list = page.getQuesitions();
        //根据第二层问题的多少，来动态加载布局
        for (int i = 0; i < the_quesition_list.size(); i++) {
            que_view = xInflater.inflate(R.layout.quesition_layout, null);
            TextView txt_que = (TextView) que_view.findViewById(R.id.txt_question_item);
            //这是第三层布局要加入的地方
            LinearLayout add_layout = (LinearLayout) que_view.findViewById(R.id.lly_answer);
            //判断单选-多选来实现后面是*号还是*多选，
            set(txt_que, the_quesition_list.get(i).getContent(), 1);
            //获得答案即第三层数据
            the_answer_list = the_quesition_list.get(i).getAnswers();
            imglist2 = new ArrayList<ImageView>();

            if (the_answer_list.size() >= 2) {
                for (int j = 0; j < the_answer_list.size(); j++) {
                    ans_view = xInflater.inflate(R.layout.answer_layout, null);
                    TextView txt_ans = (TextView) ans_view.findViewById(R.id.txt_answer_item);
                    ImageView image = (ImageView) ans_view.findViewById(R.id.image);
                    View line_view = ans_view.findViewById(R.id.vw_line);
                    if (j == the_answer_list.size() - 1) {
                        //最后一条答案下面不要线是指布局的问题
                        line_view.setVisibility(View.GONE);
                    }
                    //判断单选多选加载不同选项图片
                    image.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_box));
                    Log.e("---", "------" + image);
                    imglist2.add(image);
                    txt_ans.setText(the_answer_list.get(j).getAnswer_content());
                    LinearLayout lly_answer_size = (LinearLayout) ans_view.findViewById(R.id.lly_answer_size);
                    lly_answer_size.setOnClickListener(new answerItemOnClickListener(i, j, the_answer_list, txt_ans));
                    add_layout.addView(ans_view);
                }
            } else {
                ans_view = xInflater.inflate(R.layout.answer_layout_ed, null);
                EditText editText = (EditText) ans_view.findViewById(R.id.answer_ed);
                editText.setTag(i + 1);
                AskAnswers.add(editText);
                add_layout.addView(ans_view);
            }
            imglist.add(imglist2);

            test_layout.addView(que_view);
        }
    }

    /**
     * 给定组件设置值
     * @param tv_test
     * @param content
     * @param type
     */
    private void set(TextView tv_test, String content, int type) {

        // TODO Auto-generated method stub
        String w;
        w = content;

        int start = content.length();
        int end = w.length();
        Spannable word = new SpannableString(w);
        word.setSpan(new AbsoluteSizeSpan(25), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        word.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        word.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv_test.setText(word);
    }

    /**
     * 自定义点条目击事件
     */
    class answerItemOnClickListener implements View.OnClickListener {
        private int i;
        private int j;
        private TextView txt;
        private ArrayList<Answer> the_answer_lists;

        public answerItemOnClickListener(int i, int j, ArrayList<Answer> the_answer_list, TextView text) {
            this.i = i;
            this.j = j;
            this.the_answer_lists = the_answer_list;
            this.txt = text;

        }

        //实现点击选项后改变选中状态以及对应图片
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //判断当前问题是单选还是多选
            //单选

            for (int z = 0; z < the_answer_lists.size(); z++) {
                the_answer_lists.get(z).setAns_state(0);
                imglist.get(i).get(z).setBackgroundDrawable(getResources().getDrawable(R.drawable.select_box));
            }
            if (the_answer_lists.get(j).getAns_state() == 0) {
                //如果当前未被选中
                imglist.get(i).get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.select));
                the_answer_lists.get(j).setAns_state(1);
                the_quesition_list.get(i).setQue_state(1);
            } else {
                //如果当前已被选中
                the_answer_lists.get(j).setAns_state(1);
                the_quesition_list.get(i).setQue_state(1);
            }
//            }
            //判断当前选项是否选中
        }
    }

    class submitOnClickListener implements View.OnClickListener {
        private Page page;

        public submitOnClickListener(Page page) {
            this.page = page;
        }

        long lastClick;

        @Override
        public void onClick(View arg0) {
            //大于一秒方个通过
            if (System.currentTimeMillis() - lastClick <= 1000) {
                ToastUtil.showToast(ResearchDetailActivity.this, "000000");
                return;
            }
            lastClick = System.currentTimeMillis();
            // TODO Auto-generated method stub
            //判断是否答完题
            boolean isState = true;
            //最终要的json数组
            JSONArray jsonArray = new JSONArray();
            //点击提交的时候，先判断状态，如果有未答完的就提示，如果没有再把每条答案提交（包含问卷ID 问题ID 及答案ID）
            //注：不用管是否是一个问题的答案，就以答案的个数为准来提交上述格式的数据
            for (int i = 0; i < the_quesition_list.size(); i++) {
                the_answer_list = the_quesition_list.get(i).getAnswers();
                //判断是否有题没答完
                if (the_quesition_list.get(i).getQue_state() == 0) {
                    if (!the_quesition_list.get(i).getIsAsk()) {
                        ToastUtil.showToast(getApplicationContext(), "您第" + (i + 1) + "题没有答完");
                        jsonArray = null;
                        isState = false;
                        break;
                    } else {
                        for (int a = 0; a < AskAnswers.size(); a++) {
                            String answers = AskAnswers.get(a).getText().toString();
                            if (answers == null || "".equals(answers)) {
                                int num = (Integer) AskAnswers.get(a).getTag();
                                ToastUtil.showToast(ResearchDetailActivity.this, "您题没有答完");
                                jsonArray = null;
                                isState = false;
                                break;
                            } else {
                                JSONObject json = new JSONObject();
                                for (int b = 0; b < AskAnswers.size(); b++) {
                                    int num = (Integer) AskAnswers.get(b).getTag();
                                    if (num == i + 1) {
                                        try {
                                            json.put("id", the_quesition_list.get(i).getQuesitionId());
                                            json.put("answer", AskAnswers.get(b).getText().toString());
                                            jsonArray.put(json);
                                        } catch (Exception e) {
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < the_answer_list.size(); j++) {
                        if (the_answer_list.get(j).getAns_state() == 1) {
                            JSONObject json = new JSONObject();
                            try {
                                json.put("id", the_quesition_list.get(i).getQuesitionId());
                                json.put("answer", the_answer_list.get(j).getAnswerId());
                                jsonArray.put(json);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                return;
                            }
                        }
                    }
                }
            }
            if (isState) {
                final String[] data = {null};
                if (jsonArray.length() > 0) {

                    if (!"".equals(MyApplication.getToKen())) {
                        Log.e("af", jsonArray.toString());

                        final FormBody body = new FormBody.Builder()
                                .add("username", MyApplication.getusername())
                                .add("password", MyApplication.getuserpassword())
                                .add("questionId", researchData.getList().get(position).getId())
                                .add("answer", jsonArray.toString())
                                .build();

                        final ProgressDialog pd = new ProgressDialog(ResearchDetailActivity.this);
                        pd.setCanceledOnTouchOutside(false);
                        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        pd.setMessage("正在提交，请稍后...");
                        pd.show();

                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                pd.dismiss();
                                super.handleMessage(msg);
                            }
                        };
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    data[0] = HttpDataUtil
                                            .getPublicData_POST(DatasUtil.URL_BASE + DatasUtil.URL_upAnswer, body);
                                    Log.i("ABC", data[0]);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Gson gson = new Gson();
                                final ReturnResult result = gson.fromJson(data[0], ReturnResult.class);

                                ResearchDetailActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Message message = handler.obtainMessage();
                                        handler.sendMessage(message);
                                        if (result != null && Integer.parseInt(result.getCode()) == 0) {
                                            ToastUtil.showToast(ResearchDetailActivity.this,
                                                    "提交成功，谢谢您的参与！");
                                        } else if (result != null && Integer.parseInt(result.getCode()) == 10222) {
                                            ToastUtil.showToast(ResearchDetailActivity.this,
                                                    "对不起，您已经参与过了！");
                                        } else {
                                            ToastUtil.showToast(ResearchDetailActivity.this,
                                                    "谢谢您的参与，此时服务器过忙，请稍后再试！");
                                        }
                                        finish();
                                    }
                                });
                            }
                        }.start();
                    } else {
                        ToastUtil.showToast(ResearchDetailActivity.this, "请先登录！");
                    }
                } else {
                    ToastUtil.showToast(ResearchDetailActivity.this, "提交异常，请稍后重试！");
                }
            }
        }
    }
}
