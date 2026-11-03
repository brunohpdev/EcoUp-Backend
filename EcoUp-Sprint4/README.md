# EcoUp - Sprint 4

## Descrição

O EcoUp é uma aplicação desenvolvida em Java com Spring Boot.

O objetivo do projeto é incentivar práticas sustentáveis por meio
de missões, pontuações, níveis e recomendações.

A aplicação disponibiliza uma API REST para comunicação com
outras aplicações.

## Tecnologias utilizadas

- Java 17 ou superior
- Spring Boot
- Maven
- JDBC
- Oracle Database
- IntelliJ IDEA

## Estrutura do projeto

O projeto está dividido nas seguintes camadas:

### Model

Contém as classes que representam os dados do sistema.

### DAO

Responsável pelos comandos SQL e pelo acesso ao banco de dados.

### Business

Responsável pelas validações e regras de negócio.

### Resource

Contém os endpoints REST da aplicação.

### Infra

Contém a classe ConnectionFactory, responsável pela conexão
com o banco Oracle.

### Config

Contém a configuração de CORS para permitir requisições
de outras aplicações.

## Banco de dados

A aplicação utiliza o banco Oracle da FIAP.

Configurações:

- Servidor: oracle.fiap.com.br
- Porta: 1521
- Serviço: ORCL
- Usuário: configurado na ConnectionFactory

As tabelas e sequências já devem existir no banco Oracle.

A senha não está armazenada no código-fonte.

## Como executar o projeto

1. Abrir o IntelliJ IDEA.
2. Abrir a pasta do projeto EcoUp-Sprint4.
3. Verificar se o JDK está configurado.
4. Aguardar o carregamento das dependências do Maven.
5. Configurar a variável de ambiente DB_PASSWORD.
6. Executar a classe EcoUpApplication.java.
7. Aguardar a inicialização do Spring Boot.

A aplicação será iniciada em:

http://localhost:8080

## Endpoints da API

### Usuários

GET /api/usuarios

GET /api/usuarios/{id}

POST /api/usuarios

PUT /api/usuarios/{id}

DELETE /api/usuarios/{id}

### Missões

GET /api/missoes

GET /api/missoes/{id}

POST /api/missoes

PUT /api/missoes/{id}

DELETE /api/missoes/{id}

### Categorias

GET /api/categorias

GET /api/categorias/{id}

POST /api/categorias

PUT /api/categorias/{id}

DELETE /api/categorias/{id}

### Níveis

GET /api/niveis

GET /api/niveis/{id}

POST /api/niveis

PUT /api/niveis/{id}

DELETE /api/niveis/{id}

### Avatares

GET /api/usuarios/{id}/avatar

POST /api/usuarios/{id}/avatar

GET /api/avatares/{id}

PUT /api/avatares/{id}

DELETE /api/avatares/{id}

### Recomendações

GET /api/usuarios/{id}/recomendacoes

POST /api/usuarios/{id}/recomendacoes

POST /api/usuarios/{id}/recomendacoes/gerar

GET /api/recomendacoes/{id}

PUT /api/recomendacoes/{id}

DELETE /api/recomendacoes/{id}

### Histórico e pontuação

GET /api/usuarios/{id}/nivel

GET /api/usuarios/{id}/niveis

GET /api/usuarios/{id}/missoes

GET /api/usuarios/{id}/historico

POST /api/usuarios/{id}/missoes/{missaoId}/iniciar

POST /api/usuarios/{id}/missoes/{missaoId}/concluir

PATCH /api/usuarios/{id}/missoes/{missaoId}/cancelar

## Códigos HTTP

200 - Requisição realizada com sucesso.

201 - Registro criado com sucesso.

204 - Registro excluído com sucesso.

400 - Dados inválidos.

404 - Registro não encontrado.

409 - Conflito de dados.

500 - Erro interno do servidor.

## Funcionalidades implementadas

- Cadastro de usuários.
- Consulta de usuários.
- Atualização de usuários.
- Exclusão de usuários.
- Cadastro e gerenciamento de missões.
- Cadastro e gerenciamento de categorias.
- Gerenciamento de níveis.
- Gerenciamento de avatares.
- Gerenciamento de recomendações.
- Registro de missões concluídas.
- Histórico de participação.
- Cálculo de pontuação.
- Atualização de níveis conforme a pontuação.
