package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;

import com.atguigu.eduservice.entity.CrmBanner;
import com.atguigu.eduservice.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-25
 */
@RestController
@RequestMapping("/eduservice/educms/bannerfront")
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;


    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public R index() {
        List<CrmBanner> list = bannerService.selectAllBanner();
        return R.ok().data("bannerList", list);
    }

}

