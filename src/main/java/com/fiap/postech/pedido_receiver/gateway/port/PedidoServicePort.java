package com.fiap.postech.pedido_receiver.gateway.port;

import com.fiap.postech.pedido_receiver.api.dto.*;

import java.util.List;

public interface PedidoServicePort {

    ResponseDto cadastrarPedido(PedidoRequest request);

    List<PedidoByClienteDto> buscarPedidoByCpfCliente(String cpfCliente);

    List<PedidoDto> listarTodos();

    void deletarPedido(Integer idPedido);

}
