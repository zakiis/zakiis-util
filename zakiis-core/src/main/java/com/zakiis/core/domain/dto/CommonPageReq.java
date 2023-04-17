package com.zakiis.core.domain.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonPageReq implements Serializable {

	private static final long serialVersionUID = -7580987963923117112L;

	@Positive
	private int pageSize = 10;

	@Positive
	private int pageNum = 1;
	
	@JsonIgnore
	public int getOffset() {
		return (pageNum - 1) * pageSize; 
	}
	
}
