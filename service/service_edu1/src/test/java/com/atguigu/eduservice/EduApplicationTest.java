package com.atguigu.eduservice;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.EduBlogIndex;
import com.atguigu.eduservice.mapper.EduBlogMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EduApplicationTest {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    EduBlogMapper eduBlogMapper;


    @Test
    public void testInsert() {

        IndexRequest request = new IndexRequest("underdog_blog");
        List<EduBlog> list = eduBlogMapper.selectList(null);
        for (EduBlog eduBlog : list) {
            System.out.println(eduBlog);
            Map<String, Object> map = new HashMap<>();
            map.put("id", eduBlog.getId());
            map.put("author_nickname", eduBlog.getAuthorNickname());
            map.put("description", eduBlog.getDescption());
            map.put("title", eduBlog.getTitle());
            map.put("firstPicture", eduBlog.getFirstPicture());
            map.put("viewCount", eduBlog.getViewCount());
            map.put("gmtCreate", eduBlog.getGmtCreate());
            map.put("content", eduBlog.getContent());
            request.type("_doc");
            request.source(map);
            IndexResponse response = null;
            try {
                response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
                if (response.getResult().name().equalsIgnoreCase("created")) {
                    System.out.println("成功");
                } else {
                    System.out.println("失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
       /* IndexRequest request = new IndexRequest("chinesearticleindex");
        for (Document document : documents) {
            request.source(map);
            IndexResponse response = null;
            try {
                response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
                if (response.getResult().name().equalsIgnoreCase("created")) {
                    System.out.println("成功");
                } else {
                    System.out.println("失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        /**
         * 批量从插入数据
         */
      /*  BulkRequest request = new BulkRequest();
        for (int j = 0; j < list.size(); j++) {
            Map<String, Object> item = list.get(j);
            request.add(new IndexRequest("chinesearticleindex").source(item));
        }
        try {
            BulkResponse bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            if (bulk.status().getStatus() == 200) {
                System.out.println("成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
