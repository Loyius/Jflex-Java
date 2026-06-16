# Relatório de Análise Semântica — Roteiro 7 — Condição while booleana

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro 7** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: A expressão de condição de controle de uma estrutura de repetição 'while' deve resultar estritamente no tipo 'boolean'.

### Descrição e Contexto:
Semelhante ao roteiro anterior, o analisador semântico valida a expressão condicional do laço de repetição. Se o tipo avaliado for numérico, a análise acusa falha semântica.

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_7/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_7/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
program R7 {
  int i;
  boolean continua;

  void teste() {
    i = 0;
    continua = true;
    while (i < 10) { i = i + 1; }  /* ok */
    while (continua) { i = i + 1; } /* ok */
    while (i) { i = i + 1; }       /* ERRO: int nao e boolean */
  }
}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
ERRO SEMANTICO (linha 11): condicao do 'while' deve ser booleana
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro 7
cd c:\Users\Larissa\Programming_Languages\Jflex-Java\bin\roteiro_7\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
