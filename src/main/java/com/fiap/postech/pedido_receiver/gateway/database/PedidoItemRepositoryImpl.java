package com.fiap.postech.pedido_receiver.gateway.database;

import com.fiap.postech.pedido_receiver.domain.PedidoItem;
import com.fiap.postech.pedido_receiver.domain.exceptions.ErroInternoException;
import com.fiap.postech.pedido_receiver.domain.exceptions.PedidoNotFoundException;
import com.fiap.postech.pedido_receiver.gateway.database.entity.PedidoItemEntity;
import com.fiap.postech.pedido_receiver.gateway.database.repository.PedidoItemRepositoryJPA;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoItemRepositoryPort;
import com.fiap.postech.pedido_receiver.api.mapper.PedidoItemMapper;
import com.fiap.postech.pedido_receiver.utils.ConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class PedidoItemRepositoryImpl implements PedidoItemRepositoryPort {

    @Autowired
    private PedidoItemRepositoryJPA pedidoItemRepositoryJPA;


    @Override
    public List<PedidoItem> buscarItensPedido(Integer idPedido) {
        try {
            List<PedidoItemEntity> entity = pedidoItemRepositoryJPA.findAllByIdPedido(idPedido);
            return PedidoItemMapper.INSTANCE.entityToDomain(entity);
        } catch (Exception e) {
            throw new ErroInternoException("Erro ao buscar itens do pedido: " + e.getMessage());
        }

    }

    @Override
    public boolean atualizarPedidoItem(PedidoItem item) {
        try {
            PedidoItemEntity entity = PedidoItemMapper.INSTANCE.domainToEntity(item);
            entity.setIdPedido(item.getIdPedido());
            pedidoItemRepositoryJPA.save(entity);
            return true;
        } catch (Exception e) {
            log.error("Erro ao atualizar item do pedido", e);
            return false;
        }
    }

    @Override
    public void deletarPedidoItem(Integer idPedido) {
        PedidoItemEntity itemPedido = pedidoItemRepositoryJPA.findByIdPedido(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(ConstantUtils.PEDIDO_ITEM_NAO_ENCONTRADO));
        pedidoItemRepositoryJPA.deleteById(itemPedido.getIdPedidoItem());
    }
}
