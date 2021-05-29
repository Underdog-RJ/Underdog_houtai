package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.UcenterMemberZhuye;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-26
 */
public interface UcenterMemberZhuyeService extends IService<UcenterMemberZhuye> {

    void addOwnPage(String memberId, UcenterMemberZhuye ucenterMemberZhuye);

    UcenterMemberZhuye getOwnPage(String memberId);
}
