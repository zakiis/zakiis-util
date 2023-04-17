package com.zakiis.core.builder;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class CookieBuilder {

	private static final ZoneId GMT = ZoneId.of("GMT");
	private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).withZone(GMT);
	
	private String name;
	private String value;
	private Duration maxAge;
	private String domain;
	private String path;
	private boolean secure;
	private boolean httpOnly;
	private String sameSite;

	public CookieBuilder(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public CookieBuilder domain(String domain) {
		this.domain = domain;
		return this;
	}

	public CookieBuilder path(String path) {
		this.path = path;
		return this;
	}

	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append('=').append(value);
		if (StringUtils.isNotBlank(path)) {
			sb.append("; Path=").append(path);
		}
		if (StringUtils.isNotBlank(domain)) {
			sb.append("; Domain=").append(domain);
		}
		if (!this.maxAge.isNegative()) {
			sb.append("; Max-Age=").append(this.maxAge.getSeconds());
			sb.append("; Expires=");
			long millis = this.maxAge.getSeconds() > 0 ? System.currentTimeMillis() + this.maxAge.toMillis() : 0;
			sb.append(formatCookieDate(millis));
		}
		if (this.secure) {
			sb.append("; Secure");
		}
		if (this.httpOnly) {
			sb.append("; HttpOnly");
		}
		if (StringUtils.isNotBlank(sameSite)) {
			sb.append("; SameSite=").append(this.sameSite);
		}
		return sb.toString();
	}
	
	String formatCookieDate(long date) {
		Instant instant = Instant.ofEpochMilli(date);
		ZonedDateTime time = ZonedDateTime.ofInstant(instant, GMT);
		return DATE_FORMATTER.format(time);
	}
}
