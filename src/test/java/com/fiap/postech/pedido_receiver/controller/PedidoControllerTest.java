package com.fiap.postech.pedido_receiver.controller;

import com.fiap.postech.pedido_receiver.api.controller.PedidoController;
import com.fiap.postech.pedido_receiver.api.dto.*;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoControllerTest {

    @InjectMocks
    private PedidoController controller;

    @Mock
    private PedidoServicePort service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarPedido_DeveRetornarCreated() {
        PedidoRequest request = new PedidoRequest(); // Preencha se necessário
        ResponseDto responseDto = new ResponseDto();

        when(service.cadastrarPedido(any(PedidoRequest.class))).thenReturn(responseDto);

        ResponseEntity<ResponseDto> response = controller.cadastrarPedido(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(service, times(1)).cadastrarPedido(request);
    }

    @Test
    void buscarPedidoByCpfCliente_DeveRetornarOk() {
        String cpf = "12345678900";
        List<PedidoByClienteDto> lista = Arrays.asList(new PedidoByClienteDto());
        when(service.buscarPedidoByCpfCliente(anyString())).thenReturn(lista);

        ResponseEntity<List<PedidoByClienteDto>> response = controller.buscarPedidoByCpfCliente(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
        verify(service).buscarPedidoByCpfCliente(cpf);
    }

    @Test
    void listarTodos_DeveRetornarOk() {
        List<PedidoDto> pedidos = Collections.singletonList(new PedidoDto());
        when(service.listarTodos()).thenReturn(pedidos);

        ResponseEntity<List<PedidoDto>> response = controller.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidos, response.getBody());
        verify(service).listarTodos();
    }

    @Test
    void deletarPedido_DeveRetornarNoContent() {
        Integer idPedido = 99;
        // O método service.deletarPedido não retorna nada

        ResponseEntity<Void> response = controller.deletarPedido(idPedido);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).deletarPedido(idPedido);
    }

}
