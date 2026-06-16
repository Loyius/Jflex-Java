import os

# Base directory for the roteiros
base_dir = r"c:\Users\Larissa\Programming_Languages\Jflex-Java\bin"

roteiros_info = {
    1: {
        "title": "Roteiro 1 — Declaração e Uso de Variáveis",
        "regra": "Toda variável utilizada em expressões ou atribuições deve ter sido previamente declarada na tabela de símbolos.",
        "descricao": "O analisador semântico realiza duas passagens pelos tokens. Na primeira passagem (registro), ele insere todas as declarações de variáveis e funções na tabela de símbolos. Na segunda passagem (validação), ele insere e verifica o uso das variáveis. Qualquer identificador usado que não esteja na tabela de símbolos gera um erro semântico de variável não declarada.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program Roteiro1 {
  int x;
  float y;
  boolean ativo;

  void teste() {
    x = 10;           /* ok: x foi declarado */
    y = 3.14;         /* ok: y foi declarada */
    ativo = true;     /* ok */

    z = 5;            /* ERRO: z nao foi declarada */
    print(indefinida); /* ERRO: indefinida nao foi declarada */
  }
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 15): variavel 'z' usada sem declaracao
ERRO SEMANTICO (linha 16): variavel 'indefinida' usada sem declaracao"""
    },
    2: {
        "title": "Roteiro 2 — Compatibilidade de Tipos em Atribuições",
        "regra": "O tipo do valor atribuído a uma variável deve ser compatível com o tipo declarado da variável. Atribuição de 'float' para 'int' é inválida (perda de precisão), ao passo que 'float <- int' é permitida (promoção implícita). Variáveis do tipo 'boolean' só aceitam valores booleanos.",
        "descricao": "Ao encontrar o operador de atribuição '=', o analisador semântico infere o tipo da expressão à direita (através de literais e tipos das variáveis associadas) e valida se esse tipo pode ser atribuído ao tipo declarado da variável à esquerda.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program R2 {
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
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 11): incompatibilidade de tipo na atribuicao para 'x': esperado int, encontrado float
ERRO SEMANTICO (linha 12): incompatibilidade de tipo na atribuicao para 'b': esperado boolean, encontrado int"""
    },
    3: {
        "title": "Roteiro 3 — Expressões Aritméticas",
        "regra": "Os operandos de qualquer operador aritmético (+, -, *, /, %) devem ser obrigatoriamente numéricos (do tipo 'int' ou 'float'). Expressões que envolvem misturas de 'int' e 'float' resultam em um tipo 'float'. Operandos não numéricos (como 'boolean') são inválidos.",
        "descricao": "Ao encontrar expressões binárias com operadores aditivos (ADDOP) ou multiplicativos (MULOP), o analisador infere os tipos dos sub-termos esquerdo e direito. Ambos devem pertencer ao conjunto de tipos numéricos. Caso contrário, reporta erro semântico.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program R3 {
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
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 13): operandos do operador '+' devem ser numericos"""
    },
    4: {
        "title": "Roteiro 4 — Expressões Relacionais",
        "regra": "Os operandos envolvidos em operadores relacionais (==, !=, <, >, <=, >=) devem possuir tipos compatíveis. É permitida a comparação entre valores numéricos (int/float), bem como comparação direta entre booleanos. Outras combinações mistas (como boolean vs int) geram erro semântico.",
        "descricao": "O analisador verifica os tipos dos dois operandos comparados. Se um for booleano e o outro for numérico, emite erro de incompatibilidade de tipos relacionais.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program R4 {
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
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 13): operandos do operador '==' possuem tipos incompativeis: boolean e int"""
    },
    5: {
        "title": "Roteiro 5 — Expressões Lógicas",
        "regra": "Os operadores lógicos binários (&&, ||) e o operador unário de negação (!) requerem operandos estritamente do tipo 'boolean'.",
        "descricao": "Durante a resolução semântica dos termos lógicos, o compilador verifica se as expressões avaliadas à esquerda, à direita, ou sob a negação lógica resultam em booleanos. O uso de tipos numéricos nessas operações dispara erros semânticos.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program R5 {
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
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 13): operando do operador '&&' deve ser booleano
ERRO SEMANTICO (linha 15): operando do operador '!' deve ser booleano"""
    },
    6: {
        "title": "Roteiro 6 — Condição if/else booleana",
        "regra": "A expressão fornecida como condição em um comando 'if' deve resultar obrigatoriamente no tipo 'boolean'.",
        "descricao": "O analisador infere o tipo da expressão contida na expressão de controle do 'if'. Se o tipo resolvido não for booleano, reporta-se o erro semântico de condição inválida.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program R6 {
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
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 11): condicao do 'if' deve ser booleana (linha 11)
ERRO SEMANTICO (linha 12): condicao do 'if' deve ser booleana (linha 12)"""
    },
    7: {
        "title": "Roteiro 7 — Condição while booleana",
        "regra": "A expressão de condição de controle de uma estrutura de repetição 'while' deve resultar estritamente no tipo 'boolean'.",
        "descricao": "Semelhante ao roteiro anterior, o analisador semântico valida a expressão condicional do laço de repetição. Se o tipo avaliado for numérico, a análise acusa falha semântica.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program R7 {
  int i;
  boolean continua;

  void teste() {
    i = 0;
    continua = true;
    while (i < 10) { i = i + 1; }  /* ok */
    while (continua) { i = i + 1; } /* ok */
    while (i) { i = i + 1; }       /* ERRO: int nao e boolean */
  }
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 11): condicao do 'while' deve ser booleana"""
    },
    8: {
        "title": "Roteiro 8 — Declaração e Chamada de Funções",
        "regra": "As chamadas de funções devem referenciar funções que de fato foram declaradas, e a quantidade de argumentos fornecidos na chamada (aridade) deve ser idêntica à quantidade de parâmetros formais na sua declaração.",
        "descricao": "Na primeira passagem, o analisador cadastra as funções e suas assinaturas (lista de tipos de parâmetros). Na segunda passagem, ao processar chamadas de função, valida se o nome está cadastrado e se o número de parâmetros bate com a aridade real. Também checa o caso de funções inexistentes.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program R8 {
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
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 14): chamada de 'soma': esperado 2 argumento(s), encontrado 1
ERRO SEMANTICO (linha 16): funcao 'naoExiste' nao declarada"""
    },
    9: {
        "title": "Roteiro 9 — Tipo de Retorno de Funções",
        "regra": "O tipo da expressão em qualquer comando 'return <expr>' dentro de uma função deve ser compatível com o tipo de retorno declarado na assinatura dessa função. Se a função for declarada como 'void', ela não pode retornar expressão de valor.",
        "descricao": "O analisador rastreia a função atualmente sob compilação. Ao encontrar a instrução 'return', ele avalia a expressão associada e valida o tipo contra o tipo de retorno declarado na assinatura da função corrente.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program R9 {
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
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 12): funcao 'errado': retorno esperado 'int', encontrado 'float'
ERRO SEMANTICO (linha 16): funcao 'semRetorno': retorno esperado 'void', encontrado 'int'"""
    },
    10: {
        "title": "Roteiro 10 — Escopo de Variáveis",
        "regra": "Redeclaração de variáveis no mesmo escopo local (mesmo bloco) é proibida. Redeclarações em escopos diferentes (shadowing/sombreamento) são permitidas. Variáveis declaradas dentro de blocos aninhados só são visíveis dentro destes e deixam de existir após o encerramento do escopo do bloco.",
        "descricao": "A tabela de símbolos gerencia escopos aninhados via pilha. O início do bloco empilha uma nova tabela, e o encerramento desempilha a mesma. Redeclarações locais são verificadas na tabela do topo, enquanto a resolução de nomes busca de forma recursiva de cima para baixo na pilha.",
        "arquivo_saida": "saida.txt",
        "entrada_exemplo": """program R10 {
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
}""",
        "saida_exemplo": """ERRO SEMANTICO (linha 19): variavel 'y' usada fora de escopo ou nao declarada
ERRO SEMANTICO (linha 20): variavel 'x' ja declarada neste escopo (nivel 2)"""
    }
}

for n, info in roteiros_info.items():
    dir_path = os.path.join(base_dir, f"roteiro_{n}", "bin")
    if not os.path.exists(dir_path):
        os.makedirs(dir_path)
    
    file_path = os.path.join(dir_path, f"documentacao_roteiro_{n}.md")
    
    content = f"""# Relatório de Análise Semântica — {info['title']}

Este documento apresenta a especificação, regras semânticas, implementação e casos de teste do **Roteiro {n}** do projeto do compilador Java--.

---

## 1. Regra Semântica Avaliada

**Regra**: {info['regra']}

### Descrição e Contexto:
{info['descricao']}

---

## 2. Implementação Semântica

A verificação semântica é realizada pelo módulo [`SemanticAnalyzer.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_{n}/bin/SemanticAnalyzer.java) com o auxílio da Tabela de Símbolos [`SymbolTable.java`](file:///c:/Users/Larissa/Programming_Languages/Jflex-Java/bin/roteiro_{n}/bin/SymbolTable.java).

### Estrutura e Mecanismo de Verificação:
1. **Passo 1 (Coleta/Declarações)**: O método `primeiraPassagem()` percorre a lista de tokens do programa identificando as declarações de variáveis (padrões `tipo IDENT` ou `final tipo IDENT`) e funções (`tipo IDENT ( params ) bloco`), populando a Tabela de Símbolos.
2. **Passo 2 (Resolução/Validação)**: O método `segundaPassagem()` ou o fluxo de análise semântica avalia os construtos semânticos aplicáveis ao roteiro, validando tipos de expressões e lançando erros no formato `ERRO SEMANTICO (linha N): <descrição>` quando violações ocorrem.

---

## 3. Casos de Teste (Entrada e Saída)

### Código de Entrada (`entrada.txt`)
```java
{info['entrada_exemplo'].strip()}
```

### Relatório de Saída da Compilação (`saida.txt`)
```
{info['saida_exemplo'].strip()}
```

---

## 4. Instruções para Compilação e Execução do Teste

Para compilar e testar a análise semântica deste roteiro de forma isolada, execute os seguintes comandos no terminal PowerShell:

```powershell
# 1. Navegar até o diretório do Roteiro {n}
cd c:\\Users\\Larissa\\Programming_Languages\\Jflex-Java\\bin\\roteiro_{n}\\bin

# 2. Compilar os arquivos fontes em Java
javac *.java

# 3. Executar o compilador informando entrada e saída
java Main entrada.txt saida.txt

# 4. Exibir o conteúdo da saída gerada
Get-Content saida.txt
```
"""
    
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(content)
        
    print(f"Gerado: {file_path}")

print("Concluído!")
