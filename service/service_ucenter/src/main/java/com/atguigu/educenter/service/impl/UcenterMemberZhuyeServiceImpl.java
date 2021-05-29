package com.atguigu.educenter.service.impl;

import com.atguigu.educenter.entity.UcenterMemberZhuye;
import com.atguigu.educenter.mapper.UcenterMemberZhuyeMapper;
import com.atguigu.educenter.service.UcenterMemberZhuyeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-26
 */
@Service
public class UcenterMemberZhuyeServiceImpl extends ServiceImpl<UcenterMemberZhuyeMapper, UcenterMemberZhuye> implements UcenterMemberZhuyeService {

    @Autowired
    private UcenterMemberZhuyeMapper ucenterMemberZhuyeMapper;

    @Override
    public void addOwnPage(String memberId, UcenterMemberZhuye ucenterMemberZhuye) {

        QueryWrapper<UcenterMemberZhuye> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",memberId);
        UcenterMemberZhuye selectOne = ucenterMemberZhuyeMapper.selectOne(wrapper);
        if(selectOne!=null){
            selectOne.setContent(ucenterMemberZhuye.getContent());
            ucenterMemberZhuyeMapper.updateById(selectOne);
        }else {
            ucenterMemberZhuye.setUserId(memberId);
            ucenterMemberZhuyeMapper.insert(ucenterMemberZhuye);
        }

    }

    @Override
    public UcenterMemberZhuye getOwnPage(String memberId) {
        QueryWrapper<UcenterMemberZhuye> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",memberId);
        return ucenterMemberZhuyeMapper.selectOne(wrapper);
    }
}
