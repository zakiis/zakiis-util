package com.zakiis.rpc.serializer;

import com.zakiis.core.loader.EnhancedServiceLoader;
import com.zakiis.core.loader.EnhancedServiceNotFoundException;
import com.zakiis.core.util.ReflectionUtil;

public final class SerializerServiceLoader {

    private SerializerServiceLoader() {
    }


    private static final String PROTOBUF_SERIALIZER_CLASS_NAME = "io.seata.serializer.protobuf.ProtobufSerializer";

    /**
     * Load the service of {@link Serializer}
     *
     * @param type the serializer type
     * @return the service of {@link Serializer}
     * @throws EnhancedServiceNotFoundException the enhanced service not found exception
     */
    public static Serializer load(SerializerType type) throws EnhancedServiceNotFoundException {
        if (type == SerializerType.PROTOBUF) {
            try {
                ReflectionUtil.getClassByName(PROTOBUF_SERIALIZER_CLASS_NAME);
            } catch (ClassNotFoundException e) {
                throw new EnhancedServiceNotFoundException("'ProtobufSerializer' not found. " +
                        "Please manually reference 'io.seata:seata-serializer-protobuf' dependency ", e);
            }
        }
        return EnhancedServiceLoader.load(Serializer.class, type.name());
    }
}