package com.fiap.postech.pedido_service.gateway.port;

import com.fiap.postech.pedido_service.api.dto.*;

import java.util.List;

public interface PedidoServicePort {

    ResponseDto cadastrarPedido(PedidoRequest request);

    ResponseDto atualizarPedido(Integer id, AtualizarPedidoRequest request);

    ResponseDto atualizaStatusPedido(Integer id, PedidoStatus novoStatus);

    List<PedidoDto> buscarPorCpfCliente(String cpfCliente);

    List<PedidoDto> listarTodos();

    void deletarPedido(Integer idPedido);

}
