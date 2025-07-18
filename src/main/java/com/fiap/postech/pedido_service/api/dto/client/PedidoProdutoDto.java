package com.fiap.postech.pedido_service.api.dto.client;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoProdutoDto {

    private Integer idProduto;
    private String skuProduto;
    private BigDecimal precoProduto;
}

