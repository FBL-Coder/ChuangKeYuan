package com.yitong.ChuangKeYuan.domain;

import java.util.List;

/**
 * Author：FBL  Time： 2017/9/12.
 */

public class CommentBean {


    /**
     * code : 0
     * msg : 获取成功！
     * list : [{"title":"2017学年第一学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2016学年第二学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2016学年第一学年点评","good":"你的作品在创新性方面整体设计有新意,细节功能有新意;在技术性方面结构设计完整,材料功能实现,创意实现性强;在艺术性方面作品设计优秀,艺术表现力强;在规范性方面设计方案规范,制作过程规范,作品完成度高;在团队展示与协作方面团队展示突出,分工协作明确。","bad":"","average":"5.6","level":"A-"},{"title":"2015学年第二学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2015学年第一学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2014学年第二学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2014学年第一学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2013学年第二学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2013学年第一学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2012学年第二学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2012学年第一学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2011学年第二学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"},{"title":"2011学年第一学年点评","good":"","bad":"你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。","average":"0.0","level":"A-"}]
     * referer :
     * state : fail
     */

    private String code;
    private String msg;
    private String referer;
    private String state;
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
        /**
         * title : 2017学年第一学年点评
         * good :
         * bad : 你的作品在创新性方面整体设计缺少新意,细节功能缺少新意;在技术性方面结构设计不够完整,材料功能未完全实现,创意实现性不强;在艺术性方面作品设计能力欠缺。
         * average : 0.0
         * level : A-
         */

        private String title;
        private String good;
        private String bad;
        private String average;
        private String level;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
        }

        public String getBad() {
            return bad;
        }

        public void setBad(String bad) {
            this.bad = bad;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }
}
