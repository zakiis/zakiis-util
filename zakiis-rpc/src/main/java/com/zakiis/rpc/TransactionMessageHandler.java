package com.zakiis.rpc;

import com.zakiis.rpc.protocol.AbstractMessage;
import com.zakiis.rpc.protocol.AbstractResultMessage;

public interface TransactionMessageHandler {

    /**
     * On a request received.
     *
     * @param request received request message
     * @param context context of the RPC
     * @return response to the request
     */
    AbstractResultMessage onRequest(AbstractMessage request, RpcContext context);

    /**
     * On a response received.
     *
     * @param response received response message
     * @param context  context of the RPC
     */
    void onResponse(AbstractResultMessage response, RpcContext context);

}
