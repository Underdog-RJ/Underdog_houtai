package com.atguigu.servicebase.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// validatedBy可以指定不同的校验器，适配不同类型的校验
@Constraint( validatedBy = {ListValueConstraintValidator.class})
public @interface ListValue {
    String message() default "{com.atguigu.servicebase.valid.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    //自定义一个int类型的数组
    int[] vals() default {};
}
