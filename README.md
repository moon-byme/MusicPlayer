# Reprodutor de Música EDA

Projeto acadêmico desenvolvido em Java com foco na aplicação prática de estruturas de dados em um sistema de reprodução musical. O sistema possui catálogo de músicas, busca por título e artista, fila de reprodução, histórico, ranking de músicas mais tocadas e interface gráfica em JavaFX.

## Objetivo

O objetivo do projeto é demonstrar como estruturas de dados clássicas podem ser aplicadas em um problema real, organizando e manipulando informações de um reprodutor musical de forma eficiente.

## Funcionalidades

- Listagem completa do catálogo de músicas
- Busca de músicas por título
- Listagem de músicas por artista
- Adição de músicas à fila de reprodução
- Reprodução da próxima música da fila
- Retorno para músicas anteriores por histórico
- Exibição do ranking de músicas mais tocadas
- Interface gráfica com JavaFX
- Execução também em modo terminal

## Estruturas de Dados Utilizadas

- Árvore AVL: organização e busca eficiente do catálogo em ordem alfabética
- Fila: controle da ordem de reprodução das músicas
- Pilha: armazenamento do histórico de músicas reproduzidas
- Heap binária máxima: ranking de músicas mais tocadas
- Hash: agrupamento e busca de músicas por artista
- Lista encadeada: apoio em operações internas das estruturas

## Tecnologias

- Java 21
- Maven
- JavaFX 21
- JUnit 5

## Organização do Projeto

- src/main/java/interfaces: contratos das estruturas de dados
- src/main/java/structures: implementações das estruturas
- src/main/java/model: entidades de domínio, como música e artista
- src/main/java/reproducer: lógica central do reprodutor
- src/main/java/ui: interface gráfica e controlador JavaFX
- src/main/java/main: execução em modo terminal
- src/main/resources/fxml: layout da interface gráfica
- src/test/java: testes automatizados das estruturas

## Como Executar

### Pré-requisitos

- Java 21 instalado
- Maven configurado no ambiente

### Executar a interface gráfica

```bash
mvn javafx:run
```

### Executar os testes

```bash
mvn test
```

### Compilar o projeto

```bash
mvn compile
```

### Executar em modo terminal

```bash
mvn exec:java
```

Observação: em alguns ambientes Windows, a entrada acentuada no terminal pode depender da configuração do console. Para apresentação, a interface gráfica é a forma mais estável de demonstração.

## Testes

O projeto possui testes automatizados para as principais estruturas de dados:

- ArvoreAVLTest
- FiladeReproducaoTest
- HashArtistasTest
- HeapBinariaTest
- HistoricoMusicasTest
- PilhaTest

No estado atual do repositório, a suíte executa com sucesso via Maven.

## Destaques da Interface Gráfica

A aplicação JavaFX permite:

- Navegação por músicas, artistas, fila, histórico e ranking
- Busca por título ou artista
- Exibição de cards com informações das músicas
- Barra inferior de reprodução com controles e progresso
- Integração com arquivos de áudio e imagens dos artistas

## Estrutura de Execução

O projeto oferece duas formas principais de uso:

- Interface gráfica, por meio da classe MusicPlayerUI
- Interface em terminal, por meio da classe Main

## Contexto Acadêmico

Este projeto foi desenvolvido como atividade prática da disciplina de Estruturas de Dados, com ênfase em modelagem, eficiência de busca, organização de dados e integração entre lógica de negócio e interface.

## Autoria

Projeto desenvolvido em grupo.

Sugestão para completar esta seção:

- Nome dos integrantes
- Responsabilidades de cada integrante
- Turma e disciplina

## Possíveis Melhorias Futuras

- Melhorar a tolerância da busca a erros de digitação
- Expandir o catálogo com persistência em arquivo ou banco de dados
- Adicionar playlists personalizadas salvas
- Melhorar a compatibilidade de terminal em diferentes sistemas
- Refinar feedback visual da interface gráfica
