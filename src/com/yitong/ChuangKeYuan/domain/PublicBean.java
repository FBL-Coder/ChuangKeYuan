package com.yitong.ChuangKeYuan.domain;

import java.util.List;

/**
 * Created by Say GoBay on 2016/7/25.
 */
public class PublicBean {

    /**
     * code : 0
     * msg : 上传成功！
     * url : ["http://119.147.115.203:18980/data/upload/file/20170413/58ef3afecdfe6989289.doc"]
     * pdf : ["/data/upload/pdf/58ef3afecdfe6989289.pdf"]
     * name : ["手机测试文件WORD.doc"]
     * referer : ["http://119.147.115.203:18980/data/upload/file/20170413/58ef3afecdfe6989289.doc"]
     * state : fail
     */

    private String code;
    private String msg;
    private String state;
    private List<String> url;
    private List<String> pdf;
    private List<String> name;
    private List<String> referer;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public List<String> getPdf() {
        return pdf;
    }

    public void setPdf(List<String> pdf) {
        this.pdf = pdf;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getReferer() {
        return referer;
    }

    public void setReferer(List<String> referer) {
        this.referer = referer;
    }
}
