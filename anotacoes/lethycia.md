# Documentação dos Primeiros Componentes do Sistema

Este documento descreve as principais classes e interfaces implementadas no projeto, com o objetivo de facilitar a compreensão e colaboração de novos desenvolvedores. O sistema gerencia playlists, músicas e artistas, utilizando estruturas de dados personalizadas (pilha, fila, lista encadeada, heap, tabela hash e árvore AVL) para garantir eficiência nas operações.

## Pacote `model`

Contém as classes de domínio do sistema, representando os objetos principais e suas regras de negócio.

### `Playlist.java`

**Propósito:** Representa os metadados de uma playlist (nome e descrição).

**Responsabilidades:**

- Armazenar nome (obrigatório, não vazio) e descrição (opcional).
- Ser imutável: após criada, o nome e a descrição não podem ser alterados.
- A fila interna de reprodução (ordem das músicas) não é gerenciada por esta classe, mas pelo `RepositorioPlaylists` (separação de responsabilidades).

**Detalhes de implementação:**

- Construtor valida nome não nulo/vazio e aplica `trim()`.
- Se a descrição for nula, é substituída por string vazia.
- `equals` e `hashCode` baseiam-se apenas no nome (identificador único).
- `toString` exibe nome e descrição, indicando quando está vazia.

**Observações para colaboradores:**

- Mantenha a imutabilidade: não adicione setters.
- Se precisar de outros metadados (ex.: imagem, data de criação), crie novos atributos finais e ajuste construtores.

### `Musica.java`

**Propósito:** Representa uma música no sistema, com título, artista, gênero, duração e contador de reproduções.

**Responsabilidades:**

- Armazenar informações fixas da música.
- Controlar o número de vezes que foi reproduzida (`plays`).
- Fornecer ordenação natural por título (e artista como critério de desempate) para uso na árvore AVL.
- Formatar duração no formato `mm:ss`.

**Detalhes de implementação:**

- Atributos finais, exceto `plays` (mutável).
- Construtor valida título, artista e duração; gênero pode ser nulo (substituído por `"Desconhecido"`).
- `compareTo` ordena por título (case-insensitive) e depois por artista – não considera `plays`, pois o ranking por plays é feito via `IHeap` com `Comparator` externo.
- `equals` e `hashCode` baseiam-se em título e artista (case-insensitive), garantindo que músicas diferentes com mesmo título e artista sejam consideradas iguais.
- `incrementarPlays` aumenta o contador.

**Observações para colaboradores:**

- A ordenação por plays deve ser feita com uma `IHeap` (max-heap) usando um `Comparator` que compare por `getPlays()`.
- Cuidado ao modificar `equals`/`hashCode`: eles são usados em coleções como `HashSet` e como chave em mapas.

### `Artista.java`

**Propósito:** Representa um artista, contendo apenas o nome.

**Responsabilidades:**

- Armazenar nome do artista (imutável).
- Ser usado como objeto de valor, principalmente para exibição.

**Detalhes de implementação:**

- Construtor valida nome não nulo/vazio e aplica `trim()`.
- `equals` e `hashCode` baseiam-se no nome.
- Importante: a `TabelaHash` (ver interface `IHash`) utiliza `String` como chave, não o objeto `Artista`. Isso evita problemas com implementação de `hashCode()` e simplifica a busca.

**Observações para colaboradores:**

- Esta classe é simples, mas pode ser estendida futuramente (ex.: adicionar biografia, país). Mantenha a imutabilidade.
- Em listas de artistas, considere usar `ArrayList<Artista>` para exibição, mas indexe músicas por `String` na árvore AVL.

## Pacote `interfaces`

Define os contratos das estruturas de dados personalizadas. Todas são genéricas e devem ser implementadas conforme a necessidade do sistema.

### `IPilha<T>`

**Propósito:** Interface para uma pilha (LIFO – Last In, First Out).

**Métodos:**

- `void push(T elemento)` – insere no topo.
- `T pop()` – remove e retorna o elemento do topo; lança `NoSuchElementException` se vazia.
- `T peek()` – retorna o elemento do topo sem remover; lança `NoSuchElementException` se vazia.
- `boolean isEmpty()` – verifica se está vazia.
- `int size()` – retorna a quantidade de elementos.

**Uso sugerido no sistema:** Histórico de reprodução (botão "voltar").

**Implementação esperada:** Pode ser baseada em lista encadeada ou array.

### `ILista<T>`

**Propósito:** Interface para uma lista encadeada (duplamente encadeada, preferencialmente).

**Métodos:**

- `void addFirst(T elemento)` – insere no início.
- `void addLast(T elemento)` – insere no final.
- `T removeFirst()` – remove e retorna o primeiro; lança `NoSuchElementException` se vazia.
- `T removeLast()` – remove e retorna o último; lança `NoSuchElementException` se vazia.
- `T get(int index)` – retorna o elemento na posição `index` (0-based); lança `IndexOutOfBoundsException`.
- `boolean remove(T elemento)` – remove a primeira ocorrência do elemento; retorna `true` se removeu.
- `boolean isEmpty()` – verifica se está vazia.
- `int size()` – retorna a quantidade de elementos.

**Uso sugerido:** Estrutura base para outras implementações (pilha, fila) e para listagem ordenada.

**Implementação esperada:** Classe com nós internos, mantendo referências para `head` e `tail`.

### `IHeap<T>`

**Propósito:** Interface para uma max-heap (priority queue onde o maior elemento tem prioridade).

**Métodos:**

- `T extrairMax()` – remove e retorna o maior elemento; lança `NoSuchElementException` se vazia.
- `T peek()` – retorna o maior elemento sem remover; lança `NoSuchElementException` se vazia.
- `boolean isEmpty()` – verifica se está vazia.
- `int size()` – retorna a quantidade de elementos.

**Uso sugerido no sistema:** Ranking de músicas por número de plays. A ordenação deve ser definida externamente (via `Comparator<T>` ou pela ordem natural de `T`, mas cuidado: `Musica` não ordena por plays naturalmente).

**Observações:** A interface não expõe métodos de inserção – a implementação deve prover um método para adicionar elementos (ex.: `inserir(T elemento)`). A definição exata pode variar; considere estender esta interface se necessário.

### `IHash<K, V>`

**Propósito:** Interface para uma tabela hash com encadeamento (*chaining*) para tratamento de colisões.

**Métodos:**

- `void put(K chave, V valor)` – insere ou atualiza o valor associado à chave.
- `V get(K chave)` – retorna o valor associado à chave, ou `null` se não existir.
- `boolean remove(K chave)` – remove a entrada associada à chave; retorna `true` se existia.
- `boolean containsKey(K chave)` – verifica se a chave está presente.
- `int size()` – retorna o número de entradas.

**Uso sugerido no sistema:** Busca rápida por artista ou gênero (usando `String` como chave) em tempo O(1) médio.

**Observações:** A implementação deve lidar com colisões usando listas encadeadas em cada *bucket*. O fator de carga e redimensionamento podem ser considerados para otimização.

### `IFila<T>`

**Propósito:** Interface para uma fila (FIFO – First In, First Out).

**Métodos:**

- `void enqueue(T elemento)` – insere no final.
- `T dequeue()` – remove e retorna o elemento da frente; lança `NoSuchElementException` se vazia. *(Nota: o texto original menciona apenas `peek`, mas recomenda adicionar `dequeue` para consistência. Incluímos ambos.)*
- `T peek()` – retorna o elemento da frente sem remover; lança `NoSuchElementException` se vazia.
- `boolean isEmpty()` – verifica se está vazia.
- `int size()` – retorna a quantidade de elementos.

**Uso sugerido no sistema:** Playlist de reprodução – as músicas são adicionadas ao final e tocadas na ordem de chegada.

### `IArvore<T extends Comparable<T>>`

**Propósito:** Interface para uma árvore de busca binária balanceada (AVL).

**Métodos:**

- `void inserir(T elemento)` – insere o elemento na árvore.
- `boolean buscar(T elemento)` – verifica se o elemento está presente.
- `void remover(T elemento)` – remove o elemento da árvore.
- `ILista<T> inOrder()` – retorna uma lista com os elementos em ordem crescente (percurso *in-order*).
- `boolean isEmpty()` – verifica se a árvore está vazia.

**Uso sugerido no sistema:** Indexação de músicas por título, permitindo busca em O(log n) e listagem alfabética.

**Observações:** O tipo `T` deve implementar `Comparable` para definir a ordem. A implementação deve garantir balanceamento (AVL) após inserções e remoções. O método `inOrder` retorna uma `ILista` para facilitar a iteração.