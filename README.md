# Pedido Receiver

Serviço Java baseado em Spring Boot para recebimento e processamento de pedidos, utilizando PostgreSQL, Kafka, FeignClient para comunicação entre APIs, Flyway para migrações e arquitetura hexagonal (Ports & Adapters).

---

## 🏗️ Arquitetura

O projeto segue o padrão de arquitetura hexagonal, separando as regras de negócio (domínio) das implementações externas (gateways/adapters):

- **API**: Endpoints REST para operações relacionadas a pedidos.
- **Domain**: Modelos, exceções e portas (interfaces) do domínio.
- **Gateway**: Implementações de acesso a dados (JPA), clientes externos (Cliente e Produto) e Kafka.
- **Service**: Lógica de negócio central.
- **Utils**: Utilitários e constantes.

---

## 📁 Estrutura de Pastas
```
src/main/java/com/fiap/postech/pedido_receiver/
├── api/
│   ├── controller/
│   │   └── PedidoController.java
│   ├── dto/
│   │   ├── AtualizarPedidoRequest.java
│   │   ├── PedidoByClienteDto.java
│   │   ├── PedidoDto.java
│   │   ├── PedidoItemRequest.java
│   │   ├── PedidoRequest.java
│   │   ├── PedidoStatus.java
│   │   ├── ResponseDto.java
│   │   ├── client/
│   │   │   ├── PedidoClienteDto.java
│   │   │   └── PedidoProdutoDto.java
│   │   └── kafka/
│   │       ├── PedidoItemKafkaDTO.java
│   │       └── PedidoKafkaDTO.java
│   └── mapper/
│       ├── PedidoItemMapper.java
│       └── PedidoMapper.java
├── domain/
│   ├── Pedido.java
│   ├── PedidoItem.java
│   └── exceptions/
│       ├── ErroInternoException.java
│       ├── GlobalHandlerException.java
│       ├── InvalidPedidoException.java
│       ├── PedidoNotFoundException.java
│       ├── client/
│       │   └── PedidoProdutoNotFoundException.java
│       └── kafka/
│           └── EnvioKafkaException.java
├── gateway/
│   ├── client/
│   │   ├── PedidoClienteClient.java
│   │   └── PedidoProdutoClient.java
│   ├── database/
│   │   ├── PedidoItemRepositoryImpl.java
│   │   ├── PedidoRepositoryImpl.java
│   │   ├── entity/
│   │   │   ├── PedidoEntity.java
│   │   │   └── PedidoItemEntity.java
│   │   └── repository/
│   │       ├── PedidoItemRepositoryJPA.java
│   │       └── PedidoRepositoryJPA.java
│   └── kafka/
│       ├── PedidoProducerImpl.java
│       ├── PedidoProducerPort.java
│       └── config/
│           └── KafkaConfig.java
├── port/
│   ├── PedidoItemRepositoryPort.java
│   ├── PedidoRepositoryPort.java
│   └── PedidoServicePort.java
├── service/
│   └── PedidoServiceImpl.java
├── utils/
│   └── ConstantUtils.java
└── PedidoReceiverApplication.java

```

---

## 🧩 Principais Classes

- **PedidoController**: Endpoints REST para operações de gerenciamento de pedidos.
- **PedidoServiceImpl**: Implementação da lógica de negócio para o domínio de pedidos.
- **PedidoRepositoryPort**: Interface para a implementação de persistência dos pedidos.
- **PedidoRepositoryImpl**: Implementação do repositório usando JPA para acesso ao banco de dados.
- **PedidoEntity**: Entidade JPA para persistência dos dados de pedidos.
- **Pedido**: Modelo de domínio que representa o pedido.
- **PedidoClienteClient**: Cliente para comunicação com o serviço de clientes.
- **PedidoProdutoClient**: Cliente para comunicação com o serviço de produtos.
- **PedidoProducerImpl**: Implementação para envio de mensagens para o Kafka.
- **DTOs**: Objetos para transferência de dados entre as camadas da aplicação.
- **Exceções**: Tratamento centralizado de erros e validações da aplicação.

---

## ⚙️ Configuração

O arquivo `src/main/resources/application.yml` define:

- Conexão com PostgreSQL (ajustável por variáveis de ambiente).
- Configuração do Kafka (bootstrap servers, producer, consumer).
- Flyway para migrações automáticas do banco de dados.
- JPA configurado para atualização automática do schema e exibição de SQL.
- Configuração dos serviços de cliente e produto, com as URLs dos serviços (ajustável por variável de ambiente).

---

## ▶️ Executando o Projeto

1. Configure o banco PostgreSQL e ajuste as variáveis de ambiente se necessário.
2. Configure o Kafka e ajuste as variáveis de ambiente se necessário.
3. Execute as migrações Flyway automaticamente ao iniciar a aplicação.
4. Rode o projeto com: `mvn spring-boot:run`
5. A aplicação estará disponível em `http://localhost:8080`.

---

## 📚 Documentação

A documentação dos endpoints pode ser acessada via Swagger em `/swagger-ui.html` (caso habilitado).
