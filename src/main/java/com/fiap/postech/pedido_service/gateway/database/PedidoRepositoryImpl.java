package com.fiap.postech.pedido_service.gateway.database;

import com.fiap.postech.pedido_service.api.dto.PedidoDto;
import com.fiap.postech.pedido_service.domain.Pedido;
import com.fiap.postech.pedido_service.domain.PedidoItem;
import com.fiap.postech.pedido_service.api.dto.ResponseDto;
import com.fiap.postech.pedido_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.pedido_service.domain.exceptions.PedidoNotFoundException;
import com.fiap.postech.pedido_service.gateway.database.entity.PedidoEntity;
import com.fiap.postech.pedido_service.gateway.database.entity.PedidoItemEntity;
import com.fiap.postech.pedido_service.gateway.database.repository.PedidoItemRepositoryJPA;
import com.fiap.postech.pedido_service.gateway.database.repository.PedidoRepositoryJPA;
import com.fiap.postech.pedido_service.gateway.port.PedidoRepositoryPort;
import com.fiap.postech.pedido_service.api.mapper.PedidoMapper;
import com.fiap.postech.pedido_service.utils.ConstantUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class PedidoRepositoryImpl implements PedidoRepositoryPort {


    @Autowired
    private PedidoRepositoryJPA pedidoRepositoryJPA;

    @Autowired
    private PedidoItemRepositoryJPA pedidoItemRepositoryJPA;

    @Transactional
    @Override
    public ResponseDto cadastrarPedido(Pedido pedido) {
        try {
            PedidoEntity pedidoEntity = PedidoMapper.INSTANCE.domainToEntityCreate(pedido);
            PedidoEntity savedPedido = pedidoRepositoryJPA.save(pedidoEntity);

            for (PedidoItem item : pedido.getItens()) {
                PedidoItemEntity itemEntity = PedidoMapper.INSTANCE.domainToItemEntity(item);
                itemEntity.setIdPedido(savedPedido.getIdPedido());
                pedidoItemRepositoryJPA.save(itemEntity);
            }

            return montaResponse(savedPedido, "cadastrar");
        } catch (Exception e) {
            log.error("Erro ao cadastrar pedido", e);
            throw new ErroInternoException("Erro ao cadastrar pedido: " + e.getMessage());
        }
    }

    @Override
    public Pedido buscarPedidoPorId(Integer idPedido) {
        PedidoEntity pedidoEntity = pedidoRepositoryJPA.findById(idPedido)
                .orElseThrow(() -> new ErroInternoException("Pedido não encontrado com ID: " + idPedido));
        return PedidoMapper.INSTANCE.entityToDomain(pedidoEntity);
    }

    @Transactional
    @Override
    public ResponseDto atualizarPedido(Pedido pedido) {
        try {
            PedidoEntity entity = PedidoMapper.INSTANCE.domainToEntityUpdate(pedido);
            PedidoEntity updated = pedidoRepositoryJPA.save(entity);
            return montaResponse(updated, "atualizar");

        } catch (Exception e) {
            log.error("Erro ao atualizar pedido", e);
            throw new ErroInternoException("Erro ao atualizar pedido: " + e.getMessage());
        }
    }

    @Override
    public List<Pedido> buscarPedidoPorIdCliente(Integer idCliente) {
        List<PedidoEntity> pedidoEntity = Collections.singletonList(pedidoRepositoryJPA.findByIdCliente(idCliente)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido não encontrado para o cliente do ID: " + idCliente)));
        return PedidoMapper.INSTANCE.entitysToDomain(pedidoEntity);
    }

    @Override
    public List<Pedido> listarTodos() {
        try {
            List<PedidoEntity> entities = pedidoRepositoryJPA.findAll();
            return entities.stream()
                    .map(PedidoMapper.INSTANCE::entityToDomain)
                    .toList();
        } catch (Exception e) {
            log.error("Erro ao buscar pedidos", e);
            throw new ErroInternoException("Erro ao buscar pedidos no banco de dados: " + e.getMessage());
        }

    }

    @Transactional
    @Override
    public void deletarPedido(Integer idPedido) {
        pedidoRepositoryJPA.findById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException(ConstantUtils.PEDIDO_NAO_ENCONTRADO));
        pedidoRepositoryJPA.deleteById(idPedido);
    }


    private ResponseDto montaResponse(PedidoEntity pedidoEntity, String tipoAcao) {
        ResponseDto response = new ResponseDto();

        if ("cadastrar".equals(tipoAcao)) {
            response.setMessage(ConstantUtils.PEDIDO_CADASTRADO);
        } else {
            response.setMessage(ConstantUtils.PEDIDO_CADASTRADO);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("idPedido", pedidoEntity.getIdPedido());
        data.put("idCliente", pedidoEntity.getIdCliente());
        data.put("statusPedido", pedidoEntity.getStatusPedido());
        data.put("dataPedido", pedidoEntity.getDataPedido());
        data.put("valorTotalPedido", pedidoEntity.getValorTotalPedido());

        response.setData(data);
        return response;
    }

}
