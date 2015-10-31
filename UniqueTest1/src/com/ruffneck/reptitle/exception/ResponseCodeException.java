package com.ruffneck.reptitle.exception;

import java.io.IOException;

/**
 * 响应码不正确的异常
 */
public class ResponseCodeException extends IOException {

    public ResponseCodeException(String message) {
        super(message);
    }
}
