package com.ghflusao.exception;

/** Violação de regra de negócio, tratada nos controllers e exibida ao usuário. */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
