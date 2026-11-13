# Continuum Control API

Sistema de Gestão e Controle da Integridade Temporal da Continuum Authority.

## Arquitetura de Dados

O projeto utiliza três bancos de dados diferentes, rodando via Docker, cada um resolvendo um problema específico da aplicação:

* **PostgreSQL (Relacional):** Guarda os dados centrais — agentes, usuários, eventos, anomalias, missões, autorizações, encerramentos e o histórico temporal. Os vínculos são obrigatórios (uma anomalia exige um evento, uma missão exige uma anomalia) e as chaves estrangeiras fazem o próprio banco recusar estados inválidos. Há operações que mudam várias entidades de uma vez: o `MissaoService.iniciar` altera a missão, todos os agentes designados e a anomalia, e grava o histórico. Se um agente estiver indisponível, nada é efetivado. Nem bancos de documento nem chave-valor garantem integridade transacional entre entidades distintas, justificando a separação.

* **MongoDB (NoSQL Documento):** Armazena as ocorrências relatadas em campo. Foi escolhido porque as ocorrências possuem estruturas variadas (uma *Distorção* tem campos de tempo e intensidade, enquanto uma *Evidência* tem campos de objeto e confiabilidade). Um banco relacional exigiria muitas colunas nulas ou tabelas complexas para acomodar esses dados flexíveis.

* **Redis (Chave-Valor):** Armazena em cache o Índice de Estabilidade Temporal e as contagens do painel operacional. O painel é lido com frequência, e o Redis entrega o resultado instantaneamente, aliviando o PostgreSQL de fazer sete contagens de agregações a cada consulta. O TTL serve como rede de segurança, mas a invalidação explícita (`@CacheEvict`) garante que o painel reflita alterações imediatamente. Como não possui relacionamentos estruturados nem suporte a transações complexas, não seria adequado manter os dados de negócio aqui.

## Configuração

Para rodar localmente você não precisa configurar nada, pois todos os parâmetros possuem valores padrão. No entanto, nenhum valor está fixo no código: todos são lidos de variáveis de ambiente e podem ser alterados conforme o ambiente de execução, sem necessidade de recompilação.

| Variável | Padrão | 
| ----- | ----- | 
| `SERVER_PORT` | `8080` | 
| `DB_URL` | `jdbc:postgresql://localhost:5432/continuum_control` | 
| `DB_USER` / `DB_PASSWORD` | `user` / `dev` | 
| `DDL_AUTO` | `create-drop` | 
| `MONGO_URI` | `mongodb://localhost:27017/continuum_control` | 
| `REDIS_HOST` / `REDIS_PORT` | `localhost` / `6379` | 
| `JWT_SECRET_KEY` | chave de desenvolvimento (mínimo 32 caracteres) | 
| `JWT_TTL` | `PT2H` | 

É através destas variáveis que o `docker-compose.yml` aponta a aplicação para os contêineres em vez do `localhost`.

## Como rodar localmente

### 1. Iniciar os bancos de dados

Na raiz do projeto, suba a infraestrutura utilizando o Docker:

```bash
docker compose up -d postgres mongodb redis
```

Os contêineres possuem `healthchecks` configurados para garantir que subam corretamente antes da aplicação.

### 2. Construir o artefato

Gera o JAR executável e autocontido da aplicação na pasta `target/`:

```bash
./mvnw clean package
```

É este arquivo que o Docker copia para a imagem, e ele também pode rodar sozinho via linha de comando (`java -jar target/continuum-control-0.0.1-SNAPSHOT.jar`). Para o passo 3 abaixo este build prévio não é obrigatório, pois o comando do Spring Boot compila e executa diretamente das classes.

### 3. Iniciar a API

Com os bancos rodando, inicie a aplicação Spring Boot localmente:

```bash
./mvnw spring-boot:run
```

A cada inicialização, como o `ddl-auto` está configurado como `create-drop` e a inicialização de SQL está ativa, o *schema* é recriado e o arquivo `data.sql` recarrega automaticamente a massa de dados iniciais.

*Se preferir rodar **tudo** pelo Docker (app + bancos simultaneamente), basta empacotar (`./mvnw clean package`) e rodar:*

```bash
docker compose up --build
```

### 4. Rodar os Testes

Para rodar a suíte de testes automatizados (que roda de forma independente usando H2 em memória, sem depender de containers, e valida as regras de negócio):

```bash
./mvnw test
```

## Como usar a API

Para verificar se a aplicação subiu corretamente, acesse a rota do Actuator:
`http://localhost:8080/actuator/health` (esperado: status `UP`).

### 1. Fazer Login

O sistema possui controle de acesso baseado em perfis (RBAC). A carga inicial de dados cria os seguintes usuários. **A senha para todos é `senha123`.**

* `admin@continuum.control` (ADMINISTRADOR)
* `supervisor@continuum.control` (SUPERVISOR)
* `operador@continuum.control` (OPERADOR)
* `bond@continuum.control` (AGENTE - James Bond)
* `loki@continuum.control` (AGENTE - Loki)
* `ada@continuum.control` (AGENTE - Ada)

Faça uma requisição `POST` para `http://localhost:8080/api/auth/login`:

```json
{
  "email": "admin@continuum.control",
  "senha": "senha123"
}
```

A API devolverá um token JWT com status `200 OK`.

### 2. Consumir Rotas Protegidas

Copie o token recebido e passe-o no cabeçalho `Authorization` como `Bearer <token>`.
Experimente buscar o painel de estabilidade fazendo um `GET` na rota:

```text
GET http://localhost:8080/api/estado-operacional
Authorization: Bearer <seu-token-jwt>
```

Você receberá as contagens de missões e o índice de estabilidade da linha temporal. Regras de segurança granulares bloqueiam acessos indevidos dependendo do perfil autenticado (por exemplo, impedindo que um Agente tente modificar a missão de outro).
