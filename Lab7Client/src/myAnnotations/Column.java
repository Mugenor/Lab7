package myAnnotations;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mugenor on 02.06.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name();
}
