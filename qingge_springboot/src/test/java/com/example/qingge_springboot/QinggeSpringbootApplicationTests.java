package com.example.qingge_springboot;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.qingge_springboot.entity.User;
import com.example.qingge_springboot.service.GoodService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;

@SpringBootTest
class QinggeSpringbootApplicationTests {
    @Resource
    RedisTemplate<String, User> redisTemplate;
    @Test
    void contextLoads() {
        String today = DateUtil.today();
        DateTime date = DateUtil.parse(today);
        DateTime dateTime = DateUtil.offsetDay(date, -3);
        String format = DateUtil.format(dateTime, "yyyy/MM/dd");
        System.out.println(DateUtil.dayOfWeek(date));
        System.out.println(DateUtil.beginOfWeek(date));
        System.out.println(format);
    }
    @Test
    void week(){
        ArrayList<String> weekDays = new ArrayList<>();
        DateTime dateTime = DateUtil.beginOfWeek(DateUtil.date());
        for (int i = 0; i < 7; i++) {
            DateTime dateTime1 = DateUtil.offsetDay(dateTime, i);
            String day = dateTime1.toString();
            weekDays.add(day.substring(0,day.indexOf(" ")));
        }
        System.out.println(weekDays);
    }
    @Test
    public void test(){
        User user = redisTemplate.opsForValue().get("user:tokeneyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxIiwiZXhwjoxNjY4MjYwMDA2fQ.Jqrcv0Nig8MOspbX6sHlWPtHH-UmOZEU7fZl8QAR4rQ");
        System.out.println(user);
    }

    @Resource
    GoodService goodService;
    @Test
    void good() {
        System.out.println(goodService.getMinPrice(38l));
    }
}
