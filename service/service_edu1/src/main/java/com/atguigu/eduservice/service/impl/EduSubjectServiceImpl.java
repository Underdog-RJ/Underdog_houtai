package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-21
 */

/**
 * 整合springCache简化缓存开发
 *  1.引入依赖
 *      spring-boot-starter-cache,spring-boot-starter-data-redis
 *  2.写配置
 *      1.自动配置了那些
 *          CacheAutoConfiguration会导入RedisCacheConfiguretion
 *          自动配置好缓存管理器RedisCacheManger
 *      2.配置使用redis作为缓存
 *          spring.cache.type = redis
 * 3.测试使用缓存
 *          @Cacheable 触发将数据库保存到缓存中
 *          @CacheEvict 触发将缓存删除的操作
 *          @CachePut 不影响方法执行更新缓存  双写模式写，必须有返回值
 *          @Cacheing 组合以上多个操作
 *          @CacheCongig 在类级别共享缓存的相同配置
 *          1.开启缓存功能，@EnchaleCacheing
 *          2.只需要使用注解就能完成缓存操作
 *4.级联更新又有关联的数据
 *      @CacheEvit：失效模式
 *      1.同时进行多种缓存操作，@Caching
 *
 *     @Caching(evict = {
 *             @CacheEvict(value = "category",key = "'1'"),
 *             @CacheEvict(value = "category",key = "'2'")
 *     })
 *      2.指定删除某个分区下的所有数据@CacheEvict(value="categoty",allEntries=true)
 *      3.存储统一类型的数据，都可以指定在同一个分区.分区名作为默认的前缀
 *5.Spring-Cache的不足
 *  1.读模式
 *      缓存穿透：查询一个null数据。解决：缓存空数据: cache-null-value=true
 *      缓存击穿：大量并发进来同时查询一个正好过期的数据。解决：加锁 默认是无加锁的
 *      缓存雪崩：大量的key同时过期，解决：加随机时间。Spring-cache-redis-time-to-live
 *  2.写模式(缓存与数据库一值)
 *      1.读写加锁
 *      2.引入Canal，感知到Mysql的更新去更新数据库
 *      3.都多写多，直接去数据库查询就行。
 *  3.总结：
 *      常规数据(都多写少，即时性，已制定要求不高的数据)  完全可以使用Spring-Cache：写模式，（只要在缓存的数据加了过期时间就足够了）
 *      特殊数据：特殊设计
 *6.原理
 *      CacheManager(RedisChacheManager) -> Cache (RedisCache) -> Cache负责缓存的读写
 *
 *
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public void addSubject(MultipartFile file, EduSubjectService subjectService) {
        try {
            //文件输入流
            InputStream in = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(in, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
            redisTemplate.delete("catelogJSON");
        } catch (Exception e) {

        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        // 给缓存中放 JSON字符串，拿出的json字符串，还用逆转为能用的对象【序列化与反序列化】
        //1.加入缓存的逻辑，缓存中村的数据是json字符串
        String catelogJSON = redisTemplate.opsForValue().get("catelogJSON");
        if (StringUtils.isEmpty(catelogJSON)) {
            //缓存中没有，查询数据库
            List<OneSubject> allOneTwoSubjectFromBb = getAllOneTwoSubjectFromBbWithRedisLock();
            return allOneTwoSubjectFromBb;
        }
        //如果为复杂类型，可以使用TypeReference来转换
        List<OneSubject> oneSubjects = JSON.parseObject(catelogJSON, new TypeReference<List<OneSubject>>() {
        });
        return oneSubjects;

    }

    // 从数据库中查找
    public List<OneSubject> getAllOneTwoSubjectFromBbWithRedisLock() {

        //1.占分布式锁，去redis占坑
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
        if(lock){
            //枷锁成功，执行业务
            //2,设置过期时间，必须和加锁是同步的原子的
            List<OneSubject> dateFromDB;
            try {
                dateFromDB = getDateFromDB();
            } finally {
                String script="if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                Long lock1 = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }

            //获取值对比，对比成功删除=原子操作,lua脚本的解锁
//            String lockValue = redisTemplate.opsForValue().get("lock");
//            if(Objects.equals(uuid,lockValue)){
//                //删除锁
//                redisTemplate.delete("lock");
//            }


            return dateFromDB;
        }else {
            //加锁失败，重试。
            //休眠100mx 重试
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getAllOneTwoSubjectFromBbWithRedisLock();// 自旋的方式获取锁
        }
    }

    private List<OneSubject> getDateFromDB() {
        //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
        String catelogJSON = redisTemplate.opsForValue().get("catelogJSON");

        if (!StringUtils.isEmpty(catelogJSON)) {
            List<OneSubject> oneSubjects = JSON.parseObject(catelogJSON, new TypeReference<List<OneSubject>>() {
            });
            return oneSubjects;
        }


        //1.查询所有一级分类 parentid=0
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);//baseMapper 是mybatis自动注入的 不用引入  this.list

        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperOne.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);//baseMapper 是mybatis自动注入的 不用引入  this.list

        //2.创建List集合，用于存储最终封装数据
        List<OneSubject> finalSubjectList = new ArrayList<>();

        //3.封装一级分类
        //查询出来所有的一级分类list集合遍历，得到每个一级分类对象，获取每个一级分类对象值
        //封装到要求的list集合里面list<OneSubject> finalSubjectList
        for (int i = 0; i < oneSubjectList.size(); i++) {
            //得到oneSubjectList每个eduSubject对象
            EduSubject eduSubject = oneSubjectList.get(i);

            //把eduSubject里面值获取出来，放到OneSubJect对象里面
            //多个OneSubject放到finalSubjectList里面
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(eduSubject, oneSubject);

            finalSubjectList.add(oneSubject);

            //在一级分类循环遍历查询所有的二级分类
            //创建list集合封装每个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            //遍历二级分类list集合
            for (int m = 0; m < twoSubjectList.size(); m++) {
                //获取每个二级分类
                EduSubject tSubject = twoSubjectList.get(m);
                //判断二级分类parentid和一级分类id时候一样
                if (tSubject.getParentId().equals(eduSubject.getId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject, twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            //把一级下面所有二级分类放到一级分类里面
            oneSubject.setChildren(twoFinalSubjectList);
        }
        //3.查询到的数据库再放入缓存，将对象转为json放入缓存
        String s = JSON.toJSONString(finalSubjectList);
        redisTemplate.opsForValue().set("catelogJSON", s, 1, TimeUnit.DAYS);
        return finalSubjectList;
    }


    // 从数据库中查找
    public List<OneSubject> getAllOneTwoSubjectFromBb() {

        synchronized (this) {
            //得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续查询
            return getDateFromDB();
        }

    }


    /**
     *  1.每一个需要缓存的数据我们都放到那个名字的缓存【缓存的分区（按照业务类型分）】
     *  2.@Cacheable({"category"})
     *      代表当前方法的结果需要缓存，如果缓存中有，方法不用调用
     *  3.默认行为
     *      1.如果缓存中有，方法不用调用
     *      2.key默认自动生成，缓存的名字：：SimpleKey[] 自动生成的key值
     *      3.缓存的value的值，默认使用jdk序列化机制，将序列化后的数据存入到redis中
     *      4.默认ttl的事件-1
     *  4.自定义：
     *      1.指定生成的缓存使用的key：key属性指定，接收一个SpEL #root.method.name 以方法名作作为key
     *      2.指定缓存的数据存活事件。配置文件中修改ttl
     *      3.将数据保存为json格式
     * 5.原理
     *  CacheAutoConfiguration-> RedisCacheCongiguration ->
     *      自动配置了RedisCacheManageer->初始化所有的缓存->每个缓存决定使用什么配置
     *      ->如果redisCacheConfiguration有就用自己的，没有就用默认配置
     *      ->想改缓存的配置，只需要给容器中放一个ReidisCacheConfiguration即可
     *      ->就会应用到当前RedisCacheManager管理的又有缓存分区中
     *
     *
     * @return
     */
    @Cacheable(value={"category"},key = "#root.method.name",sync = true)
    @Override
    public List<TwoSubject> getTwoAllSucject() {
        System.out.println("getTwoAllSucject");
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.ne("parent_id", "0");
        List<EduSubject> eduSubjects = baseMapper.selectList(wrapper);
        List<TwoSubject> twoSubjectList = eduSubjects.stream().map(eduSubject ->
                {
                    return new TwoSubject(eduSubject.getId(), eduSubject.getTitle());
                }
        ).collect(Collectors.toList());
        return twoSubjectList;
    }
}
