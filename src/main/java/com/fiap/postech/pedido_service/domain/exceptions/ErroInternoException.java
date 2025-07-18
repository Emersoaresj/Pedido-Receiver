package com.fiap.postech.pedido_service.domain.exceptions;

public class ErroInternoException extends RuntimeException {
    public ErroInternoException(String message) {
        super(message);
    }
}
