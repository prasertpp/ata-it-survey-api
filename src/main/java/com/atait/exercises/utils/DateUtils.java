package com.atait.exercises.utils;

import com.atait.exercises.exception.ValidationException;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneRulesException;
import java.util.Date;
import java.util.Objects;

public class DateUtils {

    public static final String DDMMYYYY_SLASH_PATTERN = "dd/MM/yyyy";
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static Date strtoDate(String pattern, String dateStr, DateParsingOption opt) {
        LocalDateTime dt = strtoLocalDateTime(pattern, dateStr, opt);
        if (Objects.isNull(dt)) return null;
        return Date.from(dt.atZone(ZoneId.of("UTC")).toInstant());
    }

    public static LocalDateTime strtoLocalDateTime(String pattern, String dateStr, DateParsingOption opt) {
        if (StringUtils.isBlank(pattern) || StringUtils.isBlank(dateStr) || Objects.isNull(opt)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);

            LocalDateTime localDateTime = switch (opt) {
                case START_OF_DAY -> date.atStartOfDay();
                case END_OF_DAY -> date.atTime(LocalTime.MAX);
            };
            return localDateTime;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            logger.warn("[DateUtils]::strtoLocalDateTime occurs error : {}", e);
        }
        return null;
    }


    public static boolean isBefore(String pattern, String dateA, String dateB) {
        if(StringUtils.isBlank(pattern) || StringUtils.isBlank(dateA)|| StringUtils.isBlank(dateB) ){
            throw new ValidationException("invalid arguments");
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            LocalDate localDateA = LocalDate.parse(dateA, formatter);
            LocalDate localDateB = LocalDate.parse(dateB, formatter);
            return localDateA.isBefore(localDateB);
        }catch (DateTimeParseException| IllegalArgumentException e){
            logger.error("[DateUtil]::isBefore pattern : "+pattern+" dateA : "+ dateA+" dateB : "+dateB);
            throw new ValidationException("unable to parseDate"+e.getMessage());
        }

    }

    public enum DateParsingOption {
        END_OF_DAY,
        START_OF_DAY
    }

}
