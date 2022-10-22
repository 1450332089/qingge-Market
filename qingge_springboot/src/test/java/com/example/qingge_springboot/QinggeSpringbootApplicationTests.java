package com.example.qingge_springboot;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class QinggeSpringbootApplicationTests {

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

    }

}
