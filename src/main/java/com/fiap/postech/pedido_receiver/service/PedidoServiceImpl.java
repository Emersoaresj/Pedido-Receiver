package com.fiap.postech.pedido_receiver.service;

import com.fiap.postech.pedido_receiver.api.dto.*;
import com.fiap.postech.pedido_receiver.domain.Pedido;
import com.fiap.postech.pedido_receiver.domain.PedidoItem;
import com.fiap.postech.pedido_receiver.api.dto.client.PedidoClienteDto;
import com.fiap.postech.pedido_receiver.api.dto.client.PedidoProdutoDto;
import com.fiap.postech.pedido_receiver.api.dto.kafka.PedidoItemKafkaDTO;
import com.fiap.postech.pedido_receiver.api.dto.kafka.PedidoKafkaDTO;
import com.fiap.postech.pedido_receiver.domain.exceptions.ErroInternoException;
import com.fiap.postech.pedido_receiver.domain.exceptions.InvalidPedidoException;
import com.fiap.postech.pedido_receiver.domain.exceptions.PedidoNotFoundException;
import com.fiap.postech.pedido_receiver.domain.exceptions.client.PedidoProdutoNotFoundException;
import com.fiap.postech.pedido_receiver.gateway.client.PedidoClienteClient;
import com.fiap.postech.pedido_receiver.gateway.client.PedidoProdutoClient;
import com.fiap.postech.pedido_receiver.gateway.kafka.PedidoProducerPort;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoItemRepositoryPort;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoRepositoryPort;
import com.fiap.postech.pedido_receiver.gateway.port.PedidoServicePort;
import com.fiap.postech.pedido_receiver.utils.ConstantUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PedidoServiceImpl implements PedidoServicePort {

    @Autowired
    private PedidoClienteClient pedidoClienteClient;

    @Autowired
    private PedidoProdutoClient produtoClient;

    @Autowired
    private PedidoRepositoryPort repositoryPort;

    @Autowired
    private PedidoProducerPort pedidoProducerPort;

    @Autowired
    private PedidoItemRepositoryPort pedidoItemRepositoryPort;


    @Override
    public ResponseDto cadastrarPedido(PedidoRequest request) {

        PedidoClienteDto clienteDto = chamadaClienteClient(request.getCpfCliente());
        Integer idCliente = clienteDto.getIdCliente();

        List<PedidoItem> itens = montarItensPedido(request.getItens());

        Pedido pedido = montarPedido(idCliente, itens);

        validarPedido(pedido);

        ResponseDto response = repositoryPort.cadastrarPedido(pedido);
        Integer idPedido = buscaIdPedido(response);
        pedido.setIdPedido(idPedido);

        // Envio do pedido para o Kafka
        PedidoKafkaDTO dto = mapPedidoParaKafkaDTO(pedido);
        pedidoProducerPort.enviarMensagem(dto);

        return response;
    }


    @Override
    public ResponseDto atualizaStatusPedido(Integer id, PedidoStatus novoStatus) {
        try {
            Pedido pedidoExistente = repositoryPort.buscarPedidoPorId(id);
            pedidoExistente.setStatusPedido(novoStatus);

            return repositoryPort.atualizarPedido(pedidoExistente);
        } catch (Exception e) {
            log.error("Erro ao atualizar status do pedido: {}", e.getMessage());
            throw new ErroInternoException("Erro ao atualizar status do pedido: " + e.getMessage());
        }

    }

    @Override
    public List<PedidoByClienteDto> buscarPedidoByCpfCliente(String cpfCliente) {
        try {
            // 1. Busca as informações do cliente (via Feign)
            PedidoClienteDto clienteDto = chamadaClienteClient(cpfCliente);

            // 2. Busca todos os pedidos do cliente (atenção: retorna uma lista!)
            List<Pedido> pedidos = repositoryPort.buscarPedidoPorIdCliente(clienteDto.getIdCliente());

            // 3. Para cada pedido, busca os itens, monta o PedidoDto e adiciona na lista de retorno
            List<PedidoByClienteDto> resultado = new ArrayList<>();
            for (Pedido pedido : pedidos) {
                List<PedidoItem> itens = buscarItensPedidoByIdPedido(pedido.getIdPedido());
                pedido.setItens(itens);

                PedidoByClienteDto dto = new PedidoByClienteDto(
                        pedido.getIdPedido(),
                        clienteDto.getNomeCliente(),
                        pedido.getStatusPedido(),
                        pedido.getDataPedido(),
                        pedido.getValorTotalPedido(),
                        itens
                );
                resultado.add(dto);
            }

            return resultado;

        } catch (PedidoNotFoundException | FeignException.NotFound e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar pedidos por CPF do cliente", e);
            throw new ErroInternoException("Erro interno ao tentar buscar pedidos: " + e.getMessage());
        }
    }


    @Override
    public List<PedidoDto> listarTodos() {
        try {
            // 1. Busca todos os pedidos
            List<Pedido> pedidos = repositoryPort.listarTodos();

            // 2. Para cada pedido, busca os itens, monta o PedidoDto
            List<PedidoDto> resultado = new ArrayList<>();
            for (Pedido pedido : pedidos) {
                // Busca os itens do pedido
                List<PedidoItem> itens = buscarItensPedidoByIdPedido(pedido.getIdPedido());
                pedido.setItens(itens);

                PedidoDto dto = new PedidoDto(
                        pedido.getIdPedido(),
                        pedido.getIdCliente(),
                        pedido.getStatusPedido(),
                        pedido.getDataPedido(),
                        pedido.getValorTotalPedido(),
                        itens
                );
                resultado.add(dto);
            }

            return resultado;

        } catch (ErroInternoException e) {
            log.error("Erro inesperado ao buscar pedidos", e);
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao listar pedidos", e);
            throw new ErroInternoException("Erro interno ao tentar listar pedidos: " + e.getMessage());
        }
    }


    @Override
    public void deletarPedido(Integer idPedido) {
        try {
            repositoryPort.deletarPedido(idPedido);
            pedidoItemRepositoryPort.deletarPedidoItem(idPedido);
        } catch (PedidoNotFoundException e) {
            log.error("Pedido não encontrado para o ID: {}", idPedido, e);
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar pedido por ID: {}", idPedido, e);
            throw new ErroInternoException("Erro interno ao tentar buscar pedido: " + e.getMessage());
        }
    }

    private List<PedidoItem> buscarItensPedidoByIdPedido(Integer id) {
        try {
            List<PedidoItem> itens = pedidoItemRepositoryPort.buscarItensPedido(id);
            if (itens == null || itens.isEmpty()) {
                log.warn("Nenhum item encontrado para o pedido: {}", id);
                throw new InvalidPedidoException(ConstantUtils.ITENS_PEDIDO_NAO_ENCONTRADOS);
            }
            return itens;
        } catch (InvalidPedidoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar itens do pedido: {}", e.getMessage());
            throw new ErroInternoException("Erro ao buscar itens do pedido: " + e.getMessage());
        }
    }

    private PedidoClienteDto chamadaClienteClient(String cpfCliente) {
        // buscar infos do cliente via Feign
        try {
            return pedidoClienteClient.buscarPorCpf(cpfCliente);
        } catch (feign.FeignException.NotFound e) {
            log.warn("Cliente não encontrado para o CPF: {}", cpfCliente);
            throw new InvalidPedidoException(ConstantUtils.CLIENTE_NAO_ENCONTRADO);
        } catch (Exception e) {
            log.error("Erro ao chamar o serviço de cliente: {}", e.getMessage());
            throw new ErroInternoException("Erro ao buscar cliente: " + e.getMessage());
        }
    }

    private List<PedidoItem> montarItensPedido(List<PedidoItemRequest> itensRequest) {
        List<PedidoItem> itens = new ArrayList<>();
        for (PedidoItemRequest itemReq : itensRequest) {
            PedidoProdutoDto produto = chamadaProdutoClient(itemReq.getSkuProduto());
            PedidoItem item = new PedidoItem(
                    null,
                    null,
                    produto.getIdProduto(),
                    itemReq.getQuantidadeItem(),
                    produto.getPrecoProduto()
            );
            itens.add(item);
        }
        return itens;
    }

    private Pedido montarPedido(Integer idCliente, List<PedidoItem> itens) {
        BigDecimal valorTotal = calcularValorTotal(itens);

        return new Pedido(
                null,
                idCliente,
                PedidoStatus.ABERTO,
                LocalDateTime.now(),
                valorTotal,
                itens
        );
    }

    private BigDecimal calcularValorTotal(List<PedidoItem> itens) {
        return itens.stream()
                .map(i -> i.getPrecoUnitarioItem().multiply(BigDecimal.valueOf(i.getQuantidadeItem())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @SuppressWarnings("unchecked")
    private Integer buscaIdPedido(ResponseDto response) {
        Map<String, Object> data = (Map<String, Object>) response.getData();
        Integer idPedido = (Integer) data.get("idPedido");
        return idPedido;
    }

    private PedidoProdutoDto chamadaProdutoClient(String skuProduto) {
        try {
            return produtoClient.buscarPorSku(skuProduto);
        } catch (feign.FeignException.NotFound e) {
            log.warn("Produto não encontrado para o SKU: {}", skuProduto);
            throw new PedidoProdutoNotFoundException(ConstantUtils.PRODUTO_NAO_ENCONTRADO);
        } catch (Exception e) {
            log.error("Erro ao chamar o serviço de produto: {}", e.getMessage());
            throw new ErroInternoException("Erro ao buscar produto: " + e.getMessage());
        }
    }


    private void validarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new InvalidPedidoException(ConstantUtils.PEDIDO_NAO_PODE_SER_NULO);
        }
        if (!pedido.clienteValido()) {
            throw new InvalidPedidoException(ConstantUtils.CLIENTE_NAO_ENCONTRADO);
        }
        if (!pedido.itensValidos()) {
            throw new InvalidPedidoException(ConstantUtils.ITENS_PEDIDO_INVALIDOS);
        }
        if (!pedido.valorTotalValido()) {
            throw new InvalidPedidoException(ConstantUtils.VALOR_TOTAL_PEDIDO_INVALIDO);
        }
    }

    private PedidoKafkaDTO mapPedidoParaKafkaDTO(Pedido pedido) {
        List<PedidoItemKafkaDTO> itensKafka = new ArrayList<>();
        for (PedidoItem item : pedido.getItens()) {
            PedidoItemKafkaDTO itemKafka = new PedidoItemKafkaDTO(
                    item.getIdProduto(),
                    item.getQuantidadeItem(),
                    item.getPrecoUnitarioItem()
            );
            itensKafka.add(itemKafka);
        }
        return new PedidoKafkaDTO(
                pedido.getIdPedido(),
                pedido.getIdCliente(),
                pedido.getValorTotalPedido(),
                itensKafka
        );
    }


}