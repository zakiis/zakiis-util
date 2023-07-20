package com.zakiis.rpc.protocol;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RpcMessage {

	private int id;
    private byte messageType;
    private byte codec;
    private byte compressor;
    private Map<String, String> headMap = new HashMap<>();
    private Object body;
}
