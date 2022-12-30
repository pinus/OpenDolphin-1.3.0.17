package open.dolphin.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.swing.*;
import java.io.IOException;

/**
 * Json Utility.
 *
 * @author pns
 */
public class JsonUtils {
    private final static ObjectMapper mapper = new ObjectMapper();
    static { initialilze(mapper); }

    public static void initialilze(ObjectMapper objectMapper) {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Unable to make field private javax.swing.ImageIcon$AccessibleImageIcon javax.swing.ImageIcon.accessibleContext accessible:
        // module java.desktop does not "opens javax.swing" to unnamed module @21508cd1
        objectMapper.addMixIn(ImageIcon.class, ImageIconIgnore.class);
    }

    /**
     * Utility method to test converter
     *
     * @param obj object
     * @return Json string
     * */
    public static String toJson(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Utility method to test converter
     *
     * @param <T> formal parameter
     * @param json Json String
     * @param clazz Class to extract
     * @return Object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }
}
