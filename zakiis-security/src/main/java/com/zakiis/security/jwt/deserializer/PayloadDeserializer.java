package com.zakiis.security.jwt.deserializer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.zakiis.core.util.JsonUtil;
import com.zakiis.security.jwt.interfaces.Payload;
import com.zakiis.security.jwt.interfaces.PublicClaims;

public class PayloadDeserializer extends JsonDeserializer<Payload> {

	@Override
	public Payload deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		Map<String, JsonNode> tree = p.getCodec().readValue(p, new TypeReference<Map<String, JsonNode>>() {
        });
        if (tree == null) {
            return null;
        }

        String issuer = JsonUtil.getString(tree.get(PublicClaims.ISSUER));
        String subject = JsonUtil.getString(tree.get(PublicClaims.SUBJECT));
        List<String> audience = JsonUtil.getStringList(tree.get(PublicClaims.AUDIENCE));
        Date issuedAt = JsonUtil.getDate(tree.get(PublicClaims.ISSUED_AT));
        Date expiresAt = JsonUtil.getDate(tree.get(PublicClaims.EXPIRES_AT));
        Date notBefore = JsonUtil.getDate(tree.get(PublicClaims.NOT_BEFORE));
        String jwtId = JsonUtil.getString(tree.get(PublicClaims.JWT_ID));

        return new BasicPayload(issuer, subject, audience, expiresAt, notBefore, issuedAt, jwtId, tree);
	}

}
