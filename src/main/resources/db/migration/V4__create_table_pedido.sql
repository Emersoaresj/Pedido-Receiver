-- Tabela pedido
CREATE TABLE pedido (
    id_pedido SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    status_pedido VARCHAR(50) NOT NULL DEFAULT 'ABERTO',
    data_pedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_total_pedido DECIMAL(12,2) NOT NULL
);

-- Tabela pedido_item
CREATE TABLE pedido_item (
    id_pedido_item SERIAL PRIMARY KEY,
    id_pedido INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    quantidade_item INTEGER NOT NULL,
    preco_unitario_item DECIMAL(10,2) NOT NULL
);