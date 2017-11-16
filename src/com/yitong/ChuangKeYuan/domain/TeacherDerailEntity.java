package com.yitong.ChuangKeYuan.domain;

        import java.io.Serializable;
        import java.util.List;

/**
 * 作者：FBL  时间： 2016/7/19.
 */
public class TeacherDerailEntity implements Serializable {

    /**
     * code : 0
     * msg : 获取成功
     * id : 818
     * user_id : 97
     * user_login : qwchuhaotian
     * user_nicename : 徐萍
     * grade_level : [{"sum":"3","name":"细节功能"},{"sum":"2","name":"整体设计"},{"sum":"1","name":"艺术表现"},{"sum":"1","name":"完成作品"},{"sum":"1","name":"作品设计"}]
     * signature : 注重孩子的道德培养，保持积极向上的生活态度
     * thumb : http://222.72.139.156:18980/data/upload/image/20161115/1479172497.jpg
     * profession : 外企文职
     * baby_name : 褚昊天
     * class_name : 蔷薇班
     * age : 0
     * desc : 与孩子做朋友，做一个懂道理的妈妈
     * hits : 0
     * datetime : 2016-11-15 09:15:01
     * referer :
     * state : fail
     */

    private String code;
    private String msg;
    private String id;
    private String user_id;
    private String user_login;
    private String user_nicename;
    private String signature;
    private String thumb;
    private String profession;
    private String baby_name;
    private String class_name;
    private String age;
    private String desc;
    private String hits;
    private String datetime;
    private String referer;
    private String state;
    private List<GradeLevelBean> grade_level;

    private String year_working;
    private String politics_status;

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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

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

    public List<GradeLevelBean> getGrade_level() {
        return grade_level;
    }

    public void setGrade_level(List<GradeLevelBean> grade_level) {
        this.grade_level = grade_level;
    }

    public static class GradeLevelBean {
        /**
         * sum : 3
         * name : 细节功能
         */

        private String sum;
        private String name;

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getYear_working() {
        return year_working;
    }

    public void setYear_working(String year_working) {
        this.year_working = year_working;
    }


    public String getPolitics_status() {
        return politics_status;
    }

    public void setPolitics_status(String politics_status) {
        this.politics_status = politics_status;
    }
}
