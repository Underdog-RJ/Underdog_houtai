package com.atguigu.eduservice.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EduPub对象", description="")
public class EduPub implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "讲师名字")
    private String teacherName;

    @ApiModelProperty(value = "一级分类名字")
    private String subjuctLevel1Name;

    @ApiModelProperty(value = "二级分类名字")
    private String subjectLevel2Name;

    @ApiModelProperty(value = "课程名称")
    private String title;

    @ApiModelProperty(value = "课程价格")
    private BigDecimal price;

    @ApiModelProperty(value = "课时数")
    private Integer lessonNum;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "课程计划")
    private String teachPlan;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;


}
