package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.order.UcenterMemberOrder;
import com.atguigu.educenter.entity.*;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.service.UcenterMemberZhuyeService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.educenter.vo.CountInfo;
import com.atguigu.educenter.vo.RegisterVo;
import com.atguigu.educenter.vo.UcentmentberVo;
import com.atguigu.eduservice.entity.UnReadMessage;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-30
 */
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @Autowired
    private UcenterMemberZhuyeService ucenterMemberZhuyeService;

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember ucenterMember){
        //调用service方法实现登录
        //返回token的值,使用jwt生成
        String token=ucenterMemberService.login(ucenterMember);
        return R.ok().data("token",token);
    }

    @GetMapping("thirdLogin/{code}")
    public R loginUser(@PathVariable String code){

        UcenterMember member=ucenterMemberService.loginUser(code);
        //使用jwt根据member对象生成token字符串
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return R.ok().data("member",member).data("token",jwtToken);
    }

    //修改用户信息
    @PostMapping("updateUser")
    public R updateUser(@RequestBody UcenterMember ucenterMember){
        ucenterMemberService.updateById(ucenterMember);
        return R.ok();
    }

    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    @GetMapping("setMobile/{mobile}/{code}")
    public R setMobile(@PathVariable String mobile,@PathVariable String code,HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        return ucenterMemberService.setMobile(mobile,code,userId);
    }

    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = ucenterMemberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    //根据token字符转获取用户信息
    @GetMapping("getUcenterPay/{memberId}")
    public UcenterMember getInfo(@PathVariable String memberId)
    {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = ucenterMemberService.getById(memberId);
        return ucenterMember;
    }


    //根据id获取用户信息
    @GetMapping("getUserInfoById/{memberId}")
    public R getUserInfoById(@PathVariable String memberId)
    {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = ucenterMemberService.getById(memberId);
        OtherMember member=new OtherMember();
        BeanUtils.copyProperties(ucenterMember,member);
        return R.ok().data("member",member);
    }



    //根据用户id获取用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member = ucenterMemberService.getById(id);
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    //查询某一天注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count=ucenterMemberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }

    /**
     * 搜索用户
     * @param nickname
     * @param request
     * @return
     */
    @GetMapping("getUserByName/{nickname}")
    public R getUserByName(@PathVariable String nickname,HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        List<UcenterMember> list=ucenterMemberService.getUserByName(nickname,memberId);
        return R.ok().data("list",list);
    }

    /**
     * 添加用户
     * @param friendid
     * @param request
     * @return
     */
    @GetMapping("addFriend/{friendid}")
    public R addFriend(@PathVariable String friendid,HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        return ucenterMemberService.addFriend(friendid,memberId);
    }

    /**
     * 根据用户id查询请求添加他的好友
     * @param request
     * @return
     */
    @GetMapping("findFriendReqByUserid")
    public R findFriendReqByUserid(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        List<UcentmentberVo> list= ucenterMemberService.findFriendReqByUserid(memberId);
        return R.ok().data("list",list);
    }

    /**
     * 同意添加对方为好友
     */
    @GetMapping("acceptFriendReq/{reqId}")
    public R acceptFriendReq(@PathVariable String reqId){

        return ucenterMemberService.acceptFriendReq(reqId);
    }

    /**
     * 拒绝添加对方为好友
     */
    @GetMapping("ignoreFriendReq/{reqId}")
    public R ignoreFriendReq(@PathVariable String reqId){
        return ucenterMemberService.ignoreFriendReq(reqId);
    }


    @GetMapping("getAllFriendByUserId")
    public R getAllFriendByUserId(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        List<UcenterMember> list=ucenterMemberService.ucenterMemberService(memberId);
        return R.ok().data("list",list);
    }


    @PostMapping("addOwnPage")
    public R addOwnPage(HttpServletRequest request,@RequestBody UcenterMemberZhuye ucenterMemberZhuye){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        ucenterMemberZhuyeService.addOwnPage(memberId,ucenterMemberZhuye);
        return R.ok();
    }


    @GetMapping("getOwnPage")
    public R getOwnPage(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMemberZhuye ucenterMemberZhuye=ucenterMemberZhuyeService.getOwnPage(memberId);
        return R.ok().data("ucenterMemberZhuye",ucenterMemberZhuye);
    }

    //根据用户id获取用户主页
    @GetMapping("getOwnPageById/{memberId}")
    public R getOwnPageById(@PathVariable String memberId){
        UcenterMemberZhuye ucenterMemberZhuye=ucenterMemberZhuyeService.getOwnPage(memberId);
        return R.ok().data("ucenterMemberZhuye",ucenterMemberZhuye);
    }


    @GetMapping("setOwnMail/{mail}")
    public R setMail(@PathVariable String mail){
        return  ucenterMemberService.setMail(mail);
    }

    @GetMapping("valideOwnMail/{mail}/{code}")
    public R valideOwnMail(@PathVariable String mail,@PathVariable String code,HttpServletRequest request){
        String memeberId = JwtUtils.getMemberIdByJwtToken(request);

        return  ucenterMemberService.valideOwnMail(memeberId,mail,code);
    }

    /**
     * 根据批量用户ids查询用户信息
     */
    @PostMapping("getUserInfoByIds")
    public R getUserInfoByIds(@RequestBody List<UnReadMessage> list){

        List<UcenterVo> result =ucenterMemberService.getUserInfoByIds(list);
        return R.ok().data("list",result);
    }


    /**
     * 获取用户的收藏数，说说数
     */
    @GetMapping("getUserCountInfo")
    public R getUserCountInfo(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        CountInfo info=ucenterMemberService.getUserCountInfo(userId);
        return R.ok().data("countInfo",info);
    }

    /**
     * 根据id获取用户的收藏数，说说数
     */
    @GetMapping("getUserCountInfoById/{memberId}")
    public R getUserCountInfo(@PathVariable String memberId){
        CountInfo info=ucenterMemberService.getUserCountInfo(memberId);
        return R.ok().data("countInfo",info);
    }

    /**
     * 根据用户id修改密码
     */
    @GetMapping("updateUserPassword/{password}")
    public R updateUserPassword(@PathVariable String password, HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember  ucenterMember=ucenterMemberService.updateUserPassword(userId,password);
        return R.ok().data("ucenterMember",ucenterMember);
    }


    //根据邮箱重置密码
    @PostMapping("resetPassword")
    public R resetPassword(@RequestBody ResetPasswordVo resetPasswordVo){

        return  ucenterMemberService.resetPassword(resetPasswordVo);
    }
}

