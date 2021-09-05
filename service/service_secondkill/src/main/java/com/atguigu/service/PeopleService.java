package com.atguigu.service;

import com.atguigu.entity.people;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("PeopleService")
public interface PeopleService {

    // 根据id 查询
    Mono<people> findById(String id);

    // 查询全部用户
    Flux<people> findAllPeople();

    //添加用户
    Mono<Void> save(Mono<people> peopleMono);

}
