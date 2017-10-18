package aroundwise.nepi.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by mihai on 13/09/16.
 */
public class DateUtil {
    public static String getCurrentDateAsString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
