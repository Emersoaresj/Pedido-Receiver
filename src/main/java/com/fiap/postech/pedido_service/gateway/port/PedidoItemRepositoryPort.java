package com.fiap.postech.pedido_service.gateway.port;

import com.fiap.postech.pedido_service.domain.PedidoItem;

import java.util.List;

public interface PedidoItemRepositoryPort {

    List<PedidoItem> buscarItensPedido(Integer idPedido);

    boolean atualizarPedidoItem(PedidoItem pedidoItem);

}
