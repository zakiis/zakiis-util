package com.zakiis.security.test.model;

public class User {

	private Long id;
	private String username;
	private String password;
	private String age;
	private Address residence;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Address getResidence() {
		return residence;
	}
	public void setResidence(Address residence) {
		this.residence = residence;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
