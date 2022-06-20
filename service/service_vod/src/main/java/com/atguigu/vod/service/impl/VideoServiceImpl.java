package com.atguigu.vod.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.entity.PageObject;
import com.atguigu.servicebase.entity.media.MediaFile;
import com.atguigu.vod.dao.MediaFileRepository;
import com.atguigu.vod.entity.QueryMediaFileRequest;
import com.atguigu.vod.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    MediaFileRepository mediaFileRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public R findVideo(HttpServletRequest request, Integer page, Integer size, QueryMediaFileRequest queryMediaFileRequest) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        Pageable pageable = PageRequest.of(page - 1, size);
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (memberId != null) {
            criteria.and("userId").is(memberId);
        }
        if (!StringUtils.isEmpty(queryMediaFileRequest.getFileOriginalName())) {
            Pattern pattern = Pattern.compile(".*?" + queryMediaFileRequest.getFileOriginalName() + ".*");
            criteria.and("fileOriginalName").regex(pattern);
        }
        if (!StringUtils.isEmpty(queryMediaFileRequest.getFileType())) {
            criteria.and("fileType").is(queryMediaFileRequest.getFileType());
        }
        if (!StringUtils.isEmpty(queryMediaFileRequest.getProcessStatus())) {
            criteria.and("processStatus").is(queryMediaFileRequest.getProcessStatus());
        }
        query.addCriteria(criteria);

        long count = mongoTemplate.count(query, MediaFile.class);

        List<MediaFile> mediaFiles = mongoTemplate.find(query.with(pageable), MediaFile.class);
        Page<MediaFile> wPage = new PageImpl(mediaFiles, pageable, count);
        return R.ok().data("pageObj", wPage);
    }
}
