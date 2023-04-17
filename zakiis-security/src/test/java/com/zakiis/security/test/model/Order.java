package com.zakiis.security.test.model;

import java.math.BigDecimal;

public class Order {

	private String orderNo;
	private BigDecimal money;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	
}
