package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.TbFriend;
import com.atguigu.educenter.entity.TbFriendReq;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.TbFriendReqService;
import com.atguigu.educenter.service.TbFriendService;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.MailUtils;
import com.atguigu.educenter.vo.RegisterVo;

import com.atguigu.educenter.vo.UcentmentberVo;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-30
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private TbFriendService tbFriendService;

    @Autowired
    private TbFriendReqService tbFriendReqService;

    @Override
    public String login(UcenterMember ucenterMember) {

        //获取登录手机号和密码
        String password = ucenterMember.getPassword();
        String mobile = ucenterMember.getMobile();

        //手机号和密码是否为空
        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile)){
            throw new GuliException(20001,"登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobieMember = baseMapper.selectOne(wrapper);
        if(mobieMember==null){
            throw new GuliException(20001,"登录失败");
        }

        //判断密码
        if(!MD5.encrypt(password).equals(mobieMember.getPassword()))
        {
            throw new GuliException(20001,"登录失败");
        }

        //判断用户是否禁用
        if(mobieMember.getIsDisabled()){
            throw new GuliException(20001,"登录失败");
        }
        //登录成功
        //生成token字符串，使用jwt工具类
        String token = JwtUtils.getJwtToken(mobieMember.getId(), mobieMember.getNickname());
        return token;
    }

    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息，进行校验
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        //校验参数
        if(StringUtils.isEmpty(nickname) ||
                StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new GuliException(20001,"注册失败");
        }

        //判断验证码
        //获取验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!redisCode.equals(redisCode)){
            throw new GuliException(20001,"注册失败");
        }

        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuliException(20001,"注册失败");
        }

        //添加到数据库
        //添加注册信息到数据库
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");

        this.save(member);
    }
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer countRegister(String day) {

        return baseMapper.countRegister(day);
    }

    @Override
    public List<UcenterMember> getUserByName(String nickname,String memeberId) {
        QueryWrapper<UcenterMember> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("nickname",nickname);
        List<UcenterMember> ucenterMembers = baseMapper.selectList(queryWrapper);
        List<UcenterMember> collect = ucenterMembers.stream().filter(ucenterMember -> !ucenterMember.getId().equals(memeberId)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public R addFriend(String friendid, String memberId) {

        QueryWrapper<TbFriend> tbFriendQueryWrapper=new QueryWrapper<>();

        tbFriendQueryWrapper.eq("userid",memberId);
        tbFriendQueryWrapper.eq("friends_id",friendid);

        TbFriend one = tbFriendService.getOne(tbFriendQueryWrapper);
        if(!StringUtils.isEmpty(one)){
            return R.ok().message("已经是您的好友了");
        }

        QueryWrapper<TbFriendReq> tbFriendReqQueryWrapper=new QueryWrapper<>();

        tbFriendReqQueryWrapper.eq("from_userid",memberId);
        tbFriendReqQueryWrapper.eq("to_userid",friendid);
        tbFriendReqQueryWrapper.eq("status",0);

        TbFriendReq tbFriendReq = tbFriendReqService.getOne(tbFriendReqQueryWrapper);

        if(!StringUtils.isEmpty(tbFriendReq)){
            return R.ok().message("您已经申请过了");
        }


        TbFriendReq tbF=new TbFriendReq();
        tbF.setFromUserid(memberId);
        tbF.setToUserid(friendid);
        tbF.setStatus(0);
        tbFriendReqService.save(tbF);
        return R.ok().message("申请成功");
    }

    @Override
    public List<UcentmentberVo> findFriendReqByUserid(String memberId) {
        QueryWrapper<TbFriendReq> tbFriendReqQueryWrapper=new QueryWrapper<>();
        tbFriendReqQueryWrapper.eq("to_userid",memberId);
        tbFriendReqQueryWrapper.eq("status",0);

        List<TbFriendReq> listReq = tbFriendReqService.list(tbFriendReqQueryWrapper);
        List<String> ids = listReq.stream().map(tbFriendReq -> tbFriendReq.getFromUserid()).collect(Collectors.toList());

        if(ids.size()==0){
           return null;
        }
        List<UcenterMember> ucenterMemberList = baseMapper.selectBatchIds(ids);
        List<UcentmentberVo> finalList=new ArrayList<>();
        for (TbFriendReq tbFriendReq : listReq) {
            for (UcenterMember ucenterMember : ucenterMemberList) {
                if(tbFriendReq.getFromUserid().equals(ucenterMember.getId())){
                    UcentmentberVo ucentmentberVo=new UcentmentberVo();
                    BeanUtils.copyProperties(ucenterMember,ucentmentberVo);
                    ucentmentberVo.setReqId(tbFriendReq.getId());
                    finalList.add(ucentmentberVo);
                }
            }
        }
        return finalList;
    }

    @Override
    public R acceptFriendReq(String reqId) {
        //1.将好友请求的status标志设置为1，表示已经处理了该还有请求

        TbFriendReq one = tbFriendReqService.getById(reqId);
        if(!one.getStatus().equals(0)){
            return R.ok().message("已处理");
        }
        one.setStatus(1);
        tbFriendReqService.updateById(one);
        String friendId = one.getFromUserid();
        String memberId = one.getToUserid();
        //2.互相添加好友，在tb_friend表中应该有两条记录
        TbFriend oneFriend =new TbFriend();
        oneFriend.setFriendsId(friendId);
        oneFriend.setUserid(memberId);

        TbFriend twoFriend =new TbFriend();
        twoFriend.setFriendsId(memberId);
        twoFriend.setUserid(friendId);

        List<TbFriend> list=new ArrayList<>();
        list.add(oneFriend);
        list.add(twoFriend);

        tbFriendService.saveBatch(list);
        return R.ok().message("添加成功");
    }

    @Override
    public R ignoreFriendReq(String reqId) {
        //1.将好友请求的status标志设置为1，表示已经处理了该还有请求

        TbFriendReq one = tbFriendReqService.getById(reqId);
        if(!one.getStatus().equals(0)){
            return R.ok().message("已处理");
        }
        one.setStatus(1);
        tbFriendReqService.updateById(one);
        return R.ok().message("已拒绝");
    }

    @Override
    public List<UcenterMember> ucenterMemberService(String memberId) {

         QueryWrapper<TbFriend> tbFriendQueryWrapper=new QueryWrapper<>();
         tbFriendQueryWrapper.eq("userid",memberId);
        List<TbFriend> list = tbFriendService.list(tbFriendQueryWrapper);
        List<String> ids = list.stream().map(tbFriend -> tbFriend.getFriendsId()).collect(Collectors.toList());
        if(ids.size()==0){
            return null;
        }
        List<UcenterMember> memberList = baseMapper.selectBatchIds(ids);
        return memberList;
    }




    @Override
    public boolean setMail(String userId, String mail) {
        boolean flag = MailUtils.sendMail(mail, "儿子", "我是你爸爸");
        if(flag==true){
            UcenterMember ucenterMember = baseMapper.selectById(userId);
            ucenterMember.setMail(mail);
            ucenterMember.setMailType(0);
            baseMapper.updateById(ucenterMember);
        }
        return flag;
    }
}
