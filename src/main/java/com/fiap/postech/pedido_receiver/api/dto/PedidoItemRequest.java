package com.fiap.postech.pedido_receiver.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PedidoItemRequest {

    @NotBlank(message = "O SKU do produto é obrigatório")
    private String skuProduto;

    @NotNull(message = "A quantidade do produto é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    private Integer quantidadeItem;

}
