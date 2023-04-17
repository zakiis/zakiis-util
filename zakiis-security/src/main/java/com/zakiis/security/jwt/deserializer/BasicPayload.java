package com.zakiis.security.jwt.deserializer;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.zakiis.core.util.JsonUtil;
import com.zakiis.security.jwt.interfaces.Payload;

public class BasicPayload implements Payload {

	private final String issuer;
	private final String subject;
	private final List<String> audience;
	private final Date expiresAt;
	private final Date notBefore;
	private final Date issuedAt;
	private final String jwtId;
	private final Map<String, JsonNode> tree;

	BasicPayload(String issuer, String subject, List<String> audience, Date expiresAt, Date notBefore, Date issuedAt,
			String jwtId, Map<String, JsonNode> tree) {
		this.issuer = issuer;
		this.subject = subject;
		this.audience = audience != null ? Collections.unmodifiableList(audience) : null;
		this.expiresAt = expiresAt;
		this.notBefore = notBefore;
		this.issuedAt = issuedAt;
		this.jwtId = jwtId;
		this.tree = tree != null ? Collections.unmodifiableMap(tree) : Collections.<String, JsonNode>emptyMap();
	}

	Map<String, JsonNode> getTree() {
		return tree;
	}

	@Override
	public String getIssuer() {
		return issuer;
	}

	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public List<String> getAudience() {
		return audience;
	}

	@Override
	public Date getExpiresAt() {
		return expiresAt;
	}

	@Override
	public Date getNotBefore() {
		return notBefore;
	}

	@Override
	public Date getIssuedAt() {
		return issuedAt;
	}

	@Override
	public String getId() {
		return jwtId;
	}

	@Override
	public String getClaim(String name) {
		return JsonUtil.getString(tree.get(name));
	}

}
