package com.fiap.postech.pedido_receiver.serviceImpl;

import com.fiap.postech.pedido_receiver.api.dto.*;
import com.fiap.postech.pedido_receiver.api.dto.client.PedidoClienteDto;
import com.fiap.postech.pedido_receiver.api.dto.client.PedidoProdutoDto;
import com.fiap.postech.pedido_receiver.api.dto.kafka.PedidoKafkaDTO;
import com.fiap.postech.pedido_receiver.domain.Pedido;
import com.fiap.postech.pedido_receiver.domain.PedidoItem;
import com.fiap.postech.pedido_receiver.domain.exceptions.ErroInternoException;
import com.fiap.postech.pedido_receiver.domain.exceptions.InvalidPedidoException;
import com.fiap.postech.pedido_receiver.domain.exceptions.PedidoNotFoundException;
import com.fiap.postech.pedido_receiver.domain.exceptions.client.PedidoProdutoNotFoundException;
import com.fiap.postech.pedido_receiver.gateway.client.PedidoClienteClient;
import com.fiap.postech.pedido_receiver.gateway.client.PedidoProdutoClient;
import com.fiap.postech.pedido_receiver.gateway.kafka.PedidoProducerPort;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoItemRepositoryPort;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoRepositoryPort;
import com.fiap.postech.pedido_receiver.service.PedidoServiceImpl;
import com.fiap.postech.pedido_receiver.utils.ConstantUtils;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {

    @InjectMocks
    private PedidoServiceImpl service;

    @Mock
    private PedidoClienteClient pedidoClienteClient;
    @Mock
    private PedidoProdutoClient produtoClient;
    @Mock
    private PedidoRepositoryPort repositoryPort;
    @Mock
    private PedidoProducerPort pedidoProducerPort;
    @Mock
    private PedidoItemRepositoryPort pedidoItemRepositoryPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------- Testes para cadastrarPedido -----------

    @Test
    void cadastrarPedido_DeveCadastrarComSucesso() {
        // Arrange
        PedidoRequest request = new PedidoRequest();
        request.setCpfCliente("12345678900");

        // Monte a lista de itens corretamente!
        List<PedidoItemRequest> itensRequest = new ArrayList<>();
        itensRequest.add(new PedidoItemRequest(
                "SKU123", // skuProduto
                2         // quantidadeItem
        ));
        request.setItens(itensRequest);

        PedidoClienteDto clienteDto = new PedidoClienteDto();
        clienteDto.setIdCliente(1);

        // Mock para produtoClient (busca pelo sku)
        PedidoProdutoDto produtoDto = new PedidoProdutoDto();
        produtoDto.setIdProduto(1);
        produtoDto.setPrecoProduto(new BigDecimal("10.0"));
        when(produtoClient.buscarPorSku(anyString())).thenReturn(produtoDto);

        // Mock do client de cliente
        when(pedidoClienteClient.buscarPorCpf(anyString())).thenReturn(clienteDto);

        // Mock para salvar pedido no repositório
        ResponseDto responseDto = new ResponseDto();
        Map<String, Object> data = new HashMap<>();
        data.put("idPedido", 1);
        responseDto.setData(data);
        when(repositoryPort.cadastrarPedido(any(Pedido.class))).thenReturn(responseDto);

        // Mock do envio para o Kafka
        doNothing().when(pedidoProducerPort).enviarMensagem(any(PedidoKafkaDTO.class));

        // Act
        ResponseDto retorno = service.cadastrarPedido(request);

        // Assert
        assertNotNull(retorno);
        assertEquals(data, retorno.getData());
        verify(pedidoProducerPort).enviarMensagem(any(PedidoKafkaDTO.class));
    }



    // ----------- Testes para buscarPedidoByCpfCliente -----------

    @Test
    void buscarPedidoByCpfCliente_DeveRetornarPedidos() {
        PedidoClienteDto clienteDto = new PedidoClienteDto();
        clienteDto.setIdCliente(1);
        clienteDto.setNomeCliente("João");

        Pedido pedido = new Pedido(1, 1, PedidoStatus.ABERTO, null, new BigDecimal("20.0"), Collections.emptyList());

        // Retorne ao menos um item na busca de itens do pedido!
        PedidoItem item = new PedidoItem(1, 1, 1, 2, new BigDecimal("10.0"));

        when(pedidoClienteClient.buscarPorCpf(anyString())).thenReturn(clienteDto);
        when(repositoryPort.buscarPedidoPorIdCliente(anyInt())).thenReturn(Collections.singletonList(pedido));
        when(pedidoItemRepositoryPort.buscarItensPedido(anyInt())).thenReturn(Collections.singletonList(item));

        List<PedidoByClienteDto> resultado = service.buscarPedidoByCpfCliente("12345678900");

        assertFalse(resultado.isEmpty());
        assertEquals("João", resultado.get(0).getNomeCliente());
    }


    @Test
    void buscarPedidoByCpfCliente_DeveLancarErroInternoException() {
        when(pedidoClienteClient.buscarPorCpf(anyString())).thenThrow(new RuntimeException("erro"));

        ErroInternoException ex = assertThrows(
                ErroInternoException.class,
                () -> service.buscarPedidoByCpfCliente("111")
        );
        assertTrue(ex.getMessage().contains("Erro interno ao tentar buscar pedidos"));
    }

    // ----------- Testes para listarTodos -----------

    @Test
    void listarTodos_DeveRetornarPedidos() {
        Pedido pedido = new Pedido(1, 1, PedidoStatus.ABERTO, null, new BigDecimal("20.0"), Collections.emptyList());
        PedidoItem item = new PedidoItem(1, 1, 1, 2, new BigDecimal("10.0"));
        when(repositoryPort.listarTodos()).thenReturn(Collections.singletonList(pedido));
        when(pedidoItemRepositoryPort.buscarItensPedido(anyInt())).thenReturn(Collections.singletonList(item)); // Pelo menos um item!

        List<PedidoDto> resultado = service.listarTodos();

        assertFalse(resultado.isEmpty());
    }


    @Test
    void listarTodos_DeveLancarErroInternoException() {
        when(repositoryPort.listarTodos()).thenThrow(new RuntimeException("erro"));

        ErroInternoException ex = assertThrows(
                ErroInternoException.class,
                () -> service.listarTodos()
        );
        assertTrue(ex.getMessage().contains("Erro interno ao tentar listar pedidos"));
    }

    // ----------- Testes para deletarPedido -----------

    @Test
    void deletarPedido_DeveDeletarComSucesso() {
        doNothing().when(repositoryPort).deletarPedido(anyInt());
        doNothing().when(pedidoItemRepositoryPort).deletarPedidoItem(anyInt());

        assertDoesNotThrow(() -> service.deletarPedido(1));
        verify(repositoryPort).deletarPedido(1);
        verify(pedidoItemRepositoryPort).deletarPedidoItem(1);
    }

    @Test
    void deletarPedido_DeveLancarPedidoNotFoundException() {
        doThrow(new PedidoNotFoundException("Pedido não encontrado")).when(repositoryPort).deletarPedido(anyInt());

        assertThrows(PedidoNotFoundException.class, () -> service.deletarPedido(5));
    }

    @Test
    void deletarPedido_DeveLancarErroInternoException() {
        doThrow(new RuntimeException("erro inesperado")).when(repositoryPort).deletarPedido(anyInt());

        ErroInternoException ex = assertThrows(
                ErroInternoException.class,
                () -> service.deletarPedido(2)
        );
        assertTrue(ex.getMessage().contains("Erro interno ao tentar buscar pedido"));
    }

    @Test
    void buscarPedidoByCpfCliente_DeveLancarErro_SeItensPedidoVazio() {
        PedidoClienteDto clienteDto = new PedidoClienteDto();
        clienteDto.setIdCliente(1);
        when(pedidoClienteClient.buscarPorCpf(anyString())).thenReturn(clienteDto);
        when(repositoryPort.buscarPedidoPorIdCliente(anyInt()))
                .thenReturn(Collections.singletonList(new Pedido()));
        when(pedidoItemRepositoryPort.buscarItensPedido(anyInt()))
                .thenReturn(Collections.emptyList());

        ErroInternoException ex = assertThrows(
                ErroInternoException.class,
                () -> service.buscarPedidoByCpfCliente("cpf_qualquer")
        );
        assertTrue(ex.getMessage().contains("Itens do pedido não encontrados"));
    }

    @Test
    void listarTodos_DeveLancarErroInternoException_QuandoRepositoryLancaExceptionAoBuscarItens() {
        Pedido pedido = new Pedido(1, 1, PedidoStatus.ABERTO, null, new BigDecimal("20.0"), Collections.emptyList());
        when(repositoryPort.listarTodos()).thenReturn(Collections.singletonList(pedido));
        // Aqui, mocka a exception:
        when(pedidoItemRepositoryPort.buscarItensPedido(anyInt()))
                .thenThrow(new RuntimeException("Erro simulado no banco"));

        assertThrows(
                ErroInternoException.class,
                () -> service.listarTodos()
        );
    }


    @Test
    void buscarPedidoByCpfCliente_DeveLancarInvalidPedidoException_QuandoClienteNaoEncontrado() {
        // Arrange
        String cpf = "12345678900";
        // Mocka o pedidoClienteClient para lançar o NotFound!
        when(pedidoClienteClient.buscarPorCpf(anyString()))
                .thenThrow(mock(FeignException.NotFound.class));

        // Act & Assert
        ErroInternoException thrown = assertThrows(
                ErroInternoException.class,
                () -> service.buscarPedidoByCpfCliente(cpf)
        );
        assertEquals("Erro interno ao tentar buscar pedidos: Cliente não encontrado para o CPF informado.", thrown.getMessage());
    }

    @Test
    void cadastrarPedido_DeveLancarPedidoProdutoNotFoundException_QuandoProdutoNaoEncontrado() {
        PedidoRequest request = new PedidoRequest();
        request.setCpfCliente("12345678900");
        PedidoItemRequest itemRequest = new PedidoItemRequest();
        itemRequest.setSkuProduto("ABC");
        itemRequest.setQuantidadeItem(2);
        request.setItens(Collections.singletonList(itemRequest));

        PedidoClienteDto clienteDto = new PedidoClienteDto();
        clienteDto.setIdCliente(1);

        when(pedidoClienteClient.buscarPorCpf(anyString())).thenReturn(clienteDto);
        // Aqui simula que ao buscar o SKU o Feign client lança NotFound
        when(produtoClient.buscarPorSku(anyString()))
                .thenThrow(mock(FeignException.NotFound.class));

        assertThrows(
                PedidoProdutoNotFoundException.class,
                () -> service.cadastrarPedido(request)
        );
    }

    @Test
    void cadastrarPedido_DeveLancarErroInternoException_QuandoProdutoClientLancaOutraException() {
        PedidoRequest request = new PedidoRequest();
        request.setCpfCliente("12345678900");
        PedidoItemRequest itemRequest = new PedidoItemRequest();
        itemRequest.setSkuProduto("XYZ");
        itemRequest.setQuantidadeItem(2);
        request.setItens(Collections.singletonList(itemRequest));

        PedidoClienteDto clienteDto = new PedidoClienteDto();
        clienteDto.setIdCliente(1);

        when(pedidoClienteClient.buscarPorCpf(anyString())).thenReturn(clienteDto);
        when(produtoClient.buscarPorSku(anyString()))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ErroInternoException thrown = assertThrows(
                ErroInternoException.class,
                () -> service.cadastrarPedido(request)
        );
        assertTrue(thrown.getMessage().contains("Erro ao buscar produto"));
    }


}
