package com.yitong.ChuangKeYuan.domain;

import android.graphics.Bitmap;

/**
 * 作者：FBL  时间： 2016/5/27.
 */
public class ListData {
    String title;
    String video_path;
    int tv_tag;
    String classid;
    int image;
    int ratingbar;
    Bitmap photo;

    public int getTv_tag() {
        return tv_tag;
    }

    public void setTv_tag(int tv_tag) {
        this.tv_tag = tv_tag;
    }
    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public int getRatingbar() {
        return ratingbar;
    }

    public void setRatingbar(int ratingbar) {
        this.ratingbar = ratingbar;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
