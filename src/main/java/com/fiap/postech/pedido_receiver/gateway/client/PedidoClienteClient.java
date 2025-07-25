package com.fiap.postech.pedido_receiver.gateway.client;

import com.fiap.postech.pedido_receiver.api.dto.client.PedidoClienteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service", url = "${cliente.service.url}")
public interface PedidoClienteClient {

    @GetMapping("/api/clientes/cpf/{cpf}")
    PedidoClienteDto buscarPorCpf(@PathVariable("cpf") String cpf);
}

