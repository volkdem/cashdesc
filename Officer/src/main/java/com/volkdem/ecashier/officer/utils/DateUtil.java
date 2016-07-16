package com.volkdem.ecashier.officer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Evgeny on 04.07.2016.
 */
public class DateUtil {
    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat( "dd/MM/yyyy@HH:mm" );
    public static String format(Date date) {
        return datetimeFormat.format( date );
    }
}
