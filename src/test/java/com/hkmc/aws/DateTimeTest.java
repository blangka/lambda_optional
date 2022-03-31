package com.hkmc.aws;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.*;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Locale;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DateTimeTest {

    @Test
    public void LocalDate() throws Exception {
        // 년 월 일
        LocalDate date = LocalDate.of(2014, 3, 18);
        int year = date.getYear(); // 2014
        Month month = date.getMonth(); // MARCH
        int day = date.getDayOfMonth(); // 18
        DayOfWeek dow = date.getDayOfWeek(); // TUESDAY
        int len = date.lengthOfMonth(); // 31 (3월의 길이)
        boolean leap = date.isLeapYear(); // false (윤년이 아님)
        System.out.println(date);

        int y = date.get(ChronoField.YEAR);
        int m = date.get(ChronoField.MONTH_OF_YEAR);
        int d = date.get(ChronoField.DAY_OF_MONTH);

        // 시 분 초
        LocalTime time = LocalTime.of(13, 45, 20); // 13:45:20
        int hour = time.getHour(); // 13
        int minute = time.getMinute(); // 45
        int second = time.getSecond(); // 20
        System.out.println(time);

        // 연 월 일 시 분 초
        LocalDateTime dt1 = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 20); // 2014-03-18T13:45
        LocalDateTime dt2 = LocalDateTime.of(date, time);
        LocalDateTime dt3 = date.atTime(13, 45, 20);
        LocalDateTime dt4 = date.atTime(time);
        LocalDateTime dt5 = time.atDate(date);
        System.out.println(dt1);

        LocalDate date1 = dt1.toLocalDate();
        System.out.println(date1);
        LocalTime time1 = dt1.toLocalTime();
        System.out.println(time1);

        // 기계의 날짜와 시간으로
        // 기계에서는 단위로 시간을 표현하는 것이 아닌 연속된 시간에서 특정 지점까지의 큰수로 표현하는 것이 일반 적이다.
        // 1970 1월 1일 0시 0분 0초 UTC 기준으로 한다.
        Instant instant = Instant.ofEpochSecond(44 * 365 * 86400);
        Instant now = Instant.now();

        //duration 은 간격이다. 특정 시간이 아닌 둘시간 간의 간격이다.
        Duration d1 = Duration.between(LocalTime.of(13, 45, 10), time);
        Duration d2 = Duration.between(instant, now);
        System.out.println("duration : " + d1.getSeconds());
        System.out.println("duration : " + d2.getSeconds());

        Duration threeMinutes = Duration.of(3, ChronoUnit.MINUTES);
        System.out.println(threeMinutes);

        JapaneseDate japaneseDate = JapaneseDate.from(date);
        System.out.println(japaneseDate);

        // Zoned Date
        ZonedDateTime zonedNow = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        System.out.println(zonedNow); // 2019-10-11T15:23:08.605+09:00[Asia/Seoul]

        ZonedDateTime zonedNow2 = ZonedDateTime.now(ZoneId.of("UTC"));
        System.out.println(zonedNow2); // 2019-10-11T06:23:08.605Z[UTC]

        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        System.out.println(nowUTC);

        LocalDateTime nowSeoul = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        System.out.println(nowSeoul);

        ZonedDateTime nowSeoulZonedTime = nowSeoul.atZone(ZoneId.of("Asia/Seoul"));
        System.out.println(nowSeoulZonedTime);
    }

    @Test
    public void TemporalAdjuster() throws Exception {
        LocalDate date = LocalDate.of(2014, 3, 18);
        date = date.with(nextOrSame(DayOfWeek.SUNDAY));
        System.out.println(date);
        date = date.with(lastDayOfMonth());
        System.out.println(date);

        date = date.with(new NextWorkingDay());
        System.out.println(date);
        date = date.with(nextOrSame(DayOfWeek.FRIDAY));
        System.out.println(date);
        date = date.with(new NextWorkingDay());
        System.out.println(date);

        date = date.with(nextOrSame(DayOfWeek.FRIDAY));
        System.out.println(date);
        date = date.with(temporal -> {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) {
                dayToAdd = 3;
            }
            if (dow == DayOfWeek.SATURDAY) {
                dayToAdd = 2;
            }
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        });
        System.out.println(date);
    }

    private static class NextWorkingDay implements TemporalAdjuster {
        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) {
                dayToAdd = 3;
            }
            if (dow == DayOfWeek.SATURDAY) {
                dayToAdd = 2;
            }
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        }
    }

    @Test
    public void DateFormatter() throws Exception {
        LocalDate date = LocalDate.of(2014, 3, 18);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);

        System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println(date.format(formatter));
        System.out.println(date.format(italianFormatter));

        DateTimeFormatter complexFormatter = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(". ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.ITALIAN);

        System.out.println(date.format(complexFormatter));
    }
}
