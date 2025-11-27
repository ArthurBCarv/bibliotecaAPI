# BibliotecaAPI

API REST para gerenciamento de biblioteca desenvolvida com Spring Boot.

## 📋 Descrição

Sistema de gerenciamento de biblioteca que permite cadastrar autores, livros e críticas. A API oferece operações CRUD completas para todas as entidades.

## 🚀 Tecnologias

- **Java 23**
- **Spring Boot 3.3.4**
- **Spring Data JPA**
- **SQLite** (banco de dados)
- **Bean Validation** (validações)
- **Swagger/OpenAPI** (documentação)
- **Gradle** (gerenciamento de dependências)

## 📦 Estrutura do Projeto

```
src/main/java/br/com/biblioteca/
├── controller/       # Endpoints REST
├── service/          # Lógica de negócio
├── repository/       # Acesso ao banco de dados
├── model/            # Entidades JPA
├── dto/              # Objetos de transferência de dados
├── exception/        # Tratamento de exceções
└── config/           # Configurações
```

## 🔧 Entidades

### Autor
- `id`: Identificador único
- `nome`: Nome do autor (obrigatório)
- `biografia`: Biografia do autor (obrigatório)

### Livro
- `id`: Identificador único
- `titulo`: Título do livro (obrigatório)
- `isbn`: ISBN do livro (obrigatório, formato: XXX-X-XX-XXXXXX-X)
- `editora`: Editora (obrigatório)
- `dataPublicacao`: Data de publicação (não pode ser futura)
- `autores`: Lista de autores (mínimo 1)
- `mediaAvaliacoes`: Média das avaliações

### Critica
- `id`: Identificador único
- `conteudo`: Conteúdo da crítica (obrigatório, 10-1000 caracteres)
- `nota`: Nota de 1 a 5 (obrigatório)
- `usuario`: Nome do usuário (obrigatório)
- `dataCriacao`: Data de criação
- `livro`: Livro relacionado (obrigatório)

## 🎯 Endpoints Principais

### Autores
- `GET /autores` - Lista todos os autores
- `GET /autores/{id}` - Busca autor por ID
- `POST /autores` - Cria novo autor
- `PUT /autores/{id}` - Atualiza autor
- `DELETE /autores/{id}` - Remove autor

### Livros
- `GET /livros` - Lista todos os livros
- `GET /livros/{id}` - Busca livro por ID
- `POST /livros` - Cria novo livro
- `PUT /livros/{id}` - Atualiza livro
- `DELETE /livros/{id}` - Remove livro

### Críticas
- `GET /criticas/{id}` - Busca crítica por ID
- `POST /criticas` - Cria nova crítica
- `PUT /criticas/{id}` - Atualiza crítica
- `DELETE /criticas/{id}` - Remove crítica
- `GET /criticas/livro/{livroId}` - Lista críticas de um livro

## ⚙️ Como Executar

### Pré-requisitos
- Java 23 ou superior
- Gradle (ou usar o wrapper incluído)

## 📚 Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## ✅ Validações

A API implementa validações nos DTOs:

- **Campos obrigatórios**: `@NotBlank`, `@NotNull`, `@NotEmpty`
- **Tamanho de texto**: `@Size`
- **Valores numéricos**: `@Min`, `@Max`
- **Datas**: `@PastOrPresent`
- **Formato**: `@Pattern` (ISBN)

## 🗄️ Banco de Dados

O projeto utiliza SQLite com arquivo `biblioteca.db` criado automaticamente na raiz do projeto. O Hibernate está configurado para atualizar o schema automaticamente (`ddl-auto=update`).

