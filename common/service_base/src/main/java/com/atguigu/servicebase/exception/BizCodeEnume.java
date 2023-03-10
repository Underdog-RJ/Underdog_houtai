package com.atguigu.servicebase.exception;

public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000, "系统位置异常"),
    UNLOGIN_EXCEPTION(28004, "用户未登录"),
    VALID_EXCEPTION(10000, "参数格式检验异常"),
    IDERROR_EXCEPTION(10001, "查询不到该对象"),
    INVALID_MOBILE(10001, "手机号不正确"),
    INVALID_USER(10002, "该用户未注册"),
    INVALID_PASSWORD(10003, "账号或者密码不对"),
    INVALID_USER_ERROR(10004, "用户被禁用");

    private int code;
    private String msg;

    BizCodeEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
