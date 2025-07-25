package com.fiap.postech.pedido_receiver.gateway.kafka;


import com.fiap.postech.pedido_receiver.api.dto.kafka.PedidoKafkaDTO;

public interface PedidoProducerPort {

    void enviarMensagem(PedidoKafkaDTO dto);

}

