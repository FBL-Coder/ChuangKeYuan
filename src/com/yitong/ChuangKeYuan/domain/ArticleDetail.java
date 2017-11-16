package com.yitong.ChuangKeYuan.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：FBL  时间： 2016/7/13.
 */
public class ArticleDetail implements Serializable {

    /**
     * code : 0
     * msg : get the data successfully
     * id : 359
     * author : 万东管理员
     * title : dtfgg
     * excerpt :
     * "post_data": "一#http://222.72.139.156:18980/data/upload/image/20161124/1479978901.jpg#二#http://222.72.139.156:18980/data/upload/image/20161124/1479978912.jpg#三#http://222.72.139.156:18980/data/upload/image/20161124/1479978922.jpg#http://222.72.139.156:18980/data/upload/file/20161124305580/5836afb4c3cc0662265.doc#",
     * content_url : http://222.72.139.156:18980/index.php/Webapi/Article/articleInfoHtml2/id/359/token/90c53746500c2a307f6fca765f11a39d
     * content : <!DOCTYPE html><html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0"/><link rel="stylesheet" type="text/css" href="http://222.72.139.156:18980/themes/simplebootx/Webapi/Article/articleInfo.css" /></head><body><div class="sub_title"><span class="author">万东管理员 2016-10-11 17:53:20</span><ul class="star" style="padding-top:1.5em;"><li><img class="star0" src="http://222.72.139.156:18980/themes/simplebootx/Webapi/Article/image/star-off.png"></li><li><img class="star1" src="http://222.72.139.156:18980/themes/simplebootx/Webapi/Article/image/star-off.png"></li><li><img class="star2" src="http://222.72.139.156:18980/themes/simplebootx/Webapi/Article/image/star-off.png"></li><li><img class="star3" src="http://222.72.139.156:18980/themes/simplebootx/Webapi/Article/image/star-off.png"></li><li><img class="star4" src="http://222.72.139.156:18980/themes/simplebootx/Webapi/Article/image/star-off.png"></li></ul><span class="hits">阅读量:2</span></div><div class="title"><h3>dtfgg</h3></div><div class="content"><img style="width:100%; margin:0 auto;" src="http://222.72.139.156:18980//data/upload/image/20161011/1476179598.jpg" />  fggghh</div><h4 class="commentH4" style="margin-top:2em">评论</h4><div class="comment"><div class="commentItem"><p style="color:#bbb;text-align:center;">木有评论~~~</p></div><input type="hidden" id="mydata" data-token="90c53746500c2a307f6fca765f11a39d" data-aid="359" data-url="http://222.72.139.156:18980/index.php/Webapi/Article/commentList" data-url2="http://222.72.139.156:18980/index.php/Webapi/Article/delComment"  value="1" /><input type="hidden" id="starval" data-img="http://222.72.139.156:18980/themes/simplebootx/Webapi/Article/image/star-on.png" value="0" /><script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script><script type="text/javascript" src="http://222.72.139.156:18980/themes/simplebootx/Webapi/Article/articleInfoData.js"></script></body></html>
     * thumb : http://222.72.139.156:18980//data/upload/image/20161011/1476179598.jpg
     * photos : ["http://222.72.139.156:18980//data/upload/image/20161011/1476179598.jpg"]
     * audio : http://222.72.139.156:18980/public/images/video.png
     * video : http://222.72.139.156:18980/public/images/video.png
     * content_txt :   fggghh
     * hits : 2
     * like : 0
     * recommended : 0
     * star : 0
     * comment_count : h
     * is_favorites : 2
     * datetime : 2016-10-11 17:53:20
     * referer :
     * state : fail
     */

    private String code;
    private String msg;
    private String id;
    private String author;
    private String title;
    private String excerpt;
    private String content_url;
    private String content;
    private String thumb;
    private String post_data;
    private String audio;
    private String video;
    private String content_txt;
    private String hits;
    private String like;
    private String recommended;
    private String star;
    private String comment_count;
    private String is_favorites;
    private String datetime;
    private String referer;
    private String state;
    private List<String> photos;

    private String attachment;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPost_data() {
        return post_data;
    }

    public void setPost_data(String post_data) {
        this.post_data = post_data;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getContent_txt() {
        return content_txt;
    }

    public void setContent_txt(String content_txt) {
        this.content_txt = content_txt;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getIs_favorites() {
        return is_favorites;
    }

    public void setIs_favorites(String is_favorites) {
        this.is_favorites = is_favorites;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}


