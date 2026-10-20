# CityInclui — Back-end

API REST reativa do **CityInclui**, um site que ajuda pessoas com deficiência visual a encontrar restaurantes com recursos de acessibilidade. Projeto universitário.

> Este repositório contém a **API (back-end)**. A interface e o recurso de leitura por voz, que lê a tela dinamicamente enquanto a pessoa navega pelo teclado, ficam no front-end: [cityInclui-frontEnd](https://github.com/DevMiranda7/cityInclui-frontEnd).

## O que a API faz

- **Anunciantes (donos de restaurantes):** cadastro com fotos, descrição, cardápio e lista de recursos de acessibilidade; edição de perfil e exclusão de conta.
- **Clientes:** cadastro, perfil, edição e exclusão de conta.
- **Avaliações:** clientes avaliam restaurantes com nota de 1 a 5 e comentário; só o autor edita ou apaga a própria avaliação.
- **Busca e ranking:** pesquisa paginada por nome do restaurante ou cardápio e lista dos 5 restaurantes mais bem avaliados.
- **Autenticação:** login com JWT e dois perfis (`OWNER` e `CLIENT`).
- **Fotos:** upload de imagens JPEG e PNG para o Amazon S3.

## Stack

| Camada | Tecnologias |
|---|---|
| Linguagem e framework | Java 21, Spring Boot 3.5 |
| Web | Spring WebFlux (programação reativa) |
| Segurança | Spring Security reativo, JWT (jjwt 0.12), BCrypt |
| Dados | Spring Data R2DBC, PostgreSQL (driver `r2dbc-postgresql`) |
| Armazenamento | Amazon S3 (AWS SDK v2, `S3AsyncClient`) |
| Validação | Jakarta Bean Validation |
| Build | Maven (wrapper incluído) |
| Infraestrutura | Elastic Beanstalk (API), Amplify (front-end), Aurora/RDS PostgreSQL, Route 53 |

## Arquitetura

Organização em camadas, com DTOs separados das entidades e serviços definidos por interface:

```
src/main/java/com/gtp/cityinclui/
├── config/        # Segurança (CORS, regras de acesso), S3, auditoria R2DBC, BCrypt
├── security/      # JwtUtil, JwtAuthenticationManager, JwtSecurityContextRepository
├── controller/    # Auth, Customer, RestaurantOwner, Review, Search
├── service/       # Interfaces e implementações (impl/)
├── repository/    # ReactiveCrudRepository e consultas SQL nativas
├── entity/        # Customer, RestaurantOwner, Review, Accessibility, PhotoRegister
├── dto/           # Objetos de entrada e saída por domínio
└── exception/     # Exceções de negócio e GlobalExceptionHandler
```

O fluxo é reativo de ponta a ponta (`Mono` e `Flux`), inclusive no acesso ao banco e no upload para o S3. A autenticação lê o header `Authorization: Bearer <token>` e monta as permissões a partir da claim `roles` do token.

## Modelo de dados

Tabelas esperadas, inferidas das entidades. O repositório não traz script de criação do banco, então confira contra o seu schema:

| Tabela | Colunas |
|---|---|
| `owner` | `id`, `nome_restaurante`, `nome_anunciante`, `cardapio`, `descricao`, `email`, `telefone`, `senha` (hash), `token_recuperacao`, `token_expiracao`, `data_cadastro` |
| `client` | `id`, `nome_completo`, `email`, `telefone`, `senha` (hash), `data_cadastro` |
| `photo` | `id`, `owner_id`, `url_photo` |
| `acessibilidade` | `id`, `owner_id`, `acessibilidades` |
| `avaliacao` | `id`, `owner_id`, `client_id`, `client_nome`, `nota`, `comentario`, `data_hora` |

## Endpoints

Base: `/cityinclui`, exceto a autenticação.

### Autenticação

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/auth/login` | Público | Corpo: `email`, `senha`, `userType` (`OWNER` ou `CLIENT`). Retorna `{ "token": "..." }` |
| POST | `/auth/logout` | Público | Limpa o cookie `auth_token` |

Nas demais rotas protegidas, envie `Authorization: Bearer <token>`.

### Anunciantes

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/cadastrar-anunciante` | Público | `multipart/form-data`: parte `owner` (JSON) e parte `photos` (1 ou mais imagens) |
| GET | `/restaurantes` | Público | Lista os restaurantes |
| GET | `/restaurante/{ownerId}` | Público | Detalhe com fotos, acessibilidades e avaliações |
| GET | `/restaurantes/top5` | Público | Os 5 com maior média de avaliação |
| GET | `/perfil-anunciante` | `OWNER` | Perfil do anunciante autenticado |
| PUT | `/editar-perfil` | `OWNER` | `multipart/form-data`: parte `owner` (JSON) e `photos` opcional (até 3) |
| DELETE | `/conta-delete` | `OWNER` | Exclui a conta, as fotos (inclusive no S3) e as acessibilidades |

### Clientes

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/cadastrar-cliente` | Público | Cadastra um cliente |
| GET | `/cliente/me` | `CLIENT` | Perfil do cliente autenticado |
| PUT | `/editar-cliente` | `CLIENT` | Atualiza nome e telefone |
| DELETE | `/delete-conta` | `CLIENT` | Exclui a conta |

### Avaliações

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/owner/{ownerId}/avaliacoes` | Público | Lista as avaliações do restaurante |
| POST | `/owner/{ownerId}/avaliacoes` | `CLIENT` | Cria avaliação (uma por cliente e restaurante) |
| PUT | `/owner/{ownerId}/avaliacoes/{avaliacaoId}` | `CLIENT` | Edita a própria avaliação |
| DELETE | `/owner/{ownerId}/avaliacoes/{avaliacaoId}` | `CLIENT` | Apaga a própria avaliação |

### Busca

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/pesquisa?q=termo&page=0&size=9` | Público | Busca por nome do restaurante ou cardápio, sem diferenciar maiúsculas, com paginação |

## Regras de validação

- Telefone: 10 ou 11 dígitos, somente números.
- Senha: mínimo de 8 caracteres.
- Nome do cliente: 3 a 50 caracteres.
- Descrição do restaurante no cadastro: 100 a 500 caracteres. É obrigatório informar ao menos um recurso de acessibilidade.
- Nota da avaliação: de 1 a 5.
- Fotos: apenas JPEG e PNG; pelo menos uma no cadastro e no máximo 3 na edição.
- Erros retornam `{ "message": "...", "status": 400 }`, com status HTTP adequado (400, 401, 404, 409, 500).

## Como executar

### Pré-requisitos

- JDK 21
- PostgreSQL com as tabelas acima criadas
- Bucket S3 e credenciais AWS com permissão de `PutObject` e `DeleteObject`

### Variáveis de ambiente

| Variável | Descrição |
|---|---|
| `SPRING_R2DBC_URL` | URL R2DBC, por exemplo `r2dbc:postgresql://localhost:5432/cityinclui` |
| `SPRING_R2DBC_USERNAME` | Usuário do banco |
| `SPRING_R2DBC_PASSWORD` | Senha do banco |
| `AWS_S3_REGION` | Região do bucket |
| `AWS_S3_ACCESS_KEY` | Chave de acesso AWS |
| `AWS_S3_SECRET_KEY` | Chave secreta AWS |
| `AWS_S3_BUCKET` | Nome do bucket |
| `JWT_SECRET` | Segredo de assinatura, com no mínimo 32 caracteres |
| `JWT_EXPIRATION_MS` | Validade do token em milissegundos |
| `ALLOWED_ORIGINS` | (opcional) Origens permitidas no CORS, separadas por vírgula. Padrão: `http://localhost:3000` |

Nunca versione esses valores.

### Rodando

```bash
git clone https://github.com/DevMiranda7/cityInclui-backEnd.git
cd cityInclui-backEnd

# defina as variáveis de ambiente (veja a tabela acima) e execute:
./mvnw spring-boot:run
```

Para gerar o pacote de deploy:

```bash
./mvnw clean package
```

O teste atual (`CityIncluiApplicationTests`) apenas carrega o contexto da aplicação, então `./mvnw test` exige as variáveis de ambiente e o banco disponíveis.

## Deploy

- **API:** AWS Elastic Beanstalk, com as variáveis de ambiente configuradas no ambiente.
- **Banco:** PostgreSQL no Aurora/RDS.
- **Imagens:** Amazon S3.
- **Front-end:** AWS Amplify, com domínio no Route 53.

## Autores

- Luan Miranda
- Emerson Leandro
- Felipe Wozniak
- Gabriel Pigozzi
- Gustavo Renner
- Daniel Tolledo
