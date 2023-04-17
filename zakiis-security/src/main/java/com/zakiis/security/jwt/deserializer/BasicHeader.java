package com.zakiis.security.jwt.deserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.zakiis.core.util.JsonUtil;
import com.zakiis.security.jwt.interfaces.Header;

public class BasicHeader implements Header {

	private final String algorithm;
    private final String type;
    private final String contentType;
    private final String keyId;
    private final Map<String, JsonNode> tree;
    
    BasicHeader(String algorithm, String type, String contentType, String keyId, Map<String, JsonNode> tree) {
        this.algorithm = algorithm;
        this.type = type;
        this.contentType = contentType;
        this.keyId = keyId;
        this.tree = Collections.unmodifiableMap(tree == null ? new HashMap<String, JsonNode>() : tree);
    }

    Map<String, JsonNode> getTree() {
        return tree;
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getKeyId() {
        return keyId;
    }

    @Override
    public String getHeaderClaim(String name) {
        return JsonUtil.getString(tree.get(name));
    }
}
