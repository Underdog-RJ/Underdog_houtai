package com.atguigu.pierce.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.commonutils.R;
import com.atguigu.pierce.entity.Client;
import com.atguigu.pierce.netty.config.ProxyConfig;
import com.atguigu.pierce.service.PierceService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 隧道控制器
 */
@RestController
@RequestMapping("pierceservice")
@CrossOrigin
public class PierceController {

    @Autowired
    PierceService pierceService;

    @PutMapping("updateClient")
    public R updateClient(@RequestBody Client client){
        return pierceService.updateClient(client);
    }

    @PostMapping("getUserMetrics/{page}/{size}")
    public R getUserMetrics(@PathVariable Integer page,@PathVariable Integer size,HttpServletRequest request){
        return pierceService.getUserMetrics(page,size,request);
    }


    /**
     * 创建隧道
     * @param httpServletRequest
     * @param client
     * @return
     */
    @PostMapping("createTunnel")
    public R createTunnel(HttpServletRequest httpServletRequest, @RequestBody Client client) {
      return   pierceService.createTunnel(client,httpServletRequest);
    }

    /**
     * 获取用户的通道
     * @return
     */
    @PostMapping("getUserChannel/{page}/{size}")
    public R getUserChannel(HttpServletRequest request, @PathVariable Integer page,@PathVariable Integer size){
        return pierceService.getUserChannel(request,page,size);
    }


    @GetMapping("getAll")
    public R getAll() {
        String str = "[{'name':'myProxy','clientKey':'bc14ea4e7a1342098b17179b1fc2a96f','proxyMappings':[{'inetPort':9000,'lan':'127.0.0.1:8080','name':'MyProxy'},{'inetPort':9001,'lan':'172.17.0.1:8001','name':'mytest'}],'status':1},{'name':'SpiderMan','clientKey':'fe9106699a764370849b7f7c27c6a5fa','proxyMappings':[{'inetPort':9002,'lan':'127.0.0.1:9090','name':'spidermantest'}],'status':0},{'name':'ads','clientKey':'b82f21c357e343a484e3650f8620460a','proxyMappings':[],'status':0}]";
        str = (String) JSON.toJSON(str);

        return R.ok().data("test", "test");
    }


    /**
     * 检测是否还有公网端口号
     * @return
     */
    @GetMapping("hasPort")
    public R hasPort() {
        return pierceService.hasPort();
    }


    @PutMapping("changeClientKey")
    public R changeClientKey(@RequestBody Client client){
        return pierceService.changeClientKey(client);
    }
}
