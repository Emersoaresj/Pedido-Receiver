package com.fiap.postech.pedido_service.gateway.kafka;


import com.fiap.postech.pedido_service.api.dto.kafka.PedidoKafkaDTO;

public interface PedidoProducerPort {

    void enviarMensagem(PedidoKafkaDTO dto);

}

