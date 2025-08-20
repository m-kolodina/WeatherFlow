package model;

import lombok.Data;

@Data
public class ErrorResponse {
    private Error error;

    @Data
    public static class Error {
        private int code;
        private String message;
    }
}