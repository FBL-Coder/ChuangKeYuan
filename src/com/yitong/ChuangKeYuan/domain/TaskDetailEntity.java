package com.yitong.ChuangKeYuan.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：FBL  时间： 2016/7/28.
 */
public class TaskDetailEntity implements Serializable {

    /**
     * code : 0
     * msg : get the data successfully
     * article : {"id":"175","author":"管理员","title":"绿绿班任务","excerpt":"","content_url":"http://192.168.1.111/index.php/Webapi/Article/articleInfoHtml2/id/175/uid/22","content":"<p>绿绿班任务内容<\/p>","thumb":"http://192.168.1.111/data/upload/20160807/57a609bb4bce4.png","photo":[],"hits":"23","like":"0","recommended":"0","star":"0","datetime":"2016-08-07 00:00:31","isaddwork":"1"}
     * working : [{"id":"72","uid":"22","nicename":"大黄人","title":"作业(文件)","url":"http://192.168.1.111/data/upload/file/20160809/57a94318ecec9.mp4","type":"video","status":"1","createtime":"2016-08-09 10:42:56"},{"id":"70","uid":"22","nicename":"大黄人","title":"东辉(文件)","url":"http://192.168.1.111/data/upload/file/20160809/57a942eb39e5b.mp4","type":"video","status":"1","createtime":"2016-08-09 10:41:51"},{"id":"71","uid":"22","nicename":"大黄人","title":"东辉(文件)","url":"http://192.168.1.111/data/upload/file/20160809/57a942b85fc8f.mp4","type":"video","status":"1","createtime":"2016-08-09 10:41:51"},{"id":"69","uid":"22","nicename":"大黄人","title":"问津(文件)","url":"http://192.168.1.111/data/upload/file/20160809/57a9428d02f42.mp4","type":"video","status":"1","createtime":"2016-08-09 10:40:21"},{"id":"68","uid":"22","nicename":"大黄人","title":"和(视频)","url":"http://192.168.1.111/data/upload/video/20160809/57a94268c2929.3gp","type":"video","status":"1","createtime":"2016-08-09 10:39:42"},{"id":"67","uid":"22","nicename":"大黄人","title":"个(图片)","url":"http://192.168.1.111/data/upload/image/20160809/57a9424460e94.jpg","type":"image","status":"1","createtime":"2016-08-09 10:39:04"}]
     * referer :
     * state : fail
     */

    private String code;
    private String msg;
    /**
     * id : 175
     * author : 管理员
     * title : 绿绿班任务
     * excerpt :
     * content_url : http://192.168.1.111/index.php/Webapi/Article/articleInfoHtml2/id/175/uid/22
     * content : <p>绿绿班任务内容</p>
     * thumb : http://192.168.1.111/data/upload/20160807/57a609bb4bce4.png
     * photo : []
     * hits : 23
     * like : 0
     * recommended : 0
     * star : 0
     * datetime : 2016-08-07 00:00:31
     * isaddwork : 1
     */

    private ArticleBean article;
    private String referer;
    private String state;
    /**
     * id : 72
     * uid : 22
     * nicename : 大黄人
     * title : 作业(文件)
     * url : http://192.168.1.111/data/upload/file/20160809/57a94318ecec9.mp4
     * type : video
     * status : 1
     * createtime : 2016-08-09 10:42:56
     */

    private List<WorkingBean> working;

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

    public ArticleBean getArticle() {
        return article;
    }

    public void setArticle(ArticleBean article) {
        this.article = article;
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

    public List<WorkingBean> getWorking() {
        return working;
    }

    public void setWorking(List<WorkingBean> working) {
        this.working = working;
    }

    public static class ArticleBean implements Serializable{
        private String id;
        private String author;
        private String title;
        private String excerpt;
        private String content_url;
        private String content;
        private String thumb;
        private String hits;
        private String like;
        private String recommended;
        private String star;
        private String datetime;
        private String isaddwork;
        private String time;
        private List<String> photos;
        private String video;

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

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getIsaddwork() {
            return isaddwork;
        }

        public void setIsaddwork(String isaddwork) {
            this.isaddwork = isaddwork;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }
        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }
    }

    public static class WorkingBean implements Serializable{
        private String id;
        private String uid;
        private String nicename;
        private String title;
        private String url;
        private String type;
        private String status;
        private String createtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNicename() {
            return nicename;
        }

        public void setNicename(String nicename) {
            this.nicename = nicename;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }
}
