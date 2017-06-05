import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.time.ZoneId;

/**
 * Created by Mugenor on 05.06.2017.
 */
public class GsonDeserializeExclusion implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaredClass() == ZoneId.class;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

}