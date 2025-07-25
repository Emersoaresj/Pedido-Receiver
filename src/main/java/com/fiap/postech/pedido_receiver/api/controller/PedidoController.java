package com.fiap.postech.pedido_receiver.api.controller;

import com.fiap.postech.pedido_receiver.api.dto.*;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoServicePort;
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


    @PutMapping("/status/{id}")
    public ResponseEntity<ResponseDto> atualizarStatusPedido(
            @PathVariable("id") Integer id,
            @RequestParam("status") PedidoStatus novoStatus) {
        ResponseDto response = service.atualizaStatusPedido(id, novoStatus);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{cpfCliente}")
    public ResponseEntity<List<PedidoByClienteDto>> buscarPedidoByCpfCliente(@PathVariable String cpfCliente) {
        List<PedidoByClienteDto> response = service.buscarPedidoByCpfCliente(cpfCliente);
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
