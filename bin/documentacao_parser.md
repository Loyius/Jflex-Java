# Documentação do Compilador Java-- — Parser e Analisador Semântico

**Grupo**: Mayssa Barbosa Dias; Larissa Queiroz Ramos; Fernando Medeiros; Matheus Augusto  
**Curso**: 7º semestre CC — Turma "A"  
**Professor**: Alessandra Hauck

---

## 1. Descrição da Linguagem Suportada

A linguagem **Java--** é um subconjunto simplificado de Java, projetada para fins didáticos de compiladores. Suporta:

- Declaração de variáveis com tipos: `int`, `float`, `boolean`, `char`, `String`, `void`
- Declaração de funções com parâmetros tipados e retorno
- Estruturas de controle: `if/else`, `while`, `for`
- Comandos: atribuição, `print`, `read`, `return`
- Expressões: aritméticas (`+`, `-`, `*`, `/`, `%`), relacionais (`==`, `!=`, `<`, `>`, `<=`, `>=`), lógicas (`&&`, `||`, `!`)
- Comentários de bloco (`/* ... */`) e de linha (`//`)
- Literais: inteiros decimais e hexadecimais, floats, chars, strings, booleanos (`true`/`false`)

### Exemplo de programa válido

```java
program Exemplo {
  final int MAX = 100;
  int contador;
  float media;

  int fatorial(int n) {
    if (n <= 1) { return 1; }
    else        { return n * fatorial(n - 1); }
  }

  void main() {
    contador = 0;
    while (contador < MAX) {
      media = media + contador;
      contador = contador + 1;
    }
    print(media);
  }
}
```

---

## 2. Tabela de Tokens e Regras Léxicas

| Token | Lexemas (exemplos) | Expressão Regular |
|-------|-------------------|-------------------|
| `program` | `program` | literal |
| `final`/`class`/`void`/`if`/`else`/`while`/`for`/`return`/`read`/`print`/`new`/`true`/`false`/`null` | literais | literais |
| `int`/`float`/`boolean`/`char`/`String` | literais | literais |
| `IDENT` | `x`, `contador`, `media` | `[a-zA-Z_][a-zA-Z0-9_]*` |
| `INT` | `0`, `10`, `255` | `[0-9]+` |
| `INT_HEX` | `0xFF`, `0x1A` | `0x[0-9a-fA-F]+` |
| `FLOAT` | `3.14`, `0.5` | `[0-9]+\.[0-9]+` |
| `CHAR_CONST` | `'a'`, `'\n'` | `'([^\\'\n\r]|\\[btnr'\"\\])'` |
| `STRING` | `"ola"` | `"[^"\n]*"` |
| `==` `!=` `<=` `>=` `<` `>` | operadores relacionais | literais |
| `=` | atribuição | literal |
| `+` `-` | ADDOP | literais |
| `*` `/` `%` | MULOP | literais |
| `&&` `\|\|` `!` | operadores lógicos | literais |
| `;` `,` `.` `(` `)` `{` `}` `[` `]` | pontuação | literais |

### Erros Léxicos Detectados

| Padrão | Mensagem |
|--------|---------|
| `0X...` | hexadecimal invalido (use prefixo 0x minusculo) |
| `0x` sem dígitos | hexadecimal invalido (faltando digitos apos 0x) |
| `5.` | float invalido (faltando parte decimal) |
| `.5` | float invalido (faltando parte inteira) |
| Qualquer outro char | simbolo invalido |

---

## 3. Gramática BNF/EBNF Completa

```bnf
<programa>    ::= ("program" | "class") IDENT "{" <corpo> "}"
                | <corpo>

<corpo>       ::= (<func_decl> | <var_decl>)*

<var_decl>    ::= ["final"] <tipo> IDENT ["=" <expr>]
                  {"," IDENT ["=" <expr>]} ";"

<func_decl>   ::= <tipo> IDENT "(" [<params>] ")" <bloco>

<params>      ::= <tipo> IDENT {"," <tipo> IDENT}

<tipo>        ::= "int" | "float" | "boolean" | "char" | "String" | "void" | IDENT

<bloco>       ::= "{" <stmt>* "}"

<stmt>        ::= <var_decl>
                | IDENT "=" <expr> ";"
                | IDENT "[" <expr> "]" "=" <expr> ";"
                | IDENT "(" [<args>] ")" ";"
                | "if" "(" <expr> ")" <stmt> ["else" <stmt>]
                | "while" "(" <expr> ")" <stmt>
                | "for" "(" [<for_init>] ";" [<expr>] ";" [<for_update>] ")" <stmt>
                | "return" [<expr>] ";"
                | "print" "(" [<args>] ")" ";"
                | "read" "(" IDENT ")" ";"
                | <bloco>
                | ";"

<for_init>    ::= <tipo> IDENT "=" <expr> | IDENT "=" <expr>
<for_update>  ::= IDENT "=" <expr>

<expr>        ::= <expr_or>
<expr_or>     ::= <expr_and> {"||" <expr_and>}
<expr_and>    ::= <expr_rel> {"&&" <expr_rel>}
<expr_rel>    ::= <expr_add> [("==" | "!=" | "<" | ">" | "<=" | ">=") <expr_add>]
<expr_add>    ::= <expr_mul> {("+" | "-") <expr_mul>}
<expr_mul>    ::= <expr_unary> {("*" | "/" | "%") <expr_unary>}
<expr_unary>  ::= ["-" | "!"] <expr_prim>
<expr_prim>   ::= INT | INT_HEX | FLOAT | CHAR_CONST | STRING | "true" | "false" | "null"
                | IDENT ["(" [<args>] ")" | "[" <expr> "]"]
                | "(" <expr> ")"
                | "new" <tipo> ["(" [<args>] ")" | "[" <expr> "]"]

<args>        ::= <expr> {"," <expr>}
```

---

## 4. Instrução de Compilação e Execução

### Pré-requisitos

- **Java JDK 8+** instalado e no PATH
- **JFlex** (opcional, para regenerar Scanner.java a partir de Scanner.flex)
- **JCup** (opcional, para gerar parser a partir de Parser.cup)

### Entrega 1 — Parser

```powershell
# Compilar
cd bin
javac *.java

# Executar sobre entrada.txt
java Main entrada.txt

# Saída é gravada automaticamente em saida.txt
```

### Entrega 2 — Roteiros Semânticos (N = 1 a 10)

```powershell
cd bin\roteiro_N\bin
javac *.java
java Main entrada.txt
# Saída em saida.txt
```

### Regerar Scanner.java com JFlex (opcional)

```bash
# Copie o Scanner.flex para a pasta bin/ e execute:
java -jar jflex-full-1.9.1.jar Scanner.flex
```

### Gerar parser com JCup (opcional)

```bash
java -jar java-cup.jar -parser parser -symbols sym Parser.cup
```

---

## 5. Exemplo de Entrada e Saída Comentados

### Entrada (`bin/entrada.txt`)

```java
program Exemplo {
  final int MAX = 100;
  int contador;
  ...
  int fatorial(int n) {
    if (n <= 1) { return 1; }
    else { return n * fatorial(n - 1); }
  }
}
```

### Saída (`bin/saida.txt`)

```
=== Compilador Java-- | Análise Léxica + Sintática ===
Entrada : entrada.txt  |  Saída: saida.txt

--- FASE 1: ANÁLISE LÉXICA ---
<program, program, linha 1>
<IDENT, Exemplo, linha 1>
<{, {, linha 1>
<final, final, linha 3>
<int, int, linha 3>
<IDENT, MAX, linha 3>
<= , =, linha 3>
<INT, 100, linha 3>
<;, ;, linha 3>
...

Tokens reconhecidos : 201
Erros léxicos       : 0

--- FASE 2: ANÁLISE SINTÁTICA ---

Erros sintáticos    : 0

>>> Análise sintática concluída com sucesso <<<
```

### Exemplo de Erro Léxico e Sintático

Dado `x = 0XFF;` e `if x > 0`:

```
ERRO LEXICO na linha 3, col 5: hexadecimal invalido (use prefixo 0x minusculo): 0XFF
ERRO SINTATICO (linha 5): esperado '(', mas encontrado 'x'
```

---

## 6. Análise Semântica — Roteiros 1 a 10

| Roteiro | Regra Implementada | Arquivo |
|---------|-------------------|---------|
| 1 | Variável declarada antes do uso | `roteiro_1/bin/` |
| 2 | Compatibilidade de tipos em atribuições | `roteiro_2/bin/` |
| 3 | Operandos aritméticos devem ser numéricos | `roteiro_3/bin/` |
| 4 | Tipos compatíveis em expressões relacionais | `roteiro_4/bin/` |
| 5 | Operandos de `&&`/`\|\|`/`!` devem ser booleanos | `roteiro_5/bin/` |
| 6 | Condição de `if` deve ser booleana | `roteiro_6/bin/` |
| 7 | Condição de `while` deve ser booleana | `roteiro_7/bin/` |
| 8 | Chamada de função com aridade correta | `roteiro_8/bin/` |
| 9 | Tipo de retorno compatível com declaração | `roteiro_9/bin/` |
| 10 | Escopo de variáveis (local vs global) | `roteiro_10/bin/` |

### Tabela de Símbolos (`SymbolTable.java`)

Suporta escopos aninhados via pilha de mapas. Métodos principais:

| Método | Descrição |
|--------|-----------|
| `enterScope()` | Abre novo escopo (bloco/função) |
| `exitScope()` | Fecha escopo atual |
| `declararVar(nome, tipo, linha)` | Registra variável; retorna false se redeclarada |
| `declararFuncao(nome, retorno, params, linha)` | Registra função |
| `buscar(nome)` | Busca do escopo mais interno ao mais externo |
| `isDeclared(nome)` | Verifica se declarado em qualquer escopo visível |
| `getType(nome)` | Retorna tipo do símbolo |

---

## 7. Dificuldades Encontradas e Decisões de Projeto

### 7.1 Ausência do JCup JAR
O `java-cup.jar` não estava disponível no projeto. **Decisão**: implementar o parser como Recursive Descent em Java puro, mantendo o `Parser.cup` como especificação formal da gramática (pode ser compilado pelo aluno quando o JAR estiver disponível).

### 7.2 Loop Infinito no Parser (recuperação de erros)
O primeiro protótipo do parser entrava em loop infinito ao encontrar `else` após um `if` aninhado, porque `esperar()` não consumia o token em caso de erro. **Solução**: `esperar()` registra o erro mas não consome; `parseBloco()` tem guarda de progresso forçado (`if (atual() == antes) consumir()`).

### 7.3 Ambiguidade if/else (dangling else)
Tratamos o `else` sempre associado ao `if` mais próximo, conforme a regra padrão de Java (greedy matching no `match(sym.KW_ELSE)`).

### 7.4 Análise Semântica por Passagem de Tokens
Os analisadores semânticos operam sobre a lista de tokens (não sobre uma AST), o que é uma simplificação. Isso significa que alguns casos de uso aninhado complexo podem não ser detectados. Para um compilador completo, o ideal seria construir uma AST durante a análise sintática.

### 7.5 Tipos de Usuário (IDENT como tipo)
A linguagem permite usar identificadores como tipos (ex: `MinhaClasse obj;`). O parser aceita isso; a verificação semântica de tipos de usuário está fora do escopo dos roteiros 1-10.

---

## 8. Estrutura de Arquivos

```
bin/
├── Scanner.flex          — especificação JFlex do scanner
├── Scanner.java          — scanner integrado (retorna Token)
├── sym.java              — constantes de token
├── Token.java            — classe Token
├── parser.java           — parser Recursive Descent
├── Parser.cup            — gramática formal (notação JCup)
├── SymbolTable.java      — tabela de símbolos (escopos)
├── Main.java             — orquestrador Léxica + Sintática
├── entrada.txt           — código Java-- de teste
├── saida.txt             — gerado automaticamente
├── roteiro_1/bin/        — Semântica: Declaração e uso
├── roteiro_2/bin/        — Semântica: Tipos em atribuições
├── roteiro_3/bin/        — Semântica: Expressões aritméticas
├── roteiro_4/bin/        — Semântica: Expressões relacionais
├── roteiro_5/bin/        — Semântica: Expressões lógicas
├── roteiro_6/bin/        — Semântica: Condição if/else
├── roteiro_7/bin/        — Semântica: Condição while
├── roteiro_8/bin/        — Semântica: Chamada de funções
├── roteiro_9/bin/        — Semântica: Tipo de retorno
└── roteiro_10/bin/       — Semântica: Escopo de variáveis

Raiz do projeto:
├── Scanner.flex          — scanner standalone (para uso com JFlex)
├── Scanner.java          — scanner standalone (gerado pelo JFlex)
├── entrada.txt           — teste do scanner standalone
└── saida_entrada.txt     — saída do scanner standalone
```

---

## 9. Referências

1. Aho, A. V., Lam, M. S., Sethi, R., Ullman, J. D. *Compiladores: Princípios, Técnicas e Ferramentas* (Livro do Dragão). 2ª edição.
2. Documentação JFlex: [http://jflex.de](http://jflex.de)
3. Documentação Java CUP: [http://www2.cs.tum.edu/projects/cup/](http://www2.cs.tum.edu/projects/cup/)
4. PDF do trabalho: "Trabalho Prático: Analisador Léxico/Sintático/Semântico — Compiladores", Prof. Alessandra Hauck.
