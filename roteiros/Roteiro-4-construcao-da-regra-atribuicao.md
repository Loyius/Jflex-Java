# Página 1

Compiladores
Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao
1 Introdu¸ c˜ ao
Agora que SEU Compilador j´ areconhece declara¸ c˜ oes de vari´ aveise express˜ oes matem´ aticas,
vamos dar mais um passo essencial:
Ensinar SEU Compilador a RECONHECER o comando de atribui¸ c˜ ao.
Ou seja, instru¸ c˜ oes do tipo:
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao


# Página 2

Compiladores
2 O que ´ e um Comando de Atribui¸ c˜ ao?
´E uma ORDEM SIMPLES! Ou seja:
PEGUE o valor da express˜ aodo lado DIREITO do s´ ımbolo= e
GUARDE na vari´ aveldo lado ESQUERDO!
Exemplo:
3 Nova Regra: Comando de Atribui¸ c˜ ao
A forma geral do comando de atribui¸ c˜ ao´ e:
Designator IGUAL Expr PTVIRG
Prontinho! Agora precisamos pensar em QUAL regra colocar na nossa atribui¸ c˜ ao!
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao


# Página 3

Compiladores
4 Atualizando Nossa Gram´ atica
J´ a temos aestrutura do Program, que ´ e onde colocamos TODAS as express˜ oes matem´ aticas.
Observe que:
O n˜ ao terminalexpr list est´ a DENTRO deprogram , pois ainda estamos no IN´ICIO do nosso
Analisador Sint´ atico.
Tanto ´ e que, no momento em que as regras forem crescendo ter´ ıamos que ALTERAR este
ponto.
E a hora chegou, ´ e agora!
Pense comigo... Se dentro ABRE CHAVE e FECHA CHAVE aceita uma lista de express˜ oes
matem´ aticas. Agora, vamos fazer aceitar uma lista de express˜ oese atribui¸ c˜ oes.
Veja que:
Ent˜ ao, de forma geral, basta criarmos uma regra ( statemet ) que ACEITE express˜ oesou
atribui¸ c˜ oes.
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao


# Página 4

Compiladores
Ou seja:
E, agora, teremos que CRIAR uma Lista de Statements ( statement aux ), para derivarmos
a quantidade que for necess´ ario. Ou seja:
Prontinho! Agora ´ e s´ orodar e fazer os testes!
Obs.: Agora, N ˜AO precisamos mais do n˜ ao terminalexpr list . Ent˜ ao, se vocˆ e quiser pode
retir´ a-lo. Mas se deixar ai n˜ ao dar´ a erro.
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao


# Página 5

Compiladores
5 Dica:
• NUNCA tente adicionar TUDO de uma vez!
• Ensine ao compilador um comportamento por vez !
• O comando de atribui¸ c˜ aoVEM DEPOIS da declara¸ c˜ ao de vari´ aveis, como na vida real da
programa¸ c˜ ao. Pois:
Vocˆ e S´O PODE atribuir valor a ALGO que j´ afoi declarado !!!
6 Arquivos
6.1 Arquivo parser.cup
import java cup.runtime.*;
/* TERMINAIS */
terminal PTVIRG, VIRG, PTO, MAIS, MENOS, DIV, MULT, MOD, IGUAL;
terminal ABRE PARENT, FECHA PARENT, ABRE CHAVE, FECHA CHAVE, ABRE COLCH, FECHA COLCH;
terminal KW PROGRAM, KW IF, KW ELSE, KW WHILE;
terminal IDENT;
terminal String OP RELACIONAL;
terminal Double NUMBER;
/* N ˜AO TERMINAIS */
non terminal expr list, expr ptv;
non terminal Double expr, term, factor, designator;
non terminal type, varDecl op, varDecl, varDecl aux;
non terminal statement, statement aux, program;
program ::= KW PROGRAM IDENT varDecl aux ABRE CHAVE statement aux FECHA CHAVE ;
statement aux ::= statement aux statement
| statement ;
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao


# Página 6

Compiladores
statement ::= expr ptv
| designator IGUAL expr:e PTVIRG
{: System.out.println (‘‘Atribuicao reconhecida: ’’ + e); : } ;
type ::= IDENT ABRE COLCH FECHA COLCH
| IDENT ;
varDecl aux ::= varDecl varDecl aux
| /* vazio */ ;
varDecl ::= type IDENT:id
{: System.out.println (‘‘Declaracao de variavel:’’+ id); : } ;
varDecl op PTVIRG
//so para dar uma quebra de linha
{: System.out.println (‘‘ ’’); : } ;
varDecl op ::= varDecl op VIRG IDENT:id
{: System.out.println (‘‘, ’’+id); : } ;
| /* vazio */ ;
expr ptv ::= expr:e {: System.out.println(‘‘= ’’ + e); : } PTVIRG; }
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
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao


# Página 7

Compiladores
6.2 Arquivo scanner.flex
Precisamos GARANTIR que o s´ ımbolo= est´ ano scanner. Ou seja:
‘‘=’’ { return new Symbol (sym.IGUAL); }
Portanto, se ainda n˜ ao colocou,adicione!
import java cup.runtime.Symbol;
%%
%class Scanner
%cupsym sym
%cup
%unicode
%line
%column
%public
// Defini¸ c~ oes de macros (ajuste conforme o seu arquivo .flex)
digito = [0-9]
letra = [a-zA-Z]
digitos = [0-9]+
opRelacional = ‘‘>’’ | ‘‘<’’ | ‘‘>=’’ | ‘‘<=’’ | ‘‘==’’ | ‘‘!=’’
Ident = {letra} ({letra} | {digito})∗
fimdeLinha = \r | \n | \r\n
espaco = {fimdeLinha} | [ \t\f ]
%%
{digitos} {
double aux = Double.parseDouble (yytext());
return new Symbol (sym.NUMBER, Double.parseDouble (aux));
}
‘‘program’’ { return new Symbol (sym.KW PROGRAM); }
{opRelacional} {
String opRelacional = yytext();
return new Symbol (sym.OP RELACIONAL, opRelacional);
// Foi Modificado para OP RELACIONAL, n~ ao opRelacional symbol
}
‘‘ + ’’ { return new Symbol(sym.MAIS); }
‘‘ - ’’ { return new Symbol(sym.MENOS); }
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao


# Página 8

Compiladores
‘‘ / ’’ { return new Symbol(sym.DIV); }
‘‘ * ’’ { return new Symbol(sym.MULT); }
// Vamos manter ainda o MOD, mas a gram´ atica acima n~ ao usa
‘‘ % ’’ { return new Symbol(sym.MOD); }
‘‘ ; ’’ { return new Symbol(sym.PTVIRG); }
‘‘ ( ’’ { return new Symbol(sym.ABRE PARENT); }
‘‘ ) ’’ { return new Symbol(sym.FECHA PARENT); }
‘‘ { ’’ { return new Symbol(sym.ABRE CHAVE); }
‘‘ } ’’ { return new Symbol(sym.FECHA CHAVE); }
‘‘ [ ’’ { return new Symbol(sym.ABRE COLCH); }
‘‘ ] ’’ { return new Symbol(sym.FECHA COLCH); }
‘‘ . ’’ { return new Symbol(sym.PTO); }
‘‘ , ’’ { return new Symbol(sym.VIRG); }
‘‘ = ’’ { return new Symbol(sym.IGUAL); }
{Ident} { return new Symbol (sym.IDENT, yytext()); }
{espaco} { /* despreza */ }
{[ ∧ ]} { /* Caractere inv´ alido */
return new Symbol (sym.EOF,yyline, yycolumn, yytext());
}
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao


# Página 9

Compiladores
6.3 Arquivo Main.java
import java.io.*;
class Main {
public static void main (String[ ] args) throws Exception {
//## Para ler a entrada do teclado, use o comando abaixo:
// java.util.Scanner scanner = new java.util.Scanner(System.in);
//Para ler a entrada do arquivo
FileReader in = new FileReader (‘‘teste.txt’’);
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
Ao executar o arquivo de entrada teste.txt, teremos a seguinte sa´ ıda:
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao


# Página 10

Compiladores
7 Dica:
• NUNCA tente adicionar tudo de uma vez!
• ENSINE seu Compilador um comportamento por vez!
• O comando de atribui¸ c˜ aovem DEPOIS da declara¸ c˜ ao de vari´ aveis(como ocorre na maioria
das Linguagens de Programa¸ c˜ ao da vida real).
• Vocˆ e S´O PODE ATRIBUIR Aalgo que j´ a foideclarado!!!
8 Conclus˜ ao
Com esse NOVO comando , SEU compilador j´ a ENTENDE a base de qualquer algoritmo
real!!! Pois, ele consegue:
• Declarar vari´ aveis
• Atribuir valores
• Fazer contas
Portanto, estamos prontos para as pr´ oximas etapas!
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Atribui¸ c˜ ao
