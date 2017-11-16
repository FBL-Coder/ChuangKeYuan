package com.yitong.ChuangKeYuan.domain;

import java.util.ArrayList;

/**
 * 作者：FBL  时间： 2016/7/19.
 */
public class Quesition {
    //题目id
    private String quesitionId;
    //单选多选标识
    private String type;
    //题目
    private String content;
    //选项
    private ArrayList<Answer> answers;
    //是否解答
    private int que_state;

    //是否是问答题
    private boolean IsAsk;

    public boolean getIsAsk() {
        return IsAsk;
    }

    public void setIsAsk(boolean ask) {
        IsAsk = ask;
    }

    public int getQue_state() {
        return que_state;
    }
    public void setQue_state(int que_state) {
        this.que_state = que_state;
    }

    public String getQuesitionId() {
        return quesitionId;
    }
    public void setQuesitionId(String quesitionId) {
        this.quesitionId = quesitionId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public ArrayList<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

}
