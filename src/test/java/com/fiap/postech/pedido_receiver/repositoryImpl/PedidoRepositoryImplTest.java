package com.fiap.postech.pedido_receiver.repositoryImpl;

import com.fiap.postech.pedido_receiver.api.dto.PedidoStatus;
import com.fiap.postech.pedido_receiver.api.dto.ResponseDto;
import com.fiap.postech.pedido_receiver.api.mapper.PedidoMapper;
import com.fiap.postech.pedido_receiver.domain.Pedido;
import com.fiap.postech.pedido_receiver.domain.PedidoItem;
import com.fiap.postech.pedido_receiver.domain.exceptions.ErroInternoException;
import com.fiap.postech.pedido_receiver.domain.exceptions.PedidoNotFoundException;
import com.fiap.postech.pedido_receiver.gateway.database.PedidoRepositoryImpl;
import com.fiap.postech.pedido_receiver.gateway.database.entity.PedidoEntity;
import com.fiap.postech.pedido_receiver.gateway.database.entity.PedidoItemEntity;
import com.fiap.postech.pedido_receiver.gateway.database.repository.PedidoItemRepositoryJPA;
import com.fiap.postech.pedido_receiver.gateway.database.repository.PedidoRepositoryJPA;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoRepositoryImplTest {

    @Mock
    PedidoRepositoryJPA pedidoRepositoryJPA;

    @Mock
    PedidoRepositoryPort pedidoRepository;

    @Mock
    PedidoItemRepositoryJPA pedidoItemRepositoryJPA;

    @InjectMocks
    PedidoRepositoryImpl repository;


    @BeforeEach
    void setUp() {
        pedidoRepositoryJPA = mock(PedidoRepositoryJPA.class);
        pedidoItemRepositoryJPA = mock(PedidoItemRepositoryJPA.class);

        repository = new PedidoRepositoryImpl();
        ReflectionTestUtils.setField(repository, "pedidoRepositoryJPA", pedidoRepositoryJPA);
        ReflectionTestUtils.setField(repository, "pedidoItemRepositoryJPA", pedidoItemRepositoryJPA);
    }

    @Test
    void cadastrarPedido_DeveSalvarItensCorretamente() {
        // Arrange
        PedidoItem item = new PedidoItem(1, 1, 1, 2, new BigDecimal("10.0"));
        List<PedidoItem> itens = List.of(item);
        Pedido pedido = new Pedido(1, 1, PedidoStatus.ABERTO, LocalDateTime.now(), new BigDecimal("20.0"), itens);

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setIdPedido(10);
        PedidoEntity savedPedido = new PedidoEntity();
        savedPedido.setIdPedido(10);

        PedidoItemEntity itemEntity = new PedidoItemEntity();
        itemEntity.setIdPedido(10);

        // Aqui você NÃO faz mock do Mapper!
        when(pedidoRepositoryJPA.save(any())).thenReturn(savedPedido);
        when(pedidoItemRepositoryJPA.save(any())).thenReturn(itemEntity);

        // Act
        ResponseDto resp = repository.cadastrarPedido(pedido);

        // Assert
        assertNotNull(resp);
        verify(pedidoRepositoryJPA).save(any(PedidoEntity.class));
        verify(pedidoItemRepositoryJPA, atLeastOnce()).save(any(PedidoItemEntity.class));
    }



    @Test
    void cadastrarPedido_DeveLancarErroInternoException_EmCasoDeErro() {
        Pedido pedido = new Pedido(null, 1, null, LocalDateTime.now(), BigDecimal.TEN, Collections.emptyList());
        when(pedidoRepositoryJPA.save(any())).thenThrow(new RuntimeException("erro banco"));

        assertThrows(ErroInternoException.class, () -> repository.cadastrarPedido(pedido));
    }

    @Test
    void buscarPedidoPorIdCliente_DeveRetornarListaDePedidos() {
        PedidoEntity entity = new PedidoEntity();
        entity.setIdPedido(1);
        entity.setIdCliente(2);

        List<PedidoEntity> pedidoEntities = Arrays.asList(entity);

        when(pedidoRepositoryJPA.findByIdCliente(anyInt())).thenReturn(Optional.of(pedidoEntities));

        List<Pedido> pedidos = repository.buscarPedidoPorIdCliente(2);

        assertNotNull(pedidos);
        assertFalse(pedidos.isEmpty());
        assertEquals(1, pedidos.get(0).getIdPedido());
    }

    @Test
    void buscarPedidoPorIdCliente_DeveLancarPedidoNotFoundException_QuandoNaoEncontrar() {
        when(pedidoRepositoryJPA.findByIdCliente(anyInt())).thenReturn(Optional.empty());

        assertThrows(PedidoNotFoundException.class, () -> repository.buscarPedidoPorIdCliente(123));
    }

    @Test
    void listarTodos_DeveRetornarPedidos() {
        PedidoEntity entity = new PedidoEntity();
        entity.setIdPedido(11);
        entity.setIdCliente(1);
        List<PedidoEntity> entities = Collections.singletonList(entity);

        when(pedidoRepositoryJPA.findAll()).thenReturn(entities);

        List<Pedido> pedidos = repository.listarTodos();

        assertNotNull(pedidos);
        assertFalse(pedidos.isEmpty());
        assertEquals(11, pedidos.get(0).getIdPedido());
    }

    @Test
    void listarTodos_DeveLancarErroInternoException_EmCasoDeErro() {
        when(pedidoRepositoryJPA.findAll()).thenThrow(new RuntimeException("Falha ao buscar"));

        assertThrows(ErroInternoException.class, () -> repository.listarTodos());
    }

    @Test
    void deletarPedido_DeveDeletarComSucesso() {
        PedidoEntity entity = new PedidoEntity();
        entity.setIdPedido(100);

        when(pedidoRepositoryJPA.findById(anyInt())).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> repository.deletarPedido(100));
        verify(pedidoRepositoryJPA).deleteById(100);
    }

    @Test
    void deletarPedido_DeveLancarPedidoNotFoundException() {
        when(pedidoRepositoryJPA.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(PedidoNotFoundException.class, () -> repository.deletarPedido(99));
    }


}
