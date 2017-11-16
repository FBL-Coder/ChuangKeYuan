package com.easemob.easeui.domain;

import java.util.List;

/**
 * 作者：傅博龍  时间： 2016/8/5.
 */
public class AllUser {

    /**
     * code : 0
     * msg : 操作成功！
     * list : [{"id":"1","user_login":"admin","user_nicename":"admin","avatar":"http://192.168.1.111/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png"},{"id":"2","user_login":"王小豆","user_nicename":"王大豆","avatar":"http://192.168.1.111/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png"},{"id":"3","user_login":"lilaoshi","user_nicename":"李老师","avatar":"http://192.168.1.111/data/upload/20160726/5796bf508c780.jpg"},{"id":"5","user_login":"王城","user_nicename":"王城","avatar":"http://192.168.1.111/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png"},{"id":"6","user_login":"superadmin","user_nicename":"管理员","avatar":"http://192.168.1.111/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png"},{"id":"7","user_login":"hulaoshi","user_nicename":"胡老师","avatar":"http://192.168.1.111/data/upload/20160726/5796bffabbf12.jpg"},{"id":"9","user_login":"long","user_nicename":"abc","avatar":"http://192.168.1.111/data/upload/avatar/1470298584.jpg"},{"id":"8","user_login":"zhanglaoshi","user_nicename":"张老师","avatar":"http://192.168.1.111/data/upload/20160726/5796be79e33f8.jpg"},{"id":"10","user_login":"fenglei","user_nicename":"大冯","avatar":"http://192.168.1.111/data/upload/image/20160720/578f53619235f.jpg"},{"id":"11","user_login":"123456","user_nicename":"123456","avatar":"http://192.168.1.111/data/upload/20160726/579725cfbdd56.jpg"},{"id":"12","user_login":"wangcheng","user_nicename":"王城","avatar":"http://192.168.1.111/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png"},{"id":"15","user_login":"yebai","user_nicename":"叶白","avatar":"http://192.168.1.111/data/upload/20160718/578c925d2be55.jpg"},{"id":"16","user_login":"guolaoshi","user_nicename":"郭老师","avatar":"http://192.168.1.111/data/upload/20160726/5796bf26dedc3.jpg"},{"id":"17","user_login":"zhangsan","user_nicename":"张老师(绿萝班)","avatar":"http://192.168.1.111/data/upload/20160726/5796c0528a86e.jpg"},{"id":"18","user_login":"limiao","user_nicename":"李妙","avatar":"http://192.168.1.111/data/upload/20160718/578c895b4bb05.jpg"},{"id":"19","user_login":"maojingang","user_nicename":"毛金慧","avatar":"http://192.168.1.111/data/upload/20160725/5795825b143dc.jpg"},{"id":"20","user_login":"iosTest","user_nicename":"大爷","avatar":"http://192.168.1.111/data/upload/image/20160804/57a3119d3fb63.png"},{"id":"23","user_login":"huanglili","user_nicename":"黄莉莉","avatar":"http://192.168.1.111/data/upload/20160726/5796b56350490.jpg"},{"id":"21","user_login":"FBL","user_nicename":"张三","avatar":"http://192.168.1.111/data/upload/avatar/1470298615.jpg"},{"id":"22","user_login":"huang","user_nicename":"123456","avatar":"http://192.168.1.111/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png"},{"id":"24","user_login":"luwenyi","user_nicename":"陆雯伊","avatar":"http://192.168.1.111/data/upload/20160726/5796b6a104074.jpg"},{"id":"25","user_login":"chengcunyun","user_nicename":"程春云","avatar":"http://192.168.1.111/data/upload/20160726/5796b6f2ac962.jpg"},{"id":"26","user_login":"houling","user_nicename":"侯玲","avatar":"http://192.168.1.111/data/upload/20160726/5796ba927bd35.jpg"},{"id":"27","user_login":"jixiaofeng","user_nicename":"季晓凤","avatar":"http://192.168.1.111/data/upload/20160726/5796bb1298da6.jpg"},{"id":"28","user_login":"hupei","user_nicename":"胡蓓","avatar":"http://192.168.1.111/data/upload/20160726/5796bb4f1b4c4.jpg"},{"id":"29","user_login":"zhangtie","user_nicename":"张轶","avatar":"http://192.168.1.111/data/upload/20160726/5796bc420d2c2.jpg"},{"id":"30","user_login":"luli","user_nicename":"陆丽","avatar":"http://192.168.1.111/data/upload/20160726/5796bc9cc8e14.jpg"},{"id":"31","user_login":"zhanglan","user_nicename":"张兰","avatar":"http://192.168.1.111/data/upload/20160726/5796bcce34691.jpg"},{"id":"32","user_login":"gaolaoshi","user_nicename":"高老师","avatar":"http://192.168.1.111/data/upload/20160726/5796c08519e67.png"},{"id":"33","user_login":"he","user_nicename":"何景春","avatar":"http://192.168.1.111/data/upload/image/20160804/57a308383fe13.png"}]
     * referer :
     * state : fail
     */

    private String code;
    private String msg;
    private String referer;
    private String state;
    /**
     * id : 1
     * user_login : admin
     * user_nicename : admin
     * avatar : http://192.168.1.111/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
