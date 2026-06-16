# Relatório de Análise Semântica — Roteiro 9 — Tipo de Retorno de Funções

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro 9** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: O tipo da expressão em qualquer comando 'return <expr>' dentro de uma função deve ser compatível com o tipo de retorno declarado na assinatura dessa função. Se a função for declarada como 'void', ela não pode retornar expressão de valor.

### Descrição e Contexto:
O analisador rastreia a função atualmente sob compilação. Ao encontrar a instrução 'return', ele avalia a expressão associada e valida o tipo contra o tipo de retorno declarado na assinatura da função corrente.

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_9/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_9/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
program R9 {
  int retornaInt() {
    return 42;       /* ok: int <- int */
  }

  float retornaFloat() {
    return 3.14;     /* ok: float <- float */
  }

  int errado() {
    return 3.14;     /* ERRO: int <- float */
  }

  void semRetorno() {
    return 1;        /* ERRO: void nao deve retornar valor */
  }
}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
ERRO SEMANTICO (linha 12): funcao 'errado': retorno esperado 'int', encontrado 'float'
ERRO SEMANTICO (linha 16): funcao 'semRetorno': retorno esperado 'void', encontrado 'int'
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro 9
cd c:\Users\Larissa\Programming_Languages\Jflex-Java\bin\roteiro_9\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
