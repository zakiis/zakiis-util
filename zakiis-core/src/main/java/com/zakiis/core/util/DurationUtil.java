package com.zakiis.core.util;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


public class DurationUtil {

    public static final Duration DEFAULT_DURATION = Duration.ofMillis(-1);

    public static final String DAY_UNIT = "d";
    public static final String HOUR_UNIT = "h";
    public static final String MINUTE_UNIT = "m";
    public static final String SECOND_UNIT = "s";
    public static final String MILLIS_SECOND_UNIT = "ms";

    private static final Pattern SIMPLE = Pattern.compile("^([\\+\\-]?\\d+)([a-zA-Z]{1,2})$");
    private static final Pattern ISO8601 = Pattern.compile("^[\\+\\-]?P.*$");

    public static Duration parse(String str) {
        if (StringUtils.isBlank(str)) {
            return DEFAULT_DURATION;
        }

        if (SIMPLE.matcher(str).matches()) {
            if (str.contains(MILLIS_SECOND_UNIT)) {
                long value = doParse(MILLIS_SECOND_UNIT, str);
                return Duration.ofMillis(value);
            } else if (str.contains(DAY_UNIT)) {
                long value = doParse(DAY_UNIT, str);
                return Duration.ofDays(value);
            } else if (str.contains(HOUR_UNIT)) {
                long value = doParse(HOUR_UNIT, str);
                return Duration.ofHours(value);
            } else if (str.contains(MINUTE_UNIT)) {
                long value = doParse(MINUTE_UNIT, str);
                return Duration.ofMinutes(value);
            } else if (str.contains(SECOND_UNIT)) {
                long value = doParse(SECOND_UNIT, str);
                return Duration.ofSeconds(value);
            } else {
                throw new UnsupportedOperationException("\"" + str + "\" can't parse to Duration");
            }
        }

        try {
            if (ISO8601.matcher(str).matches()) {
                return Duration.parse(str);
            }
        } catch (DateTimeParseException e) {
            throw new UnsupportedOperationException("\"" + str + "\" can't parse to Duration", e);
        }

        try {
            int millis = Integer.parseInt(str);
            return Duration.ofMillis(millis);
        } catch (Exception e) {
            throw new UnsupportedOperationException("\"" + str + "\" can't parse to Duration", e);
        }
    }

    private static long doParse(String unit, String str) {
        str = str.replace(unit, "");
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw new UnsupportedOperationException("\"" + str + "\" can't parse to Duration", e);
        }
    }

}
