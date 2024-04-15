package com.mindconnect.mindconnect.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeFormatter {
    public static String formatRelativeTime(LocalDateTime timeStamp){
        Duration interval = Duration.between(timeStamp, LocalDateTime.now());
        Long days = interval.toDays();
        Long hours = interval.toHours() % 24;
        Long minutes = interval.toMinutes() % 60;

        StringBuilder timeFormat = new StringBuilder();
        if(days > 0){
            timeFormat.append(days).append("d ");
        }
        if(hours > 0){
            timeFormat.append(hours).append("h ");
        }
        if(minutes > 0 || days == 0 && hours == 0){
            timeFormat.append(minutes).append("m");
        }
        return timeFormat.toString().trim() + " ago";
    }

    public static DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy, h:mma", Locale.US);
    }
}
