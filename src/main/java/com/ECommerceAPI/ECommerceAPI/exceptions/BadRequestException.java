package com.ECommerceAPI.ECommerceAPI.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
