package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.UcenterShuoshuo;
import com.atguigu.educenter.mapper.UcenterShuoshuoMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.service.UcenterShuoshuoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UcenterMemberService ucenterMemberService;


    @Override
    public List<UcenterShuoshuo> getShuoshuoById(String id) {
        QueryWrapper<UcenterShuoshuo> wrapper=new QueryWrapper<>();
        wrapper.eq("acl_user_id",id);
        wrapper.eq("is_hide",1);
        List<UcenterShuoshuo> list = baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public R addShuoshuo(UcenterShuoshuo ucenterShuoshuo, HttpServletRequest request) {
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        if(StringUtils.isEmpty(userIdByJwtToken)){
            return R.error();
        }
        String id = userIdByJwtToken.get("id");
        ucenterShuoshuo.setAclUserId(userIdByJwtToken.get("id"));
        baseMapper.insert(ucenterShuoshuo);
        UcenterMember member = ucenterMemberService.getById(id);
        boolean flag = checkSign(id, LocalDate.now());
        if(!flag){
            boolean b = doSign(id, LocalDate.now());
            member.setUCoin(member.getUCoin()+5);
            ucenterMemberService.updateById(member);
        }
        return R.ok().data("userInfo",member);
    }


    public boolean doSign(String uid, LocalDate date) {
        int offset = date.getDayOfMonth() - 1;

        return redisTemplate.opsForValue().setBit(buildSignKey(uid, date), offset, true);
    }

    public boolean checkSign(String uid, LocalDate date) {
        int offset = date.getDayOfMonth() - 1;
        return redisTemplate.opsForValue().getBit(buildSignKey(uid, date), offset);
    }




    /**
     * String.format()  %s代表字符串，%d代表数字
     * @param uid
     * @param date
     * @return
     */
    private static String buildSignKey(String uid, LocalDate date) {
        return String.format("shuoshuo:sign:%s:%s", uid, formatDate(date));
    }

    private static String formatDate(LocalDate date) {
        return formatDate(date, "yyyyMM");
    }

    private static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }


}
