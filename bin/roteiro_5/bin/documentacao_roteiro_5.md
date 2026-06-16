# Relatório de Análise Semântica — Roteiro 5 — Expressões Lógicas

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro 5** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: Os operadores lógicos binários (&&, ||) e o operador unário de negação (!) requerem operandos estritamente do tipo 'boolean'.

### Descrição e Contexto:
Durante a resolução semântica dos termos lógicos, o compilador verifica se as expressões avaliadas à esquerda, à direita, ou sob a negação lógica resultam em booleanos. O uso de tipos numéricos nessas operações dispara erros semânticos.

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_5/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_5/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
program R5 {
  boolean a;
  boolean b;
  int x;

  void teste() {
    a = true;
    b = false;
    x = 1;
    if (a && b) { print(a); }       /* ok */
    if (a || b) { print(b); }       /* ok */
    if (x && b) { print(x); }      /* ERRO: int em operacao logica */
    if (!a) { print(a); }           /* ok */
    if (!x) { print(x); }           /* ERRO: int em negacao logica */
  }
}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
ERRO SEMANTICO (linha 13): operando do operador '&&' deve ser booleano
ERRO SEMANTICO (linha 15): operando do operador '!' deve ser booleano
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro 5
cd c:\Users\Larissa\Programming_Languages\Jflex-Java\bin\roteiro_5\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
