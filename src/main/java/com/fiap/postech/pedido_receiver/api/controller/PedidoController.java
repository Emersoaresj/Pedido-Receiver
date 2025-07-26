package com.fiap.postech.pedido_receiver.api.controller;

import com.fiap.postech.pedido_receiver.api.dto.*;
import com.fiap.postech.pedido_receiver.domain.exceptions.ErroInternoException;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
public class PedidoController {

    @Autowired
    private PedidoServicePort service;

    @Operation(summary = "Cadastrar um novo pedido")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDto.class),
                    examples = @ExampleObject(value = """
                        {
                          "message": "Pedido cadastrado com sucesso",
                          "data": {
                            "itens": [
                              {
                                "idPedidoItem": 1,
                                "idPedido": 1,
                                "idProduto": 99,
                                "quantidadeItem": 2,
                                "precoUnitarioItem": 100.0
                              }
                            ]
                          }
                        }
                        """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2024-07-24T10:00:00",
                                    "status": 400,
                                    "errors": {
                                        "skuProduto": "SKU obrigatório",
                                        "quantidade": "Quantidade obrigatória"
                                    }
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroInternoException.class),
                    examples = @ExampleObject(value = "{\"message\": \"Erro interno!\"}")))
    })
    @PostMapping
    public ResponseEntity<ResponseDto> cadastrarPedido(@Valid @RequestBody PedidoRequest request) {
        ResponseDto response = service.cadastrarPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Buscar pedidos por CPF do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoByClienteDto.class),
                    examples = @ExampleObject(value = """
                    [
                      {
                        "idPedido": 1,
                        "nomeCliente": "João",
                        "statusPedido": "ABERTO",
                        "dataPedido": "2024-07-24T17:35:00",
                        "valorTotalPedido": 200.0,
                        "itens": [
                          {
                            "idPedidoItem": 1,
                            "idPedido": 1,
                            "idProduto": 99,
                            "quantidadeItem": 2,
                            "precoUnitarioItem": 100.0
                          }
                        ]
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                                "timestamp": "2024-07-24T10:00:00",
                                "status": 400,
                                "errors": {
                                    "skuProduto": "SKU obrigatório",
                                    "quantidade": "Quantidade obrigatória"
                                }
                            }
                        """))),
            @ApiResponse(responseCode = "404", description = "Cliente ou Pedido não encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDto.class),
                    examples = {
                            @ExampleObject(
                                    name = "Cliente não encontrado",
                                    value = """
                                    {
                                        "message": "Cliente não encontrado!"
                                    }
                                """),
                            @ExampleObject(
                                    name = "Pedido não encontrado",
                                    value = """
                                    {
                                        "message": "Pedido não encontrado!"
                                    }
                                """)
                    })),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroInternoException.class),
                    examples = @ExampleObject(value = "{\"message\": \"Erro interno!\"}")))
    })
    @GetMapping("/cliente/{cpfCliente}")
    public ResponseEntity<List<PedidoByClienteDto>> buscarPedidoByCpfCliente(
            @Parameter(description = "CPF do cliente (apenas números)", example = "12345678901")
            @PathVariable String cpfCliente) {
        List<PedidoByClienteDto> response = service.buscarPedidoByCpfCliente(cpfCliente);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Listar todos os pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoDto.class),
                    examples = @ExampleObject(value = """
                        [
                          {
                            "idPedido": 1,
                            "idCliente": 1,
                            "statusPedido": "ABERTO",
                            "dataPedido": "2024-07-24T17:35:00",
                            "valorTotalPedido": 200.0,
                            "itens": [
                              {
                                "idPedidoItem": 1,
                                "idPedido": 1,
                                "idProduto": 99,
                                "quantidadeItem": 2,
                                "precoUnitarioItem": 100.0
                              }
                            ]
                          }
                        ]
                        """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2024-07-24T10:00:00",
                                    "status": 400,
                                    "errors": {
                                        "skuProduto": "SKU obrigatório",
                                        "quantidade": "Quantidade obrigatória"
                                    }
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroInternoException.class),
                    examples = @ExampleObject(value = "{\"message\": \"Erro interno!\"}")))
    })
    @GetMapping
    public ResponseEntity<List<PedidoDto>> listarTodos() {
        List<PedidoDto> response = service.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Deletar pedido por ID")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2024-07-24T10:00:00",
                                    "status": 400,
                                    "errors": {
                                        "skuProduto": "SKU obrigatório",
                                        "quantidade": "Quantidade obrigatória"
                                    }
                                }
                            """))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDto.class),
                    examples = @ExampleObject(value = "{\"message\": \"Pedido não encontrado!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErroInternoException.class),
                    examples = @ExampleObject(value = "{\"message\": \"Erro interno!\"}")))
    })
    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> deletarPedido(
            @Parameter(description = "ID do pedido", example = "1")
            @PathVariable Integer idPedido) {
        service.deletarPedido(idPedido);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
