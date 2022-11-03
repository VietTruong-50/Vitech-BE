package com.hust.vitech.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    T result;
    String errorCode;
    Object message;
    int responseCode;

    public static <T> ApiResponse<T> successWithResult(T result){
        return new ApiResponse<T>(result, null, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
    }

    public static <T> ApiResponse<T> successWithResult(T result, String message){
        return new ApiResponse<T>(result, null, message, HttpStatus.OK.value());
    }

    public static <T> ApiResponse<T> failureWithCode(String errorCode, String message){
        return new ApiResponse<T>(null, errorCode, message, HttpStatus.BAD_REQUEST.value());
    }

    public static <T> ApiResponse<T> failureWithCode(String errorCode, String message, T result){
        return new ApiResponse<T>(result, errorCode, message, HttpStatus.BAD_REQUEST.value());
    }

    public static <T> ApiResponse<T> failureWithCode(String errorCode, String message, T result, HttpStatus httpStatus){
        return new ApiResponse<T>(result, errorCode, message, httpStatus.value());
    }
}
