package util;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 *
 * @auther jinzhanwei
 * @data 2017/6/20 8:31
 * @project slm
 */
public class JsonUtil {
    /**
     * object转map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objectToMap(Object obj) {
        if (obj != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonStr = objectMapper.writeValueAsString(obj);
                Map<String, Object> map = JSONToObject(jsonStr, Map.class);
                return map;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * map转object
     *
     * @param obj
     * @return
     */
    public static <T> T mapToObject(Object obj,Class<T> clazz) {
        if (obj != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonStr = objectMapper.writeValueAsString(obj);
                return JSONToObject(jsonStr, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Object对像转换成JSON
     *
     * @param obj
     * @return String
     */
    public static String objectToJSON(Object obj) {
        if (obj != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * object写入HttpServletResponse
     *
     * @param response
     * @param object
     * @return
     */
   /* public static void writerJsonToResponse(HttpServletResponse response, Object object) {
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try {
            writer(response, objectToJSON(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * json字符串转换成JavaBean对象
     *
     * @param <T>
     * @param jsonStr
     * @param clazz
     * @return
     */
    public static <T> T jsonToEntity(String jsonStr, Class<T> clazz) {
        try {
            return JSONToObject(jsonStr, clazz);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转换Map集合
     *
     * @param jsonStr
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String jsonStr) {
        try {
            return JSONToObject(jsonStr, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转换成list<T>
     *
     * @param jsonStr
     * @return
     */
    public static List<?> jsonToList(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        try {
            return JSONToObject(jsonStr, List.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转换成list<T>,T复杂对象
     *
     * @param jsonStr
     * @param elementClasses list元素类型
     * @return
     */
    public static List<?> jsonToList(String jsonStr, Class<?> elementClasses) {
        try {
            JavaType javaType = getCollectionType(List.class, elementClasses);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonStr, javaType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转换成map<K,V>,K/V复杂对象
     *
     * @param jsonStr
     * @param keyClasses     K类型
     * @param elementClasses V类型
     * @return
     */
    public static Map<?, ?> jsonToMap(String jsonStr, Class<?> keyClasses, Class<?> elementClasses) {
        try {
            JavaType javaType = getCollectionType(Map.class, keyClasses, elementClasses);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonStr, javaType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*private static void writer(HttpServletResponse response, String str) {
        try {
            //设置页面不缓存
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = null;
            out = response.getWriter();
            out.print(str);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private static <E> E JSONToObject(String jsonStr, Class<E> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonStr.toString(), clazz);
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * Object对像转换成JSON 并且去空值
     *
     * @param obj
     * @return String
     */
    public static String objectToJSONandRemoveNull(Object obj) {
        if (obj != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            // 过滤对象的null属性.
            objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
            // 过滤map中的null值
            //objectMapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
            try {
                return objectMapper.writeValueAsString(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
