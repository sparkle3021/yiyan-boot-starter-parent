package com.yiyan.boot.common.utils.desensitization;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

/**
 * 脱敏序列化器
 *
 * @author MENGJIAO
 */
public class SensitiveInfoSerializer extends JsonSerializer implements ContextualSerializer {

    private DesensitizedUtil.DesensitizedType type;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String desensitized = DesensitizedUtil.desensitized((CharSequence) value, type);
        gen.writeObject(desensitized);
    }

    @Override
    public JsonSerializer createContextual(SerializerProvider prov, BeanProperty property) {
        if (property != null) {
            Desensitization desensitization = property.getAnnotation(Desensitization.class);
            if (desensitization != null) {
                this.type = desensitization.type();
            }
        }
        return this;
    }
}