package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-01
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;


    @Override
    public void registerCount(String day) {

        //添加记录之前删除表中相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper=new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        R r = ucenterClient.countRegister(day);
        Integer countRegister = (Integer)r.getData().get("countRegister");
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO
        //把获取数据添加到数据库，统计分析表里面
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);
        sta.setDateCalculated(day);
        sta.setLoginNum(loginNum);
        sta.setVideoViewNum(videoViewNum);
        sta.setCourseNum(courseNum);
        baseMapper.insert(sta);

    }

    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {
        //根据条件查询对于数据
        QueryWrapper<StatisticsDaily> wrapper=new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        //因为返回的有两部分数据：日期和日期对应的数量
        //掐断要求数组json结构，对应后端java代码是list集合
        //创建两个list集合，一个日期list一个list
        List<String> date_calculatedList=new ArrayList<>();
        List<String> numDataList=new ArrayList<>();
        //遍历查询所有数据list集合进行封装

        for (int i = 0; i < staList.size(); i++) {
            StatisticsDaily daily = staList.get(i);
            //封装日期
            date_calculatedList.add(daily.getDateCalculated());
            //封装对应的数量
            switch (type) {
                case "login_num":
                    numDataList.add(daily.getLoginNum()+"");
                    break;
                case "register_num":
                    numDataList.add(daily.getRegisterNum()+"");
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum()+"");
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum()+"");
                    break;
                default:
                    break;
            }
        }
        Map<String,Object> map=new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("numDataList",numDataList);
        return map;
    }
}
