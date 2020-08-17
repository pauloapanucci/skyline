![Java CI with Maven - Build](https://github.com/pauloapanucci/skyline/workflows/Java%20CI%20with%20Maven/badge.svg?branch=develop)

# Skyline

Skyline é uma API que permite a criação de usuários em uma casa de câmbio de Bitcoins. Você pode:
- Criar um usuário;
- Fazer a transferência de saldo em Real (R$) para a carteira do usuário;
- Comprar Bitcoins utilizando o saldo em R$ disponível na carteira;
- Consultar o saldo em R$ da carteira;
- Consultar o saldo de Bitcoins da carteira;
- Consultar o lucro (ou perda caso negativo) em reais desde a primeira compra de Bitcoin;
- Consultar o total em R$ gasto em compra de Bitcoin desde a primeira compra;
- Consultar as 5 ultimas transações realizadas na carteira (transferência em R$ para a carteira ou compra de Bitcoins);
- Consultar o preço atual do Bitcoin em R$;
- Consultar informações gerais e de saúde da aplicação.

### Skyline Downstream

Mesmo se tratando de um desafio para um processo seletivo, foram elencadas *features* a serem implementadas no quadro [Skyline Downstream](https://github.com/pauloapanucci/skyline/projects/1). Acompanhe o trabalho em andamento no quadro :)

### Considerações sobre a implementação

As considerações sobre a implementação estão num [README](https://github.com/pauloapanucci/skyline/blob/develop/doc/README.md) separado dentro do diretório doc, para que a página inicial do projeto não fique muito extensa.

### Documentação da API (OpenAPI)

A documentação da API no formato OpenAPI está na pasta [doc](https://github.com/pauloapanucci/skyline/blob/develop/doc/swagger.yaml). Caso necessário, utilize o  [swagger editor](https://editor.swagger.io/).

### Tecnologia

- Java 8 - devido ao suporte públido mantido pela Oracle;
- Spring e Spring Boot - Devido a característica IoC (Inversion of Control) em que o controle dos objetos e da aplicação é transferido ao framework;
- MySQL - banco de dados consolidado e open source
- Docker - facilidade na disponibilidade do ambiente para desenvolvimento e produção

### Pré-requisito

- docker e docker-compose

### Executando a API

Instale o docker e/ou o docker-compose caso não tenha estes instalados (ver pré-requisitos).

```sh
$ docker-compose up --build
```

para executar em background:
```sh
$ docker-compose up  --build --detach
```

### Informações de disponibilidade da API

A API estará disponível na seguinte url:

```sh
http://localhost:8090/skyline/api
```

### Testando a API

A pasta [doc](https://github.com/pauloapanucci/skyline/blob/develop/doc/Skyline.postman_collection.json) contém uma collection do [Postman](https://www.postman.com/downloads/) para testar os endpoints desta API.

#### Executando a API do código fonte
Execute o comando
```sh
$ mvn clean install -DskipTests
```
para gerar um artefato chamado skyline.jar na pasta target.

Para executar o jar:
```sh
$ java -jar skyline.jar
```
