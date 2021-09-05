package com.atguigu.service.impl;

import com.atguigu.entity.people;
import com.atguigu.service.PeopleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class PeopleServiceImpl implements PeopleService {

    private final static Map<String,people> map=new HashMap(){
        {
            put("1", new people("zhangsan", "12"));
            put("2",new people("李四","13"));
            put("3",new people("王五","13"));
        }
    };

    @Override
    public Mono<people> findById(String id) {
        return Mono.justOrEmpty(map.get(id));
    }

    @Override
    public Flux<people> findAllPeople() {
        return Flux.fromIterable(map.values());
    }

    @Override
    public Mono<Void> save(Mono<people> peopleMono) {
        return peopleMono.doOnNext(people -> {
            int size = map.size()+1;
            map.put(size+"",people);
        }).thenEmpty(Mono.empty());
    }
}
