package open.dolphin.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Json Utility
 */
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Utility method to test converter
     *
     * @param obj
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
     * @param <T>
     * @param json
     * @param clazz
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
