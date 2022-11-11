package com.atguigu.eduservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {

    private String image_path;
    private String tsp;
    private String name;

}
