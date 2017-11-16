package com.yitong.ChuangKeYuan.domain;

import java.util.List;

/**
 * 作者：FBL  时间： 2016/9/30.
 */
public class ClassTeacherlist {


    /**
     * code : 0
     * msg : get the data successfully
     * totalpage : 1
     * currentPage : 1
     * list : [{"id":"czoyOiIyNiI7VFBfcGhw","user_login":"houling","user_nicename":"侯玲(苗苗班)","avatar":"http://192.168.1.103:18980/data/upload/20160805/57a43d913567e.jpg","signature":"本着\u201c和孩子一起成长\u201d的信念，祝愿我们的孩子健康、快乐、充满爱心。","mobile":"13400001003","class_id":"34","class_name":"苗苗班","position":"幼儿园高级","political":"群众","working":"11","describe":"本着\u201c和孩子一起成长\u201d的信念，祝愿我们的孩子健康、快乐、充满爱心。"},{"id":"czoyOiIyNyI7VFBfcGhw","user_login":"jixiaofeng","user_nicename":"季晓凤(苗苗班)","avatar":"http://192.168.1.103:18980/data/upload/20160805/57a43de8aba95.jpg","signature":"快乐孩子的快乐，好奇孩子的好奇，感受孩子的感受，幸福孩子的幸福!","mobile":"13400010004","class_id":"34","class_name":"苗苗班","position":"幼儿园一级","political":"共青团员","working":"3","describe":"快乐孩子的快乐，好奇孩子的好奇，感受孩子的感受，幸福孩子的幸福!"},{"id":"czoyOiIyOCI7VFBfcGhw","user_login":"hupei","user_nicename":"胡蓓(苗苗班)","avatar":"http://192.168.1.103:18980/data/upload/20160817/57b42b1ea5f8e.jpg","signature":"踏实工作，希望每一个孩子都能健康成长！","mobile":"13400010005","class_id":"34","class_name":"苗苗班","position":"高级保育员","political":"群众","working":"8","describe":"踏实工作，希望每一个孩子都能健康成长！"}]
     * referer :
     * state : fail
     */

    private String code;
    private String msg;
    private String totalpage;
    private String currentPage;
    private String referer;
    private String state;
    /**
     * id : czoyOiIyNiI7VFBfcGhw
     * user_login : houling
     * user_nicename : 侯玲(苗苗班)
     * avatar : http://192.168.1.103:18980/data/upload/20160805/57a43d913567e.jpg
     * signature : 本着“和孩子一起成长”的信念，祝愿我们的孩子健康、快乐、充满爱心。
     * mobile : 13400001003
     * class_id : 34
     * class_name : 苗苗班
     * position : 幼儿园高级
     * political : 群众
     * working : 11
     * describe : 本着“和孩子一起成长”的信念，祝愿我们的孩子健康、快乐、充满爱心。
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
        private String user_login;
        private String user_nicename;
        private String avatar;
        private String signature;
        private String mobile;
        private String class_id;
        private String class_name;
        private String position;
        private String political;
        private String working;
        private String describe;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getThumb() {
            return avatar;
        }

        public void setThumb(String avatar) {
            this.avatar = avatar;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getPolitical() {
            return political;
        }

        public void setPolitical(String political) {
            this.political = political;
        }

        public String getWorking() {
            return working;
        }

        public void setWorking(String working) {
            this.working = working;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }
    }
}
