package com.hotel.management.hotel_management.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException{
    @Getter
    private HttpStatus status;
    private String message;
    public APIException(HttpStatus status,String message)
    {
        this.status=status;
        this.message=message;
    }
    public APIException(HttpStatus status,String message,String message1) {

        super(message);
        this.status=status;
        this.message=message1;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
