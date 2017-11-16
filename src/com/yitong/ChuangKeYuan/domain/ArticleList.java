package com.yitong.ChuangKeYuan.domain;

import java.util.List;

/**
 * 作者：FBL  时间： 2016/7/12.
 */
public class ArticleList {

    /**
     * code : 0
     * msg : get the data successfully
     * totalpage : 1
     * currentPage : 1
     * banner : http://192.168.1.112/data/upload/20160714/57876a4cb3ceb.png
     * list : [{"id":"16","title":"2016幼儿园六一创意小活动","excerpt":" ","thumb":"http://192.168.1.112/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png","hits":"49","like":"1","recommended":"0","comment_count":"0","star":"0","datetime":"2016-07-05 10:36:31"},{"id":"13","title":"十二五课题\u201c幼儿园创意美术教学的实践研究\u201d方案","excerpt":" ","thumb":"http://192.168.1.112/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png","hits":"14","like":"0","recommended":"0","comment_count":"20","star":"0","datetime":"2016-07-05 10:30:48"},{"id":"11","title":"幼儿教育互联网方案","excerpt":"幼儿教育互联网方案","thumb":"http://192.168.1.112/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png","hits":"8","like":"0","recommended":"0","comment_count":"0","star":"0","datetime":"2016-07-01 09:41:34"},{"id":"1","title":"这里是列表演示文章","excerpt":"这里是列表演示文章","thumb":"http://192.168.1.112/data/upload/20160628/5771f33e7c7e5.png","hits":"11","like":"1","recommended":"0","comment_count":"0","star":"0","datetime":"2016-06-28 11:46:33"}]
     */

    private String code;
    private String msg;
    private String totalpage;
    private String currentPage;
    private String banner;
    /**
     * id : 16
     * title : 2016幼儿园六一创意小活动
     * excerpt :
     * thumb : http://192.168.1.112/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png
     * hits : 49
     * like : 1
     * recommended : 0
     * comment_count : 0
     * star : 0
     * datetime : 2016-07-05 10:36:31
     */

    private List<ListBean> list;

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

    public String getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(String totalpage) {
        this.totalpage = totalpage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String id;
        private String title;
        private String excerpt;
        private String thumb;
        private String hits;
        private String author;
        private String content_url;
        private String post_status;
        private String like;
        private String recommended;
        private String comment_count;
        private String star;
        private String datetime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent_url() {
            return content_url;
        }

        public void setContent_url(String content_url) {
            this.content_url = content_url;
        }

        public String getPost_status() {
            return post_status;
        }

        public void setPost_status(String post_status) {
            this.post_status = post_status;
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

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
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
    }
}
