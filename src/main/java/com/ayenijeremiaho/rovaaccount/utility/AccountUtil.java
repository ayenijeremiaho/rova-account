package com.ayenijeremiaho.rovaaccount.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountUtil {

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Long generateAccountNumber() {
        return (long) (Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
    }

    public static String getDateAsString(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }
}
