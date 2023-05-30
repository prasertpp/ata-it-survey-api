package com.atait.exercises.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtils {

    public static final String DMY_PATTERN = "dd/MM/yyyy";
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static Date strtoDate(String pattern, String dateStr, DateParsingOption opt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);

            LocalDateTime localDateTime = switch (opt) {
                case START_OF_DAY -> date.atStartOfDay();
                case END_OF_DAY -> date.atTime(LocalTime.MAX);
            };
            return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
        } catch (DateTimeParseException | NullPointerException | IllegalArgumentException e) {
            logger.warn("strToDate occurs error : {}",e);
        }
        return null;
    }


    public static boolean isBefore(String pattern, String dateA, String dateB) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate localDateA = LocalDate.parse(dateA, formatter);
        LocalDate localDateB = LocalDate.parse(dateB, formatter);
        return localDateA.isBefore(localDateB);
    }

    public enum DateParsingOption {
        END_OF_DAY,
        START_OF_DAY
    }

}
