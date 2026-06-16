# Relatório de Análise Semântica — Roteiro 6 — Condição if/else booleana

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro 6** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: A expressão fornecida como condição em um comando 'if' deve resultar obrigatoriamente no tipo 'boolean'.

### Descrição e Contexto:
O analisador infere o tipo da expressão contida na expressão de controle do 'if'. Se o tipo resolvido não for booleano, reporta-se o erro semântico de condição inválida.

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_6/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_6/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
program R6 {
  int x;
  boolean ativo;

  void teste() {
    x = 5;
    ativo = true;
    if (x > 0) { print(x); }      /* ok: relacional boolean */
    if (ativo) { print(ativo); }  /* ok: variavel boolean */
    if (x) { print(x); }          /* ERRO: int nao e boolean */
    if (x + 1) { print(x); }      /* ERRO: aritmetica nao e boolean */
  }
}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
ERRO SEMANTICO (linha 11): condicao do 'if' deve ser booleana (linha 11)
ERRO SEMANTICO (linha 12): condicao do 'if' deve ser booleana (linha 12)
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro 6
cd c:\Users\Larissa\Programming_Languages\Jflex-Java\bin\roteiro_6\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
