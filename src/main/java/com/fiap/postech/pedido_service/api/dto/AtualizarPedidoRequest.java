package com.fiap.postech.pedido_service.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class AtualizarPedidoRequest {

    private List<PedidoItemRequest> itens;
}
