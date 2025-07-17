-- Tipo ENUM
CREATE TYPE pedido_status AS ENUM (
    'ABERTO',
    'FECHADO_COM_SUCESSO',
    'FECHADO_SEM_ESTOQUE',
    'FECHADO_SEM_CREDITO'
);

-- Tabela pedido
CREATE TABLE pedido (
    id_pedido SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    status_pedido pedido_status NOT NULL DEFAULT 'ABERTO',
    data_pedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_total_pedido DECIMAL(12,2) NOT NULL
);
