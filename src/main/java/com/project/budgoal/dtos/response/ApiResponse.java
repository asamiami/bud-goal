package com.project.budgoal.dtos.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ApiResponse<T> {

    private String message;

    private String status;

    private HttpStatus code;

    private T data;

    public ApiResponse(String message, String status, HttpStatus code, T data) {
        this.message = message;
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public ApiResponse(String message, String status, HttpStatus code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public ApiResponse(String message, HttpStatus code, T data) {
        this.status = message;
        this.code = code;
        this.data = data;
    }

    public ApiResponse(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }

    public ApiResponse() {
    }
}
