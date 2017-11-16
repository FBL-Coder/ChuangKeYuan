package com.yitong.ChuangKeYuan.domain;

import java.util.List;

/**
 * 作者：FBL  时间： 2016/8/1.
 */
public class Task_List {

    /**
     * code : 0
     * msg : get the data successfully
     * totalpage : 1
     * currentPage : 1
     * banner : http://192.168.1.103:18980/data/upload/20160806/57a5fb85c5e04.png
     * list : [{"id":"310","user_id":"55","user_login":"wtgulaoshi","user_nicename":"顾老师(梧桐班)","title":"嗨咯","signature":"","thumb":"http://192.168.1.103:18980/data/upload/null","content_url":"http://192.168.1.103:18980/index.php/Webapi/Article/articleInfoHtml2/id/310","profession":"","desc":"士力架","hits":"30","datetime":"2016-09-29 15:25:39","class_name":"梧桐班","post_end_time":"2017-05-05 00:00:00","is_overdue":"0","is_work":"0"},{"id":"183","user_id":"6","user_login":"superadmin","user_nicename":"管理员","title":"梧桐班任务","signature":"","thumb":"http://192.168.1.103:18980/data/upload/20160807/57a60a9eec05e.png","content_url":"http://192.168.1.103:18980/index.php/Webapi/Article/articleInfoHtml2/id/183","profession":"","desc":"<p>梧桐班任务内容<\/p>","hits":"65","datetime":"2016-08-07 00:04:21","class_name":"梧桐班","post_end_time":"2016-08-31 23:58:00","is_overdue":"1","is_work":"0"},{"id":"312","user_id":"56","user_login":"wtxielaoshi","user_nicename":"谢老师(梧桐班)","title":"sydhdh","signature":"","thumb":"http://192.168.1.103:18980/data/upload/null","content_url":"http://192.168.1.103:18980/index.php/Webapi/Article/articleInfoHtml2/id/312","profession":"","desc":"么啥","hits":"4","datetime":"2016-09-29 16:47:53","class_name":"梧桐班","post_end_time":"2016-01-01 00:00:00","is_overdue":"1","is_work":"0"}]
     * referer :
     * state : fail
     */

    private String code;
    private String msg;
    private String totalpage;
    private String currentPage;
    private String banner;
    private String referer;
    private String state;
    /**
     * id : 310
     * user_id : 55
     * user_login : wtgulaoshi
     * user_nicename : 顾老师(梧桐班)
     * title : 嗨咯
     * signature :
     * thumb : http://192.168.1.103:18980/data/upload/null
     * content_url : http://192.168.1.103:18980/index.php/Webapi/Article/articleInfoHtml2/id/310
     * profession :
     * desc : 士力架
     * hits : 30
     * datetime : 2016-09-29 15:25:39
     * class_name : 梧桐班
     * post_end_time : 2017-05-05 00:00:00
     * is_overdue : 0
     * is_work : 0
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String id;
        private String user_id;
        private String user_login;
        private String user_nicename;
        private String title;
        private String signature;
        private String app_auth_txt;
        private String thumb;
        private String content_url;
        private String profession;
        private String desc;
        private String hits;
        private String datetime;
        private String class_name;
        private String post_end_time;
        private String is_overdue;
        private String is_work;

        public String getApp_auth_txt() {
            return app_auth_txt;
        }

        public void setApp_auth_txt(String app_auth_txt) {
            this.app_auth_txt = app_auth_txt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_login() {
            return user_login;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
        }

        public String getUser_nicename() {
            return user_nicename;
        }

        public void setUser_nicename(String user_nicename) {
            this.user_nicename = user_nicename;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getContent_url() {
            return content_url;
        }

        public void setContent_url(String content_url) {
            this.content_url = content_url;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getHits() {
            return hits;
        }

        public void setHits(String hits) {
            this.hits = hits;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getPost_end_time() {
            return post_end_time;
        }

        public void setPost_end_time(String post_end_time) {
            this.post_end_time = post_end_time;
        }

        public String getIs_overdue() {
            return is_overdue;
        }

        public void setIs_overdue(String is_overdue) {
            this.is_overdue = is_overdue;
        }

        public String getIs_work() {
            return is_work;
        }

        public void setIs_work(String is_work) {
            this.is_work = is_work;
        }
    }
}
