package com.atguigu.vod.dao;


import com.atguigu.servicebase.entity.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MediaFileRepository extends MongoRepository<MediaFile,String> {
}
