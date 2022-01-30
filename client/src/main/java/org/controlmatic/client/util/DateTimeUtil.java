package org.controlmatic.client.util;

import java.time.format.DateTimeFormatter;

/**
 * Utility class for dealing with dates and times.
 */
public class DateTimeUtil {
    public final static DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public final static DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    public final static DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
}
