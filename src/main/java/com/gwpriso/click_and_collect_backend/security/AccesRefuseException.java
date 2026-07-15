package com.gwpriso.click_and_collect_backend.security;

public class AccesRefuseException extends RuntimeException {
    public AccesRefuseException(String message) {
        super(message);
    }
}