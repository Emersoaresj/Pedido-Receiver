package com.fiap.postech.pedido_service.api.mapper;

import com.fiap.postech.pedido_service.domain.PedidoItem;
import com.fiap.postech.pedido_service.gateway.database.entity.PedidoItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PedidoItemMapper {

    PedidoItemMapper INSTANCE = Mappers.getMapper(PedidoItemMapper.class);

    @Mapping(target = "idPedido", ignore = true)
    List<PedidoItem> entityToDomain(List<PedidoItemEntity> entity);

    @Mapping(target = "idPedido", ignore = true)
    PedidoItemEntity domainToEntity (PedidoItem pedidoItem);
}

