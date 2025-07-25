package com.fiap.postech.pedido_receiver.gateway.database.repository;

import com.fiap.postech.pedido_receiver.gateway.database.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepositoryJPA extends JpaRepository<PedidoEntity, Integer> {

    Optional<List<PedidoEntity>> findByIdCliente(Integer idCliente);
}
