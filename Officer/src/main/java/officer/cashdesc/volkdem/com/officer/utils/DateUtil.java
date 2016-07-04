package officer.cashdesc.volkdem.com.officer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Evgeny on 04.07.2016.
 */
public class DateUtil {
    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat( "dd/mm/yyyy@HH:MM" );
    public static String format(Date date) {
        return datetimeFormat.format( date );
    }
}