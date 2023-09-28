package com.yiyan.boot.common.utils.date;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间类型转换工具
 *
 * @author MENGJIAO
 * @createDate 2023-04-23 17:58
 */
@Component
public class DateConverter implements Converter<String, Date> {

    private static final String DEFAULT_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将Date对象转换为LocalDate对象
     *
     * @param date Date对象
     * @return LocalDate对象
     */
    public static LocalDate toLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 将LocalDate对象转换为Date对象
     *
     * @param localDate LocalDate对象
     * @return Date对象
     */
    public static Date toDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 将Date对象转换为LocalDateTime对象
     *
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    /**
     * 将LocalDateTime对象转换为Date对象
     *
     * @param localDateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    @Override
    public Date convert(@NotNull String source) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT_PATTERN);
            LocalDateTime localDateTime = LocalDateTime.parse(source, formatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
            return Date.from(zonedDateTime.toInstant());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
