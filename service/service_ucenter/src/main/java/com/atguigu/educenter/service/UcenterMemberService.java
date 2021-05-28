package com.atguigu.educenter.service;

import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.UcenterVo;
import com.atguigu.educenter.vo.RegisterVo;
import com.atguigu.educenter.vo.UcentmentberVo;
import com.atguigu.eduservice.entity.UnReadMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-30
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);

    Integer countRegister(String day);

    List<UcenterMember> getUserByName(String nickname,String memberId);

    R addFriend(String friendid, String memberId);

    List<UcentmentberVo> findFriendReqByUserid(String memberId);

    R acceptFriendReq(String reqId);

    R ignoreFriendReq(String reqId);

    List<UcenterMember> ucenterMemberService(String memberId);


    boolean setMail(String userId, String mail);

    List<UcenterVo> getUserInfoByIds(List<UnReadMessage> list);
}
