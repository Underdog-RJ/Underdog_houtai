package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterShuoshuo;
import com.atguigu.educenter.service.UcenterShuoshuoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/educenter/ucenter-shuoshuo")
public class UcenterShuoshuoController {

    @Autowired
    private UcenterShuoshuoService ucenterShuoshuoService;
    //1 查询讲师表所有数据
    @GetMapping("page/{current}/{limit}")
    @ApiOperation(value = "分页查询本人说说")
    public R findPageShuoshuo(@PathVariable Long current,@PathVariable Long limit, @RequestParam("userId") String userId){
//       Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        System.out.println("userID是："+userId);
        Page<UcenterShuoshuo> pageshuoshuo=new Page<>(current,limit);
        //调用service的方法实现查询所有的操作
        //创建page对象
        //构造条件
        QueryWrapper<UcenterShuoshuo> wrapper=new QueryWrapper<>();
        wrapper.eq("acl_user_id",userId);
        wrapper.orderByDesc("gmt_create");
        //调用方法实现条件查询分页
        ucenterShuoshuoService.page(pageshuoshuo, wrapper);
        long total = pageshuoshuo.getTotal();
        List<UcenterShuoshuo> records = pageshuoshuo.getRecords();
        Map<String, Object> map = new HashMap<>();

        map.put("items", records);
        map.put("current", pageshuoshuo.getCurrent());
        map.put("pages", pageshuoshuo.getPages());
        map.put("size", pageshuoshuo.getSize());
        map.put("total", pageshuoshuo.getTotal());
        map.put("hasNext", pageshuoshuo.hasNext());
        map.put("hasPrevious", pageshuoshuo.hasPrevious());
        return R.ok().data(map);
    }

    //2.逻辑删除讲师的方法
    @DeleteMapping("{id}")
    @ApiOperation(value = "逻辑删除说说")
    public R removeshuoshuo(@ApiParam(name = "id",value = "说说ID",required = true) @PathVariable String id){
        boolean flag = ucenterShuoshuoService.removeById(id);
        if(flag)
        {
            return R.ok();
        }else {
            return R.error();
        }
    }
    //添加课程分类
    @PostMapping("addshuoshuo")
    public R addShuoshuo(@RequestBody UcenterShuoshuo ucenterShuoshuo,HttpServletRequest request){
        System.out.println("方法执行了");
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        ucenterShuoshuo.setAclUserId(userIdByJwtToken.get("id"));
        ucenterShuoshuoService.save(ucenterShuoshuo);
        return R.ok();
    }


    //更改隐藏
    @PostMapping("hide")
    public R updateHide(@RequestBody Map map){
        UcenterShuoshuo ucenterShuoshuo = new UcenterShuoshuo();
        System.out.println("方法执行了");
        System.out.println("id"+map.get("id").toString()+"isHide:"+map.get("isHide"));
        ucenterShuoshuo.setId(map.get("id").toString());
        ucenterShuoshuo.setIsHide(Integer.valueOf(map.get("isHide").toString()));
        boolean flag = ucenterShuoshuoService.updateById(ucenterShuoshuo);
        System.out.println("更改结果"+flag);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

