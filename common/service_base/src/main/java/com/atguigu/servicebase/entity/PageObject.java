package com.atguigu.servicebase.entity;

import lombok.Data;

import java.util.List;

/**
 * @author underdog_rj
 * @date2022/4/319:55
 */
@Data
public class PageObject<T> {
    List<T> results;
    long total;
}
