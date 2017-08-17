package com.gk.quartzAdmin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * Date:  17/7/11 下午5:28
 */
public class DateUtil {

    public static final String DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND2 = "yyyy/MM/dd HH:mm:ss";

    /**
     * @param date    时间。若为空，则返回空串
     * @param pattern 时间格式化
     * @return 格式化后的时间字符串.
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 格式化日期
     *
     * @param date    时间。若为空，则返回空串
     * @param pattern 时间格式化
     * @param locale  本地化
     * @return 格式化后的时间字符串
     */
    public static String format(Date date, String pattern, Locale locale) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern, locale).format(date);
    }

    /**
     * 字符串转化为时间
     * @param date 时间字符串
     * @param pattern 时间格式化
     * @return 时间
     */
    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}