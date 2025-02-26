<h1 align="center"> Sys Água 💻💦</h1>

## 📌 Índice

1. [Sobre o Projeto](#sobre-o-projeto)
2. [📌 Arquitetura e Metodologias](#-arquitetura-e-metodologias)
2. [🚀 Tecnologias](#tecnologias)
3. [🛠️ Funcionalidades](#funcionalidades)
4. [🤝 Colaboradores](#colaboradores)
5. [📄 Licença](#licenca)

---

## 📜 Sobre o Projeto&#x20;

Um sistema completo para gerenciar pedidos, controle de estoque e entregas de água, facilitando a organização do processo logístico e melhorando o atendimento ao cliente.

Este projeto foi desenvolvido como parte da disciplina Projeto Integrado em Engenharia de Software I, visando a aplicação prática de conceitos de engenharia de software no desenvolvimento de uma solução real.

Durante o desenvolvimento, foram trabalhados conceitos avançados de **Gerência de Configuração**, incluindo o uso de Docker, Docker Compose, GitHub Actions (CI/CD), conceitos de GitHub Workflow e Git Flow.

Além disso, houve um foco no aprendizado de **projeto detalhado de software**, utilizando padrões de projeto e os princípios SOLID, garantindo uma arquitetura escalável e modular.

O projeto também contemplou conceitos de **Redes de Computadores**, trabalhando com a camada de aplicação no protocolo HTTP na implementação da API REST, e conceitos de **fundamentos de Bancos de Dados**, aplicados na modelagem e otimização do PostgreSQL.

## 📌 Arquitetura e Metodologias

O desenvolvimento do Sys Água seguiu os princípios SOLID e utilizou diversos padrões de projeto para garantir um código modular e escalável.

Além disso, foram implementados diferentes ambientes de execução como:
- Desenvolvimento (dev), onde trabalhamos com banco H2 facilitando nos testes.
- Staging, onde usamos um banco postgreSQL local, simulando o ambiente de produção.
- Produção (prod), onde usamos docker para criar todo o sistema incluindo o banco postgres.

O gerenciamento do projeto seguiu a metodologia Scrum, com sprints definidas para entregas incrementais e revisões constantes.

---

## 🚀 Tecnologias&#x20;

O projeto Sys Água é composto por duas partes principais: uma API REST desenvolvida em **Spring Boot** e um aplicativo desktop construído com **JavaFX**.

### 🌐 API REST

🔗 [Repositório da API](https://github.com/CristianoMends/sys-agua/tree/develop/sysagua-api)

- **Java 21 [LTS]**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Security**
- **JSON Web Tokens (JWT)**
- **PostgreSQL**
- **Flyway Migrations**
- **Swagger OpenAPI**
- **Docker compose**
- **Gradle**

### 🖥️ Aplicação Desktop (JavaFX)

🔗 [Repositório da Aplicação](https://github.com/CristianoMends/sys-agua/tree/develop/sysagua-app)

- **Java 21 [LTS]**
- **JavaFX**
- **FXML**
- **Maven**
- **Jackson ObjectMapper**

---

## 🛠️ Funcionalidades&#x20;

- ✅ Cadastro e gerenciamento de pedidos 📦
- ✅ Gerenciamento de clientes e fornecedores 📊
- ✅ Controle de estoque eficiente 📊
- ✅ Gestão de entregas 🚚
- ✅ Segurança com autenticação JWT 🔐
- ✅ Interface intuitiva para desktop 🎨
- ✅ Documentação interativa com Swagger 📜

---

## 🤝 Colaboradores&#x20;

| [Cristiano](https://github.com/CristianoMends)                                                                            | [Rafael](https://github.com/Rafaelleit3)                                                                        | [Natan](https://github.com/jnatansb)                                                                           | [Josias](https://github.com/josiasdev)                                                                         |
|---------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| <img src="https://avatars.githubusercontent.com/u/116528159?v=4" width="100px" alt="Foto de perfil de Cristiano Mendes"/> | <img src="https://avatars.githubusercontent.com/u/137407431?v=4" width="100px" alt="Foto de perfil de Rafael"/> | <img src="https://avatars.githubusercontent.com/u/111660222?v=4" width="100px" alt="Foto de perfil de Natan"/> | <img src="https://avatars.githubusercontent.com/u/71450649?v=4" width="100px" alt="Foto de perfil de Josias"/> |

---

## 📄 Licença&#x20;

Projeto desenvolvido para fins acadêmicos.

💡 **Dúvidas ou sugestões?** Entre em contato com os colaboradores através do GitHub!

🚀 **Contribua!** Sinta-se à vontade para abrir issues e pull requests. 😃

