package com.todayworker.springboot.auth.exception;

import com.todayworker.springboot.domain.common.exception.BaseException;
import com.todayworker.springboot.domain.common.exception.enums.BaseErrorCodeIF;

public class OAuthException extends BaseException {
    public OAuthException(BaseErrorCodeIF errorCode) {
        super(errorCode);
    }

    public OAuthException(BaseErrorCodeIF errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }

}
