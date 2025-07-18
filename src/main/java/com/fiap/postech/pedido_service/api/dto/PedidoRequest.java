package com.fiap.postech.pedido_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PedidoRequest {

    @NotBlank(message = "O CPF do cliente é obrigatório")
    private String cpfCliente;
    @NotNull(message = "A lista de itens do pedido é obrigatória")
    @Size(min = 1, message = "O pedido deve ter pelo menos um item")
    private List<PedidoItemRequest> itens;
}

