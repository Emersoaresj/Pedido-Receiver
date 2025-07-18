package com.fiap.postech.pedido_service.utils;

import lombok.Data;

@Data
public class ConstantUtils {




    private ConstantUtils() {
        throw new IllegalStateException("Classe Utilitária");
    }


    //ERROS
    public static final String CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado para o CPF informado.";
    public static final String PRODUTO_NAO_ENCONTRADO = "Produto não encontrado para o SKU informado.";

    public static final String PEDIDO_NAO_PODE_SER_NULO = "O pedido não pode ser nulo!";
    public static final String PEDIDO_NAO_ENCONTRADO = "Pedido não encontrado para o ID informado.";
    public static final String ITENS_PEDIDO_INVALIDOS = "Itens do pedido inválidos! Verifique se todos os itens possuem SKU e quantidade válidos.";
    public static final String ITENS_PEDIDO_NAO_ENCONTRADOS = "Itens do pedido não encontrados. Verifique se os SKUs/IDs dos produtos estão corretos.";
    public static final String VALOR_TOTAL_PEDIDO_INVALIDO = "Valor total do pedido inválido! O valor total deve ser maior que zero.";

    //SUCESSO
    public static final String PEDIDO_CADASTRADO = "Pedido cadastrado com sucesso!";
}
