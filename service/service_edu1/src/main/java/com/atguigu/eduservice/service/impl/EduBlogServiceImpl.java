package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.BlogEnjoy;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.UcenterMemberPay;
import com.atguigu.eduservice.entity.vo.BlogQuery;
import com.atguigu.eduservice.mapper.BlogEnjoyMapper;
import com.atguigu.eduservice.mapper.EduBlogMapper;
import com.atguigu.eduservice.service.EduBlogService;
import com.atguigu.servicebase.entity.PageObject;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-03
 */
@Service
public class EduBlogServiceImpl extends ServiceImpl<EduBlogMapper, EduBlog> implements EduBlogService {

    @Autowired
    private EduBlogMapper eduBlogMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private BlogEnjoyMapper blogEnjoyMapper;

    @Override
    public IPage<EduBlog> findByPage(Long page, Long limit, BlogQuery blogQuery, HttpServletRequest request) {
        Page<EduBlog> blogPage = new Page<>(page, limit);
        QueryWrapper<EduBlog> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", 0);
        String title = blogQuery.getTitle();
        String begin = blogQuery.getBegin();
        String end = blogQuery.getEnd();
        String flag = blogQuery.getFlag();

        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(begin)) {
            //构造模糊查询条件
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            //构造模糊查询条件
            wrapper.le("gmt_modified", end);
        }
        if (!StringUtils.isEmpty(flag)) {
            wrapper.eq("flag", flag);
        }
        //排序
        wrapper.orderByDesc("gmt_create");
        Map<String, String> map = JwtUtils.getUserIdByJwtToken(request);
        if (!Objects.equals(map.get("nickname"), "admin")) {
            wrapper.eq("author_id", map.get("id"));
        }
        return eduBlogMapper.selectPage(blogPage, wrapper);
    }


    @Override
    public Map<String, Object> findBypageFront(long page, long limit, BlogQuery blogQuery) {
        QueryWrapper<EduBlog> wrapper = new QueryWrapper<>();
        String title = blogQuery.getTitle();
        String begin = blogQuery.getBegin();
        String end = blogQuery.getEnd();
        String flag = blogQuery.getFlag();
        wrapper.eq("is_deleted", 0);

        if (!StringUtils.isEmpty(blogQuery.getSubjectParentId())) {//一级分类
            wrapper.eq("subject_parent_id", blogQuery.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(blogQuery.getSubjectId())) {
            wrapper.eq("subject_id", blogQuery.getSubjectId());
        }
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(begin)) {
            //构造模糊查询条件
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            //构造模糊查询条件
            wrapper.le("gmt_modified", end);
        }
        if (!StringUtils.isEmpty(flag)) {
            wrapper.eq("flag", flag);
        }
        Page<EduBlog> blogPage = new Page<>(page, limit);
        baseMapper.selectPage(blogPage, wrapper);
        List<EduBlog> records = blogPage.getRecords();
        long current = blogPage.getCurrent();
        long pages = blogPage.getPages();
        long size = blogPage.getSize();
        long total = blogPage.getTotal();
        boolean hasNext = blogPage.hasNext();
        boolean hasPrevious = blogPage.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    @Override
    public PageObject getBlogByUserId(String id, Integer page, Integer size) {
        QueryWrapper<EduBlog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EduBlog::getAuthorId, id).eq(EduBlog::getIsDeleted, 0);
        Page<EduBlog> pageObj = new Page<>(page, size);
        IPage<EduBlog> iPage = eduBlogMapper.selectPage(pageObj, queryWrapper);
        PageObject<EduBlog> pageObject = new PageObject<>();
        pageObject.setResults(iPage.getRecords());
        pageObject.setTotal(iPage.getTotal());
        return pageObject;

    }

    @Override
    public R addBlogInfo(EduBlog eduBlog, HttpServletRequest request) {
        Map<String, String> user = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(user)) {
            return R.error();
        }
        String id = user.get("id");
        eduBlog.setAuthorId(id);
        eduBlog.setAuthorNickname(user.get("nickname"));
        eduBlog.setPublished(true);
        eduBlog.setRecommend(true);
        eduBlog.setAppreciation(true);
        eduBlog.setShareStatement(true);
        eduBlog.setFlag("Normal");
        eduBlog.setViewCount(1);
        baseMapper.insert(eduBlog);
        //检测用户今天是否已经签到
        boolean flag = checkSign(id, LocalDate.now());
        UcenterMemberPay ucenterPay = ucenterClient.getUcenterPay(id);
        if (!flag) {
            //没签到则更新为签到，并且更改用户的u币
            doSign(id, LocalDate.now());

            Integer uCoin = ucenterPay.getUCoin();
            ucenterPay.setUCoin(uCoin + 10);
            ucenterClient.updateUseruCoin(uCoin + 10, request.getHeader("token"));
        }

        return R.ok().data("userInfo", ucenterPay);
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
        return String.format("blog:sign:%s:%s", uid, formatDate(date));
    }

    private static String formatDate(LocalDate date) {
        return formatDate(date, "yyyyMM");
    }

    private static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public R deleteBlogById(String id) {
        EduBlog eduBlog = baseMapper.selectById(id);
        eduBlog.setIsDeleted(1);
        baseMapper.updateById(eduBlog);
        // 删除这个博客对应的收受列表
        QueryWrapper<BlogEnjoy> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BlogEnjoy::getBlogId,id);
        blogEnjoyMapper.delete(queryWrapper);
        return R.ok();
    }
}
