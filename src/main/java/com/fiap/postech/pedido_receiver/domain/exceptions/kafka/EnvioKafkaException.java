package com.fiap.postech.pedido_receiver.domain.exceptions.kafka;

public class EnvioKafkaException extends RuntimeException {
    public EnvioKafkaException(String message, Throwable cause) {
        super(message, cause);
    }
}
