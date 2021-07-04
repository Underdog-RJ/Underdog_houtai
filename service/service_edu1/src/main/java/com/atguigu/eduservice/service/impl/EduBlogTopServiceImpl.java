package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.EduBlogTop;
import com.atguigu.eduservice.entity.UcenterMemberPay;
import com.atguigu.eduservice.entity.vo.BlogQuery;
import com.atguigu.eduservice.mapper.EduBlogMapper;
import com.atguigu.eduservice.mapper.EduBlogTopMapper;
import com.atguigu.eduservice.service.EduBlogService;
import com.atguigu.eduservice.service.EduBlogTopService;
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
 *  服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-03
 */
@Service
public class EduBlogTopServiceImpl extends ServiceImpl<EduBlogTopMapper, EduBlogTop> implements EduBlogTopService {


}
