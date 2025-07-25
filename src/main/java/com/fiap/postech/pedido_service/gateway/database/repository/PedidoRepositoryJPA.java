package com.fiap.postech.pedido_service.gateway.database.repository;

import com.fiap.postech.pedido_service.gateway.database.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepositoryJPA extends JpaRepository<PedidoEntity, Integer> {

    Optional<PedidoEntity> findByIdCliente(Integer idCliente);
}
