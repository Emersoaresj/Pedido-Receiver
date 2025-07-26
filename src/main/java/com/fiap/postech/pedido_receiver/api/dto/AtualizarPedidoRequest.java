package com.fiap.postech.pedido_receiver.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema(description = "Requisição para atualização dos itens de um pedido")
public class AtualizarPedidoRequest {

    @Schema(
            description = "Lista de itens que irão compor o pedido após a atualização",
            example = """
            [
              {
                "skuProduto": "SKU-123",
                "quantidadeItem": 2
              },
              {
                "skuProduto": "SKU-456",
                "quantidadeItem": 1
              }
            ]
        """
    )
    private List<PedidoItemRequest> itens;
}