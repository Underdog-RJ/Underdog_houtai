package demo;


import com.atguigu.livingservice.entity.EduLiving;
import com.atguigu.livingservice.mapper.EduLivingMapper;
import com.atguigu.livingservice.service.EduLivingService;
import org.junit.Test;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
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

    @Test
    @RabbitListener(queues = "underdog_rj")
    public void test1(){
        System.out.println("fff");
    }

    @RabbitListener(queues = "underdog_rj")
    public static void main(String msg) {



    }
}
