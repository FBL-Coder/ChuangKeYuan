package com.yitong.ChuangKeYuan.friendscircle.bean;

import java.util.List;

/**
 * 作者：傅博龍  时间： 2016/7/25.
 */
public class FindData {


    /**
     * code : 0
     * msg : get the data successfully
     * totalpage : 1
     * currentPage : 1
     * user : {"id":"9","name":"abc","headUrl":"http://192.168.1.112/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png"}
     * list : [{"id":"6","content":"多图测试内容","type":"2","photos":["http://192.168.1.112/data/moments/image/20160719/578df7e53dc3c.jpg","http://192.168.1.112/data/moments/image/20160719/578df7e53ebf7.jpg","http://192.168.1.112/data/moments/image/20160719/578df7e53f6bf.jpg"],"audioUrl":"","videoUrl":"","createTime":"2016-07-19 17:55:56","favorters":{"id":"10","user":{"id":"10","name":"fenglei"}},"comments":{"id":"3","article_id":"6","user":{"id":"9","name":"abc"},"toReplyUser":{"id":"10","name":"大冯"},"content":"你猜","createTime":"2016-07-20 09:13:45"},"user":{"id":"9","name":"abc","headUrl":"http://192.168.1.112/data/upload/20160718/578c9479d6bba.jpg"}},{"id":"3","content":"测试视频内容","type":"3","photos":"","audioUrl":"","videoUrl":"http://192.168.1.112/data/moments/video/20160719/578da6823b222.mp4","createTime":"2016-07-19 15:25:16","favorters":{"id":"20","user":{"id":"20","name":"你大爷"}},"comments":[],"user":{"id":"9","name":"abc","headUrl":"http://192.168.1.112/data/upload/20160718/578c9479d6bba.jpg"}},{"id":"2","content":"测试内容","type":"2","photos":["http://192.168.1.112/data/moments/image/20160719/578d9bb43f165.jpg"],"audioUrl":"","videoUrl":"","createTime":"2016-07-19 15:19:29","favorters":{"id":"20","user":{"id":"20","name":"你大爷"}},"comments":[],"user":{"id":"9","name":"abc","headUrl":"http://192.168.1.112/data/upload/20160718/578c9479d6bba.jpg"}}]
     */

    private String code;
    private String msg;
    private String totalpage;
    private String currentPage;
    private String banner;
    /**
     * id : 9
     * name : abc
     * headUrl : http://192.168.1.112/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png
     */

    private User user;
    /**
     * id : 6
     * content : 多图测试内容
     * type : 2
     * photos : ["http://192.168.1.112/data/moments/image/20160719/578df7e53dc3c.jpg","http://192.168.1.112/data/moments/image/20160719/578df7e53ebf7.jpg","http://192.168.1.112/data/moments/image/20160719/578df7e53f6bf.jpg"]
     * audioUrl :
     * videoUrl :
     * createTime : 2016-07-19 17:55:56
     * favorters : {"id":"10","user":{"id":"10","name":"fenglei"}}
     * comments : {"id":"3","article_id":"6","user":{"id":"9","name":"abc"},"toReplyUser":{"id":"10","name":"大冯"},"content":"你猜","createTime":"2016-07-20 09:13:45"}
     * user : {"id":"9","name":"abc","headUrl":"http://192.168.1.112/data/upload/20160718/578c9479d6bba.jpg"}
     */

    private List<CircleItem> list;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CircleItem> getList() {
        return list;
    }

    public void setList(List<CircleItem> list) {
        this.list = list;
    }

}
