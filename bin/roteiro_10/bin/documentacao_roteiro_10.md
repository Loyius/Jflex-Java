# Relatório de Análise Semântica — Roteiro 10 — Escopo de Variáveis

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro 10** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: Redeclaração de variáveis no mesmo escopo local (mesmo bloco) é proibida. Redeclarações em escopos diferentes (shadowing/sombreamento) são permitidas. Variáveis declaradas dentro de blocos aninhados só são visíveis dentro destes e deixam de existir após o encerramento do escopo do bloco.

### Descrição e Contexto:
A tabela de símbolos gerencia escopos aninhados via pilha. O início do bloco empilha uma nova tabela, e o encerramento desempilha a mesma. Redeclarações locais são verificadas na tabela do topo, enquanto a resolução de nomes busca de forma recursiva de cima para baixo na pilha.

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_10/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_10/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
program R10 {
  int global;      /* escopo global */

  void externa() {
    int local;     /* escopo da funcao */
    local = 5;     /* ok */
    global = 10;   /* ok: global visivel */
  }

  void teste() {
    int x;
    x = 1;
    {
      int y;
      y = 2;       /* ok: y no escopo interno */
      x = y;       /* ok: x visivel aqui */
    }
    x = y;         /* ERRO: y fora do escopo */
    int x;         /* ERRO: redeclaracao de x no mesmo escopo */
  }
}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
ERRO SEMANTICO (linha 19): variavel 'y' usada fora de escopo ou nao declarada
ERRO SEMANTICO (linha 20): variavel 'x' ja declarada neste escopo (nivel 2)
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro 10
cd c:\Users\Larissa\Programming_Languages\Jflex-Java\bin\roteiro_10\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
