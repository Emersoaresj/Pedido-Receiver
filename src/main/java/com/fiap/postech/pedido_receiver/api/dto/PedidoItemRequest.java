package com.fiap.postech.pedido_receiver.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Item do pedido recebido na requisição")
public class PedidoItemRequest {

    @Schema(description = "SKU do produto", example = "SKU-123")
    @NotBlank(message = "O SKU do produto é obrigatório")
    private String skuProduto;

    @Schema(description = "Quantidade do produto", example = "3")
    @NotNull(message = "A quantidade do produto é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    private Integer quantidadeItem;
}