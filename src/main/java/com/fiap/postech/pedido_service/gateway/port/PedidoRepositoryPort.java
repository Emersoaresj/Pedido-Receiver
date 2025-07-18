package com.fiap.postech.pedido_service.gateway.port;

import com.fiap.postech.pedido_service.domain.Pedido;
import com.fiap.postech.pedido_service.api.dto.ResponseDto;

public interface PedidoRepositoryPort {

    ResponseDto cadastrarPedido(Pedido pedido);

    Pedido buscarPedidoPorId(Integer idPedido);

    ResponseDto atualizarPedido(Pedido pedido);

}
