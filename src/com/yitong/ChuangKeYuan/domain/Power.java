package com.yitong.ChuangKeYuan.domain;

/**
 * Created by Say GoBay on 2016/9/23.
 */
public class Power {
    public String getPowerName() {
        return powerName;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }

    private String powerName;
    public Power(String powerName) {
        super();
        this.powerName = powerName;
    }


}