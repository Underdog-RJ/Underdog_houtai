package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.order.UcenterMemberOrder;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.educenter.vo.RegisterVo;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember ucenterMember){
        //调用service方法实现登录
        //返回token的值,使用jwt生成
        String token=ucenterMemberService.login(ucenterMember);
        return R.ok().data("token",token);
    }

    @GetMapping("appWxLogin/{code}")
    public R appWxLogin(@PathVariable String code){
        //获取code值，临时票据，类似于验证码
        try {
            //拿着code请求没微信固定的地址，得到两个值：access_token和openid
            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            // 拼接三个参数 id 密钥，code
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            //请求这个拼接好的地址，得到返回两个值access_token和openid
            //使用httpClient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //从accessTokenInfo字符串获取出来这两个值，access_token和openid
            //把accessTokenInfo字符串转换为map集合，根据map里面的key获取对应的值
            Gson gson=new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String)mapAccessToken.get("access_token");
            String openid = (String)mapAccessToken.get("openid");

            UcenterMember member=ucenterMemberService.getOpenIdMember(openid);
            if(member==null)
            {
                //拿着得到access_token和openid再去请求微信提供固定的地址，获取扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接连个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                access_token,
                        openid
                );
                //发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String)userInfoMap.get("nickname");
                String headimgurl = (String)userInfoMap.get("headimgurl");//头像
                Double sex = (Double)userInfoMap.get("sex");//头像
                member=new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                member.setSex(sex.intValue());
                ucenterMemberService.save(member);

            }
            return R.ok().data("userInfo",member);
        } catch (Exception e) {
            throw new GuliException(20001,"登陆失败");
        }
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

}

