package com.atguigu.edu_media_processor.dao;


import com.atguigu.servicebase.entity.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaFileRepository extends MongoRepository<MediaFile,String> {
}
