package com.zakiis.rpc.serializer;


public enum SerializerType {

    /**
     * The seata.
     * <p>
     * Math.pow(2, 0)
     */
    SEATA((byte)0x1),

    /**
     * The protobuf, 'io.seata:seata-serializer-protobuf' dependency must be referenced manually.
     * <p>
     * Math.pow(2, 1)
     */
    PROTOBUF((byte)0x2),

    /**
     * The kryo.
     * <p>
     * Math.pow(2, 2)
     */
    KRYO((byte)0x4),

    /**
     * The fst.
     * <p>
     * Math.pow(2, 3)
     */
    FST((byte)0x8),

    /**
     * The hessian.
     * <p>
     * Math.pow(2, 4)
     */
    HESSIAN((byte)0x16),
    ;

    private final byte code;

    SerializerType(final byte code) {
        this.code = code;
    }

    /**
     * Gets result code.
     *
     * @param code the code
     * @return the result code
     */
    public static SerializerType getByCode(int code) {
        for (SerializerType b : SerializerType.values()) {
            if (code == b.code) {
                return b;
            }
        }
        throw new IllegalArgumentException("unknown codec:" + code);
    }

    /**
     * Gets result code.
     *
     * @param name the name
     * @return the result code
     */
    public static SerializerType getByName(String name) {
        for (SerializerType b : SerializerType.values()) {
            if (b.name().equalsIgnoreCase(name)) {
                return b;
            }
        }
        throw new IllegalArgumentException("unknown codec:" + name);
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public byte getCode() {
        return code;
    }
}