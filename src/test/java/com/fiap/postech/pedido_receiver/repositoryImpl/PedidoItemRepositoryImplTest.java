package com.fiap.postech.pedido_receiver.repositoryImpl;

import com.fiap.postech.pedido_receiver.domain.PedidoItem;
import com.fiap.postech.pedido_receiver.domain.exceptions.ErroInternoException;
import com.fiap.postech.pedido_receiver.domain.exceptions.PedidoNotFoundException;
import com.fiap.postech.pedido_receiver.gateway.database.PedidoItemRepositoryImpl;
import com.fiap.postech.pedido_receiver.gateway.database.entity.PedidoItemEntity;
import com.fiap.postech.pedido_receiver.gateway.database.repository.PedidoItemRepositoryJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoItemRepositoryImplTest {

    @Mock
    private PedidoItemRepositoryJPA pedidoItemRepositoryJPA;

    @InjectMocks
    private PedidoItemRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarItensPedido_DeveRetornarItens() {
        // Arrange
        Integer idPedido = 1;
        PedidoItemEntity entity = new PedidoItemEntity();
        entity.setIdPedido(idPedido);
        // Adicione mais campos se precisar para o mapper funcionar

        when(pedidoItemRepositoryJPA.findAllByIdPedido(idPedido)).thenReturn(List.of(entity));

        // Act
        List<PedidoItem> itens = repository.buscarItensPedido(idPedido);

        // Assert
        assertNotNull(itens);
        assertFalse(itens.isEmpty());
        verify(pedidoItemRepositoryJPA).findAllByIdPedido(idPedido);
    }

    @Test
    void buscarItensPedido_DeveLancarErroInternoException() {
        Integer idPedido = 2;
        when(pedidoItemRepositoryJPA.findAllByIdPedido(idPedido)).thenThrow(new RuntimeException("Falha DB"));

        ErroInternoException ex = assertThrows(ErroInternoException.class,
                () -> repository.buscarItensPedido(idPedido));

        assertTrue(ex.getMessage().contains("Erro ao buscar itens do pedido"));
    }

    @Test
    void atualizarPedidoItem_DeveSalvarERetornarTrue() {
        PedidoItem item = new PedidoItem();
        item.setIdPedido(1);
        // Preencha outros campos necessários para o mapper

        PedidoItemEntity entity = new PedidoItemEntity();
        entity.setIdPedido(1);
        // Mapper vai converter o item para entity “de verdade”, não precisa mockar

        when(pedidoItemRepositoryJPA.save(any(PedidoItemEntity.class))).thenReturn(entity);

        boolean result = repository.atualizarPedidoItem(item);

        assertTrue(result);
        verify(pedidoItemRepositoryJPA).save(any(PedidoItemEntity.class));
    }

    @Test
    void atualizarPedidoItem_DeveRetornarFalseSeDerErro() {
        PedidoItem item = new PedidoItem();
        item.setIdPedido(1);
        // Preencha outros campos necessários para o mapper

        when(pedidoItemRepositoryJPA.save(any(PedidoItemEntity.class))).thenThrow(new RuntimeException("Erro"));

        boolean result = repository.atualizarPedidoItem(item);

        assertFalse(result);
    }

    @Test
    void deletarPedidoItem_DeveDeletarCorretamente() {
        Integer idPedido = 10;
        PedidoItemEntity entity = new PedidoItemEntity();
        entity.setIdPedido(idPedido);
        entity.setIdPedidoItem(123);

        when(pedidoItemRepositoryJPA.findByIdPedido(idPedido)).thenReturn(Optional.of(entity));

        repository.deletarPedidoItem(idPedido);

        verify(pedidoItemRepositoryJPA).deleteById(entity.getIdPedidoItem());
    }

    @Test
    void deletarPedidoItem_DeveLancarPedidoNotFoundException() {
        Integer idPedido = 20;
        when(pedidoItemRepositoryJPA.findByIdPedido(idPedido)).thenReturn(Optional.empty());

        assertThrows(PedidoNotFoundException.class, () -> repository.deletarPedidoItem(idPedido));
    }




}
