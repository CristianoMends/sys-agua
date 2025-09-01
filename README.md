# Sys-Água - Sistema de Gestão para Distribuidoras

<p align="center">
  Sistema completo para gestão de distribuidoras de água e bebidas, com controle de estoque, pedidos, financeiro e entregas. Desenvolvido em Java com Spring Boot e JavaFX.
</p>

<p align="center">
    <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/>
    <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot"/>
    <img src="https://img.shields.io/badge/JavaFX-0769C4?style=for-the-badge&logo=oracle&logoColor=white" alt="JavaFX"/>
    <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"/>
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger"/>
</p>

## 🎯 Sobre o Projeto

O **Sys-Água** é uma solução de software robusta, desenvolvida como um projeto acadêmico para a disciplina de Projeto de Engenharia de Software I. O sistema foi projetado para otimizar e automatizar a gestão de distribuidoras de água mineral e outras bebidas, abrangendo desde o controle de estoque até a gestão financeira e de entregas.

[![YouTube Video](https://img.shields.io/badge/YouTube-Assistir-red?logo=youtube&logoColor=white)](https://www.youtube.com/watch?v=R1qipio-n7s)

A solução é composta por duas partes principais:
1.  **`sysagua-api`**: Um backend RESTful, construído com **Spring Boot**, que centraliza toda a lógica de negócio, segurança e persistência de dados.
2.  **`sysagua-app`**: Um aplicativo desktop cliente, desenvolvido com **JavaFX**, que consome a API e oferece uma interface gráfica intuitiva para os funcionários da distribuidora.

---

## ✨ Funcionalidades Principais

O sistema oferece um conjunto completo de módulos para uma gestão integrada:

* **Autenticação e Segurança**:
    * Sistema de login com autenticação baseada em `JWT (JSON Web Tokens)`.
    * Controle de acesso por `níveis de permissão` (cargos) para diferentes funcionalidades do sistema.

* **Gestão de Cadastros**:
    * CRUD completo para `Clientes`, `Fornecedores`, `Entregadores` e `Usuários` do sistema.
    * Cadastro detalhado de `Produtos`, organizados por `Linhas` e `Categorias`.

* **Controle de Estoque**:
    * Monitoramento em tempo real da quantidade de produtos.
    * Registro de `compras` de fornecedores para entrada de novos itens.
    * Histórico detalhado de movimentações de estoque (entradas e saídas).

* **Vendas e Pedidos**:
    * Criação e gerenciamento de `pedidos` de venda.
    * Controle de `status de entrega` (Pendente, Em Rota, Entregue, Cancelado).
    * Associação de pedidos a clientes e entregadores.

* **Módulo Financeiro**:
    * Gestão de `caixa (Cashier)` com registro de aberturas e fechamentos.
    * Controle de `transações` financeiras, registrando todas as entradas e saídas.

* **Dashboard e Métricas**:
    * Visualização de gráficos e dados consolidados para auxiliar na tomada de decisões.

---

## 🚀 Tecnologias Utilizadas

* **Backend (`sysagua-api`)**:
    * **[Java 21](https://www.oracle.com/java/)**: Linguagem de programação principal.
    * **[Spring Boot](https://spring.io/projects/spring-boot)**: Framework para criação da API RESTful.
    * **[Spring Security](https://spring.io/projects/spring-security)**: Para implementação da segurança e autenticação.
    * **[JPA / Hibernate](https://hibernate.org/)**: Para persistência de dados e mapeamento objeto-relacional.
    * **[PostgreSQL](https://www.postgresql.org/)**: Banco de dados relacional.
    * **[Flyway](https://flywaydb.org/)**: Ferramenta para versionamento e migração de banco de dados.
    * **[Swagger (Springdoc)](https://springdoc.org/)**: Para documentação interativa da API.
    * **[Docker](https://www.docker.com/)**: Para containerização da aplicação.

* **Frontend (`sysagua-app`)**:
    * **[Java 17](https://www.oracle.com/java/)**: Linguagem de programação.
    * **[JavaFX](https://openjfx.io/)**: Framework para construção da interface gráfica desktop.
    * **[Maven](https://maven.apache.org/)**: Ferramenta de gerenciamento de dependências e build.
    * **[FontAwesomeFX](https://github.com/Jerady/FontAwesomeFX)**: Para utilização de ícones vetoriais.

---

## ⚙️ Como Executar o Projeto

A maneira mais simples de rodar todo o ambiente (API, Banco de Dados e Aplicação Desktop) é utilizando o Docker.

### Pré-requisitos

* [Docker](https://www.docker.com/get-started)
* [Docker Compose](https://docs.docker.com/compose/install/)

### Passos para Execução

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/CristianoMends/sys-agua.git](https://github.com/CristianoMends/sys-agua.git)
    cd sys-agua
    ```

2.  **Construa e suba os containers:**
    * Execute o comando abaixo na raiz do projeto. Ele irá baixar as imagens necessárias, construir a API, o app e iniciar todos os serviços.
    ```bash
    docker-compose up --build
    ```

3.  **Acesse os serviços:**
    * **API**: Estará disponível em `http://localhost:8080`.
    * **Documentação Swagger**: Acesse `http://localhost:8080/swagger-ui.html` para ver e testar todos os endpoints.
    * **Aplicação Desktop**: A interface gráfica do `sysagua-app` será iniciada automaticamente.

Para parar todos os serviços, pressione `Ctrl + C` no terminal onde o docker-compose está rodando e depois execute:
```bash
docker-compose down
```

## 🤝 Colaboradores&#x20;
Este projeto foi desenvolvido por:

| [Cristiano](https://github.com/CristianoMends)                                                                            | [Rafael](https://github.com/Rafaelleit3)                                                                        | [Natan](https://github.com/jnatansb)                                                                           | [Josias](https://github.com/josiasdev)                                                                         |
|---------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| <img src="https://avatars.githubusercontent.com/u/116528159?v=4" width="100px" alt="Foto de perfil de Cristiano Mendes"/> | <img src="https://avatars.githubusercontent.com/u/137407431?v=4" width="100px" alt="Foto de perfil de Rafael"/> | <img src="https://avatars.githubusercontent.com/u/111660222?v=4" width="100px" alt="Foto de perfil de Natan"/> | <img src="https://avatars.githubusercontent.com/u/71450649?v=4" width="100px" alt="Foto de perfil de Josias"/> |

## 📄 Licença&#x20;

Projeto desenvolvido para fins acadêmicos.

💡 **Dúvidas ou sugestões?** Entre em contato com os colaboradores através do GitHub!

🚀 **Contribua!** Sinta-se à vontade para abrir issues e pull requests. 😃

