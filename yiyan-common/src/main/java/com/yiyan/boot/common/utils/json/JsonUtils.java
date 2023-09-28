package com.yiyan.boot.common.utils.json;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.yiyan.boot.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Json 工具类
 *
 * @author MENGJIAO
 * @createDate 2023-01-02
 */
@Slf4j
public class JsonUtils extends JSONUtil {

    private JsonUtils() {
    }

    /**
     * 配置ObjectMapper对象
     */
    public static ObjectMapper objectMapper = new ObjectMapper();

    private static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";


    // Jackson配置
    static {
        // 初始化JavaTimeModule
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        //处理LocalDateTime
        DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_PATTERN);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(localDateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(localDateTimeFormatter));

        //处理LocalDate
        DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(localDateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(localDateFormatter));

        //处理LocalTime
        DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(localTimeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(localTimeFormatter));

        //序列化时将类的数据类型存入json，以便反序列化的时候转换成正确的类型
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        //null的属性不进行序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 解决jackson2无法反序列化LocalDateTime的问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(javaTimeModule);
        //是否允许使用注释
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        //字段允许去除引号
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //不检测失败字段映射
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //时间字段输出时间戳
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //时间输出为毫秒而非纳秒
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        //空对象不出错
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //时间读取为毫秒而非纳秒
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        //是否输出空值字段
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
        //序列化时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // NULL 值处理
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                String fieldName = gen.getOutputContext().getCurrentName();
                try {
                    //反射获取字段类型
                    Field field = gen.getCurrentValue().getClass().getDeclaredField(fieldName);
                    if (Objects.equals(field.getType(), String.class)) {
                        //字符串型空值""
                        gen.writeString("");
                        return;
                    } else if (Objects.equals(field.getType(), List.class)) {
                        //列表型空值返回[]
                        gen.writeStartArray();
                        gen.writeEndArray();
                        return;
                    } else if (Objects.equals(field.getType(), Map.class)) {
                        //map型空值返回{}
                        gen.writeStartObject();
                        gen.writeEndObject();
                        return;
                    }
                } catch (NoSuchFieldException e) {
                    gen.writeString("");
                }
                //默认返回""
                gen.writeString("");
            }
        });
    }

    /**
     * 对象转换
     *
     * @param obj the obj
     * @return string string
     */
    public static <T> T cov(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

    /**
     * 将object对象转成json字符串
     *
     * @param object the object
     * @return string string
     */
    public static String toJson(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Object to Json exception ", e);
            return null;
        }
    }

    /**
     * 将JsonStr转成泛型bean
     *
     * @param <T>     the type parameter
     * @param jsonStr the gson string
     * @param cls     the cls
     * @return t t
     */
    public static <T> T toObj(String jsonStr, Class<T> cls) {
        if (StringUtils.isBlank(jsonStr) || cls == null) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonStr, cls);
        } catch (Exception e) {
            log.error("Json to Bean exception", e);
            return null;
        }
    }

    /**
     * 解析Map
     * 反序列化为Map集合
     *
     * @param mapJsonStr 地图json str
     * @param kClazz     k clazz
     * @param vClazz     v clazz
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> toMap(String mapJsonStr, Class<K> kClazz, Class<V> vClazz) {
        if (StringUtils.isBlank(mapJsonStr) || kClazz == null || vClazz == null) {
            return Collections.emptyMap();
        }
        Map<K, V> map;
        try {
            map = objectMapper.readValue(mapJsonStr, objectMapper.getTypeFactory().constructParametricType(Map.class, kClazz, vClazz));
        } catch (JsonProcessingException e) {
            log.error("Json to Map exception", e);
            return null;
        }
        return map;
    }

    /**
     * 转成list中有map的
     *
     * @param <T>     the type parameter
     * @param jsonStr the gson string
     * @return list list
     */
    public static <T> List<Map<Object, T>> toListMap(String jsonStr, Class<T> clz) {
        if (StringUtils.isBlank(jsonStr) || clz == null) {
            return null;
        }
        List<String> mapJsonStr = toList(jsonStr, String.class);

        assert mapJsonStr != null;

        List<Map<Object, T>> list = new ArrayList<>(mapJsonStr.size());
        for (String mapStr : mapJsonStr) {
            list.add(toMap(mapStr, Object.class, clz));
        }
        return list;
    }

    /**
     * 创建一个Json Object
     *
     * @return the json object
     */
    public static ObjectNode emptyJsonObject() {
        return objectMapper.createObjectNode();
    }

    /**
     * 创建Json Array
     *
     * @return the json array
     */
    public static ArrayNode createJsonArray() {
        return objectMapper.createArrayNode();
    }

}

