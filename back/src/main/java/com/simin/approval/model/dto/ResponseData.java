package com.simin.approval.model.dto;

import lombok.Data;

@Data
public class ResponseData {
    private int status;
    private String message;
    private Object data1;
    private Object data2;
    private String token;
}
