package com.atguigu.servicebase.valid;

/**
 * JSR303
 * 1.给Bean添加校验注解，并定义自己的message提示
 * 2.开启校验功能@Valid
 *      效果，校验错误以后会有默认的响应
 * 3.给校验的bean后紧跟一个BingdingResult就可以取到校验的结果
 * 4.分组校验（复杂场景）
 *      1.@NotNull(message = "修改必须指定品牌id",groups = {UpdateGroup.class})
 *      给注解标注什么情况需要进行校验
 *      2.@Validated({AddGroup.class})
 *      3.默认没有指定分组的校验注解@NotBlank，在分组的情况下不生效只会在@Validated生效
 *5.自定义校验
 *  1.编写一个自定义的校验注解
 *  2.编写一个自定义的校验器
 *  3.关联自定义的校验器和自定义的校验注解
 */
public interface AddGroup {
}
