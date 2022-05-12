package com.elcom.report.model.dto;

import org.springframework.http.HttpStatus;

public class ResponseMessageDTO {
    private int status;
    private String message;
    private Object data;

    public ResponseMessageDTO(){

    }

    public ResponseMessageDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseMessageDTO(Object data){
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.toString();
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}