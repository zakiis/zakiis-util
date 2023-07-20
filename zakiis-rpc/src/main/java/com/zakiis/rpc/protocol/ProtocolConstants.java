package com.zakiis.rpc.protocol;

import com.zakiis.rpc.compressor.CompressorType;
import com.zakiis.rpc.serializer.SerializerType;

public interface ProtocolConstants {

    /**
     * Magic code
     */
    byte[] MAGIC_CODE_BYTES = {(byte) 0xda, (byte) 0xda};

    /**
     * Protocol version
     */
    byte VERSION = 1;

    /**
     * Max frame length
     */
    int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

    /**
     * HEAD_LENGTH of protocol v1
     */
    int V1_HEAD_LENGTH = 16;
    
    /**
     * Message type: Request
     */
    byte MSGTYPE_RESQUEST_SYNC = 0;
    /**
     * Message type: Response
     */
    byte MSGTYPE_RESPONSE = 1;
    /**
     * Message type: Request which no need response
     */
    byte MSGTYPE_RESQUEST_ONEWAY = 2;
    /**
     * Message type: Heartbeat Request
     */
    byte MSGTYPE_HEARTBEAT_REQUEST = 3;
    /**
     * Message type: Heartbeat Response
     */
    byte MSGTYPE_HEARTBEAT_RESPONSE = 4;

    //byte MSGTYPE_NEGOTIATOR_REQUEST = 5;
    //byte MSGTYPE_NEGOTIATOR_RESPONSE = 6;

    /**
     * Configured codec by user, default is SEATA
     * 
     * @see SerializerType#SEATA
     */
    byte CONFIGURED_CODEC = SerializerType.getByName(SerializerType.SEATA.name()).getCode();

    /**
     * Configured compressor by user, default is NONE
     *
     * @see CompressorType#NONE
     */
    byte CONFIGURED_COMPRESSOR = CompressorType.getByName(CompressorType.NONE.name()).getCode();
}
