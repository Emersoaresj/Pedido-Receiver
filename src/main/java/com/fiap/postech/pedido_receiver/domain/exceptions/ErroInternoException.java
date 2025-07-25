package com.fiap.postech.pedido_receiver.domain.exceptions;

public class ErroInternoException extends RuntimeException {
    public ErroInternoException(String message) {
        super(message);
    }
}
