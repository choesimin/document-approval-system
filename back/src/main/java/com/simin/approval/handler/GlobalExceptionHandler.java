package com.simin.approval.handler;


import com.simin.approval.exception.NoDataFoundException;
import com.simin.approval.exception.TokenExpiredException;
import com.simin.approval.exception.UnsuitableDataException;
import com.simin.approval.model.common.Status;
import com.simin.approval.model.dto.ResponseData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoDataFoundException.class)
    public ResponseData handleException(NoDataFoundException e) {
        ResponseData responseData = new ResponseData();

        responseData.setStatus(Status.NO_DATA);
        responseData.setMessage(e.getMessage());

        return responseData;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseData handleException(TokenExpiredException e) {
        ResponseData responseData = new ResponseData();

        responseData.setStatus(Status.NO_TOKEN);

        return responseData;
    }

    @ExceptionHandler(UnsuitableDataException.class)
    public ResponseData handleException(UnsuitableDataException e) {
        ResponseData responseData = new ResponseData();

        responseData.setStatus(Status.UNSUITABLE_DATA);
        responseData.setMessage(e.getMessage());

        return responseData;
    }

}
