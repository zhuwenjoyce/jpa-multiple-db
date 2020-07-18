package com.joyce.jpa;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;

public class MyTest {
    long days31 = 31 * 24 * 60 * 60 * 1000;
    long days31_BigDecimal = new BigDecimal(31).multiply(new BigDecimal(24)).multiply(new BigDecimal(60)).multiply(new BigDecimal(60000)).longValue();
    @Test
    public void test1(){
        System.out.println("days31===" + days31);
        System.out.println("days31_BigDecimal===" + days31_BigDecimal);
        long t1 = 7 * 24 * 60 * 60 * 1000;
        if(t1 > days31){
            System.out.println("大于");
        }
        if(t1 < days31) {
            System.out.println("小于");
        }
        if(t1 == days31) {
            System.out.println("等于");
        }
    }

    @Test
    public void 两个日期值相减(){
        LocalDateTime start = LocalDateTime.of(2018, 3,5,0,0);
        LocalDateTime end = LocalDateTime.of(2018, 3,14,0,0);
        long endLong = Timestamp.valueOf(end).getTime();
        long startLong = Timestamp.valueOf(start).getTime();
        long t = endLong - startLong;
        System.out.println("endLong== "+endLong+" , startLong== "+startLong+" , t === " + t);
    }

//    @Test
//    public void 两个日期值相减(){
//        LocalDateTime start = LocalDateTime.of(2018, 3,5,0,0);
//        LocalDateTime end = LocalDateTime.of(2018, 3,13,0,0);
//        long t = Timestamp.valueOf(end).getTime() - Timestamp.valueOf(start).getTime();
//        System.out.println("t === " + t);
//    }
}
