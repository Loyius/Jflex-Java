# Relatório de Análise Semântica — Roteiro 2 — Compatibilidade de Tipos em Atribuições

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro 2** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: O tipo do valor atribuído a uma variável deve ser compatível com o tipo declarado da variável. Atribuição de 'float' para 'int' é inválida (perda de precisão), ao passo que 'float <- int' é permitida (promoção implícita). Variáveis do tipo 'boolean' só aceitam valores booleanos.

### Descrição e Contexto:
Ao encontrar o operador de atribuição '=', o analisador semântico infere o tipo da expressão à direita (através de literais e tipos das variáveis associadas) e valida se esse tipo pode ser atribuído ao tipo declarado da variável à esquerda.

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_2/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_2/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
program R2 {
  int x;
  float y;
  boolean b;

  void teste() {
    x = 10;       /* ok: int <- int */
    y = 3.14;     /* ok: float <- float */
    y = x;        /* ok: float <- int (promocao) */
    x = 3.14;     /* ERRO: int <- float (perda de precisao) */
    b = 1;        /* ERRO: boolean <- int */
    b = true;     /* ok */
  }
}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
ERRO SEMANTICO (linha 11): incompatibilidade de tipo na atribuicao para 'x': esperado int, encontrado float
ERRO SEMANTICO (linha 12): incompatibilidade de tipo na atribuicao para 'b': esperado boolean, encontrado int
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro 2
cd c:\Users\Larissa\Programming_Languages\Jflex-Java\bin\roteiro_2\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
