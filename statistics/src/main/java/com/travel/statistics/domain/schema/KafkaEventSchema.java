package com.travel.statistics.domain.schema;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;


public class KafkaEventSchema<T> implements DeserializationSchema<T>, SerializationSchema<T>{

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventSchema.class);

    private static final long serialVersionUID = 6154188370181669729L;

    private static final ObjectMapper OM = new ObjectMapper();

    private  Class<T> returnType;

    public KafkaEventSchema(Class<T> returnType){
        this.returnType = returnType;
    }

    @Override
    public T deserialize(byte[] bytes) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        String s = this.byteBuffer2String(buffer);
        return OM.readValue(s,returnType);
    }

    @Override
    public boolean isEndOfStream(T logstashMsg) {
        return false;
    }

    @Override
    public byte[] serialize(T logstashMsg) {
        return new byte[0];
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return TypeInformation.of(returnType);
    }

    /**
     *  将 ByteBuffer 转换为 String
     * @param byteBuffer
     * @return
     */
    private String byteBuffer2String(ByteBuffer byteBuffer){

        Charset charset = StandardCharsets.UTF_8;
        CharsetDecoder decoder = charset.newDecoder();
        try {
            CharBuffer charBuffer = decoder.decode(byteBuffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (CharacterCodingException e) {
            LOGGER.error("Byte buffer to String error,{}",e.getLocalizedMessage());
        }
        return "";
    }

}

