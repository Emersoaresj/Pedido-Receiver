package com.fiap.postech.pedido_receiver.domain.exceptions;

public class InvalidPedidoException extends RuntimeException {
    public InvalidPedidoException(String message) {
        super(message);
    }
}
