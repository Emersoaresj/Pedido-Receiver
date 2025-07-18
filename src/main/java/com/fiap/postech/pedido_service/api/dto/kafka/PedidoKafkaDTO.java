package com.fiap.postech.pedido_service.api.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoKafkaDTO {
    private Integer idPedido;
    private Integer idCliente;
    private BigDecimal valorTotalPedido;
    private List<PedidoItemKafkaDTO> itens;
}

