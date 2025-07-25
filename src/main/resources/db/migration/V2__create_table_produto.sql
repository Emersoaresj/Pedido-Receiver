-- Tabela produto
CREATE TABLE produto (
    id_produto SERIAL PRIMARY KEY,
    nome_produto VARCHAR(150) NOT NULL,
    sku_produto VARCHAR(50) NOT NULL UNIQUE,
    preco_produto DECIMAL(10,2) NOT NULL
);