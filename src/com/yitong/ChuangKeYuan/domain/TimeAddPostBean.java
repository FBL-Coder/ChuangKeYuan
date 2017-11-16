package com.yitong.ChuangKeYuan.domain;

/**
 * 作者：FBL  时间： 2016/8/18.
 */
public class TimeAddPostBean {


    /**
     * code : 0
     * msg : get the data successfully
     * onlinetimeStatistics : 1天09小时47分钟01秒
     * onlinetimeStatistics_h : 33小时
     * articleStatistics : 累计发帖2次
     */

    private String code;
    private String msg;
    private String onlinetimeStatistics;
    private String onlinetimeStatistics_h;
    private String articleStatistics;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOnlinetimeStatistics() {
        return onlinetimeStatistics;
    }

    public void setOnlinetimeStatistics(String onlinetimeStatistics) {
        this.onlinetimeStatistics = onlinetimeStatistics;
    }

    public String getOnlinetimeStatistics_h() {
        return onlinetimeStatistics_h;
    }

    public void setOnlinetimeStatistics_h(String onlinetimeStatistics_h) {
        this.onlinetimeStatistics_h = onlinetimeStatistics_h;
    }

    public String getArticleStatistics() {
        return articleStatistics;
    }

    public void setArticleStatistics(String articleStatistics) {
        this.articleStatistics = articleStatistics;
    }
}
