package com.atguigu.educenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author underdog_rj
 * @date2022/4/417:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UcenterApplicationTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void Test() {

//        String string = Integer.toBinaryString(1881210880);
//        System.out.println(string);
//        System.out.println(string.length());
//        LocalDate now = LocalDate.now();
//        int dayOfMonth = now.getDayOfMonth();
//
//        redisTemplate.opsForValue().setBit("mon", dayOfMonth, true);

        List<Long> execute = redisTemplate.execute(
                (RedisCallback<List<Long>>) con -> con.bitField("m2".getBytes(),
                        BitFieldSubCommands.
                                create().
                                get(BitFieldSubCommands.BitFieldType.unsigned(32)).valueAt(0)));
        Long aLong = execute.get(0);
        String string = Long.toBinaryString(aLong);
        if (string.length() < 31) {
            String join = String.join("", Collections.nCopies(31 - string.length(), "0"));
            string = join + string;
        }

//        String s = redisTemplate.opsForValue().get("u:sign:1379705728213835778:202203");
//        byte[] bytes = s.getBytes();
//        for (byte aByte : bytes) {
//            System.out.println(aByte);
//        }
//        System.out.println(s);
    }

}
