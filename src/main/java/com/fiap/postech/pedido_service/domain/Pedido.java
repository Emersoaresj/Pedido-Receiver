package com.fiap.postech.pedido_service.domain;

import com.fiap.postech.pedido_service.api.dto.PedidoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    private Integer idPedido;
    private Integer idCliente;
    private PedidoStatus statusPedido;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotalPedido;
    private List<PedidoItem> itens;

    public boolean itensValidos() {
        if (itens == null || itens.isEmpty()) return false;
        return itens.stream().allMatch(item ->
                item.getQuantidadeItem() != null && item.getQuantidadeItem() > 0 &&
                        item.getPrecoUnitarioItem() != null && item.getPrecoUnitarioItem().compareTo(BigDecimal.ZERO) > 0
        );
    }

    public boolean valorTotalValido() {
        return valorTotalPedido != null && valorTotalPedido.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean clienteValido() {
        return idCliente != null;
    }


}
