package com.atguigu.commonutils;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 *  LocalDate不带时分秒
 *  LocalDateTime带时分秒
 *
 *
 */
public class TimeUtils {

    public static void main(String[] args) {
//        String s = firseDayofYear();
//        System.out.println(s);
        String s = nowTimeAfterNDay(1);
        System.out.println(s);
    }



    //create localtime with pattern
    public static String formatTime(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }

    /**
     * 获取一年的开始时间
     */
    public static String firseDayofYear(){
        LocalDateTime localDateTime = LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).withHour(0).withMinute(0).withSecond(0);
        String firseDayofYear = formatTime(localDateTime);
        return firseDayofYear;
    }

    /**
     * 获取当前天之后的第N天
     */
    public static String nowTimeAfterNDay(Integer days){
        LocalDateTime now = LocalDateTime.now();

        TemporalAdjuster temporalAdjuster = t-> t.plus(Period.ofDays(days));
        return formatTime(now.with(temporalAdjuster));
    }




}
