package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.BlogEnjoy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-15
 */
@Repository
public interface BlogEnjoyMapper extends BaseMapper<BlogEnjoy> {

    Integer isEnjoy(@Param("blogId") String id,@Param("userId") String userId);

}
