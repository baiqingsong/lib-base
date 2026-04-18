package com.dawn.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 */
@SuppressWarnings("unused")
public class LDateUtil {
    private static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static final String PATTERN_DATE = "yyyy-MM-dd";
    private static final String PATTERN_TIME = "HH:mm:ss";

    private static SimpleDateFormat getDateTimeFormat() {
        return new SimpleDateFormat(PATTERN_DATETIME, Locale.getDefault());
    }

    private static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(PATTERN_DATE, Locale.getDefault());
    }

    private static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat(PATTERN_TIME, Locale.getDefault());
    }

    /**
     * 时间戳转换成日期时间
     * @param timestamp 日期时间long类型
     *
     * @return 日期时间字符串
     */
    public static String longToDateTime(long timestamp) {
        return getDateTimeFormat().format(new Date(timestamp));
    }

    /**
     * 时间戳转换成日期
     * @param timestamp 日期long类型
     *
     * @return 日期字符串
     */
    public static String longToDate(long timestamp) {
        return getDateFormat().format(new Date(timestamp));
    }

    /**
     * 时间戳转换成时间
     * @param timestamp 时间long类型
     *
     * @return 时间字符串
     */
    public static String longToTime(long timestamp) {
        return getTimeFormat().format(new Date(timestamp));
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static String getTime() {
        return getTimeFormat().format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static String getDate() {
        return getDateFormat().format(new Date());
    }

    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间
     */
    public static String getDateTime(){
        return getDateTimeFormat().format(new Date());
    }

    /**
     * 根据自定义格式获取系统日期时间
     * @param format 格式的字符串
     *
     * @return 系统日期时间
     */
    public static String getDateTime(String format){
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
    }

    /**
     * 日期字符串转时间戳
     * @param dateTime 日期时间字符串 格式：yyyy-MM-dd HH:mm:ss
     * @return 时间戳
     */
    public static long dateTimeToLong(String dateTime) {
        try {
            Date date = getDateTimeFormat().parse(dateTime);
            return date != null ? date.getTime() : 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 日期字符串转时间戳
     * @param date 日期字符串 格式：yyyy-MM-dd
     * @return 时间戳
     */
    public static long dateToLong(String date) {
        try {
            Date d = getDateFormat().parse(date);
            return d != null ? d.getTime() : 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取当前时间戳
     * @return 当前时间戳（毫秒）
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取两个日期之间的天数差
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 天数差
     */
    public static int getDaysBetween(long startDate, long endDate) {
        return (int) ((endDate - startDate) / (24 * 60 * 60 * 1000));
    }

    /**
     * 判断是否为今天
     * @param timestamp 时间戳
     * @return 是否为今天
     */
    public static boolean isToday(long timestamp) {
        String today = longToDate(System.currentTimeMillis());
        String target = longToDate(timestamp);
        return today.equals(target);
    }

    /**
     * 获取星期几
     * @param timestamp 时间戳
     * @return 星期几 (1-7，1表示星期一)
     */
    public static int getDayOfWeek(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == 1 ? 7 : dayOfWeek - 1; // 调整为1-7，1表示星期一
    }

    /**
     * 获取星期几的中文名称
     * @param timestamp 时间戳
     * @return 星期几的中文名称
     */
    public static String getDayOfWeekName(long timestamp) {
        String[] names = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        return names[getDayOfWeek(timestamp) - 1];
    }

    /**
     * 在时间戳基础上增加天数
     * @param timestamp 时间戳
     * @param days 增加的天数（可以为负数）
     * @return 新的时间戳
     */
    public static long addDays(long timestamp, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTimeInMillis();
    }

    /**
     * 在时间戳基础上增加小时
     * @param timestamp 时间戳
     * @param hours 增加的小时数（可以为负数）
     * @return 新的时间戳
     */
    public static long addHours(long timestamp, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTimeInMillis();
    }

    /**
     * 在时间戳基础上增加分钟
     * @param timestamp 时间戳
     * @param minutes 增加的分钟数（可以为负数）
     * @return 新的时间戳
     */
    public static long addMinutes(long timestamp, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTimeInMillis();
    }

    /**
     * 格式化时间间隔为可读字符串
     * @param millis 毫秒数
     * @return 可读的时间字符串（如 "2天3小时5分钟"）
     */
    public static String formatDuration(long millis) {
        if (millis < 0) millis = -millis;
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + "天" + (hours % 24) + "小时" + (minutes % 60) + "分钟";
        } else if (hours > 0) {
            return hours + "小时" + (minutes % 60) + "分钟";
        } else if (minutes > 0) {
            return minutes + "分钟" + (seconds % 60) + "秒";
        } else {
            return seconds + "秒";
        }
    }

    /**
     * 判断是否为闰年
     * @param year 年份
     * @return 是否为闰年
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 根据生日时间戳计算年龄
     * @param birthdayTimestamp 生日时间戳
     * @return 年龄
     */
    public static int getAge(long birthdayTimestamp) {
        Calendar birthday = Calendar.getInstance();
        birthday.setTimeInMillis(birthdayTimestamp);
        Calendar now = Calendar.getInstance();

        int age = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        if (now.get(Calendar.MONTH) < birthday.get(Calendar.MONTH) ||
                (now.get(Calendar.MONTH) == birthday.get(Calendar.MONTH)
                        && now.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return Math.max(age, 0);
    }

    /**
     * 获取某个月的天数
     * @param year 年份
     * @param month 月份（1-12）
     * @return 该月的天数
     */
    public static int getDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取友好的时间描述（如"刚刚"、"5分钟前"、"昨天"等）
     * @param timestamp 时间戳
     * @return 友好的时间描述
     */
    public static String getFriendlyTime(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        if (diff < 0) {
            return longToDateTime(timestamp);
        }

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return "刚刚";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else if (days < 2) {
            return "昨天";
        } else if (days < 3) {
            return "前天";
        } else if (days < 30) {
            return days + "天前";
        } else if (days < 365) {
            return (days / 30) + "个月前";
        } else {
            return (days / 365) + "年前";
        }
    }

    /**
     * 获取今天的开始时间戳（00:00:00）
     * @return 今天开始的时间戳
     */
    public static long getStartOfDay() {
        return getStartOfDay(System.currentTimeMillis());
    }

    /**
     * 获取指定时间当天的开始时间戳（00:00:00）
     * @param timestamp 时间戳
     * @return 当天开始的时间戳
     */
    public static long getStartOfDay(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定时间当天的结束时间戳（23:59:59）
     * @param timestamp 时间戳
     * @return 当天结束的时间戳
     */
    public static long getEndOfDay(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取星期几的中文名称（简写）
     * @param timestamp 时间戳
     * @return 星期几中文名称（如：周一）
     */
    public static String getDayOfWeekChinese(long timestamp) {
        String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        int dayOfWeek = getDayOfWeek(timestamp);
        return weekDays[dayOfWeek - 1];
    }

    /**
     * 获取年份
     * @param timestamp 时间戳
     * @return 年份
     */
    public static int getYear(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月份（1-12）
     * @param timestamp 时间戳
     * @return 月份
     */
    public static int getMonth(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期（1-31）
     * @param timestamp 时间戳
     * @return 日期
     */
    public static int getDay(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取小时（0-23）
     * @param timestamp 时间戳
     * @return 小时
     */
    public static int getHour(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分钟（0-59）
     * @param timestamp 时间戳
     * @return 分钟
     */
    public static int getMinute(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取秒（0-59）
     * @param timestamp 时间戳
     * @return 秒
     */
    public static int getSecond(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取今天开始时间戳（00:00:00）
     * @return 今天开始时间戳
     */
    public static long getTodayStart() {
        return getStartOfDay(System.currentTimeMillis());
    }

    /**
     * 获取今天结束时间戳（23:59:59）
     * @return 今天结束时间戳
     */
    public static long getTodayEnd() {
        return getEndOfDay(System.currentTimeMillis());
    }

    /**
     * 判断是否为昨天
     * @param timestamp 时间戳
     * @return 是否为昨天
     */
    public static boolean isYesterday(long timestamp) {
        long yesterday = addDays(System.currentTimeMillis(), -1);
        return longToDate(yesterday).equals(longToDate(timestamp));
    }

    /**
     * 判断是否为同一天
     * @param timestamp1 时间戳1
     * @param timestamp2 时间戳2
     * @return 是否为同一天
     */
    public static boolean isSameDay(long timestamp1, long timestamp2) {
        return longToDate(timestamp1).equals(longToDate(timestamp2));
    }

    /**
     * 格式化时间差（如：3分钟前、2小时前、昨天等）
     * @param timestamp 时间戳
     * @return 格式化后的字符串
     */
    public static String formatTimeAgo(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        if (diff < 60 * 1000) {
            return "刚刚";
        } else if (diff < 60 * 60 * 1000) {
            return (diff / (60 * 1000)) + "分钟前";
        } else if (diff < 24 * 60 * 60 * 1000) {
            return (diff / (60 * 60 * 1000)) + "小时前";
        } else if (diff < 2 * 24 * 60 * 60 * 1000) {
            return "昨天";
        } else if (diff < 7 * 24 * 60 * 60 * 1000) {
            return (diff / (24 * 60 * 60 * 1000)) + "天前";
        } else {
            return longToDate(timestamp);
        }
    }

    /**
     * 格式化时长（简短格式，如：1小时30分）
     * @param millis 毫秒数
     * @return 格式化后的时长字符串
     */
    public static String formatDurationShort(long millis) {
        long seconds = millis / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        if (hours > 0) {
            return hours + "小时" + (minutes > 0 ? minutes + "分" : "");
        } else if (minutes > 0) {
            return minutes + "分钟";
        } else {
            return seconds + "秒";
        }
    }

    /**
     * 格式化时长（HH:mm:ss格式）
     * @param millis 毫秒数
     * @return 格式化后的时长字符串
     */
    public static String formatDurationHMS(long millis) {
        long seconds = millis / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs);
    }

    /**
     * 获取当月第一天时间戳
     * @return 当月第一天开始时间戳
     */
    public static long getMonthStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当月最后一天时间戳
     * @return 当月最后一天结束时间戳
     */
    public static long getMonthEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

}
