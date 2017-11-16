package com.yitong.ChuangKeYuan.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：FBL  时间： 2016/7/19.
 */
public class DaoShiList implements Serializable {

    /**
     * code : 0
     * msg : get the data successfully
     * totalpage : 2
     * currentPage : 1
     * banner : http://192.168.1.103:18980/data/upload/20160806/57a5fb59b2cd7.png
     * list : [{"id":"272","user_id":"48","user_login":"lingcaidandan","user_nicename":"啊","signature":"","thumb":"http://192.168.1.103:18980/data/upload/image/20160923/1474615629.jpg","profession":"","baby_name":"","class_name":"","age":"0","desc":null,"hits":"0","datetime":"2016-09-23 15:27:14"},{"id":"271","user_id":"48","user_login":"lingcaidandan","user_nicename":"苹果家长","signature":"","thumb":"http://192.168.1.103:18980/data/upload/image/20160923/1474614367.jpg","profession":"","baby_name":"","class_name":"","age":"0","desc":null,"hits":"0","datetime":"2016-09-23 15:06:22"},{"id":"270","user_id":"48","user_login":"lingcaidandan","user_nicename":"哈哈","signature":"","thumb":"http://192.168.1.103:18980/data/upload/image/20160923/1474613912.jpg","profession":"","baby_name":"","class_name":"","age":"0","desc":null,"hits":"0","datetime":"2016-09-23 14:58:35"},{"id":"269","user_id":"48","user_login":"lingcaidandan","user_nicename":"呵呵","signature":"","thumb":"http://192.168.1.103:18980/data/upload/image/20160923/1474613238.jpg","profession":"","baby_name":"","class_name":"","age":"0","desc":null,"hits":"0","datetime":"2016-09-23 14:47:22"},{"id":"268","user_id":"55","user_login":"wtgulaoshi","user_nicename":"fycgg","signature":"","thumb":"http://192.168.1.103:18980/data/upload/image/20160923/1474613084.jpg","profession":"","baby_name":"","class_name":"","age":"0","desc":null,"hits":"0","datetime":"2016-09-23 14:44:48"},{"id":"267","user_id":"55","user_login":"wtgulaoshi","user_nicename":"dhcg","signature":"","thumb":"1http://192.168.1.103:18980/data/upload/image/20160923/1474612735.jpg","profession":"","baby_name":"","class_name":"","age":"0","desc":null,"hits":"0","datetime":"2016-09-23 14:38:58"},{"id":"266","user_id":"55","user_login":"wtgulaoshi","user_nicename":"dyfhh","signature":"","thumb":"1http://192.168.1.103:18980/data/upload/image/20160923/1474612464.jpg","profession":"","baby_name":"","class_name":"","age":"0","desc":null,"hits":"0","datetime":"2016-09-23 14:34:27"},{"id":"265","user_id":"55","user_login":"wtgulaoshi","user_nicename":"gsha","signature":"","thumb":"1http://192.168.1.103:18980/data/upload/image/20160923/1474612125.jpg","profession":"","baby_name":"","class_name":"","age":"0","desc":null,"hits":"0","datetime":"2016-09-23 14:30:16"},{"id":"259","user_id":"1","user_login":"admin","user_nicename":"aa","signature":"11","thumb":"http://192.168.1.103:18980/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png","profession":"22","baby_name":"顶顶顶","class_name":"蔷薇班","age":"44","desc":"55","hits":"0","datetime":"2016-09-21 15:00:05"},{"id":"257","user_id":"1","user_login":"admin","user_nicename":"新天地","signature":"地中海","thumb":"20160921/57e229ec66ee5.png","profession":"房地产","baby_name":"","class_name":"","age":"43","desc":"简介内容哦","hits":"0","datetime":"2016-09-21 14:33:35"}]
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
     * id : 272
     * user_id : 48
     * user_login : lingcaidandan
     * user_nicename : 啊
     * signature :
     * thumb : http://192.168.1.103:18980/data/upload/image/20160923/1474615629.jpg
     * profession :
     * baby_name :
     * class_name :
     * age : 0
     * desc : null
     * hits : 0
     * datetime : 2016-09-23 15:27:14
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

    public static class ListBean implements Serializable {
        private String id;
        private String user_id;
        private String user_login;
        private String user_nicename;
        private String signature;
        private String thumb;
        private String content_url;
        private String profession;
        private String baby_name;
        private String politics_status;
        private String class_name;
        private String year_working;
        private String age;
        private String desc;
        private String hits;
        private String datetime;



        public String getBaby_name() {
            return baby_name;
        }

        public void setBaby_name(String baby_name) {
            this.baby_name = baby_name;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
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

        public String getPolitics_status() {
            return politics_status;
        }

        public void setPolitics_status(String politics_status) {
            this.politics_status = politics_status;
        }

        public String getYear_working() {
            return year_working;
        }

        public void setYear_working(String year_working) {
            this.year_working = year_working;
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
    }
}
