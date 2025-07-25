package com.fiap.postech.pedido_service.api.controller;

import com.fiap.postech.pedido_service.api.dto.*;
import com.fiap.postech.pedido_service.gateway.port.PedidoServicePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoServicePort service;

    @PostMapping
    public ResponseEntity<ResponseDto> cadastrarPedido(@Valid @RequestBody PedidoRequest request) {
        ResponseDto response = service.cadastrarPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> atualizarPedido(@PathVariable("id") Integer id, @RequestBody AtualizarPedidoRequest request) {
        ResponseDto response = service.atualizarPedido(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<ResponseDto> atualizarStatusPedido(
            @PathVariable("id") Integer id,
            @RequestParam("status") PedidoStatus novoStatus) {
        ResponseDto response = service.atualizaStatusPedido(id, novoStatus);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{cpfCliente}")
    public ResponseEntity<List<PedidoDto>> buscarPorCpfCliente(@PathVariable String cpfCliente) {
        List<PedidoDto> response = service.buscarPorCpfCliente(cpfCliente);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PedidoDto>> listarTodos() {
        List<PedidoDto> response = service.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Integer idPedido) {
        service.deletarPedido(idPedido);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
