# Sys Água API

## 📌 Como Rodar o Projeto

### 🛠 Ambiente de Desenvolvimento (DEV)
O ambiente de desenvolvimento está configurado para rodar com banco de dados em memória H2 e possui migrations para popular o banco automaticamente, facilitando os testes.

▶️ Passos para Rodar o Projeto

```bash
# Clonar o repositório
https://github.com/CristianoMends/sys-agua
cd sys-agua
cd sysagua-api
````

### 🔧 Configuração das Variáveis de Ambiente
execute no terminal, altere conforme necessario:
```bash
export DEFAULT_EMAIL=dev@gmail.com
export DEFAULT_PASSWORD=dev123
export JWT_SECRET_KEY=secret1234
export SPRING_PROFILES_ACTIVE=dev
```

### compilar e testar
```bash
./gradlew clean build
```
### Rodar a aplicação localmente
```bash
./gradlew bootRun
```

## 🏗 Ambiente de Homologação (STAGING)
O ambiente de staging está configurado para rodar com PostgreSQL na máquina local.

▶️ Passos para Rodar o Projeto

```bash
# Clonar o repositório
https://github.com/CristianoMends/sys-agua
cd sys-agua
cd sysagua-api
````

### 🔧 Configuração das Variáveis de Ambiente
execute no terminal, altere conforme necessario:

```bash
export DEFAULT_EMAIL=dev@gmail.com
export DEFAULT_PASSWORD=dev123
export JWT_SECRET_KEY=secret1234
export SPRING_PROFILES_ACTIVE=dev
```

### compilar e testar
```bash
./gradlew clean build
```

### Rodar a aplicação localmente
```bash
./gradlew bootRun
```


## 🚀 Ambiente de Produção (PROD)
O ambiente de produção está configurado para rodar dentro de containers Docker.

🔧 Configuração das Variáveis de Ambiente
Crie um arquivo .env na raiz do projeto (/sys-agua) e adicione as variáveis abaixo:

```env
DB_PASSWORD=sysagua2024         #senha do banco a ser criado pelo docker
DEFAULT_EMAIL=dev@gmail.com     #email para o primeiro usuario do sistema
DEFAULT_PASSWORD=dev123         #senha para o primeiro usuario
JWT_SECRET_KEY=secret1234       #chave secreta para JWT
```
▶️ Passos para Rodar o Projeto
```sh
# Clonar o repositório
git clone https://github.com/seu-repositorio.git
cd seu-repositorio
```

# Construir e subir os containers
```sh
docker-compose up --build -d
```

### ⏹ Parar a Aplicação
Para parar a aplicação nos ambientes produção:
```sh
docker-compose down
```

Para parar no ambiente de desenvolvimento e staging:
```sh
CTRL + C  # Se estiver rodando em terminal
```
