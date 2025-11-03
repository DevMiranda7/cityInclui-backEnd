package com.gtp.cityinclui.exception;

public class CloudStorageException extends RuntimeException {
    public CloudStorageException(String message) {
        super(message);
    }

    public CloudStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
