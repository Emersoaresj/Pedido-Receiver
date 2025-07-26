package com.fiap.postech.pedido_receiver.api.dto;

import com.fiap.postech.pedido_receiver.domain.PedidoItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de pedido retornado ao buscar pedidos por cliente")
public class PedidoByClienteDto {

    @Schema(description = "ID do pedido", example = "1")
    private Integer idPedido;

    @Schema(description = "Nome do cliente", example = "João da Silva")
    private String nomeCliente;

    @Schema(description = "Status do pedido", example = "ABERTO")
    private PedidoStatus statusPedido;

    @Schema(description = "Data e hora do pedido", example = "2024-07-24T17:35:00")
    private LocalDateTime dataPedido;

    @Schema(description = "Valor total do pedido", example = "200.00")
    private BigDecimal valorTotalPedido;

    @Schema(description = "Itens do pedido")
    private List<PedidoItem> itens;
}