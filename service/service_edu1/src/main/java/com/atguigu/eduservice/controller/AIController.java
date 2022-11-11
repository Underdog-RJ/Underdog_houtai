package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.BlogEnjoy;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.ImageQuery;
import com.atguigu.eduservice.mapper.EduBlogMapper;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.AIService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClientFactory;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@RestController
@RequestMapping("/eduservice/deeplearn")
public class AIController {

    @Autowired
    EduSubjectMapper eduSubjectMapper;

    @Autowired
    EduBlogMapper eduBlogMapper;

    public Map<String, EduSubject> getAllSubject() {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.ne("parent_id", 0);
        List<EduSubject> eduSubjects = eduSubjectMapper.selectList(wrapper);
        Map<String, EduSubject> map = new HashMap<>();
        for (EduSubject eduSubject : eduSubjects) {
            map.put(eduSubject.getTitle(), eduSubject);
        }
        return map;
    }

    @GetMapping("/test")
    public void test() {
        MongoDatabase connect = get_connect();
        Map<String, EduSubject> allSubject = getAllSubject();
        MongoCollection<Document> thirdBooking = connect.getCollection("thirdBooking");
        MongoCollection<Document> fsBooking = connect.getCollection("fsBooking");
        FindIterable<Document> documents = thirdBooking.find();
        for (Document document : documents) {
            EduBlog eduBlog = new EduBlog();
            eduBlog.setAuthorId("1");
            eduBlog.setAuthorNickname("admin");
            eduBlog.setDescption(document.getString("b_description").trim());
            EduSubject eduSubject = allSubject.get(document.getString("s_title").split(" （")[0].trim());
            eduBlog.setSubjectId(eduSubject.getId());
            eduBlog.setSubjectParentId(eduSubject.getParentId());
            eduBlog.setTitle(document.getString("book_title").trim());
            Bson bson = Filters.eq("book_url", document.getString("book_url"));
            String first_pic = fsBooking.find(bson).iterator().next().getString("first_pic");
            eduBlog.setFirstPicture(first_pic);
            eduBlog.setContent(document.getString("b_description"));
            eduBlog.setAppreciation(true);
            eduBlog.setZsPicture("https://underdogedu.oss-cn-beijing.aliyuncs.com/%E7%B4%A0%E6%9D%90/88abf95a1dc9325bcb6c3de8001e2e2.jpg");
            eduBlog.setFlag("Normal");
            eduBlog.setPublished(true);
            eduBlog.setRecommend(true);
            eduBlog.setShareStatement(true);
            eduBlog.setViewCount(document.getInteger("yuedu"));
            eduBlog.setType(0);
            eduBlog.setMdContent(" ");
            eduBlog.setIsDeleted(0);
            eduBlog.setBLanguage(document.getString("language").equals("中文") ? 0 : 1);
            eduBlog.setShoucang(document.getInteger("shoucang"));
            eduBlog.setZhangjie(document.getInteger("zhangjie"));
            List<String> tags = document.getList("tags", String.class);
            StringBuilder sb = new StringBuilder();
            tags.forEach(t -> sb.append(t + ","));
            eduBlog.setTags(sb.toString());
            eduBlogMapper.insert(eduBlog);

        }
    }


    MongoDatabase get_connect() {
        MongoClient client = new MongoClient("10.1.1.146", 27017);
        MongoDatabase thirdBooking = client.getDatabase("booking");
        return thirdBooking;
    }

    @Autowired
    private AIService aiService;


    @PostMapping("/getAllImage")
    public R getAllImage(MultipartFile file, ImageQuery imageQuery) {
        if (imageQuery == null) {
            imageQuery = new ImageQuery("AttentionUnet");
        }
        return aiService.getAllImage(file, imageQuery.getModelName());
    }


}
