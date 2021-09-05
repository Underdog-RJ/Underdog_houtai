package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.builder.MatchQueryBuilderUnderdog;
import com.atguigu.eduservice.builder.MultiMatchQueryBuilderUnderdog;
import com.atguigu.eduservice.builder.QueryBuildersUnderdog;
import com.atguigu.eduservice.entity.CourseSearchParam;
import com.atguigu.eduservice.entity.EduPub;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EsCourseService;

import com.mongodb.QueryBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


    //课程搜索
    @Override
    public R list(int page, int size, CourseSearchParam courseSearchParam) {
        if(courseSearchParam==null){
            courseSearchParam=new CourseSearchParam();
        }


        //创建搜索请求对象
        SearchRequest searchRequest=new SearchRequest(index);

        //设置搜索类型
        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        // 过滤原字段
        String[] source_field_array = sourse_field.split(",");

        searchSourceBuilder.fetchSource(source_field_array,new String[]{});

        // 创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 搜索条件
        //根据关键字搜索
        if(!StringUtils.isEmpty(courseSearchParam.getKeyword())){

            MultiMatchQueryBuilderUnderdog field = QueryBuildersUnderdog.multiMatchQuery(courseSearchParam.getKeyword(), "title")
                    .minimumShouldMatch("70%")
                    .field("title", 10);
//            MultiMatchQueryBuilder
//            MatchQueryBuilderUnderdog title = new MatchQueryBuilderUnderdog("title", courseSearchParam.getKeyword());

            boolQueryBuilder.must(field);

        }

        // 根据一级分类
        if(!StringUtils.isEmpty(courseSearchParam.getSubjuct_level1_name())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("subjuct_level1_name",courseSearchParam.getSubjuct_level1_name()));
        }

        // 根据二级分类
        if(!StringUtils.isEmpty(courseSearchParam.getSubjuct_level2_name())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("subject_level2_name",courseSearchParam.getSubjuct_level2_name()));
        }

        //设置boolQueryBuilder到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);



        try {
            //执行搜索
            SearchResponse search = restHighLevelClient.search(searchRequest);
            //获取响应结果
            SearchHits hits = search.getHits();

            long totalHits = hits.totalHits;
            List<EduPub> list=new ArrayList<>();
            SearchHit[] searchHits = hits.getHits();

            for (SearchHit searchHit : searchHits) {
                EduPub eduPub=new EduPub();
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

                String title= (String) sourceAsMap.get("title");

                String cover= (String) sourceAsMap.get("cover");

                BigDecimal price=null;

                try {
                    if(sourceAsMap.get("price")!=null){
                      price=new BigDecimal((String) sourceAsMap.get("price"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                eduPub.setPrice(price);
                eduPub.setCover(cover);
                eduPub.setTitle(title);
                list.add(eduPub);
            }

            return R.ok().data("list",list).data("total",totalHits);



        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.error();

    }
}
