package com.todayworker.springboot.auth.exception;

import com.todayworker.springboot.domain.common.exception.enums.BaseErrorCodeIF;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OAuthErrorCode implements BaseErrorCodeIF {
    public static final String NOT_SUPPORTED_PROVIDER = "지원하지 않는 OAuth 공급자";

    private HttpStatus errorStatus;
    private String errorMessage;

    public static OAuthErrorCode of(HttpStatus errorStatus, String errorMessage) {
        return new OAuthErrorCode(errorStatus, errorMessage);
    }
    public static OAuthErrorCode of(HttpStatus errorStatus, String errorMessage, String param) {
        return new OAuthErrorCode(errorStatus, errorMessage, param);
    }

    private OAuthErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.errorStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    private OAuthErrorCode(HttpStatus httpStatus, String errorMessage, String param) {
        this.errorStatus = httpStatus;
        this.errorMessage = errorMessage + "[" + param + "]";
    }
}
