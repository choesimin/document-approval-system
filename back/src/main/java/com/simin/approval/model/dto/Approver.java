package com.simin.approval.model.dto;

import lombok.Data;

@Data
public class Approver {
    private int seq;
    private int user_seq;
    private int document_seq;
    private String state;
    private String opinion;
    private int turn;
    private String findate;

    private String user_name;
}
