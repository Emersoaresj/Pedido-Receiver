package com.fiap.postech.pedido_service.gateway.port;

import com.fiap.postech.pedido_service.api.dto.ResponseDto;
import com.fiap.postech.pedido_service.domain.Pedido;

import java.util.List;

public interface PedidoRepositoryPort {

    ResponseDto cadastrarPedido(Pedido pedido);

    Pedido buscarPedidoPorId(Integer idPedido);

    ResponseDto atualizarPedido(Pedido pedido);

    List<Pedido> buscarPedidoPorIdCliente(Integer idCliente);

    List<Pedido> listarTodos();

    void deletarPedido(Integer idPedido);

}
