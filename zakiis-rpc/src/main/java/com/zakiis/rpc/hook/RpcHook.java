package com.zakiis.rpc.hook;

import com.zakiis.rpc.protocol.RpcMessage;

public interface RpcHook {

    void doBeforeRequest(String remoteAddr, RpcMessage request);

    void doAfterResponse(String remoteAddr, RpcMessage request, Object response);
}
