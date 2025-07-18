package com.fiap.postech.pedido_service.gateway.port;

import com.fiap.postech.pedido_service.api.dto.AtualizarPedidoRequest;
import com.fiap.postech.pedido_service.api.dto.PedidoRequest;
import com.fiap.postech.pedido_service.api.dto.PedidoStatus;
import com.fiap.postech.pedido_service.api.dto.ResponseDto;

public interface PedidoServicePort {

    ResponseDto cadastrarPedido(PedidoRequest request);

    ResponseDto atualizarPedido(Integer id, AtualizarPedidoRequest request);

    ResponseDto atualizaStatusPedido(Integer id, PedidoStatus novoStatus);

}
