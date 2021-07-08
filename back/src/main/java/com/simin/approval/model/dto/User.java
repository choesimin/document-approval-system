package com.simin.approval.model.dto;

import lombok.Data;

@Data
public class User {
	private int seq;
	private String name;
	private String id;
	private String password;
	private String regdate;

	private String repassword;
}
