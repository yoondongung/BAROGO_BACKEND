package com.barogo.backend.exception;

public class PasswordException extends RuntimeException {

    private static final long serialVersionUID = -2274257790586533891L;

    public PasswordException(String message) {
        super(message);
    }

}
