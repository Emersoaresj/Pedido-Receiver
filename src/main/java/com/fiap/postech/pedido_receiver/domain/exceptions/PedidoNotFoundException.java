package com.fiap.postech.pedido_receiver.domain.exceptions;

public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException(String message) {
        super(message);
    }
}
