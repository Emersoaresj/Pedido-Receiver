package com.fiap.postech.pedido_receiver.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class AtualizarPedidoRequest {

    private List<PedidoItemRequest> itens;
}
