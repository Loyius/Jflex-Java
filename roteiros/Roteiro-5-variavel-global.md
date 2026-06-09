# Página 1

Compiladores
Roteiro - Parser Code- Vari´ avel Global
1 Introdu¸ c˜ ao
Neste ROTEIRO estudaremos uma ferramenta muito legal que nos d´ a a possibilidade de INSERIR
c´ odigo javaem nosso parser .
Lembre-se que: O parser ´ e produzido pelo JCup , portanto, ele ´ e um“corpo fechado”, pois
n˜ ao somos n´ os que o programamos, mas sim o Gerador Autom´ atico!!!
Por isso, esse ROTEIRO ´ e t˜ ao especial, pois aprenderemos uma forma de INCLUIR c´ odigo
java nele e, com isso, SURGE uma janela de oportunidades para a cria¸ c˜ ao do nosso compilador.
Um exemplo, ´ e a possibilidade de CRIAR vari´ aveis globais, que podem ser usadas em TO-
DAS as regras de produ¸ c˜ aoao MESMO TEMPO.
Na verdade, vocˆ e ver´ a que oparser code {: : } INSERE o c´ odigo javacomo um atributo
ou m´ etododa classe parser e, por isso, conseguimos referenci´ a-losem TODA nossa Gram´ atica.
Anteriormente, fizemos o seguinte exemplo de reconhecedor de express˜ oes:
- Neste exemplo, o Analisador RECONHECE e CALCULA o valor de TODO tipo de express˜ ao
matem´ atica, inclusive com designadores.
Para este exemplo, temos o seguinte c´ odigo:
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 2

Compiladores
1.1 Arquivo Exemplo.flex
import java cup.runtime.Symbol;
%%
%class Scanner
%cup
%unicode
%line
%column
%public
%{
public Scanner (java.io.InputStream in) {
this (new java.io.InputStreamReader (in,
java.io.charset.Charset.forName (‘‘UTF-8’’)));
}
%}
// Defini¸ c~ oes de macros (ajuste conforme o seu arquivo .flex)
digito = [0-9]
letra = [a-zA-Z]
digitos = [0-9]+
ident = {letra} ({letra} | {digito})∗
fimdeLinha = \r | \n | \r\n
espaco = {fimdeLinha} | [ \t\f ]
%%
{digitos} {
double aux = Double.parseDouble (yytext());
return new Symbol (sym.NUMBER, Double.valueOf (aux));
}
‘‘if’’ { return new Symbol (sym.KW IF); }
{ident} { return new Symbol (sym.IDENT, yytext()); }
}
‘‘ + ’’ { return new Symbol(sym.MAIS); }
‘‘ - ’’ { return new Symbol(sym.MENOS); }
‘‘ / ’’ { return new Symbol(sym.DIV); }
‘‘ * ’’ { return new Symbol(sym.MULT); }
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 3

Compiladores
‘‘ % ’’ { return new Symbol(sym.MOD); }
‘‘ ; ’’ { return new Symbol(sym.PTVIRG); }
‘‘ ( ’’ { return new Symbol(sym.ABRE PARENT); }
‘‘ ) ’’ { return new Symbol(sym.FECHA PARENT); }
‘‘ [ ’’ { return new Symbol(sym.ABRE COLCH); }
‘‘ ] ’’ { return new Symbol(sym.FECHA COLCH); }
‘‘ . ’’ { return new Symbol(sym.PTO); }
{espaco} { /* nao faz nada */ }
// Lembrando que deve estar aqui embaixo por causa da precedencia com as palavras chave
{Ident} { return new Symbol (sym.IDENT, yytext()); }
{[ ∧ ]} { /* Caractere inv´ alido */
return new Symbol (sym.EOF,yyline, yycolumn, yytext());
}
1.2 Arquivo Exemplo.cup
import java cup.runtime.*;
/* TERMINAIS */
terminal PTVIRG, VIRG, PTO, MAIS, MENOS, DIV, MULT, MOD, IDENT;
terminal Double NUMBER, IDENT;
/* N ˜AO TERMINAIS */
non terminal expr list, expr ptv;
non terminal Double expr, term, factor, designator;
expr list ::= expr list expr ptv
| expr ptv;
expr ptv ::= expr:e {: System.out.println(‘‘= ’’ + e); : } PTVIRG;
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 4

Compiladores
expr ::= expr:e MAIS term:t {: RESULT = e + t; : }
| expr:e MENOS term:t {: RESULT = e - t; : }
| MENOS term:t {: RESULT = -t; }
| term:t {: RESULT = t.doubleValue(); : } ;
term ::= factor:f MULT term:t {: RESULT = f * t; : }
| factor:f DIV term:t {: RESULT = f / t; : }
| factor:f {: RESULT = f.doubleValue(); : } ;
factor ::= NUMBER:n {: RESULT = n.doubleValue(); : }
| ABRE PARENT expr:e FECHA PARENT {: RESULT = e.doubleValue(); : }
| designator:d {: RESULT = d.doubleValue(); } ;
designator ::= designator ABRE COLCH expr FECHA COLCH
{: RESULT = Double.valueOf (1.0); : }
| designator PTO IDENT
{: RESULT = Double.valueOf (1.0); : }
| IDENT {: RESULT = Double.valueOf (1.0); : } ;
Aten¸ c˜ ao:Foi colocado um bloco {: : } vinculado ao IDENT e, DENTRO deste bloco SEM-
PRE est´ a sendo RETORNADO ovalor 1.0 .
Isso foi feito para que a express˜ ao continue sendo computada, afinal factor SEMPRE es-
pera valor double como retorno e, N ˜AO podemos DEIXAR a recurs˜ aoQUEBRA!!!
Como N ˜AO SABEMOS o valor do IDENT , foi adotado o valor 1.0 APENAS para efeito de
teste.
1.3 Arquivo Main.java
- Neste exemplo, vamos usar a entrada pelo arquivo , sen˜ ao fica muito trabalhoso escrever toda
hora todas as express˜ oes.
Mas, na fun¸ c˜ aomain abaixo, existe uma outra op¸ c˜ ao comentadae, caso vocˆ e queira fazer a
entrada via teclado , basta descomentar a linha e comentar a chamada via arquivo .
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 5

Compiladores
import java.io.*;
import java.io.FileInputStream;
class Main {
public static void main (String[ ] args) throws Exception {
//## Para ler a entrada do teclado, use o comando abaixo:
// java.util.Scanner scanner = new java.util.Scanner(System.in);
//Para ler a entrada do arquivo
FileInputStream in = new FileInputStream (‘‘teste.txt’’);
Scanner scanner = new Scanner (in);
parser parser = new parser (scanner);
try {
parser.parse();
System.out.println (‘‘Arquivo sem erros de sintaxe!’’);
} catch (Exception e) {
System.out.println (‘‘Erro de sintaxe:’’ + e);
}
}
}
1.3.1 O Problema...
Agora, suponha que nos foi solicitado algo extra:
O Analisador DEVE CONTER quantas express˜ oesforam RECONHECIDAS!
E, tamb´ em, CALCULAR osomat´ orio totaldos SEUS valores!!!
Mas, como podemos fazer isso?
A 1a coisa que precisamos fazer ´ e ENTENDERo que est´ a sendo pedido!!!Ou seja, precisamos
APRESENTAR na TELA as seguintes informa¸ c˜ oes:
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 6

Compiladores
Agora que entendemos claramente o que precisamos entregar , podemos pensar na solu¸ c˜ ao
deste problema.
Olhando para o as regras de produ¸ c˜ oes, qual delas usaremos para CONTAR quantas ex-
press˜ oes teremos?
Basta pensarmos no que significa CADA uma delas e teremos a resposta!!! Ou seja:
• A regra expr list significa lista de express˜ oes
• Portanto, ´ e NESTAregra que criaremos um bloco {: : } para fazer a CONTAGEM
• Observe que, ESTA regra tem APENAS 2 op¸ c˜ oesde deriva¸ c˜ ao. Ou seja:
expr list → expr list expr ptv ou expr list → expr ptv
Portanto:
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 7

Compiladores
Ent˜ ao,o que n´ os precisamos fazer?
Precisamos INCREMENTAR um contador SEMPRE que DERIVARMOS a regra:
expr list → expr list expr ptv
E, INCREMENTAR este mesmo contador SEMPRE que DERIVARMOS a regra:
expr list → expr ptv
Viu o tamanho do problema? Ainda n˜ ao? Observe o exemplo abaixo...
Suponha que “algum desavisado” crie este contador dentro da 1a regra, como est´ a mostrado
abaixo. O que aconteceria? Qual o problema de se fazer isso?
Os problemas s˜ ao:
• TODA e qualquer vari´ avelcriada em um bloco {: : } S´O EXISTE no escopo deste bloco .
• Neste caso, criamos a vari´ avelcontador no 1o bloco
• Portanto, N˜AO FAZ SENTIDO NENHUM usar esta MESMA vari´ avelcontador no 2o bloco!
• Se fizermos isso, quando formos compilar o parser.java , ele ACUSAR ´A o cl´ assico ERRO,
informando que “a vari´ avel contadorN˜AO FOI DECLARADA neste escopo”
Ent˜ ao, estamos diante de umproblema bem grande, certo?
Aparentemente sim, mas calma que vocˆ e ver´ a que ´ e SIMPLES de resolver. . .
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 8

Compiladores
1.3.2 A solu¸ c˜ ao
O que precisamos fazer ´ e, simplesmente, ADICIONAR adicionar umatributo contador na classe
parser.java .
Ou seja, se a classe parser.java POSSUI um atributo contador , isso significa que, ele PO-
DER SER ACESSADO “globalmente”, concorda?
Portanto, TODAS as regras de produ¸ c˜ aoter˜ ao ACESSO a ele. Simples assim!
Mas como fazemos isso????
O JCup nos d´ a suporte para adicionarQUALQUER c´ odigo javana classe parser.java .
Com isso, podemos dizer que o JCup deixa de ser um “corpo fechado”!
Para isso, usaremos o comando: parse code {: : } .
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 9

Compiladores
1.4 Solu¸ c˜ ao para ocontador
No nosso exemplo, o contador ficar´ a assim:
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 10

Compiladores
1.5 Solu¸ c˜ ao para osomatorio
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 11

Compiladores
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 12

Compiladores
2 Imprimindo o relat´ orio
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 13

Compiladores
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 14

Compiladores
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 15

Compiladores
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 16

Compiladores
3 Melhoria 1 - Fun¸ c˜ ao noparser.code
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 17

Compiladores
3.1 Arquivo Exemplo02.cup
Desta forma, temos mais possibilidade de reutiliza¸ c˜ ao de c´ odigo!
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 18

Compiladores
4 Melhoria 2 - Fun¸ c˜ aoMain
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 19

Compiladores
4.1 Arquivo Main.java
Essa ´ e a beleza daOrienta¸ c˜ ao ` a Objetos, pois ela permite V ´ARIAS formas de trabalho.
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 20

Compiladores
5 Atividades
5.1 Atividade 1
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 21

Compiladores
5.2 Atividade 2
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global


# Página 22

Compiladores
Prof.: Alessandra Hauck Roteiro - Parser Code - Vari´ avel Global
