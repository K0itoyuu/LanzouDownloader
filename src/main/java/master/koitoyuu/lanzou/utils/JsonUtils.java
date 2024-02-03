package master.koitoyuu.lanzou.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Method;

//适配新版本和旧版本的Gson JsonParser类
public class JsonUtils {
    public static JsonObject parseString(String json) {
        try {
            Class<?> clazz = Class.forName("com.google.gson.JsonParser");
            Method method = clazz.getMethod("parseString", String.class);
            try {
                //新版本
                return ((JsonElement) method.invoke(null,json)).getAsJsonObject();
            } catch (Exception e) {
                //旧版本
                Object instance = clazz.newInstance();
                return ((JsonElement) method.invoke(instance,json)).getAsJsonObject();
            }
        } catch (Exception e) {
            return new JsonObject();
        }
    }
}
