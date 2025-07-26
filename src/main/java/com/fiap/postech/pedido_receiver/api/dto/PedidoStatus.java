package com.fiap.postech.pedido_receiver.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do pedido")
public enum PedidoStatus {
    ABERTO,
    FECHADO_COM_SUCESSO,
    FECHADO_SEM_ESTOQUE,
    FECHADO_SEM_CREDITO
}