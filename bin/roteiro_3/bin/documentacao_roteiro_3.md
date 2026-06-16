# Relatório de Análise Semântica — Roteiro 3 — Expressões Aritméticas

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro 3** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: Os operandos de qualquer operador aritmético (+, -, *, /, %) devem ser obrigatoriamente numéricos (do tipo 'int' ou 'float'). Expressões que envolvem misturas de 'int' e 'float' resultam em um tipo 'float'. Operandos não numéricos (como 'boolean') são inválidos.

### Descrição e Contexto:
Ao encontrar expressões binárias com operadores aditivos (ADDOP) ou multiplicativos (MULOP), o analisador infere os tipos dos sub-termos esquerdo e direito. Ambos devem pertencer ao conjunto de tipos numéricos. Caso contrário, reporta erro semântico.

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_3/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_3/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
program R3 {
  int a;
  float b;
  boolean flag;

  void teste() {
    a = 5;
    b = 2.0;
    flag = true;
    a = a + b;          /* aviso: int + float */
    b = a * b;          /* ok: numericos */
    a = flag + 1;       /* ERRO: boolean em operacao aritmetica */
  }
}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
ERRO SEMANTICO (linha 13): operandos do operador '+' devem ser numericos
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro 3
cd c:\Users\Larissa\Programming_Languages\Jflex-Java\bin\roteiro_3\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
