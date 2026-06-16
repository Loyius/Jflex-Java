# Relatório de Análise Semântica — Roteiro 8 — Declaração e Chamada de Funções

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro 8** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: As chamadas de funções devem referenciar funções que de fato foram declaradas, e a quantidade de argumentos fornecidos na chamada (aridade) deve ser idêntica à quantidade de parâmetros formais na sua declaração.

### Descrição e Contexto:
Na primeira passagem, o analisador cadastra as funções e suas assinaturas (lista de tipos de parâmetros). Na segunda passagem, ao processar chamadas de função, valida se o nome está cadastrado e se o número de parâmetros bate com a aridade real. Também checa o caso de funções inexistentes.

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_8/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_8/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
program R8 {
  int soma(int a, int b) {
    return a + b;
  }

  void imprime(int x) {
    print(x);
  }

  void teste() {
    int r;
    r = soma(3, 4);       /* ok: aridade 2 */
    r = soma(3);          /* ERRO: aridade errada (1 arg, esperado 2) */
    imprime(r);           /* ok: aridade 1 */
    naoExiste(r);         /* ERRO: funcao nao declarada */
  }
}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
ERRO SEMANTICO (linha 14): chamada de 'soma': esperado 2 argumento(s), encontrado 1
ERRO SEMANTICO (linha 16): funcao 'naoExiste' nao declarada
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro 8
cd c:\Users\Larissa\Programming_Languages\Jflex-Java\bin\roteiro_8\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
