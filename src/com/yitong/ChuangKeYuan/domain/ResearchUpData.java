package com.yitong.ChuangKeYuan.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：FBL  时间： 2016/9/22.
 */
public class ResearchUpData implements Serializable {

    String Quesition_data;

    List<Quesition_up> options;

    public String getQuesition_data() {
        return Quesition_data;
    }

    public void setQuesition_data(String quesition_data) {
        Quesition_data = quesition_data;
    }

    public List<Quesition_up> getUp() {
        return options;
    }

    public void setUp(List<Quesition_up> up) {
        this.options = up;
    }

    public class Quesition_up implements Serializable{

        String option;

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }
    }
}
