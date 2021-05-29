package com.atguigu.eduservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnReadMessage  {
    private String name;
    private Integer count;
}
