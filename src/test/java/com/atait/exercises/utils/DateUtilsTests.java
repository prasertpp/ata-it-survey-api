package com.atait.exercises.utils;

import com.atait.exercises.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertNull;


class DateUtilsTests {

    @Test
     void test_strtoDate_success_startDay(){
        Date d = DateUtils.strtoDate(DateUtils.DDMMYYYY_SLASH_PATTERN,"10/12/2022", DateUtils.DateParsingOption.START_OF_DAY);
        assertNotNull("date must be able to parse with format dd/MM/yyyy",d);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
        c.setTime(d);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int  year= c.get(Calendar.YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        assertEquals("day",10,day);
        assertEquals("month",12-1,month);
        assertEquals("year",2022,year);
//        with gmt
        assertEquals("hour",0,hour);
        assertEquals("minute",0,minute);
        assertEquals("second",0,second);
    }

    @Test
     void test_strtoDate_success_endOfDay(){
        Date d = DateUtils.strtoDate(DateUtils.DDMMYYYY_SLASH_PATTERN,"10/12/2022", DateUtils.DateParsingOption.END_OF_DAY);
        assertNotNull("date must be able to parse with format dd/MM/yyyy",d);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
        c.setTime(d);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int  year= c.get(Calendar.YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        assertEquals("day",10,day);
        assertEquals("month",12-1,month);
        assertEquals("year",2022,year);
//        with gmt
        assertEquals("hour",23,hour);
        assertEquals("minute",59,minute);
        assertEquals("second",59,second);
    }

    @Test
    void test_strtoDate_failed_null_request(){
        Date d = DateUtils.strtoDate(null,"10/12/2012", DateUtils.DateParsingOption.START_OF_DAY);
        assertNull("result should be null",d);

        d =  DateUtils.strtoDate(DateUtils.DDMMYYYY_SLASH_PATTERN,null, DateUtils.DateParsingOption.START_OF_DAY);
        assertNull("result should be null",d);

        d =  DateUtils.strtoDate(DateUtils.DDMMYYYY_SLASH_PATTERN,"10/12/2012", null);
        assertNull("result should be null",d);
    }

    @Test
    void test_strtoDate_invalid_dateWithFormat(){
        Date d =  DateUtils.strtoDate(DateUtils.DDMMYYYY_SLASH_PATTERN,"1/12/2012", DateUtils.DateParsingOption.START_OF_DAY);
        assertNull("result should be null",d);
    }

    @Test
    void test_isBefore_success(){
        assertTrue(DateUtils.isBefore(DateUtils.DDMMYYYY_SLASH_PATTERN,"20/12/2022","21/12/2022"));
        assertFalse(DateUtils.isBefore(DateUtils.DDMMYYYY_SLASH_PATTERN,"21/12/2022","20/12/2022"));
    }

    @Test
    void test_isBefore_failed_parseIncorrectDate(){
        try {
            DateUtils.isBefore(DateUtils.DDMMYYYY_SLASH_PATTERN, "2/12/2022", "21/12/2022");
        }catch (ValidationException e){
            assertEquals("parseDateText","unable to parseDateText '2/12/2022' could not be parsed at index 0",e.getMessage());
        }
    }

    @Test
    void test_isBefore_failed_invalidFormatPattern(){
        try {
            DateUtils.isBefore("k/i/m", "20/12/2022", "21/12/2022");
        }catch (ValidationException e){
            assertEquals("parseDateText","unable to parseDateUnknown pattern letter: i",e.getMessage());
        }

    }

    @Test
    void test_isBefore_failed_nullParameter(){
        try {
            DateUtils.isBefore(null, "20/12/2022", "21/12/2022");
        }catch (ValidationException e){
            assertEquals("should validate blank params","invalid arguments",e.getMessage());
        }

        try {
            DateUtils.isBefore(DateUtils.DDMMYYYY_SLASH_PATTERN, null, "21/12/2022");
        }catch (ValidationException e){
            assertEquals("should validate blank params","invalid arguments",e.getMessage());
        }

        try {
            DateUtils.isBefore(DateUtils.DDMMYYYY_SLASH_PATTERN, "20/12/2022", null);
        }catch (ValidationException e){
            assertEquals("should validate blank params","invalid arguments",e.getMessage());
        }

    }







}
