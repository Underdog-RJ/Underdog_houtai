package com.atguigu.eduservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AllSearchEntity {
    List<Map<String, Object>> course;
    List<Map<String, Object>> blog;

}
