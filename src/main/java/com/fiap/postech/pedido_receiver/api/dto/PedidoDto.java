package com.fiap.postech.pedido_receiver.api.dto;

import com.fiap.postech.pedido_receiver.domain.PedidoItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {
    private Integer idPedido;
    private Integer idCliente;
    private PedidoStatus statusPedido;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotalPedido;
    private List<PedidoItem> itens;
}
