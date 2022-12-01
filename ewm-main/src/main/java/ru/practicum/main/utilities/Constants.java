package ru.practicum.main.utilities;

import java.time.format.DateTimeFormatter;

public class Constants {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static DateTimeFormatter dateTimeFormat() {
        return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    }
}
