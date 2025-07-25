-- Tabela cliente
CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    nome_cliente VARCHAR(150) NOT NULL,
    cpf_cliente VARCHAR(14) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    numero_endereco VARCHAR(20) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    complemento_endereco VARCHAR(255),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2)
);