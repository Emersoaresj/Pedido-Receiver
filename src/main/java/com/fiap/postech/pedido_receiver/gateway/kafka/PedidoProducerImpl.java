package com.fiap.postech.pedido_receiver.gateway.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.postech.pedido_receiver.api.dto.kafka.PedidoKafkaDTO;
import com.fiap.postech.pedido_receiver.domain.exceptions.kafka.EnvioKafkaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PedidoProducerImpl implements PedidoProducerPort {

    private static final String TOPICO = "pedidos-criados";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PedidoProducerImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void enviarMensagem(PedidoKafkaDTO dto) {
        try {
            String mensagem = objectMapper.writeValueAsString(dto); // Serializa o DTO
            kafkaTemplate.send(TOPICO, mensagem);
            log.info("Mensagem enviada para o tópico {}: {}", TOPICO, mensagem);
        } catch (JsonProcessingException e){
            log.error("Erro ao serializar mensagem para o Kafka", e);
            throw new EnvioKafkaException("Erro ao serializar mensagem para o Kafka", e);
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem para o Kafka", e);
            throw new EnvioKafkaException("Erro ao enviar mensagem para o Kafka", e);
        }
    }

}
