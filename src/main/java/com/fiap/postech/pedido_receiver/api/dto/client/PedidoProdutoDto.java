package com.fiap.postech.pedido_receiver.api.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Informações do produto no contexto de um pedido")
public class PedidoProdutoDto {

    @Schema(description = "ID do produto", example = "10")
    private Integer idProduto;

    @Schema(description = "SKU do produto", example = "SKU-123")
    private String skuProduto;

    @Schema(description = "Preço do produto", example = "99.99")
    private BigDecimal precoProduto;
}

