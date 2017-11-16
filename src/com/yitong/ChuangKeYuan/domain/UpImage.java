package com.yitong.ChuangKeYuan.domain;

/**
 * Created by Say GoBay on 2016/11/9.
 */
public class UpImage {
    int loact;
    String url;
    int tag = 0;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getLoact() {
        return loact;
    }

    public void setLoact(int loact) {
        this.loact = loact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
