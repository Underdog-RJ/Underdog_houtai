package com.atguigu.educenter.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.ws.soap.Addressing;

@Data
@NoArgsConstructor
@Addressing
public class UcenterVo {

    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    private Integer count;
}
