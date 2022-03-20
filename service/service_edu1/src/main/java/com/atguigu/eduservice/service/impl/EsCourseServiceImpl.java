package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;

import com.atguigu.eduservice.entity.CourseSearchParam;
import com.atguigu.eduservice.entity.EduPub;
import com.atguigu.eduservice.service.EsCourseService;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.io.IOException;

@Service
public class EsCourseServiceImpl implements EsCourseService {

    @Value("${underdog.course.index}")
    private String index;

    @Value("${underdog.course.type}")
    private String type;

    @Value("${underdog.course.sourse_field}")
    private String sourse_field;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    private final static String PREFIX_RANK = "prefix_rank:";


    //课程搜索
    @Override
    public R list(int page, int size, CourseSearchParam courseSearchParam) {
        if (courseSearchParam == null) {
            courseSearchParam = new CourseSearchParam();
        }

        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);

        //设置搜索类型
//        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 过滤原字段
        String[] source_field_array = sourse_field.split(",");

        searchSourceBuilder.fetchSource(source_field_array, new String[]{});

        // 创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 搜索条件
        //根据关键字搜索
        if (!StringUtils.isEmpty(courseSearchParam.getKeyword())) {
            MultiMatchQueryBuilder field = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "title", "teacher_name")
                    .minimumShouldMatch("70%")
                    .field("title", 10);
            boolQueryBuilder.must(field);
        }

        // 根据一级分类
        if (!StringUtils.isEmpty(courseSearchParam.getSubjuct_level1_name())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("subjuct_level1_name", courseSearchParam.getSubjuct_level1_name()));
        }

        // 根据二级分类
        if (!StringUtils.isEmpty(courseSearchParam.getSubjuct_level2_name())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("subject_level2_name", courseSearchParam.getSubjuct_level2_name()));
        }

        //设置boolQueryBuilder到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        // 设置分页参数
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }
        int from = (page - 1) * size;
        searchSourceBuilder.from(from); // 记录起始下表
        searchSourceBuilder.size(size); // 记录大小
        // 设置高亮标签
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</front>");
        // 设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("title"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("teacher_name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);


        try {
            //执行搜索
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //获取响应结果
            SearchHits hits = search.getHits();

            long totalHits = hits.totalHits;
            List<EduPub> list = new ArrayList<>();
            SearchHit[] searchHits = hits.getHits();

            for (SearchHit searchHit : searchHits) {
                EduPub eduPub = new EduPub();
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

                String title = (String) sourceAsMap.get("title");

                String cover = (String) sourceAsMap.get("cover");

                BigDecimal price = null;

                try {
                    if (sourceAsMap.get("price") != null) {
                        price = new BigDecimal((String) sourceAsMap.get("price"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                eduPub.setPrice(price);
                eduPub.setCover(cover);
                eduPub.setTitle(title);
                list.add(eduPub);
            }

            return R.ok().data("list", list).data("total", totalHits);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.error();

    }

    /**
     * 获取前10个排行榜
     *
     * @return
     */
    @Override
    public R searchTop() {
        Set set = redisTemplate.opsForZSet().reverseRange(PREFIX_RANK, 0, 10);
        return R.ok().data("top", set);
    }

    @Override
    public R updateKeyWord(String keyword) {
        Double score = redisTemplate.opsForZSet().score(PREFIX_RANK, keyword);
        if (score == null)
            score = 0d;
        redisTemplate.opsForZSet().add(PREFIX_RANK, keyword, score + 1);
        return R.ok().message("添加成功");
    }
}
