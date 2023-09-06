package com.springproject;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	@JsonProperty("login_id")
    private String loginId;
    private String password;
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "User [loginId=" + loginId + ", password=" + password + "]";
	}
    
	
}
