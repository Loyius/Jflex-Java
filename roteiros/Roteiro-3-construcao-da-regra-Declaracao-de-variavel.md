# Página 1

Compiladores
Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel
1 Introdu¸ c˜ ao
Neste ROTEIRO, vamosensinar ao nosso Analisador comoreconhecer declara¸ c˜ oes de vari´ aveis,
tanto simples, quanto vetores.
Vamos, tamb´ em, PERMITIR que ousu´ ario declareM´ULTIPLAS vari´ aveisna MESMA linha ,
separadas por v´ ırgulas(como fazemos normalmente em Java ou C.
At´ e aqui, nosso compilador j´ aentende bem express˜ oescomo:
Mas ele ainda N ˜AO reconhece a declara¸ c˜ ao das vari´ aveis, o que ´ e fundamental para um
programa real!
Veja este trecho:
Nosso parser ainda N˜AO SABE interpretar isso! Ent˜ ao,vamos ensin´ a-lo a fazer isso!
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 2

Compiladores
2 Novas Regras que Vamos Criar
2.1 Tipo ( Type)
Vamos come¸ car DEFININDO ostipos de vari´ aveisque o compilador PODE entender.
Ent˜ ao, como sempre fazemos, vamos colocar alguns exemplos para nos nortear...
Neste exemplo estamos construindo APENAS o tipo , ou seja, isso ´ e, apenas, a 1a parte.
Assim, de forma simples, a regra ser´ a:
O IDENT acima representa o nome do tipo . Ele pode ser um tipo primitivo como int ou
float , ou at´ e umnome de classe, como Pessoa .
Obs.: No SEU scanner o IDENT DEVE ser colocado l´ a no final, para N ˜AO ter precedˆ encias
sobre as palavras reservadas . Aten¸ c˜ ao com isso!
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 3

Compiladores
2.2 Declara¸ c˜ ao de Vari´ avel (VarDecl)
Agora, vamos CRIAR a regra da declara¸ c˜ aoCOMPLETA de vari´ aveis!
E, agora, temos um exemplo completo declara¸ c˜ ao de vari´ aveis:
Assim, pensando de forma simples, a declara¸ c˜ ao de vari´ avel´ e:
Portanto, a declara¸ c˜ ao de vari´ avelSEMPRE ´ e:
• Um tipo ;
• Um identificador ( nome da vari´ avel);
• E o ponto e v´ ırgula; no final.
Prontinho! O processo ´ e simples quando pensamos no primeiro exemplo.
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 4

Compiladores
2.3 Complemento da Declara¸ c˜ ao (VarDecl op)
Agora que conclu´ ımos a declara¸ c˜ ao de vari´ avelSIMPLES , vamos pensar na declara¸ c˜ ao de
M´ULTIPLAS vari´ avel.
Ou seja, as Linguagens de Programa¸ c˜ aoACEITAM a seguinte declara¸ c˜ ao:
A ideia aqui ´ e CRIAR APENAS aparte que vem a partir da virgula (por exemplo, , b ).
E, se olharmos apenas para o exemplo abaixo:
Rascunhando...
Porem, podemos ter quantas declara¸ c˜ oesdesejarmos, como por exemplo:
• int a, b, c, d;
Por isso, basta fazermos uma recurs˜ aoda seguinte forma:
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 5

Compiladores
Finalmente, vamos JUNTAR ` aregra VarDecl , que fizemos anteriormente.
Veja que, a regra VarDecl estava assim:
Agora, a regra VarDecl vai ficar assim:
Veja como as regras est˜ ao sendo constru´ ıdas de formatranquila e did´ atica. Mas ainda falta
um detalhe...
Vocˆ e percebeu que, no exemplo acima, precisamos fazer VarDecl op contemplar o vazio?
Pois da forma como est´ a, somos obrigados a declarar MAIS de UMA vari´ avelSEMPRE . O
que ´ e um ERRO!
Portanto, para resolver isso, basta acrescentar o vazio na regra VarDecl op , e tudo fica cer-
tinho!
Finalmente, temos as regras VarDecl e VarDecl op :
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 6

Compiladores
2.4 Exemplo 1 - Declara¸ c˜ ao SIMPLES de Vari´ aveis
2.5 Exemplo 2 - Declara¸ c˜ ao de M´ULTIPLAS Vari´ aveis
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 7

Compiladores
2.6 Exemplo 3 - Declara¸ c˜ ao de VETOR
3 Dica:
O segredo para fazer um parser forte ´ e COMEC ¸ AR pelos exemplos que vocˆ e quer que SEU Com-
pilador ACEITE! E, depois, MODELAR suas regras a partir destes exemplos.
Veja que, TODOS os exemplos funcionaram gra¸ cas ` arecurs˜ ao ` a esquerdada regra VarDecl op .
Mas cuidado! A recurs˜ ao ` a esquerdaPODE GERAR PROBLEMA noShift-Reduce, quando
juntar com a gram´ atica toda.
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 8

Compiladores
4 Juntando com NOSSA Gram´ atica
Para SUA Gramatica ACEITAR tanto express˜ oesquanto declara¸ c˜ ao de vari´ aveis, precisamos
CRIAR uma regra PAI .
Neste caso, vamos ajustar o formato de nossa Linguagem Java--, que definimos nas primeiras
aulas.
E, pelo que vimos, as declara¸ c˜ ao de vari´ aveisDEVEM acontecer ANTES do bloco do pro-
grama.
Ficando algo deste tipo:
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 9

Compiladores
Defini¸ c˜ ao daLinguagem Java-- (n˜ ao podemos esquecer!)
Program → ‘‘program’’ ident {ConstDecl | VarDecl | ClassDecl}
‘‘{’’ {MethodDecl} ‘‘}’’
ConstDecl → ‘‘final’’ Type ident ‘‘ = ’’ (number | chaConst) ‘‘ ; ’’
VarDecl → Type ident {‘‘ , ’’ ident} “ ; ”
ClassDecl → ‘‘class’’ ident ‘‘{’’ {VarDecl} ‘‘}’’
MethodDecl → (Type | ‘‘void’’) ident ‘‘(’’ [ FormPars ] ‘‘)’’ {VarDecl} Block
FormPars → Type ident {‘‘ , ’’ Type ident}
Type → ident [ ‘‘[ ]’’]
Block → ‘‘{’’ {Statement} ‘‘}’’
Statement → Designator {‘‘ = ’’ Expr | ActPars} ‘‘ ; ’’
| ‘‘if’’ ‘‘(’’ Condition ‘‘)’’ Statement [‘‘else’’ Statement]
| ‘‘while’’ ‘‘(’’ Condition ‘‘)’’ Statement
| ‘‘return’’ [ Expr ] ‘‘ ; ’’
| ‘‘read’’ ‘‘(’’ Designator ‘‘) ; ’’
| ‘‘print’’ ‘‘(’’ Expr [‘‘ , ’’ number ] ‘‘)’’ ‘‘ ; ’’
| Block
| ‘‘ ; ’’
ActPars → ‘‘(’’ [ Expr {‘‘ , ’’ Expr ]‘‘)’’
Condition → Expr Relop Expr
Relop → ‘‘==’’ | ‘‘!=’’ | ‘‘>’’ | ‘‘>=’’ | ‘‘<’’ | ‘‘<=’’
Expr → [‘‘ - ’’] Term {Addop Term}
Term → Factor { Mulop Factor}
Factor → Designator [ ActPars ] | number | charConst
| ‘‘new’’ ident [‘‘[’’ Expr ‘‘]’’]
| ‘‘(’’ Expr ‘‘)’’
Designator → ident {‘‘ · ’’ ident | ‘‘[’’ Expr ‘‘]’’}
Addop → ‘‘+’’ | ‘‘-’’
Mulop → ‘‘*’’ | ‘‘/’’ | ‘‘%’’
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 10

Compiladores
Ent˜ ao, basicamente vamos:
1) Criar a regra B ´ASICA de Program; e
2) Criar uma recurs˜ aopara podermos CRIAR quantas varDecl desejarmos, ou seja, vamos CRIAR
o n˜ ao terminalvarDecl aux .
Portanto, a regra varDecl aux vai ficar assim:
Ao fazer isso, nosso parser est´ a bem legal,aceitando declara¸ c˜ oes de vari´ aveisno in´ ıcio do
programa e uma lista de express˜ oes.
Agora vamos rodar/testar e ver que legal!
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 11

Compiladores
4.1 Arquivo parser.cup
import java cup.runtime.*;
/* TERMINAIS */
terminal PTVIRG, PTO, VIRG, MAIS, MENOS, DIV, MULT, MOD, OP RELACIONAL;
terminal ABRE PARENT, FECHA PARENT, ABRE CHAVE, FECHA CHAVE, ABRE COLCH, FECHA COLCH;
terminal KW PROGRAM;
terminal Double NUMBER;
terminal String IDENT;
/* N~AO TERMINAIS */
non terminal expr list, expr ptv;
non terminal Double expr, term, factor, designator;
non terminal type, varDecl op, varDecl, varDecl aux;
non terminal program;
program ::= KW PROGRAM IDENT varDecl aux ABRE CHAVE expr list FECHA CHAVE;
type ::= IDENT ABRE COLCH FECHA COLCH
| IDENT ;
varDecl aux ::= varDecl varDecl aux
| /* vazio */ ;
varDecl ::= type IDENT:id
{: System.out.print (‘‘Declaracao variavel:’’+id); : }
varDecl op PTVIRG
//so para dar uma quebra de linha
{: System.out.println (‘‘ ’’); : };
varDecl op ::= varDecl op VIRG IDENT:id
{: System.out.print (‘‘, ’’+id); : }
| /* vazio */ ;
expr list ::= expr list expr ptv
| expr ptv;
expr ptv ::= expr:e {: System.out.println(‘‘= ’’ + e); : } PTVIRG; }
expr ::= expr:e MAIS term:t {: RESULT = e + t; : }
| expr:e MENOS term:t {: RESULT = e - t; : }
| MENOS term:t {: RESULT = -t; : }
| term:t {: RESULT = t.doubleValue(); : };
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 12

Compiladores
term ::= factor:f MULT term:t {: RESULT = f * t; : }
| factor:f DIV term:t {: RESULT = f / t; : }
| factor:f {: RESULT = f.doubleValue(); : };
factor ::= NUMBER:n {: RESULT = n.doubleValue(); : }
| ABRE PARENT expr:e FECHA PARENT {: RESULT = e.doubleValue(); : }
| designator:d {: RESULT = d.doubleValue(); };
designator ::= designator ABRE COLCH expr FECHA COLCH
{: RESULT = Double.valueOf (1.0); : }
| designator PTO IDENT
{: RESULT = Double.valueOf (1.0); : }
| IDENT {: RESULT = Double.valueOf (1.0); : };
// designator ::= IDENT designator sufixo
// {: RESULT = 1.0; : };
// designator sufixo ::= PTO IDENT designator sufixo
// | ABRE COLCH expr FECHA COLCH designator sufixo
// | /* vazio */ ;
A ideia ´ e essa mesma, nada de complicado, agora ´ e s´ o rodar!
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 13

Compiladores
4.2 Arquivo scanner.flex
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
espaco = {fimdeLinha} |[ \t\f ]
%%
{digitos} {
double aux = Double.parseDouble (yytext());
return new Symbol (sym.NUMBER, aux);
}
‘‘program’’ { return new Symbol (sym.KW PROGRAM); }
{opRelacional} {
String opRelacional = yytext();
return new Symbol (sym.OP RELACIONAL, opRelacional);
// Foi Modificado para OP RELACIONAL, n~ ao opRelacional symbol
}
‘‘ + ’’ { return new Symbol(sym.MAIS); }
‘‘ - ’’ { return new Symbol(sym.MENOS); }
‘‘ / ’’ { return new Symbol(sym.DIV); }
‘‘ * ’’ { return new Symbol(sym.MULT); }
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 14

Compiladores
// Vamos manter ainda o MOD, mas a gram´ atica acima n~ ao usa
‘‘ % ’’ { return new Symbol(sym.MOD); }
‘‘ ; ’’ { return new Symbol(sym.PTVIRG); }
‘‘ ( ’’ { return new Symbol(sym.ABRE PARENT); }
‘‘ ) ’’ { return new Symbol(sym.FECHA PARENT); }
‘‘ { ’’ { return new Symbol(sym.FECHA CHAVE); }
‘‘ } ’’ { return new Symbol(sym.FECHA CHAVE); }
‘‘ [ ’’ { return new Symbol(sym.FECHA COLCH); }
‘‘ ] ’’ { return new Symbol(sym.FECHA COLCH); }
‘‘ .’’ { return new Symbol(sym.PTO); }
‘‘ , ’’ { return new Symbol(sym.VIRG); }
{Ident} { return new Symbol (sym.IDENT, yytext()); }
{espaco} { /* despreza */ }
{[ ∧ ]} { /* Caractere inv´ alido */
// Retornar EOF ou um s´ ımbolo de erro para continuar a an´ alise
return new Symbol (sym.EOF);
}
4.3 Arquivo Main.java
Agora, vamos usar a entrada pelo arquivo , sen˜ ao fica muito trabalhoso escrever toda hora todas
as express˜ oes.
Mas na fun¸ c˜ aomain abaixo, veja que, existe a outra op¸ c˜ aocomentada , caso vocˆ e queira
fazer a entrada via teclado , basta descomentar a linha e comentar a chamada via arquivo .
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 15

Compiladores
import java.io.*;
class Main {
public static void main (String[ ] args) throws Exception {
// Para ler a entrada do teclado
// Scanner scanner = new Scanner(System.in);
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
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel


# Página 16

Compiladores
5 Conclus˜ ao
Agora, SEU Compilador ENTENDE quando vocˆ edeclara vari´ aveis, sejam vari´ aveisSIMPLES ,
vetores , ou m´ ultiplas vari´ aveisde uma vez .
Essa capacidade ´ e fundamental para validarmos, no futuro, se uma vari´ avelest´ adeclarada
ANTES de ser usada , ou se a vari´ avelJ´A FOI declarada duas vezes .
Prof.: Alessandra Hauck Roteiro - Constru¸ c˜ ao da Regra: Declara¸ c˜ ao de Vari´ avel
