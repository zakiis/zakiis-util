package com.zakiis.core.domain.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonPageResp<T> implements Serializable {

	private static final long serialVersionUID = 264843070774401640L;
	
	private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> dataList;

    
    public static <T> CommonPageResp<T> of(List<T> dataList, CommonPageReq pageReq, Long total) {
    	CommonPageResp<T> pageResp = new CommonPageResp<>();
    	pageResp.setPageNum(pageReq.getPageNum());
    	pageResp.setPageSize(pageReq.getPageSize());
    	pageResp.setTotal(total);
    	pageResp.setTotalPage((int)((total + pageReq.getPageSize() - 1) / pageReq.getPageSize()));
    	pageResp.setDataList(dataList);
    	return pageResp;
    }
}
