# Relatório de Análise Semântica — Roteiro 4 — Expressões Relacionais

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro 4** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: Os operandos envolvidos em operadores relacionais (==, !=, <, >, <=, >=) devem possuir tipos compatíveis. É permitida a comparação entre valores numéricos (int/float), bem como comparação direta entre booleanos. Outras combinações mistas (como boolean vs int) geram erro semântico.

### Descrição e Contexto:
O analisador verifica os tipos dos dois operandos comparados. Se um for booleano e o outro for numérico, emite erro de incompatibilidade de tipos relacionais.

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_4/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_4/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
program R4 {
  int x;
  float y;
  boolean b;

  void teste() {
    x = 5;
    y = 2.0;
    b = true;
    if (x > y) { print(x); }        /* ok: int vs float */
    if (x == y) { print(x); }       /* ok: numericos */
    if (b == x) { print(b); }       /* ERRO: boolean vs int */
  }
}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
ERRO SEMANTICO (linha 13): operandos do operador '==' possuem tipos incompativeis: boolean e int
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro 4
cd c:\Users\Larissa\Programming_Languages\Jflex-Java\bin\roteiro_4\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
