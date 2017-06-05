import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Created by Mugenor on 05.06.2017.
 */
public class Test {
    public static void main(String[] args) {
        Timestamp timestamp = new Timestamp(3243554455445l);
        OffsetDateTime time = OffsetDateTime.now();
        System.out.println(time);
        time =time.withHour(1);
        System.out.println(time.getYear());
        System.out.println(time.getYear() + "-" + time.getMonth().getValue() + "-" + time.getDayOfMonth() +
        " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
    }
}
