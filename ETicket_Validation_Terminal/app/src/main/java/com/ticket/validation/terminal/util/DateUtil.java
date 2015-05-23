package com.ticket.validation.terminal.util;

import java.text.SimpleDateFormat;

/**
 * Created by dengshengjin on 15/5/23.
 */
public class DateUtil {
    public static String getFormatTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String timeStr = simpleDateFormat.format(time);
        return timeStr;
    }
}
