# Roteiro - Local, Bloco e Precedência (expr, term, factor)

Continuando o estudo sobre as **ações associadas** às **regras de produção**, exercitaremos um pouco mais sobre a importância de saber o local correto onde colocar o **bloco** `{::}`.

Neste roteiro vamos utilizar, como exemplo, uma **Gramática** (G) um pouco mais interessante, usaremos agora as variáveis `expr`, `term` e `factor`, pois assim teremos mais representatividade.

Com estas **variáveis** bem definidas nossa **Gramática** SERÁ CAPAZ de aceitar as **expressões matemáticas** e, respeitando a precedência do operador `*` em relação ao operador `+`.

E para entender bem esta **precedência**, vamos colocar **blocos** `{::}` para imprimir cada uma das **operações** que estão sendo disparadas.

Assim, nossa **Gramática**, que salvamos no arquivo `parser.cup` ficará assim:

```java
import java_cup.runtime.*;

/* TERMINAIS */
terminal PTVIRG, MAIS, MENOS, MULT, DIV, MOD, ABRE_PARENT, FECHA_PARENT;
terminal Double NUMBER;

/* NÃO TERMINAIS */
non terminal expr_list, expr_ptv;
non terminal Double expr, term, factor;

expr_list ::= expr_list expr_ptv
            | expr_ptv;

expr_ptv ::= expr:e {: System.out.println("= " + e); :} PTVIRG;

expr ::= expr:e MAIS term:t {:
                    System.out.println("+ ");
                    RESULT = e + t;
                :}
       | expr:e MENOS term:t {:
                    System.out.println("- ");
                    RESULT = e - t;
                :}
       | MENOS term:t {:
                    System.out.println("u- ");
                    RESULT = -t;
                :}
       | term:t {: RESULT = new Double(t.doubleValue()); :}
;
```

```java
term ::= factor:f MULT term:t {:
                    System.out.println("* ");
                    RESULT = f * t;
                :}
       | factor:f DIV term:t {:
                    System.out.println("/ ");
                    RESULT = f / t;
                :}
       | factor:f {: RESULT = new Double(f.doubleValue()); :}
;

factor ::= NUMBER:n {:
                    System.out.println(n + " ");
                    RESULT = new Double(n.doubleValue());
                :}
         | ABRE_PARENT expr:e FECHA_PARENT {:
                    RESULT = new Double(e.doubleValue());
                :}
;
```

Perceba que vamos imprimir **CADA PASSO** e computar o resultado. Assim conseguiremos enxergar com mais clareza o que está acontecendo.

> **Atenção:** Definimos nossos terminais e não terminais com o TIPO `Double`, portanto, temos que LEMBRAR DISSO lá no `Scanner.flex`.

Assim, nosso arquivo `scanner.flex` ficará assim:

```java
import java_cup.runtime.Symbol;

%%

%class Scanner
%unicode
%cup
%line
%column

%{
    // Codigo para ler direto do teclado
    public Scanner(java.io.InputStream in) {
        this(new java.io.InputStreamReader(in, java.io.charset.Charset.forName("UTF-8")));
    }
%}

digito   = [0-9]
letra    = [a-zA-Z]
digitos  = [0-9]+
fimdeLinha = \r | \n | \r\n
espaco   = {fimdeLinha} | [ \t\f ]

%%

{digitos} {
    double aux = Double.parseDouble(yytext());
    return new Symbol(sym.NUMBER, new Double(aux));
}

"+"  { return new Symbol(sym.MAIS);        }
"-"  { return new Symbol(sym.MENOS);       }
"/"  { return new Symbol(sym.DIV);         }
"*"  { return new Symbol(sym.MULT);        }
"%"  { return new Symbol(sym.MOD);         }
";"  { return new Symbol(sym.PTVIRG);      }
"("  { return new Symbol(sym.ABRE_PARENT); }
")"  { return new Symbol(sym.FECHA_PARENT);}

{espaco} { /* despreza */ }

[^ ] { /* Caractere inválido */
    return new Symbol(sym.EOF, yyline, yycolumn, yytext());
}
```

O arquivo `Main.java` faz a UNIÃO de nossas camadas **Scanner** (Analisador Léxico) e **Parser** (Analisador Sintático).

Neste exemplo, assim como nos anteriores, temos passado para o Scanner via teclado como sistema de entrada (`System.in`).

Abaixo, temos o arquivo `Main.java`:

```java
import java.io.*;

class Main {
    public static void main(String[] args) throws Exception {
        // ## Lendo a entrada pelo arquivo
        // FileReader in = new FileReader("teste.txt");
        // Scanner scanner = new Scanner(in);

        // ## Lendo a entrada pelo teclado
        Scanner scanner = new Scanner(System.in);
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

Ao executar teremos a seguinte saída:

```
java -cp ".;.\jflex-full-1.9.1.jar;.\java-cup-11b.jar" .\Main.java
1 + 2 * 3;
1.0 2.0 3.0 * + = 7.0
(1+2) * 3;
1.0 2.0 + 3.0 * = 9.0
```

Observe que:

1. Para a entrada `1 + 2 * 3;`
   - Temos a saída: `1.0 2.0 3.0 * + = 7.0`
   - Assim, observamos que, de fato nossa Gramática **define** uma **precedência** MAIOR para o **operador** `*` do que o `+`.

2. Para a entrada `(1 + 2) * 3;`
   - Temos a saída: `1.0 2.0 + 3.0 * = 9.0`
   - Agora, podemos **conferir** que realmente a nossa Gramática está **funcionando certinha quando utilizamos parênteses** `( )`, pois de fato ela **executou** o `+` antes do `*`!

---

## Atividade

1. Coloque esse código para funcionar em seu computador e verifique seu funcionamento.
   - Verificar se seu funcionamento não é APENAS fazer rodar, mas sim, **entender** CADA detalhe que conversamos.
   - Na sequência, **execute** CADA uma das expressões abaixo no seu computador e **crie no papel** a ***Parse Tree*** deles para conferir o resultado.
   - Esse PAPEL é para ser entregue!!! E, VALERÁ NOTA!!!

   a) `2 + 4 * 7 + 3;`  
   b) `12 / 3 / 4;`  
   c) `4 + 8 / 3;`  
   d) `(4 + 8) / 3;`

2. Faça as devidas alterações em SEU compilador (Scanner e Parser) para que ele ACEITE, agora, a função `MOD`.

   **Dica:**
   - Atenção com aqueles pontos entre os arquivos `.cup` e `.flex` que PRECISAM ESTAR COERENTES.
   - E, lembre-se o operador `MOD` tem a MESMA **precedência** dos operadores divisão (`/`) e multiplicação (`*`).

3. Neste exercício vamos **observar** um pouco mais nossa Gramática.

   No nosso arquivo `.cup` do **Exercício 1**, nossas operações de soma, subtração, multiplicação, divisão, etc... estão TODAS com a **recursividade à direita**, como por exemplo:

   ```
   term → factor MULT term
   ```

   a) Antes de fazer qualquer coisa, RODE A SOLUÇÃO do seu **Exercício 1** para a entrada `12 / 4 / 2` e, depois, FAÇA a **Parse Tree** no papel.

   b) Agora, pegue TODAS as suas **regras de produção** e passe elas para a **recursividade à esquerda**.
      - Ou seja, APENAS troque elas de posição no seu arquivo `.cup` e rode para a MESMA entrada `12 / 4 / 2` e, depois, FAÇA a **Parse Tree** no papel.

   Comente no papel o que aconteceu? Quais DIFERENÇAS você encontrou e, EXPLIQUE o porque deste acontecimento.

4. Altere SEU compilador, para que ele faça a **LEITURA DAS ENTRADAS** a partir de um **arquivo de texto** (`teste.txt`) e NÃO DO TECLADO.

   Use o arquivo:

   ```
   1 + 1;
   -1 - 2 - 3;
   1 + 2 * 3;
   1 * 2 + 3;
   12 / 4 / 2;
   (1 + 2) * 3;
   ```

   **Obs.:**
   - Em compiladores de verdade, essa é a versão utilizada, pois entregamos o arquivo (**código fonte** que fizemos) para ele e, ele compila.
   - Agora, na prática de nossa disciplina, utilizaremos mais a versão de **entrada via teclado**, pois fazemos testes mais pontuais.
