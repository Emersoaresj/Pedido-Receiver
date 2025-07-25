package com.fiap.postech.pedido_receiver.domain.exceptions;

import com.fiap.postech.pedido_receiver.domain.exceptions.client.PedidoProdutoNotFoundException;
import com.fiap.postech.pedido_receiver.domain.exceptions.kafka.EnvioKafkaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalHandlerException {


    // Constantes para as chaves de resposta
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MENSAGEM = "mensagem";
    private static final String PATH = "path";


    @ExceptionHandler(ErroInternoException.class)
    public ResponseEntity<Map<String, Object>> handlerErroBancoDeDados(ErroInternoException erroInternoException, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put(ERROR, "Erro Interno.");
        response.put(MENSAGEM, erroInternoException.getMessage());
        response.put(PATH, request.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // PARA VALIDAÇÃO DE CAMPOS NA REQUISIÇÃO
    public ResponseEntity<Map<String, Object>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PedidoProdutoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerProdutoPedidoNotFoundException(PedidoProdutoNotFoundException pedidoProdutoNotFoundException) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(MENSAGEM, pedidoProdutoNotFoundException.getMessage());
        response.put(STATUS, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPedidoException.class)
    public ResponseEntity<Map<String, Object>> handlerInvalidPedidoException(InvalidPedidoException invalidPedidoException) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(MENSAGEM, invalidPedidoException.getMessage());
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerPedidoNotFoundException(PedidoNotFoundException pedidoNotFoundException) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(MENSAGEM, pedidoNotFoundException.getMessage());
        response.put(STATUS, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EnvioKafkaException.class)
    public ResponseEntity<Map<String, Object>> handlerEnvioKafkaException(EnvioKafkaException envioKafkaException) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(MENSAGEM, envioKafkaException.getMessage());
        response.put(STATUS, HttpStatus.SERVICE_UNAVAILABLE.value());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
