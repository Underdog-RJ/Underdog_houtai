package com.atguigu.eduservice.enumpackage;

public enum IndexEnum {
    COURSE_INDEX("underdog_course"),
    BLOG_INDEX("underdog_blog");

    IndexEnum(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
