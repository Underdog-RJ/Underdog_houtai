package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.vo.BlogQuery;
import com.atguigu.eduservice.mapper.EduBlogMapper;
import com.atguigu.eduservice.service.EduBlogService;
import com.atguigu.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-03
 */
@Service
public class EduBlogServiceImpl extends ServiceImpl<EduBlogMapper, EduBlog> implements EduBlogService {

    @Autowired
    private EduBlogMapper eduBlogMapper;


    @Override
    public IPage<EduBlog> findByPage(Long page, Long limit, BlogQuery blogQuery) {
        Page<EduBlog> blogPage=new Page<>(page,limit);
        QueryWrapper<EduBlog> wrapper =new QueryWrapper<>();
        String title = blogQuery.getTitle();
        String begin = blogQuery.getBegin();
        String end = blogQuery.getEnd();
        String flag = blogQuery.getFlag();

        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(begin))
        {
            //构造模糊查询条件
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end))
        {
            //构造模糊查询条件
            wrapper.le("gmt_modified",end);
        }
        if(!StringUtils.isEmpty(flag)){
            wrapper.eq("flag",flag);
        }
        //排序
        wrapper.orderByDesc("gmt_create");

        return eduBlogMapper.selectPage(blogPage,wrapper);
    }


    @Override
    public Map<String, Object> findBypageFront(long page, long limit, BlogQuery blogQuery) {
        QueryWrapper<EduBlog> wrapper=new QueryWrapper<>();
        String title = blogQuery.getTitle();
        String begin = blogQuery.getBegin();
        String end = blogQuery.getEnd();
        String flag = blogQuery.getFlag();
        if(!StringUtils.isEmpty(blogQuery.getSubjectParentId())){//一级分类
            wrapper.eq("subject_parent_id",blogQuery.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(blogQuery.getSubjectId())) {
            wrapper.eq("subject_id", blogQuery.getSubjectId());
        }
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(begin))
        {
            //构造模糊查询条件
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end))
        {
            //构造模糊查询条件
            wrapper.le("gmt_modified",end);
        }
        if(!StringUtils.isEmpty(flag)){
            wrapper.eq("flag",flag);
        }
        Page<EduBlog> blogPage=new Page<>(page,limit);
        baseMapper.selectPage(blogPage,wrapper);
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
    public List<EduBlog> getBlogByUserId(String id) {

        return eduBlogMapper.getBlogByUserId(id);

    }


}
