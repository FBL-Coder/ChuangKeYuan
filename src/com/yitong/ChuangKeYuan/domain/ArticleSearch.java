package com.yitong.ChuangKeYuan.domain;

import java.util.List;

/**
 * 作者：FBL  时间： 2016/7/15.
 */
public class ArticleSearch {

    /**
     * code : 0
     * msg : get the data successfully
     * list : [{"id":"1","keyword":"教育方案","statistical":"15"},{"id":"2","keyword":"教育","statistical":"13"},{"id":"4","keyword":"老师","statistical":"4"},{"id":"6","keyword":"L","statistical":"3"},{"id":"3","keyword":"资深老师","statistical":"2"},{"id":"11","keyword":"课","statistical":"1"},{"id":"10","keyword":"课程","statistical":"1"},{"id":"9","keyword":"老","statistical":"1"},{"id":"8","keyword":"Lao","statistical":"1"},{"id":"7","keyword":"La","statistical":"1"}]
     */

    private String code;
    private String msg;
    /**
     * id : 1
     * keyword : 教育方案
     * statistical : 15
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String id;
        private String keyword;
        private String statistical;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getStatistical() {
            return statistical;
        }

        public void setStatistical(String statistical) {
            this.statistical = statistical;
        }
    }
}
