package datalicious.com.news.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nayan on 27/6/16.
 */
public class DateUtil {
    /**
     * formats a date in dd-mm-yyyy pattern
     */
    public static final SimpleDateFormat DATE_FORMATTER_OUT = new SimpleDateFormat(
            "dd/MMMM/yyyy HH:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat DATE_FORMATTER_INPUT;

    static {
        DATE_FORMATTER_INPUT = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        DATE_FORMATTER_INPUT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static final String dateToString(String date) {
        try {
            return DATE_FORMATTER_OUT.format(DATE_FORMATTER_INPUT.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
}
