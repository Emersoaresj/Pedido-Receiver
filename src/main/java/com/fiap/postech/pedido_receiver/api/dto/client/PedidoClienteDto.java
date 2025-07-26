package com.fiap.postech.pedido_receiver.api.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Informações básicas do cliente no contexto de um pedido")
public class PedidoClienteDto {

    @Schema(description = "ID do cliente", example = "1")
    private Integer idCliente;

    @Schema(description = "CPF do cliente", example = "12345678901")
    private String cpfCliente;

    @Schema(description = "Nome do cliente", example = "João da Silva")
    private String nomeCliente;
}