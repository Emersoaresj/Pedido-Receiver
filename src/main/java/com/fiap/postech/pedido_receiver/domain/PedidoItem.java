package com.fiap.postech.pedido_receiver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItem {
    private Integer idPedidoItem;
    private Integer idPedido;
    private Integer idProduto;
    private Integer quantidadeItem;
    private BigDecimal precoUnitarioItem;
}

