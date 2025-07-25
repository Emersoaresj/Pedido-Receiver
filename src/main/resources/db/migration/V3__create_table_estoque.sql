-- Tabela estoque
CREATE TABLE estoque (
    id_estoque SERIAL PRIMARY KEY,
    id_produto INTEGER NOT NULL UNIQUE,
    quantidade_estoque INTEGER NOT NULL,
    sku_produto VARCHAR(50) NOT NULL UNIQUE
);