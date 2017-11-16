package com.yitong.ChuangKeYuan.domain;

import java.util.List;

/**
 * 作者：FBL  时间： 2016/7/20.
 */
public class ResearchData {


    /**
     * code : 0
     * msg : 获取成功
     * totalpage : 1
     * list : [{"id":"15","name":"我的第一个问卷","author":"huanglili","pri":"全部","createtime":"2016-09-28 11:29:55","questions":[{"id":"23","question":"喜欢吃吗？","num":"3","items":[{"A":"喜欢"},{"B":"一般"},{"C":"不喜欢"}]},{"id":"24","question":"喜欢喝吗？","num":"4","items":[{"A":"非常喜欢"},{"B":"喜欢"},{"C":"一般"},{"D":"不喜欢"}]},{"id":"25","question":"你对我有啥看法？有本事说出来！","num":"0","items":[]}]}]
     * referer :
     * state : fail
     */

    private String code;
    private String msg;
    private String totalpage;
    private String referer;
    private String state;
    /**
     * id : 15
     * name : 我的第一个问卷
     * author : huanglili
     * pri : 全部
     * createtime : 2016-09-28 11:29:55
     * questions : [{"id":"23","question":"喜欢吃吗？","num":"3","items":[{"A":"喜欢"},{"B":"一般"},{"C":"不喜欢"}]},{"id":"24","question":"喜欢喝吗？","num":"4","items":[{"A":"非常喜欢"},{"B":"喜欢"},{"C":"一般"},{"D":"不喜欢"}]},{"id":"25","question":"你对我有啥看法？有本事说出来！","num":"0","items":[]}]
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
        private String name;
        private String author;
        private String loginname;
        private String pri;
        private String createtime;
        private String content_url;
        private String isAnswer;
        /**
         * id : 23
         * question : 喜欢吃吗？
         * num : 3
         * items : [{"A":"喜欢"},{"B":"一般"},{"C":"不喜欢"}]
         */

        private List<QuestionsBean> questions;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getLoginname() {
            return loginname;
        }

        public void setLoginname(String loginname) {
            this.loginname = loginname;
        }

        public String getPri() {
            return pri;
        }

        public void setPri(String pri) {
            this.pri = pri;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getContent_url() {
            return content_url;
        }

        public void setContent_url(String content_url) {
            this.content_url = content_url;
        }

        public String getIsAnswer() {
            return isAnswer;
        }

        public void setIsAnswer(String isAnswer) {
            this.isAnswer = isAnswer;
        }

        public List<QuestionsBean> getQuestions() {
            return questions;
        }

        public void setQuestions(List<QuestionsBean> questions) {
            this.questions = questions;
        }

        public static class QuestionsBean {
            private String id;
            private String question;
            private String num;
            /**
             * A : 喜欢
             */

            private List<ItemsBean> items;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public static class ItemsBean {
                private String A;
                private String B;
                private String C;
                private String D;

                public String getA() {
                    return A;
                }

                public void setA(String A) {
                    this.A = A;
                }
                public String getD() {
                    return D;
                }

                public void setD(String d) {
                    D = d;
                }

                public String getB() {
                    return B;
                }

                public void setB(String b) {
                    B = b;
                }

                public String getC() {
                    return C;
                }

                public void setC(String c) {
                    C = c;
                }


            }
        }
    }
}
