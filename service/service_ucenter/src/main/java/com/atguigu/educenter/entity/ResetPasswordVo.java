package com.atguigu.educenter.entity;

import lombok.Data;

@Data
public class ResetPasswordVo {

    private String mail;
    private String code;
    private String password;

}
