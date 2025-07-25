package com.fiap.postech.pedido_receiver.api.dto.client;

import lombok.Data;

@Data
public class PedidoClienteDto {

    private Integer idCliente;
    private String cpfCliente;
    private String nomeCliente;
}
