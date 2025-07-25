package com.fiap.postech.pedido_service.api.mapper;

import com.fiap.postech.pedido_service.domain.Pedido;
import com.fiap.postech.pedido_service.domain.PedidoItem;
import com.fiap.postech.pedido_service.gateway.database.entity.PedidoEntity;
import com.fiap.postech.pedido_service.gateway.database.entity.PedidoItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(target = "idPedido", ignore = true)
    PedidoEntity domainToEntityCreate(Pedido pedido);

    PedidoEntity domainToEntityUpdate(Pedido pedido);

    @Mapping(target = "idPedidoItem", ignore = true)
    @Mapping(target = "idPedido", ignore = true)
    PedidoItemEntity domainToItemEntity(PedidoItem item);

    @Mapping(target = "itens", ignore = true)
    Pedido entityToDomain (PedidoEntity pedidoEntity);

    @Mapping(target = "itens", ignore = true)
    List<Pedido> entitysToDomain (List<PedidoEntity> pedidoEntity);
}

