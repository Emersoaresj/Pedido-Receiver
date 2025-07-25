package com.fiap.postech.pedido_receiver.domain.exceptions.client;

public class PedidoProdutoNotFoundException extends RuntimeException {
    public PedidoProdutoNotFoundException(String message) {
        super(message);
    }
}
