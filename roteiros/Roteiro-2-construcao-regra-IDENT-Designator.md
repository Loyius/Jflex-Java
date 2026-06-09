# Roteiro - Construção das Regras: IDENT e Designator

## 1 Introdução

Neste ROTEIRO vamos introduzir em nossa Gramática as **expressões** (`expr`, `term` e `factor`), o `identificador` e `designator`.

Para que assim, nosso **reconhecedor de expressões** esteja cada vez mais completo, aceitando TODO tipo de **variáveis** (`designator`).

---

## 2 IDENT

Sabemos que um `IDENT` é um **nome de variável válido**.

Portanto, DEVE **iniciar** com uma **letra** e, na sequência pode vir **letra** ou **dígito** quantas vezes for necessário.

Assim, em nosso arquivo `.flex`, adicionaremos uma **macro** e uma **regra** para definir o identificador.

Afinal de contas, um `identificador` é um ***TOKEN***. Ou seja, o **Scanner** nos retorna SEMPRE as **células mais básicas**: os *tokens*.

```
// macro
ident = {letra} ({letra} | {digito})*

// regra
{ident} { return new Symbol(sym.IDENT, yytext()); }
```

Preste atenção para os seguintes pontos:

- O `identificador` é um **nome de variável**, portanto é uma `String`. Por isso, vamos **retornar** junto com o `Symbol` o **lexema** (`yytext()`) do identificador em questão.
- E, o mais importante, a regra:

  ```
  {ident} { return new Symbol(sym.IDENT, yytext()); }
  ```

  DEVE VIR, obrigatoriamente **após** TODAS as **KEY_WORDS**.

- Senão, seu **Scanner** vai **retornar** TODAS as **palavras chaves** (`if`, `while`, `program`, `print`, `for`) são `IDENT`.
- Lembra que a ORDEM da regra no **arquivo** `.flex` faz diferença?
  - Quem vem primeiro tem **prioridade**.
- Portanto, vamos colocar uma regra da **key_word** `if` só para gente conferir este fato.

Assim, nosso arquivo `.cup`, nós vamos fazer dois passos:

### 2.1 1º Passo

- Criar o terminal `IDENT`
- Que neste caso não precisa informar NENHUM **tipo**
- Mas se tivéssemos de informar um tipo, este tipo seria `String`, pois todo `identificador` é uma **palavra**

### 2.2 2º Passo

- Onde **adicionar** o `IDENT` dentro da nossa gramática?
- Você concorda que a **variável** vai entrar, basicamente, no lugar de um `NUMBER`?
- Pois de fato, quando olhamos o conteúdo de um `identificador` (uma **variável**) ele é um **número**. Concorda?
- Então, neste caso, vamos CRIAR uma NOVA **regra de produção** dentro do `factor`, ou seja:

  ```
  factor → IDENT
  ```

  - Pronto!

### 2.3 Arquivo `parser.cup`

```java
import java_cup.runtime.*;

/* TERMINAIS */
terminal PTVIRG, MAIS, MENOS, MULT, DIV, MOD;
terminal ABRE_PARENT, FECHA_PARENT, KW_IF;
terminal Double NUMBER;

/* NÃO TERMINAIS */
non terminal expr_list, expr_ptv;
non terminal Double expr, term, factor;

expr_list ::= expr_list expr_ptv
            | expr_ptv;

expr_ptv ::= expr:e {: System.out.println("= " + e); :} PTVIRG;

expr ::= expr:e MAIS  term:t {: RESULT = e + t;          :}
       | expr:e MENOS term:t {: RESULT = e - t;          :}
       | MENOS  term:t       {: RESULT = -t;             :}
       | term:t              {: RESULT = t.doubleValue(); :}
;

term ::= factor:f MULT term:t {: RESULT = f * t;          :}
       | factor:f DIV  term:t {: RESULT = f / t;          :}
       | factor:f             {: RESULT = f.doubleValue(); :}
;

factor ::= NUMBER:n              {: RESULT = n.doubleValue();  :}
         | ABRE_PARENT expr:e FECHA_PARENT {: RESULT = e.doubleValue(); :}
         | IDENT:id              {: RESULT = id.doubleValue(); :}
;
```

> **Atenção:** Coloquei um **bloco** `{: :}` vinculado ao `IDENT`!
>
> - E, DENTRO deste **bloco** está sendo retornado SEMPRE o **valor** `1.0`
> - Fiz isso, para que nossa **expressão** continue sendo computada, afinal `factor` espera SEMPRE um **valor** `double` como **retorno** e, NÃO podemos DEIXAR a **recursão** quebrar
> - Como NÃO sabemos o **valor** do `IDENT`, vamos adotar `1.0` APENAS para efeito de teste.

### 2.4 Arquivo `Main.java`

Agora, vamos usar a **entrada pelo arquivo**, senão fica muito trabalhoso escrever toda hora todas as expressões.

Mas na função `main` abaixo, veja que, **existe a outra opção comentada**, caso você queira fazer a **entrada via teclado**, basta **descomentar** a linha e **comentar** a chamada **via arquivo**.

```java
import java.io.*;

class Main {
    public static void main(String[] args) throws Exception {
        // ## Para ler a entrada do teclado
        // Scanner scanner = new Scanner(System.in);

        // Para ler a entrada do arquivo
        FileReader in = new FileReader("teste.txt");
        Scanner scanner = new Scanner(in);
        parser parser = new parser(scanner);

        try {
            parser.parse();
            System.out.println("Arquivo sem erros de sintaxe!");
        } catch (Exception e) {
            System.out.println("Erro de sintaxe: " + e);
        }
    }
}
```

Ao executar, teremos a seguinte **saída**:

| Entrada       | Saída                         |
|---------------|-------------------------------|
| `a + b;`      | `= 2.0`                       |
| `1 + 2 * a;`  | `= 3.0`                       |
| `-a + 1;`     | `= 0.0`                       |
| `c - a * b;`  | `= 0.0`                       |
|               | `Arquivo sem erros de sintaxe!` |

Perceba que, TODAS as **variáveis** (`IDENT`) foram consideradas com o **valor** de `1.0`.

Analise o arquivo `.cup`, para você entender o porquê.

---

## 3 DESIGNATOR

O `designador` nada mais é que uma **extensão do conceito** de `IDENT`.

Se utilizamos normalmente `IDENT` para **nome de variável**, o `DESIGNATOR` é para REPRESENTAR:

- **nome de variáveis**;
- **vetores**;
- **classes**;
- **atributos**;
- e muito mais...

A seguir, temos **alguns exemplos** de `designadores` que nosso Analisador DEVE ACEITAR:

```
a
pessoa.idade;
pessoa.idade[1 + 2];
vetor[a + pessoa.idade].altura;
```

A princípio pode parecer perda de tempo listar alguns exemplos da estrutura que se deseja fazer, mas não é. Esse é o segredo para o sucesso!

Assim, podemos concluir que um `designador` pode ser:

- Uma **variável** sozinha;
- Um **vetor**;
- Uma **classe** com seus **atributos**;
- Ou seja, **variações** destes itens acima.

Assim nossa **regra de produção** `designador` será da seguinte forma:

```
designator ::= designator ABRE_COLCH expr FECHA_COLCH
             | designator PTO IDENT
             | IDENT
             ;
```

Perceba que, para fazer esta regra tivemos que CRIÁ-LA no **arquivo** `.cup`:

- `terminal ABRE_COLCH;`
- `terminal FECHA_COLCH;`
- `terminal PTO; non terminal designator;`

Desta forma, obrigatoriamente, precisamos ADICIONAR estes ITENS lá no **arquivo** `.flex` e FAZER o retorno do `Symbol` correto.

O **próximo passo** é ANALISAR em qual **regra de produção** vamos ADICIONAR o `DESIGNATOR`.

Para responder esta pergunta, basta lembrarmos que um `DESIGNATOR` é um `IDENT` **mais abrangente**!

Logo, vamos RETIRAR o `IDENT` da **regra de produção** `factor` e, TROCÁ-LO por `DESIGNATOR`, ou seja:

```
factor → IDENT       (antes)
factor → designator  (depois)
```

Consequentemente, colocaremos o `IDENT` dentro de `designator`, como vimos acima.

E, outro ponto que muitos esquecem é que, o conteúdo dos **colchetes** (`[]`) de qualquer **vetor** PODE SER COMPOSTO por uma **expressão** e NÃO APENAS um **número**.

Logo, o **arquivo** `parser.cup` ficará assim:

```java
import java_cup.runtime.*;

/* TERMINAIS */
terminal PTVIRG, PTO, VIRG, MAIS, MENOS, DIV, MULT, MOD;
terminal ABRE_PARENT, FECHA_PARENT, OP_RELACIONAL, ABRE_CHAVE, FECHA_CHAVE, ABRE_COLCH;
terminal FECHA_COLCH, KW_IF;
terminal Double NUMBER, IDENT;

/* NÃO TERMINAIS */
non terminal expr_list, expr_ptv;
non terminal Double expr, term, factor, designator;

expr_list ::= expr_list expr_ptv
            | expr_ptv;

expr_ptv ::= expr:e {: System.out.println("= " + e); :} PTVIRG;

expr ::= expr:e MAIS  term:t {: RESULT = e + t;          :}
       | expr:e MENOS term:t {: RESULT = e - t;          :}
       | MENOS  term:t       {: RESULT = -t;             :}
       | term:t              {: RESULT = t.doubleValue(); :}
;

term ::= factor:f MULT term:t {: RESULT = f * t;          :}
       | factor:f DIV  term:t {: RESULT = f / t;          :}
       | factor:f             {: RESULT = f.doubleValue(); :}
;

factor ::= NUMBER:n                        {: RESULT = n.doubleValue(); :}
         | ABRE_PARENT expr:e FECHA_PARENT {: RESULT = e.doubleValue(); :}
         | designator:d                    {: RESULT = d.doubleValue(); :}
;

designator ::= designator ABRE_COLCH expr FECHA_COLCH {: RESULT = Double.valueOf(1.0); :}
             | designator PTO IDENT                   {: RESULT = Double.valueOf(1.0); :}
             | IDENT                                  {: RESULT = Double.valueOf(1.0); :}
;
```

> **Atenção:** Foi colocado um **bloco** `{: :}` vinculado ao `IDENT`. E, DENTRO deste **bloco** está retornando SEMPRE o **valor** `1.0`.
>
> Isso foi feito para que nossa **expressão** continue sendo computada, afinal `factor` espera SEMPRE um **valor** `double` como **retorno** e NÃO podemos deixar a **recursão** QUEBRAR!
>
> Mas, como NÃO SABEMOS o **valor** do `IDENT`, adotaremos o valor `1.0`, APENAS para efeito de teste.

### 3.1 Arquivo `scanner.flex`

```java
import java_cup.runtime.Symbol;

%%

%class Scanner
%cupsym sym
%cup
%unicode
%line
%column
%public

// Definições de macros (ajuste conforme o seu arquivo .flex)
digito      = [0-9]
letra       = [a-zA-Z]
digitos     = [0-9]+
opRelacional = ">" | "<" | ">=" | "<=" | "==" | "!="
Ident       = {letra} ({letra} | {digito})*
fimdeLinha  = \r | \n | \r\n
espaco      = {fimdeLinha} | [ \t\f ]

%%

{opRelacional} {
    String opRelacional = yytext();
    return new Symbol(sym.OP_RELACIONAL, opRelacional);
}

"+"  { return new Symbol(sym.MAIS);         }
"-"  { return new Symbol(sym.MENOS);        }
"/"  { return new Symbol(sym.DIV);          }
"*"  { return new Symbol(sym.MULT);         }
"%"  { return new Symbol(sym.MOD);          }
";"  { return new Symbol(sym.PTVIRG);       }
"("  { return new Symbol(sym.ABRE_PARENT);  }
")"  { return new Symbol(sym.FECHA_PARENT); }
"{"  { return new Symbol(sym.ABRE_CHAVE);   }
"}"  { return new Symbol(sym.FECHA_CHAVE);  }
"["  { return new Symbol(sym.ABRE_COLCH);   }
"]"  { return new Symbol(sym.FECHA_COLCH);  }
"."  { return new Symbol(sym.PTO);          }
","  { return new Symbol(sym.VIRG);         }

{Ident}  { return new Symbol(sym.IDENT, yytext()); }

{espaco} { /* despreza */ }

[^ ]  { /* Caractere inválido */
    return new Symbol(sym.EOF);
}
```

### 3.2 Arquivo `Main.java`

Vamos usar a **entrada pelo arquivo**, senão fica muito trabalhoso escrever toda hora todas as expressões.

```java
import java.io.*;

class Main {
    public static void main(String[] args) throws Exception {
        // Para ler a entrada do teclado
        // Scanner scanner = new Scanner(System.in);

        // Para ler a entrada do arquivo
        FileReader in = new FileReader("teste.txt");
        Scanner scanner = new Scanner(in);
        parser parser = new parser(scanner);

        try {
            parser.parse();
            System.out.println("Arquivo sem erros de sintaxe!");
        } catch (Exception e) {
            System.out.println("Erro de sintaxe: " + e);
        }
    }
}
```

Ao executar, teremos a seguinte **saída**:

| Entrada                      | Saída                          |
|------------------------------|--------------------------------|
| `a + b;`                     | `= 2.0`                        |
| `pessoa.idade + 1;`          | `= 2.0`                        |
| `-pessoa.idade[1 + 2];`      | `= -1.0`                       |
| `1 + vetor[a + pessoa.idade];` | `= 2.0`                      |
| `(3-2) * 4 + vetor[a.idade];`| `= 5.0`                        |
| `-1 - vetor[8] - 3;`         | `= -5.0`                       |
|                              | `Arquivo sem erros de sintaxe!` |

Perceba que:

- Nosso compilador está ficando **cada vez mais abrangente**!
- Para TODAS as **variáveis** (`Designator`) foi considerado o **valor** `1.0`.

---

## 4 Usuário Informando o Valor da Variável

No exemplo anterior o valor do `designator` (**variável**) estava congelado no **valor** `1.0`, o que NÃO está correto!

Por isso, vamos deixar que o **usuário** INFORME o **valor** do `identificador`.

Assim, o `parser` SERÁ CAPAZ de **efetuar a operação corretamente**.

Para isso, basta usarmos a **Classe SCANNER** do `java.util` para PEDIR ao **usuário** que INFORME este **valor**.

Perceba que, DENTRO dos **blocos** `{: :}`, podemos FAZER QUALQUER **programação java** normalmente.

> **Atenção:** NÃO ESQUEÇA fazer os `imports` necessários no **início do arquivo** `.cup`.
> E, neste caso, precisamos importar o `java.util.Scanner`.

Assim, imediatamente APÓS "casar" com `IDENT`, ele irá dispor o **código java** para o preenchimento desta **variável**.

Abaixo, temos o arquivo `.cup`:

```java
import java.util.Scanner;
import java_cup.runtime.*;

/* TERMINAIS */
terminal PTVIRG, MAIS, MENOS, DIV, MULT, MOD;
terminal Double NUMBER;
terminal IDENT;

/* NÃO TERMINAIS */
non terminal Double expr_list, expr_ptv;
non terminal Double expr;

// ...

designator ::= designator ABRE_COLCH expr FECHA_COLCH
                   {: RESULT = Double.valueOf(1.0); :}
             | designator PTO IDENT
                   {: RESULT = Double.valueOf(1.0); :}
             | IDENT:id {:
                   Scanner ler = new Scanner(System.in);
                   double num1;
                   System.out.print("Informe o valor da variável (" + id + "):");
                   num1 = ler.nextDouble();
                   RESULT = num1;
               :}
;
```

Esta solução vai funcionar, porém **APENAS** quando a entrada que for uma **variável sozinha** SEM **ponto** (`.`) ou **colchetes** (`[]`). Pois, estamos **usando** aqui o `IDENT`.

E, o que queremos é que o **usuário** informe para TODO e QUALQUER `designator` que aparecer.

Portanto, a **MELHOR solução** é **subir um nível** e colocar este código java na regra de produção:

```
factor → designator {: ... :}
```

Logo, o arquivo `.cup` fica da seguinte maneira:

```java
import java.util.Scanner;
import java_cup.runtime.*;

// ...

factor ::= NUMBER:n
               {: RESULT = n.doubleValue(); :}
         | ABRE_PARENT expr:e FECHA_PARENT
               {: RESULT = e.doubleValue(); :}
         | designator {:
               Scanner ler = new Scanner(System.in);
               double num1;
               System.out.print("Informe o valor do designator: ");
               num1 = ler.nextDouble();
               RESULT = num1;
           :}
;

designator ::= designator ABRE_COLCH expr FECHA_COLCH
             | designator PTO IDENT
             | IDENT:id
;
```

Agora, ao executar, teremos a seguinte **saída** (todos os valores informados foram `2.0` para facilitar a conferência):

| Entrada                          | Saída                                 |
|----------------------------------|---------------------------------------|
| `a + b;`                         | `Informe o valor do designator: 2`    |
|                                  | `Informe o valor do designator: 2`    |
|                                  | `= 4.0`                               |
| `pessoa.idade + 1;`              | `Informe o valor do designator: 2`    |
|                                  | `= 3.0`                               |
| `-pessoa.idade[1 + 2];`          | `Informe o valor do designator: 2`    |
|                                  | `= -2.0`                              |
| `1 + vetor[a + pessoa.idade];`   | `Informe o valor do designator: 2` ×3 |
|                                  | `= 3.0`                               |
| `(3-2) * 4 + vetor[a.idade];`    | `Informe o valor do designator: 2` ×2 |
|                                  | `= 6.0`                               |
| `-1 - vetor[8] - 3;`             | `Informe o valor do designator: 2`    |
|                                  | `= -6.0`                              |
|                                  | `Arquivo sem erros de sintaxe!`       |

Para facilitar a conferência, TODOS os **valores** informados **via teclado** foram `2.0`, mas agora, **pode ser** QUALQUER **valor** que o **usuário** quiser!

---

## 5 Atividades

1. Este **tipo de tratamento** que estamos fazendo nos exemplos aqui pertence a qual fase (Análise Léxica, Sintática ou Semântica)? E porque?

2. **(Índice do vetor)**

   Todos nós, programadores, sabemos que é **impossível** ACESSAR um **índice negativo** de um **vetor**.

   Então, FAÇA as **devidas alterações** no arquivo `.cup`, para que o seu Analisador ACUSE um ERRO (**de semântica**) quando isso acontecer.

3. No **Exercício 2**, você, APENAS **reportou** um ERRO usando o `System.out`.

   Agora, FAÇA o seguinte:
   - Quando ele **deparar** com um **índice negativo** no **vetor**, SEU Analisador DEVE PEDIR que o **usuário** `informe um NOVO valor válido para o índice`.
