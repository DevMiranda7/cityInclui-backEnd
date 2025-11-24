package com.gtp.cityinclui.errorDTO;

import java.util.Objects;

public class ErrorResponseDTO {
    private String message;
    private int status;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ErrorResponseDTO{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponseDTO that = (ErrorResponseDTO) o;
        return status == that.status && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, status);
    }
}
