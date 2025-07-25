package com.fiap.postech.pedido_receiver.gateway.database.repository;

import com.fiap.postech.pedido_receiver.gateway.database.entity.PedidoItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoItemRepositoryJPA extends JpaRepository<PedidoItemEntity, Integer> {

    List<PedidoItemEntity> findAllByIdPedido(Integer idPedido);

    Optional<PedidoItemEntity> findByIdPedido(Integer idPedido);
}

