package com.fiap.postech.pedido_receiver.api.dto.kafka;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pedido para integração via Kafka")
public class PedidoKafkaDTO {

    @Schema(description = "ID do pedido", example = "123")
    private Integer idPedido;

    @Schema(description = "ID do cliente", example = "1")
    private Integer idCliente;

    @Schema(description = "Valor total do pedido", example = "200.00")
    private BigDecimal valorTotalPedido;

    @Schema(description = "Lista de itens do pedido")
    private List<PedidoItemKafkaDTO> itens;
}