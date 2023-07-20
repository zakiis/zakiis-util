package com.zakiis.rpc.protocol;

/**
 * The interface Message type aware.
 * @date 2023-07-06 10:14:11
 * @author Liu Zhenghua
 */
public interface MessageTypeAware {

	/**
     * Gets type code.
     * @return the type code
     */
    short getTypeCode();
}
