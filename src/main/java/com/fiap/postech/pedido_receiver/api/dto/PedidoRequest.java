package com.fiap.postech.pedido_receiver.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Requisição para cadastro de pedido")
public class PedidoRequest {

    @Schema(description = "CPF do cliente", example = "12345678901")
    @NotBlank(message = "O CPF do cliente é obrigatório")
    private String cpfCliente;

    @Schema(description = "Lista de itens do pedido")
    @NotNull(message = "A lista de itens do pedido é obrigatória")
    @Size(min = 1, message = "O pedido deve ter pelo menos um item")
    private List<PedidoItemRequest> itens;
}