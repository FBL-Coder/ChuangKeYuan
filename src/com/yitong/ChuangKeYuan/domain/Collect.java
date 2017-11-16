package com.yitong.ChuangKeYuan.domain;

import java.util.List;

/**
 * 作者：FBL  时间： 2016/7/18.
 */
public class Collect {

    /**
     * code : 0
     * msg : 获取成功！
     * totalpage : 1
     * currentPage : 1
     * list : [{"id":"11","title":"十二五课题\u201c幼儿园创意美术教学的实践研究\u201d方案","article_id":"13","thumb":"http://192.168.1.112/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png","createtime":"2016-07-18 14:47:59"},{"id":"10","title":"2016幼儿园六一创意小活动","article_id":"16","thumb":"http://192.168.1.112/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png","createtime":"2016-07-18 14:47:57"}]
     */

    private String code;
    private String msg;
    private String totalpage;
    private String currentPage;
    /**
     * id : 11
     * title : 十二五课题“幼儿园创意美术教学的实践研究”方案
     * article_id : 13
     * thumb : http://192.168.1.112/admin/themes/simplebootx/Public/assets/images/default-thumbnail.png
     * createtime : 2016-07-18 14:47:59
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String id;
        private String title;
        private String article_id;
        private String thumb;
        private String createtime;

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

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }
}
