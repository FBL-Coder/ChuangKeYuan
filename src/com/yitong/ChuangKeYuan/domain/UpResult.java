package com.yitong.ChuangKeYuan.domain;

import java.util.List;

/**
 * 作者：FBL  时间： 2016/7/20.
 */
public class UpResult {

    /**
     * code : 0
     * msg : ä¸ä¼ æåï¼
     * url : ["http://192.168.1.112/data/upload/image/20160720/578f2d2832fa0.png"]
     */

    private String code;
    private String msg;
    private List<String> url;

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

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }
}
