package demo;


import com.atguigu.livingservice.entity.EduLiving;
import com.atguigu.livingservice.mapper.EduLivingMapper;
import com.atguigu.livingservice.service.EduLivingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class testDemo {

    @Autowired
    private EduLivingService eduLivingService;

    @Test
    public void test(){
       // Integer integer = eduLivingMapper.selectCount(null)
        EduLiving one = eduLivingService.getOne(null);
        System.out.println();
        System.out.println();
    }
}
