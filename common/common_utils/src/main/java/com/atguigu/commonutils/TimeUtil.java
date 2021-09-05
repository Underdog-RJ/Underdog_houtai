package com.atguigu.commonutils;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

/**
 * @author RJ
 * 时间工具
 */
@Slf4j
public class TimeUtil {
    /**
     * 格式
     */
    static String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    static String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    static SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);

    static String YMD = "yyyy-MM-dd";

    public static String formatTime(LocalDateTime time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createTime = dateTimeFormatter.format(time);
        return createTime;
    }

    /**
     * 获取一年开始时间
     *
     * @return localDateTime
     */
    public static LocalDateTime firstDayOfYear() {
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).withHour(0).withMinute(0).withSecond(0);
    }

    public static LocalDate firstDayOfYearByLocalDate(String date) {
        LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern(YMD));
        return parse.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取一年结束时间
     *
     * @return localDateTime
     */
    public static LocalDateTime lastDayOfYear() {
        return LocalDateTime.now().with(TemporalAdjusters.lastDayOfYear()).withHour(23).withMinute(59).withSecond(59);
    }

    public static LocalDate lastDayOfYearByLocalDate(String date) {
        LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern(YMD));
        return parse.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 获取一月开始时间
     *
     * @return localDateTime
     */
    public static LocalDateTime firstDayOfMonth() {
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
    }

    public static LocalDate firstDayOfMonthByLocalDate(String date) {
        LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern(YMD));
        return parse.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取一月结束时间
     *
     * @return localDateTime
     */
    public static LocalDateTime lastDayOfMonth() {
        return LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
    }

    public static LocalDate lastDayOfMonthByNow() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate lastDayOfMonthByLocalDate(String date) {
        LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern(YMD));
        return parse.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取一周开始时间
     *
     * @return localDateTime
     */
    public static LocalDateTime firstDayOfWeek() {
        LocalDateTime monday = LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(1).withHour(0).withMinute(0).withSecond(0);
        return monday;
    }

    public static LocalDate firstDayOfWeekByLocalDate(String date) {
        LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern(YMD));
        LocalDate monday = parse.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(1);
        return monday;
    }

    /**
     * 获取一周结束时间
     *
     * @return localDateTime
     */
    public static LocalDateTime lastDayOfWeek() {
        LocalDateTime sunday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        return sunday;
    }

    public static LocalDate lastDayOfWeekByLocalDate(String date) {
        LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern(YMD));
        LocalDate monday = parse.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1);
        return monday;
    }


    /**
     * localDateTime转时间
     *
     * @param time
     * @return 字符串
     */
    @Nonnull
    public static String localDateTime2String(@Nonnull LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(YMDHMS));
    }

    @Nonnull
    public static String localDate2String(@Nonnull LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(YMD));
    }

    /**
     * localDateTime转时间
     *
     * @param time
     * @param pattern 转换格式
     * @return 字符串
     */
    @Nonnull
    public static String localDateTime2StringWithPattern(@Nonnull LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(YMDHMS));
    }

    /**
     * localDateTime转时间
     *
     * @param time
     * @param formatter 转换格式format
     * @return 字符串
     */
    @Nonnull
    public static String localDateTime2StringWithFormatter(@Nonnull LocalDateTime time, DateTimeFormatter formatter) {
        return time.format(formatter);
    }


    /**
     * 字符串转localDateTime
     *
     * @param time
     * @return localDateTime
     */
    @Nonnull
    public static LocalDateTime string2LocalDateTime(@Nonnull String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(YMDHMS));
    }

    /**
     * 字符串转localDateTime
     */
    @Nonnull
    public static LocalDate string2LocalDateTime_ymd(@Nonnull String time) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(YMD));
    }

    /**
     * 字符串转localDateTime
     *
     * @param time
     * @param pattern 转换格式
     * @return localDateTime
     */
    @Nonnull
    public static LocalDateTime string2LocalDateTimeWithPattern(@Nonnull String time, String pattern) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 字符串转localDateTime
     *
     * @param time
     * @param formatter 转换格式format
     * @return localDateTime
     */
    @Nonnull
    public static LocalDateTime string2LocalDateTimeWithFormatter(@Nonnull String time, DateTimeFormatter formatter) {
        return LocalDateTime.parse(time, formatter);
    }

    /**
     * LocalDate转时间
     *
     * @param time
     * @return 字符串
     */
    @Nonnull
    public static String localDate2String(@Nonnull LocalDate time) {
        return time.format(DateTimeFormatter.ofPattern(YMDHMS));
    }

    /**
     * localDate转时间
     *
     * @param time
     * @param pattern 转换格式
     * @return 字符串
     */
    @Nonnull
    public static String localDate2StringWithPattern(@Nonnull LocalDate time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * localDate转时间
     *
     * @param time
     * @param formatter 转换格式format
     * @return 字符串
     */
    @Nonnull
    public static String localDate2StringWithFormatter(@Nonnull LocalDate time, DateTimeFormatter formatter) {
        return time.format(formatter);
    }


    /**
     * 字符串转localDate
     *
     * @param time
     * @return localDate
     */
    @Nonnull
    public static LocalDate string2LocalDate(@Nonnull String time) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(YMDHMS));
    }

    /**
     * 字符串转localDate
     *
     * @param time
     * @param pattern 转换格式
     * @return localDate
     */
    @Nonnull
    public static LocalDate string2LocalDateWithPattern(@Nonnull String time, String pattern) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 字符串转localDate
     *
     * @param time
     * @param formatter 转换格式format
     * @return localDate
     */
    @Nonnull
    public static LocalDate string2LocalDateWithFormatter(@Nonnull String time, DateTimeFormatter formatter) {
        return LocalDate.parse(time, formatter);
    }

    /**
     * second字符串转LocalDateTime
     */
    public static LocalDateTime parseTime(String second) {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(Long.parseLong(second), 0, ZoneOffset.ofHours(+8));
        return localDateTime;
    }

    /**
     * millisecond
     */
    public static LocalDateTime parseMillisecond(Long millisecond) {
//        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());
        LocalDateTime localDateTime = new Date(millisecond).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
        return localDateTime;
    }

    public static Date localDateToUdate(LocalDate localDate) {

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        sdf.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        String format = sdf.format(date);
        try {
            return sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date localTimeToUdate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        sdf.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        String format = sdf.format(date);
        try {
            return sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

  public static Long timeStrWithPattern2Long(String time)  {
    DateTimeFormatter df = DateTimeFormatter.ofPattern(YMDHMS);
    return LocalDateTime.parse(time,df).toEpochSecond(ZoneOffset.of("+8"));
  }



    /**
     * "00:00.00" 转换成long类型
     *
     * @param time
     * @return
     */
    public static Long timeStr2Long(String time) {
        if ("00:00.00".equals(time)) {
            return 0L;
        }

//        System.out.println(time);
        try {
            long hour = Long.valueOf(time.substring(0, time.indexOf(":")));
            long min = Long.valueOf(time.substring(time.indexOf(":") + 1, time.indexOf(".")));
            long sec = Long.valueOf(time.substring(time.indexOf(".") + 1, time.length() - 1));
            return hour * 60 * 60 + min * 60 + sec;
        } catch (Exception e) {
            log.info("发生了异常:{}", e.getMessage());
            return 0L;
        }
    }
}
