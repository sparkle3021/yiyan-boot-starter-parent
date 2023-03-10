package com.oho.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 时间工具类
 *
 * @author Sparkler
 */
public class DateUtils extends cn.hutool.core.date.DateUtil {

    private static final String HOUR = "hour";

    private static final String DAY = "day";

    private static final String MONTH = "month";

    private static final String YEAR = "year";

    /**
     * The constant YYYY_MM_DD_HH_MM_SS.
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * The constant YYYY_MM_DD_HH_MM_SS_SSS.
     */
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * The constant YYYYMMDDHHMMSS.
     */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * The constant YYYY_MM_DD.
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * The constant MAX_LOCAL_TIME.
     */
    public static final LocalTime MAX_LOCAL_TIME = LocalTime.of(23, 59, 59);

    private static final ThreadLocal<DateFormat> DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS));

    private static ZoneId zoneId = ZoneId.systemDefault();

    /**
     * Gets type.
     *
     * @param start the start
     * @param end   the end
     * @return the type
     */
    public static String getType(LocalDateTime start, LocalDateTime end) {
        long diff = DateUtils.getTimeDifference(start, end);
        if (diff < 24 * 60 * 60) {
            return HOUR;
        } else if (diff < 3L * 30 * 24 * 60 * 60) {
            return DAY;
        } else if (diff < 2L * 12 * 30 * 24 * 60 * 60) {
            return MONTH;
        } else {
            return YEAR;
        }
    }

    /**
     * Gets x axis.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @param type      the type
     * @return the x axis
     */
    public static List<LocalDateTime> getXAxis(LocalDateTime startTime, LocalDateTime endTime, String type) {
        if (StringUtils.isEmpty(type)) {
            type = getType(startTime, endTime);
        }
        List<LocalDateTime> xAxis = new ArrayList<>();
        while (startTime.isBefore(endTime) || startTime.isEqual(endTime)) {
            xAxis.add(startTime);
            startTime = getTimeByType(startTime, type);
        }
        return xAxis;
    }

    private static LocalDateTime getTimeByType(LocalDateTime time, String type) {
        if (HOUR.equals(type)) {
            time = time.plusHours(1);
        } else if (MONTH.equals(type)) {
            time = time.plusMonths(1);
        } else if (YEAR.equals(type)) {
            time = time.plusYears(1);
        } else {
            time = time.plusDays(1);
        }
        return time;
    }

    /**
     * Gets date format.
     *
     * @param type the type
     * @return the date format
     */
    public static String getDateFormat(String type) {
        if (HOUR.equals(type)) {
            return "HH";
        } else if (MONTH.equals(type)) {
            return "yyyy/MM";
        } else if (YEAR.equals(type)) {
            return "yyyy";
        } else {
            return "yyyy/MM/dd";
        }
    }

    /**
     * Gets time difference.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return the time difference
     */
    public static long getTimeDifference(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.until(endTime, ChronoUnit.SECONDS);
    }

    /**
     * Gets minute difference.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return the minute difference
     */
    public static long getMinuteDifference(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.until(endTime, ChronoUnit.MINUTES);
    }

    /**
     * Gets day difference.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return the day difference
     */
    public static long getDayDifference(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.until(endTime, ChronoUnit.DAYS);
    }

    /**
     * Gets day difference.
     *
     * @param start the start
     * @param end   the end
     * @return the day difference
     */
    public static long getDayDifference(LocalDate start, LocalDate end) {
        return start.until(end, ChronoUnit.DAYS);
    }

    /**
     * LocalDateTime转string
     *
     * @param time    the time
     * @param pattern the pattern
     * @return the string
     */
    public static String formatTimeString(LocalDateTime time, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = YYYY_MM_DD_HH_MM_SS;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if (time == null) {
            return null;
        }

        return time.format(formatter);
    }

    /**
     * LocalDateTime转string
     *
     * @param time the time
     * @return the string
     */
    public static String formatTimeString(LocalDateTime time) {
        return formatTimeString(time, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * Format time string for es string.
     *
     * @param ms the ms
     * @return the string
     */
    public static String formatTimeStringForEs(Long ms) {
        if (null == ms) {
            ms = System.currentTimeMillis();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(new Date(ms));
    }

    /**
     * Format time now string.
     *
     * @param format the format
     * @return the string
     */
    public static String formatTimeNow(String format) {
        return formatTimeNow(null, format);
    }

    /**
     * Format time now string.
     *
     * @param ms     the ms
     * @param format the format
     * @return the string
     */
    public static String formatTimeNow(Long ms, String format) {
        if (null == ms) {
            ms = System.currentTimeMillis();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(new Date(ms));
    }

    /**
     * LocalDate转string
     *
     * @param time    the time
     * @param pattern the pattern
     * @return the string
     */
    public static String formatDateString(LocalDate time, String pattern) {
        if (time == null) {
            return null;
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = YYYY_MM_DD;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return time.format(formatter);
    }

    /**
     * LocalDate转string
     *
     * @param time the time
     * @return the string
     */
    public static String formatDateString(LocalDate time) {
        return formatDateString(time, YYYY_MM_DD);
    }

    /**
     * Gets hours.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return the hours
     */
    public static int getHours(LocalDateTime startTime, LocalDateTime endTime) {
        long hours = startTime.until(endTime, ChronoUnit.HOURS);
        return (int) hours;
    }

    /**
     * Date to local date local date time.
     *
     * @param date the date
     * @return the local date time
     */
    public static LocalDateTime dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * Format digit char.
     *
     * @param sign the sign
     * @return the char
     */
    public static char formatDigit(char sign) {
        switch (sign) {
            case '0':
                sign = '〇';
                break;
            case '1':
                sign = '一';
                break;
            case '2':
                sign = '二';
                break;
            case '3':
                sign = '三';
                break;
            case '4':
                sign = '四';
                break;
            case '5':
                sign = '五';
                break;
            case '6':
                sign = '六';
                break;
            case '7':
                sign = '七';
                break;
            case '8':
                sign = '八';
                break;
            case '9':
                sign = '九';
                break;
            default:
        }
        return sign;
    }

    /**
     * Format str string.
     *
     * @param str the str
     * @return the string
     */
    public static String formatStr(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            stringBuilder.append(formatDigit(str.charAt(i)));
        }
        stringBuilder.append("年");
        if (str.charAt(5) == '0') {
            stringBuilder.append(formatDigit(str.charAt(6)));
        } else {
            stringBuilder.append(formatDigit(str.charAt(5)));
            stringBuilder.append("十");
            stringBuilder.append(formatDigit(str.charAt(6)));
        }
        stringBuilder.append("月");
        return stringBuilder.toString();
    }

    /**
     * 收集起始时间到结束时间之间所有的时间并以字符串集合方式返回
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @param pattern   the pattern
     * @return list
     */
    public static List<String> collectLocalDates(LocalDateTime startTime, LocalDateTime endTime, String pattern) {
        if (Objects.isNull(startTime)) {
            return new ArrayList<>();
        }
        if (Objects.isNull(endTime)) {
            endTime = LocalDateTime.now();
        }
        // 用起始时间作为流的源头，按照每次加一天的方式创建一个无限流
        return Stream.iterate(startTime, localDateTime -> localDateTime.plusDays(1))
                // 截断无限流，长度为起始时间和结束时间的差+1个
                .limit(ChronoUnit.DAYS.between(startTime, endTime) + 1)
                // 由于最后要的是字符串，所以map转换一下
                .map(x -> formatTimeString(x, pattern))
                // 把流收集为List
                .collect(Collectors.toList());
    }

    /**
     * string 转 LocalDateTime
     *
     * @param time the time
     * @return local date time
     */
    public static LocalDateTime stringToLocalDateTime(String time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        return LocalDateTime.parse(time, df);
    }

    /**
     * String to local date time local date time.
     *
     * @param time    the time
     * @param pattern the pattern
     * @return the local date time
     */
    public static LocalDateTime stringToLocalDateTime(String time, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = YYYY_MM_DD_HH_MM_SS;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(time, df);
    }

    /**
     * String to local date local date.
     *
     * @param time the time
     * @return the local date
     */
    public static LocalDate stringToLocalDate(String time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return LocalDate.parse(time, df);
    }

    /**
     * String to local date local date.
     *
     * @param time    the time
     * @param pattern the pattern
     * @return the local date
     */
    public static LocalDate stringToLocalDate(String time, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = YYYY_MM_DD;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(time, df);
    }

    /**
     * Parse string date.
     *
     * @param date the date
     * @return the date
     */
    public static Date parseString(String date) {
        try {
            return DATE_FORMAT.get().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * yyyy-MM-dd'T'HH:mm:ss:SSSZ转换成gmt+8的LocalDateTime
     *
     * @param dateString the date string
     * @return the iso date time
     */
    public static LocalDateTime getISODateTime(String dateString) {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_INSTANT;
        Instant dateInstant = Instant.from(isoFormatter.parse(dateString));
        return LocalDateTime.ofInstant(dateInstant, ZoneId.of("Asia/Shanghai"));
    }

    /**
     * localdatetime 转date
     *
     * @param localDateTime the local date time
     * @return date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        String time = formatTimeString(localDateTime, YYYY_MM_DD_HH_MM_SS);
        return parseString(time);
    }

    /**
     * 当天的最大时间
     *
     * @param date the date
     * @return the max time
     */
    public static LocalDateTime getMaxTime(LocalDate date) {
        return LocalDateTime.of(date, MAX_LOCAL_TIME);
    }

    /**
     * 将秒转化成X天X小时X分X秒形式
     *
     * @param seconds the seconds
     * @return the string
     */
    public static String minuteToTime(Long seconds) {
        if (Objects.isNull(seconds)) {
            return null;
        }
        long day = seconds / (60 * 60 * 24);
        long hour = (seconds - day * 24 * 60 * 60) / (60 * 60);
        long min = (seconds - hour * 60 * 60 - day * 24 * 60 * 60) / 60;
        long second = seconds - min * 60 - hour * 60 * 60 - day * 24 * 60 * 60;
        StringBuilder sb = new StringBuilder();
        JavaUtil.getJavaUtil()
                .acceptIfCondition(day != 0, day, e -> sb.append(e).append("天"))
                .acceptIfCondition(hour != 0, hour, e -> sb.append(e).append("小时"))
                .acceptIfCondition(min != 0, min, e -> sb.append(e).append("分"))
                .acceptIfCondition(second != 0, second, e -> sb.append(e).append("秒"));
        return sb.toString();
    }

}
