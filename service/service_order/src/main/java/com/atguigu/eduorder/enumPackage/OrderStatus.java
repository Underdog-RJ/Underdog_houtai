package com.atguigu.eduorder.enumPackage;

import lombok.Data;


public enum OrderStatus {


    UNPID("未支付",0),
    PAID("已支付",1);

    private String status;
    private Integer flag;

    OrderStatus(String status,Integer flag) {
        this.status = status;
        this.flag = flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
