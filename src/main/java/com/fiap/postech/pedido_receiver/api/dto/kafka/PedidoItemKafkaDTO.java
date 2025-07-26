package com.fiap.postech.pedido_receiver.api.dto.kafka;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item do pedido para integração via Kafka")
public class PedidoItemKafkaDTO {

    @Schema(description = "ID do produto", example = "10")
    private Integer idProduto;

    @Schema(description = "Quantidade do item", example = "2")
    private Integer quantidadeItem;

    @Schema(description = "Preço unitário do item", example = "25.00")
    private BigDecimal precoUnitarioItem;
}