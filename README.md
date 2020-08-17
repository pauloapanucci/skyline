# Skyline

Skyline � uma API que permite a cria��o de usu�rios em uma casa de c�mbio de Bitcoins. Voc� pode:
- Criar um usu�rio;
- Fazer a transfer�ncia de saldo em Real (R$) para a carteira do usu�rio;
- Comprar Bitcoins utilizando o saldo em R$ dispon�vel na carteira;
- Consultar o saldo em R$ da carteira;
- Consultar o saldo de Bitcoins da carteira;
- Consultar o lucro (ou perda caso negativo) em reais desde a primeira compra de Bitcoin;
- Consultar o total em R$ gasto em compra de Bitcoin desde a primeira compra;
- Consultar as 5 ultimas transa��es realizadas na carteira (transfer�ncia em R$ para a carteira ou compra de Bitcoins);
- Consultar o pre�o atual do Bitcoin em R$;
- Consultar informa��es gerais e de sa�de da aplica��o.

# Considera��es sobre a implementa��o

As considera��es sobre a implementa��o est�o num [README](https://github.com/pauloapanucci/skyline/blob/develop/doc/README.md) separado dentro do diret�rio doc, para que a p�gina inicial do projeto n�o fique muito extensa.

# Documenta��o da API (OpenAPI)

A documenta��o da API no formato OpenAPI est� na pasta [doc](https://github.com/pauloapanucci/skyline/blob/develop/doc/swagger.yaml). Caso necess�rio, utilize o  [swagger editor](https://editor.swagger.io/).

### Tecnologia

- Java 8 - devido ao suporte p�blido mantido pela Oracle;
- Spring e Spring Boot - Devido a caracter�stica IoC (Inversion of Control) em que o controle dos objetos e da aplica��o � transferido ao framework;
- MySQL - banco de dados consolidado e open source
- Docker - facilidade na disponibilidade do ambiente para desenvolvimento e produ��o

### Pr�-requisito

- docker e docker-compose

### Executando a API

Instale o docker e/ou o docker-compose caso n�o tenha estes instalados (ver pr�-requisitos).

```sh
$ docker-compose up --build
```

para executar em background:
```sh
$ docker-compose up  --build --detach
```

### Informa��es de disponibilidade da API

A API estar� dispon�vel na seguinte url:

```sh
http://localhost:8090/skyline/api
```

### Testando a API

A pasta [doc](https://github.com/pauloapanucci/skyline/blob/develop/doc/Skyline.postman_collection.json) cont�m uma collection do [Postman](https://www.postman.com/downloads/) para testar os endpoints desta API.

#### Executando a API do c�digo fonte
Execute o comando
```sh
$ mvn clean install -DskipTests
```
para gerar um artefato chamado skyline.jar na pasta target.

Para executar o jar:
```sh
$ java -jar skyline.jar
```
