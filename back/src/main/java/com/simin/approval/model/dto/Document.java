package com.simin.approval.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class Document {
	private int seq;
	private int user_seq;
	private String state;
	private String title;
	private String content;
	private int turn;
	private String regdate;
	private String findate;

	private List<Approver> approver_list;
	private String category;
	private String search_word;
	private String user_name;
}
