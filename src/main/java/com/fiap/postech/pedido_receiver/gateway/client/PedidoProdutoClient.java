package com.fiap.postech.pedido_receiver.gateway.client;

import com.fiap.postech.pedido_receiver.api.dto.client.PedidoProdutoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pedido-produto-service", url = "${produto.service.url}")
public interface PedidoProdutoClient {

    @GetMapping("/api/produtos/sku/{sku}")
    PedidoProdutoDto buscarPorSku(@PathVariable("sku") String sku);

}

