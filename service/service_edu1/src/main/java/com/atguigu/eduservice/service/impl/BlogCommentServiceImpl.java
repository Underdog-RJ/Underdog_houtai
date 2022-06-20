package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.BlogComment;
import com.atguigu.eduservice.entity.UcenterMemberPay;
import com.atguigu.eduservice.mapper.BlogCommentMapper;
import com.atguigu.eduservice.service.BlogCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-04-24
 */
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements BlogCommentService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UcenterClient ucenterClient;


    @Override
    public R addCommit(BlogComment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        comment.setMemberId(memberId);

        UcenterMemberPay ucenterInfo = ucenterClient.getUcenterPay(memberId);

        comment.setNickname(ucenterInfo.getNickname());
        comment.setAvatar(ucenterInfo.getAvatar());

        baseMapper.insert(comment);

        //检测用户今天是否已经签到
        boolean flag = checkSign(memberId, LocalDate.now());
        UcenterMemberPay ucenterPay = ucenterClient.getUcenterPay(memberId);
        if (!flag) {
            //没签到则更新为签到，并且更改用户的u币
            doSign(memberId, LocalDate.now());
            Integer uCoin = ucenterPay.getUCoin();
            ucenterPay.setUCoin(uCoin + 5);
            ucenterClient.updateUseruCoin(uCoin + 5, request.getHeader("token"));
        }

        return R.ok().data("userInfo", ucenterPay).data("comment",comment);
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
     *
     * @param uid
     * @param date
     * @return
     */
    private static String buildSignKey(String uid, LocalDate date) {
        return String.format("blogCommit:sign:%s:%s", uid, formatDate(date));
    }

    private static String formatDate(LocalDate date) {
        return formatDate(date, "yyyyMM");
    }

    private static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public R commentChild(String blogId, Integer page, Integer size) {
        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BlogComment::getParentId, blogId);
        IPage<BlogComment> p = new Page<>(page, size);
        IPage<BlogComment> blogCommentIPage = baseMapper.selectPage(p, queryWrapper);
        return R.ok().data("list", blogCommentIPage.getRecords());
    }
}
