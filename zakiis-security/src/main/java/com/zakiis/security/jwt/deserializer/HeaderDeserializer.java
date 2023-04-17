package com.zakiis.security.jwt.deserializer;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.zakiis.core.util.JsonUtil;
import com.zakiis.security.jwt.interfaces.Header;
import com.zakiis.security.jwt.interfaces.PublicClaims;

public class HeaderDeserializer extends JsonDeserializer<Header> {

	@Override
	public Header deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		Map<String, JsonNode> tree = p.getCodec().readValue(p, new TypeReference<Map<String, JsonNode>>() {
		});
		if (tree == null) {
			return null;
		}
		String algorithm = JsonUtil.getString(tree.get(PublicClaims.ALGORITHM));
        String type = JsonUtil.getString(tree.get(PublicClaims.TYPE));
        String contentType = JsonUtil.getString(tree.get(PublicClaims.CONTENT_TYPE));
        String keyId = JsonUtil.getString(tree.get(PublicClaims.KEY_ID));
        return new BasicHeader(algorithm, type, contentType, keyId, tree);
	}

}
