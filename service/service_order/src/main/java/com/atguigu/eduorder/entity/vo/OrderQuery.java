package com.atguigu.eduorder.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderQuery {

    private String courseTitle;
    private String teacherName;
    private Integer payType;
    private Integer status;
    private String begin;
    private String end;


}
