package com.atguigu.eduservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EduLiving对象", description="")
public class EduLiving implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "直播唯一标识")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @TableField(exist = false)
    private int type;

    private String teacherId;

    private String livingName;

    private String timeHours;

    private String videoId;


    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;



    private Integer viewNum;

    private String livingCover;

    private String teacherName;

    private String subjectId;

    private String subjectParentId;

    private String descption;

    private String pushUrl;

    private String pullUrl;


}
