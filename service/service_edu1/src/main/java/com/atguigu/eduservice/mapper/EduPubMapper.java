package com.atguigu.eduservice.mapper;


import com.atguigu.eduservice.entity.EduPub;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-08-15
 */
public interface EduPubMapper extends BaseMapper<EduPub> {

    EduPub findBaseInfo(String id);

    List<ChapterVo> findChapterInfo(String id);
}
