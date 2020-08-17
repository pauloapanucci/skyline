
# Considera��es sobre a implementa��o

## Tecnologia  
  
- Java 8 - devido ao suporte p�blido mantido pela Oracle;  
- Spring e Spring Boot - Devido a caracter�stica IoC (Inversion of Control) em que o controle dos objetos e da aplica��o � transferido ao framework;  
- MySQL - banco de dados consolidado e open source  
- Docker - facilidade na disponibilidade do ambiente para desenvolvimento e produ��o

## Divis�o das camadas da aplica��o

A Aplica��o foi dividida nas seguintes camadas:
- **Entity:** camada que cont�m os objetos que representam as entidades a serem persistidas no banco de dados. Exemplo a classe [User](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/entity/User.java) representa a entidade Usu�rio que ser� persistida no banco de dados. Como pose ser visto, � marcado com a anota��o ``@Entity``.
- **Repository:** camada que cont�m a Interface que se comunica com o banco de dados, sendo essa interface extendida da classe JpaRepository. Exemplo a classe [UserRepository](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/repository/UserRepository.java) que � anotada com ``@Repository``. Em resumo, essa cama � respons�vel por realizar as opera��es no banco de dados com os objetos da camada **Entity**. (Inserir uma nova entidade usu�rio, por um exemplo).
- **Controller:** camada respons�vel por disponibilizar endpoints para a api, para que possa ser consumida atrav�s de m�todos HTTP. Como exemplo temos [UserController](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/controller/UserController.java)  que � anotada com ``@Controller`` e disponibiliza o endpoint ``/users`` para a realiza��o do m�todo POST.
- **Service:** camada respons�vel por receber as informa��es da **Controller**, realizar regras de neg�cio se necess�rio, e ent�o se comunicar com a camada **Repository** para assim persistir as informa��es no banco de dados. � uma camada importante para n�o permitir que os controllers n�o se comuniquem direto com a camada repository. Como exemplo temos o  [UserService](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/service/UserService.java) anotado com ``@Service``.

## Camadas auxiliares, Comunica��o com outras APIS e qualidade de funcionalidades migradas
- **DTO:** camada que cont�m classes respons�vies por objetos para transfer�ncia de dados entre a API e quem a consome. Foi tomada a decis�o de se utilizar a camada DTO para em algumas situa��es n�o retornar para quem consome a API informa��es a mais do que o necess�rio, caso retornassemos uma **Entity** como response da API. Esta camada tamb�m � utilizada no contexto dessa aplica��o para definir o que ser� esperado como *body* da requisi��o POST.
	- **Exemplos:** 
		- [UserDTO](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/dto/UserDTO.java) define [UserController](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/controller/UserController.java#L22) quais s�o os par�metros que dever� vir como JSON no corpo do m�todo POST para o endpoint ``/users`` .
		- [ValueDTO](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/dto/ValueDTO.java) define o objeto que ser� mapeado para JSON e ser� retornado como response pelo [WalletController](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/controller/WalletController.java#L35) ao realizar o m�todo GET no endpoint ``/currentbrlbalance``, que nesse caso retorna um JSON (mapeado do ValueDTO) com a informa��o do valor do saldo atual em R$ na carteira do usu�rio.
- **BitcoinAPIS (Camada que comunica a API do Skyline com outras APIs):** essa camada cont�m a classe [BtcPrice]([https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/bitcoinApis/BtcPrice.java#L61](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/bitcoinApis/BtcPrice.java#L61)) que � uma classe que segue o *pattern* *static factory method*, em que temos o m�todo est�tico ``inBrl()`` que retorna um BtcPrice com as informa��es  do pre�o em R$ do bitcoin obtidas na API [api.coinbase]( https://api.coinbase.com/v2/prices/spot?currency=BRL). Como a classe BtcPrice tem seu construtor privado e n�o possui *setters*, o usu�rio dessa API n�o precisa entender como est� sendo feita a comunica��o para obter o pre�o em R$ do Bitcoin, precisando somente utilizar o m�todo est�tico para obter um objeto, sem precisar instanciar a classe explicitamente atrav�s de contrutores ou passando par�metros para algum m�todo. Esse � um exemplo de facilitar a migra��o/reestrutura��o de c�digo, pois quem est� consumindo essa API [api.coinbase]( https://api.coinbase.com/v2/prices/spot?currency=BRL) atrav�s do m�todo ``inBrl()`` da classe BtcPrice n�o precisa mudar a chamada do m�todo caso ocorram migra��es/reestrutura��es no c�digo da BtcPrice.
	- A Classe BtcPrice nos permite a implementa��o de outros m�todos est�ticos como ``inUsd()``, ``inCad()`` para obter o valor do Bitcoin com base no d�lar americano ou canadense;
	- A Classe BTCPrice nos permite a implementa��o de outros m�todos est�ticos como ``inBrlFromApiCoinBase()``, ``inBrlFromFooBar()`` para obter o valor do Bitcoin com base no Real por�m de APIs diferentes;
	- Pode-se futuramente ler as urls do arquivo application.properties para assim configurar m�todos est�ticos que nos retone informa��es de mais de uma API.

## Considera��es sobre a performance no backend
Com o aumento do uso da API, uma considera��o que eu faria em rela��o a performance seria tornar a opera��o de compra de Bitcoin *multithread* ou separaria em outro servi�o para que mais de uma inst�ncia processasse os pedidos de compra de Bitcoins. Esta considera��o � feita pois esse processo consome os dados de uma api de terceiro o que pode se tornar uma comunica��o mais lenta em momentos espec�ficos. 

## Considera��es sobre permitir a f�cil instala��o para o cliente sem perder suas configura��es

Ao baixar o pacote disponibilizado como [Release v1.0.0](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/dto/UserDTO.java) provemos:
- A aplica��o skyline.jar;
- Diret�rio skyline-config contendo o aquivo application.properties externalizado (nesse ce�rio podemos imaginar que o cliente possua mais arquivos de *properties* e outros *resources* j� configurados e guardados);
    - Assim, facilitamos que o cliente n�o precisa reconfigurar a aplica��o, pois suas configura��es anteriores est�o salvas, e caso a atualiza��o contemple alguma mudan�a em um *resource* ou arquivo *properties* o usu�rio pode fazer em seus arquivos de acordo com o manual da aplica��o.
- Arquivo dockerfile e docker-compose, para que seja feita de forma automatizada a execu��o da aplica��o j� considerando os arquivos de configura��o externalizadas do cliente;
- Um TO-DO para pr�ximas implementa��es � utilizar o [Liquibase](https://docs.liquibase.com/home.html) para facilitar a atualiza��o da base de dados do cliente fazendo isso atrav�s da aplica��o.