package com.atguigu.servicebase.exception;

public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000,"系统位置异常"),
    VALID_EXCEPTION(10000,"参数格式检验异常");

    private int code;
    private String msg;
    BizCodeEnume(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
