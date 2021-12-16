package com.example.postit;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    public static LocalDate convertDateToLocalDate(Date date) {
        LocalDate localDate = date.toInstant() // Date -> Instant
                .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                .toLocalDate(); // ZonedDateTime -> LocalDate
        return localDate;
    }
}
