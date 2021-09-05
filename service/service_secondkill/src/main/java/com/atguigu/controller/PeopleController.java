package com.atguigu.controller;

import com.atguigu.entity.people;
import com.atguigu.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("people")
public class PeopleController {

    @Autowired
    private PeopleService peopleService;

    @GetMapping("/{id}")
    public Mono<people> getById(@PathVariable String id){
        return peopleService.findById(id);
    }


    @GetMapping("/getAllPeople")
    public Flux<people> getAllPeople(){
        return peopleService.findAllPeople();
    }

    @PostMapping("/save")
    public Mono<Void> savePeople(@RequestBody people people){
        return peopleService.save(Mono.just(people));
    }


}
