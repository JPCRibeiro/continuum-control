# Continuum Control API

### Sistema de Gestão e Controle da Integridade Temporal da Continuum Authority

## Serviços necessários

| Serviço | Versão | Porta | Função |
|---|---|---|---|
| PostgreSQL | 17 | 5432 | dados transacionais do domínio |
| MongoDB | 8 | 27017 | ocorrências de campo |
| Redis | 7.4 | 6379 | cache do estado operacional |

Os três sobem pelo docker-compose.yml e são necessários para o funcionamento completo.

## Configurar

Todas as propriedades têm valor padrão para execução local e são sobrescritas por
variável de ambiente, sem recompilar.

| Variável | Padrão |
|---|---|
| `SERVER_PORT` | `8080` |
| `DB_URL` | `jdbc:postgresql://localhost:5432/continuum_control` |
| `DB_USER` / `DB_PASSWORD` | `user` / `dev` |
| `DDL_AUTO` | `create-drop` |
| `MONGO_URI` | `mongodb://localhost:27017/continuum_control` |
| `REDIS_HOST` / `REDIS_PORT` | `localhost` / `6379` |
| `JWT_SECRET_KEY` | chave de desenvolvimento (mínimo 32 caracteres) |
| `JWT_TTL` | `PT2H` |

`DDL_AUTO` é `create-drop`: o schema é recriado a cada inicialização e o
`src/main/resources/data.sql` carrega os dados iniciais em seguida.

## Construir

```bash
./mvnw clean package
```

Gera o executável na pasta target.

## Testar

```bash
./mvnw test
```

Roda os testes sem nenhum serviço externo.

## Executar

**Com Docker** — o `Dockerfile` copia o artefato já construído, então empacote antes:

```bash
./mvnw clean package
docker compose up --build
```

Os três bancos sobem com `healthcheck` e a aplicação só inicia depois que todos
respondem.

**Local** — suba apenas os bancos:

```bash
docker compose up -d postgres mongodb redis
./mvnw spring-boot:run
```

## Verificar

**1. Saúde**

```bash
http://localhost:8080/actuator/health
```

Esperado: `{"status":"UP"}`.

**2. Autenticar**

Usuários da carga inicial, todos com a senha `senha123`:

| Email | Perfil |
|---|---|
| `operador@continuum.control` | OPERADOR |
| `supervisor@continuum.control` | SUPERVISOR |
| `admin@continuum.control` | ADMINISTRADOR |
| `bond@continuum.control` | AGENTE |
| `loki@continuum.control` | AGENTE |
| `ada@continuum.control` | AGENTE |

```bash
POST http://localhost:8080/api/auth/login

{
  "email":"admin@continuum.control",
  "senha":"senha123"
}
```

Devolve `200` com o token. Senha errada devolve `401`.

**3. Consultar com o token JWT**

```bash
http://localhost:8080/api/estado-operacional

Authorization: Bearer Token
```

Devolve as contagens operacionais e o índice de estabilidade. Sem o cabeçalho, a
mesma rota devolve `401`.

## Persistência

### PostgreSQL (relacional)

Guarda agentes, usuários, eventos, anomalias, missões, designações, autorizações, intervenções,
encerramentos e o histórico temporal.

É o modelo adequado porque esses dados são fortemente relacionados e com vínculos
obrigatórios: uma anomalia exige um evento, uma missão exige uma anomalia. E há
operações que mudam várias entidades de uma vez — o `MissaoService.iniciar` altera
a missão, todos os agentes designados e a anomalia, e grava o histórico. Se um
agente estiver indisponível, nada é efetivado.

Não caberia nos outros: nem documento nem chave-valor garantem transação entre
entidades distintas.

### MongoDB (NoSQL)

Guarda as ocorrências registradas pelos agentes durante as missões.

É o modelo adequado porque nem todas as ocorrências possuirão a mesma estrutura, cada tipo de ocorrência tem uma estrutura diferente:

- uma distorção temporal carrega intensidade e duração; uma evidência temporal carrega objeto, período estimado e grau de confiabilidade.

Não caberia no relacional: uma tabela única precisaria de colunas para todos os tipos,
nulas por construção na maior parte das linhas.

### Redis (chave-valor)

Guarda uma única chave, `estadoOperacional::atual`, com o painel operacional já
calculado e expiração de 30 segundos.

É o modelo adequado porque o acesso é por chave conhecida, sem consulta e sem
relacionamento, e a expiração é nativa. O painel é lido com frequência e custa sete
contagens sobre quatro tabelas, enquanto os números por trás mudam pouco.

Não guarda mais nada: não tem relacionamento e nem transação. Se for apagado, a próxima consulta recalcula.