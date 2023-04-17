package com.zakiis.security.jwt;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zakiis.security.jwt.deserializer.HeaderDeserializer;
import com.zakiis.security.jwt.deserializer.PayloadDeserializer;
import com.zakiis.security.jwt.interfaces.Header;
import com.zakiis.security.jwt.interfaces.Payload;

public class JWTParser {
	
	static ObjectMapper mapper = new ObjectMapper();
	static Logger logger = LoggerFactory.getLogger(JWTParser.class);
	static Logger log = LoggerFactory.getLogger(JWTParser.class);
	
	static {
		mapper
			//.setVisibility(PropertyAccessor.ALL, Visibility.NONE)
			//.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
			.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(Header.class, new HeaderDeserializer());
		simpleModule.addDeserializer(Payload.class, new PayloadDeserializer());
		mapper.registerModule(simpleModule);
	}

	public static Payload parsePayload(String json) {
		try {
			return mapper.readValue(json, Payload.class);
		} catch (Exception e) {
			log.warn("parse payload got an exception, payload:{}", json, e);
		}
		return null;
	}
	
	public static Header parseHeader(String json) {
		try {
			return mapper.readValue(json, Header.class);
		} catch (Exception e) {
			log.warn("parse header got an exception, header:{}", json, e);
		}
		return null;
	}
}
