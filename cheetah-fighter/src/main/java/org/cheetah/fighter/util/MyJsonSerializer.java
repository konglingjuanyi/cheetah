package org.cheetah.fighter.util;

import akka.serialization.JSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.cheetah.commons.logger.Err;
import org.cheetah.commons.utils.JSerializeException;

import java.io.IOException;

/**
 * Created by Max on 2016/2/28.
 */
public class MyJsonSerializer extends JSerializer {
    private final static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
        try {
            return objectMapper.readValue(bytes, manifest);
        } catch (IOException e) {
            Err.log(this.getClass(), "MyJsonSerializer fromBinaryJava fail", e);
            throw new JSerializeException(null, e);
        }
    }

    @Override
    public int identifier() {
        return 123456;
    }

    @Override
    public byte[] toBinary(Object o) {
        try {
            return objectMapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            Err.log(this.getClass(), "MyJsonSerializer toBinary fail", e);
            throw new JSerializeException(null, e);
        }
    }

    @Override
    public boolean includeManifest() {
        return false;
    }
}
