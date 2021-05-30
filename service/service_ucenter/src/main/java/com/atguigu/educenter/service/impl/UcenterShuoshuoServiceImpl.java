package com.atguigu.educenter.service.impl;

import com.atguigu.educenter.entity.UcenterShuoshuo;
import com.atguigu.educenter.mapper.UcenterShuoshuoMapper;
import com.atguigu.educenter.service.UcenterShuoshuoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-20
 */
@Service
public class UcenterShuoshuoServiceImpl extends ServiceImpl<UcenterShuoshuoMapper, UcenterShuoshuo> implements UcenterShuoshuoService {


    @Override
    public List<UcenterShuoshuo> getShuoshuoById(String id) {
        QueryWrapper<UcenterShuoshuo> wrapper=new QueryWrapper<>();
        wrapper.eq("acl_user_id",id);
        wrapper.eq("is_hide",1);
        List<UcenterShuoshuo> list = baseMapper.selectList(wrapper);
        return list;
    }
}
