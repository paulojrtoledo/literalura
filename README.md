# 📚 LiterAlura - Catálogo de Livros

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15%2B-blue)
![License](https://img.shields.io/badge/license-MIT-green)

## 📋 Sobre o Projeto

**LiterAlura** é uma aplicação de console desenvolvida em Java com Spring Boot que consome a API [Gutendex](https://gutendex.com/) (do Projeto Gutenberg) para buscar livros e armazená-los em um banco de dados PostgreSQL. O projeto foi desenvolvido como parte do desafio da formação **ONE - Oracle Next Education** em parceria com a Alura.

### ✨ Funcionalidades

- ✅ **Buscar livro por título** - Consulta a API Gutendex e salva no banco de dados
- ✅ **Listar livros registrados** - Exibe todos os livros salvos no banco
- ✅ **Listar autores registrados** - Mostra todos os autores e seus livros
- ✅ **Listar autores vivos em determinado ano** - Filtra autores que estavam vivos em um ano específico
- ✅ **Listar livros por idioma** - Filtra livros por idioma (pt, en, fr, es)

### 🚀 Funcionalidades Extras

- ✅ **Busca flexível** - Aceita acentos, maiúsculas/minúsculas e busca parcial
- ✅ **Lista de resultados** - Mostra múltiplos resultados para escolha antes de salvar
- ✅ **Detalhamento completo** - Visualização detalhada de livros e autores
- ✅ **Prevenção de duplicidade** - Não salva livros repetidos

## 🛠️ Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 4.0.3**
  - Spring Data JPA
  - Spring Web (para consumo da API)
- **PostgreSQL** - Banco de dados relacional
- **Maven** - Gerenciamento de dependências
- **Gutendex API** - Fonte de dados dos livros

## 📦 Estrutura do Projeto

src/main/java/br/com/alura/literalura/
├── dto/ # Data Transfer Objects
│ ├── AutorDTO.java
│ ├── LivroDTO.java
│ └── GutendexResponse.java
├── model/ # Entidades JPA
│ ├── Autor.java
│ └── Livro.java
├── repository/ # Repositórios Spring Data
│ ├── AutorRepository.java
│ └── LivroRepository.java
├── service/ # Lógica de negócio
│ ├── GutendexService.java
│ └── LivroService.java
├── principal/ # Classe principal com menu
│ └── Principal.java
└── LiteraluraApplication.java


## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- PostgreSQL 15 ou superior
- Maven (opcional - pode usar o wrapper incluso)

### Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE literalura;

spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

Executando a Aplicação
Com Maven Wrapper (recomendado):
# No Linux/Mac
./mvnw spring-boot:run
# No Windows
mvnw.cmd spring-boot:run
mvn spring-boot:run

📖 Como Usar
Ao iniciar a aplicação, você verá o menu interativo:

--- LITERALURA - CATÁLOGO DE LIVROS ---
1 - Buscar livro pelo título
2 - Listar livros registrados
3 - Listar autores registrados
4 - Listar autores vivos em um determinado ano
5 - Listar livros por idioma
0 - Sair
Escolha uma opção:

Exemplo de Busca
Escolha a opção 1

Digite: memorias (ou "Dom Casmurro", "cortiço", etc.)

A aplicação mostra todos os livros encontrados

Escolha o número do livro que deseja salvar

Pronto! O livro está no seu banco de dados

🧪 Testando com Livros Clássicos
Título	Autor	Dica de Busca
Dom Casmurro	Machado de Assis	"dom casmurro", "casmurro"
Memórias Póstumas de Brás Cubas	Machado de Assis	"memorias"
O Cortiço	Aluísio Azevedo	"cortico"
Pride and Prejudice	Jane Austen	"pride", "jane"
Frankenstein	Mary Shelley	"frankenstein"
Sherlock Holmes	Arthur Conan Doyle	"sherlock"

📝 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

👨‍💻 Autor
Desenvolvido por Paulo Roberto Toledo como parte do desafio LiterAlura da ONE - Oracle Next Education em parceria com a Alura.

⭐️ Se este projeto te ajudou, deixe uma estrela no GitHub! ⭐️


## 📸 **Imagens sugeridas para adicionar (opcional):**

Você pode adicionar screenshots do projeto na pasta `assets/` e incluí-las no README:

```markdown
## 📸 Screenshots

![Menu Principal](assets/menu.png)
![Busca de Livros](assets/busca.png)
![Resultados](assets/resultados.png)

🚀 Como adicionar o README ao repositório:

# Crie o arquivo README.md
# Cole o conteúdo acima
git add README.md
git commit -m "docs: add README"
git push



