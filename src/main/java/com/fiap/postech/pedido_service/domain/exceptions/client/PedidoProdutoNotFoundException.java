package com.fiap.postech.pedido_service.domain.exceptions.client;

public class PedidoProdutoNotFoundException extends RuntimeException {
    public PedidoProdutoNotFoundException(String message) {
        super(message);
    }
}
