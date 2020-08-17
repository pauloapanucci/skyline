
# Considerações sobre a implementação

## Tecnologia  
  
- Java 8 - devido ao suporte públido mantido pela Oracle;  
- Spring e Spring Boot - Devido a característica IoC (Inversion of Control) em que o controle dos objetos e da aplicação é transferido ao framework;  
- MySQL - banco de dados consolidado e open source  
- Docker - facilidade na disponibilidade do ambiente para desenvolvimento e produção

## Divisão das camadas da aplicação

A Aplicação foi dividida nas seguintes camadas:
- **Entity:** camada que contém os objetos que representam as entidades a serem persistidas no banco de dados. Exemplo a classe [User](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/entity/User.java) representa a entidade Usuário que será persistida no banco de dados. Como pose ser visto, é marcado com a anotação ``@Entity``.
- **Repository:** camada que contém a Interface que se comunica com o banco de dados, sendo essa interface extendida da classe JpaRepository. Exemplo a classe [UserRepository](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/repository/UserRepository.java) que é anotada com ``@Repository``. Em resumo, essa cama é responsável por realizar as operações no banco de dados com os objetos da camada **Entity**. (Inserir uma nova entidade usuário, por um exemplo).
- **Controller:** camada responsável por disponibilizar endpoints para a api, para que possa ser consumida através de métodos HTTP. Como exemplo temos [UserController](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/controller/UserController.java)  que é anotada com ``@Controller`` e disponibiliza o endpoint ``/users`` para a realização do método POST.
- **Service:** camada responsável por receber as informações da **Controller**, realizar regras de negócio se necessário, e então se comunicar com a camada **Repository** para assim persistir as informações no banco de dados. É uma camada importante para não permitir que os controllers não se comuniquem direto com a camada repository. Como exemplo temos o  [UserService](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/service/UserService.java) anotado com ``@Service``.

## Camadas auxiliares, Comunicação com outras APIS e qualidade de funcionalidades migradas
- **DTO:** camada que contém classes responsávies por objetos para transferência de dados entre a API e quem a consome. Foi tomada a decisão de se utilizar a camada DTO para em algumas situações não retornar para quem consome a API informações a mais do que o necessário, caso retornassemos uma **Entity** como response da API. Esta camada também é utilizada no contexto dessa aplicação para definir o que será esperado como *body* da requisição POST.
	- **Exemplos:** 
		- [UserDTO](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/dto/UserDTO.java) define [UserController](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/controller/UserController.java#L22) quais são os parâmetros que deverá vir como JSON no corpo do método POST para o endpoint ``/users`` .
		- [ValueDTO](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/dto/ValueDTO.java) define o objeto que será mapeado para JSON e será retornado como response pelo [WalletController](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/controller/WalletController.java#L35) ao realizar o método GET no endpoint ``/currentbrlbalance``, que nesse caso retorna um JSON (mapeado do ValueDTO) com a informação do valor do saldo atual em R$ na carteira do usuário.
- **BitcoinAPIS (Camada que comunica a API do Skyline com outras APIs):** essa camada contém a classe [BtcPrice]([https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/bitcoinApis/BtcPrice.java#L61](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/bitcoinApis/BtcPrice.java#L61)) que é uma classe que segue o *pattern* *static factory method*, em que temos o método estático ``inBrl()`` que retorna um BtcPrice com as informações  do preço em R$ do bitcoin obtidas na API [api.coinbase]( https://api.coinbase.com/v2/prices/spot?currency=BRL). Como a classe BtcPrice tem seu construtor privado e não possui *setters*, o usuário dessa API não precisa entender como está sendo feita a comunicação para obter o preço em R$ do Bitcoin, precisando somente utilizar o método estático para obter um objeto, sem precisar instanciar a classe explicitamente através de contrutores ou passando parâmetros para algum método. Esse é um exemplo de facilitar a migração/reestruturação de código, pois quem está consumindo essa API [api.coinbase]( https://api.coinbase.com/v2/prices/spot?currency=BRL) através do método ``inBrl()`` da classe BtcPrice não precisa mudar a chamada do método caso ocorram migrações/reestruturações no código da BtcPrice.
	- A Classe BtcPrice nos permite a implementação de outros métodos estáticos como ``inUsd()``, ``inCad()`` para obter o valor do Bitcoin com base no dólar americano ou canadense;
	- A Classe BTCPrice nos permite a implementação de outros métodos estáticos como ``inBrlFromApiCoinBase()``, ``inBrlFromFooBar()`` para obter o valor do Bitcoin com base no Real porém de APIs diferentes;
	- Pode-se futuramente ler as urls do arquivo application.properties para assim configurar métodos estáticos que nos retone informações de mais de uma API.

## Considerações sobre a performance no backend
Com o aumento do uso da API, uma consideração que eu faria em relação a performance seria tornar a operação de compra de Bitcoin *multithread* ou separaria em outro serviço para que mais de uma instância processasse os pedidos de compra de Bitcoins. Esta consideração é feita pois esse processo consome os dados de uma api de terceiro o que pode se tornar uma comunicação mais lenta em momentos específicos. 

## Considerações sobre permitir a fácil instalação para o cliente sem perder suas configurações

Ao baixar o pacote disponibilizado como [Release v1.0.0](https://github.com/pauloapanucci/skyline/blob/develop/src/main/java/com/papp/skyline/dto/UserDTO.java) provemos:
- A aplicação skyline.jar;
- Diretório skyline-config contendo o aquivo application.properties externalizado (nesse ceário podemos imaginar que o cliente possua mais arquivos de *properties* e outros *resources* já configurados e guardados);
    - Assim, facilitamos que o cliente não precisa reconfigurar a aplicação, pois suas configurações anteriores estão salvas, e caso a atualização contemple alguma mudança em um *resource* ou arquivo *properties* o usuário pode fazer em seus arquivos de acordo com o manual da aplicação.
- Arquivo dockerfile e docker-compose, para que seja feita de forma automatizada a execução da aplicação já considerando os arquivos de configuração externalizadas do cliente;
- Um TO-DO para próximas implementações é utilizar o [Liquibase](https://docs.liquibase.com/home.html) para facilitar a atualização da base de dados do cliente fazendo isso através da aplicação.