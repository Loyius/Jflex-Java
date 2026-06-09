# Roteiro 10 - Tratamento de Erro

## 1 Introdução

No ROTEIRO 8 criamos toda a base para Notificação de Erros. Nós usamos a estratégia de criar uma regra errada para capturar o erro.

- Só que, dependendo das infinitas possibilidades, isso se torna inviável, por isso usaremos o Especial Error do JCup
- O símbolo `error`
  - Desempenha o papel de um não-terminal especial
  - Ele funcionará semelhante a default da estrutura switch

### Exemplo de utilização do especial error, que tem o funcionamento semelhante ao default do switch

Aqui seguirá esta mesma lógica. Veja!

```cpp
1  void funcaoTeste(int opcao) {
2    switch (opcao) {
3      case 1:
4        calculaPrimo();
5        break;
6      case 2:
7        calculaFatorial();
8        break;
9      case 3:
10       calculaDobro();
11       break;
15     default:
16       cout << "Opção inválida!";
17   }
18 }
Ao invés de ficar criando "regras erradas" para cercar cada um dos possíveis erros, criamos apenas a regra correta, e o error que será chamado para toda e qualquer derivação que não for a correta.

Ao executar:

Executando if sem parenteses

2 Processo de Captura do Especial error
A figura abaixo ilustra bem todo o processo de captura de erros utilizando o Especial error do JCup.

Processo de captura usando especial error

Quando o parser tentar derivar um if com algum erro (faltando um parenteses, faltando uma chave...) ele vai direto para o caso error

Neste momento o método syntax_error() é disparado automaticamente pelo parser

Dai ele vai para o passo 2, no local em que nós sobrescrevemos o método syntax_error() para que ele chame a nossa classe de erros

defineErro() passo 3

Perceba que criamos o método defineErro(int linha, int coluna) com dois parâmetros justamente para que o syntax_error() o chamasse.

syntax_error() preenche apenas a linha e a coluna em que o erro aconteceu

Por isso, mais a frente (Figura 05) nós completaremos o erro preenchendo o texto usando a defineErro(String texto)

Com estas imagens, você pode entender todo este processo e as alterações que precisamos fazer em nossos códigos

Um ponto importante de observar é também é a criação dos wrappers para as chamadas dos novos defineErros

Alterações

1º. Criar o método defineErro(int linha, int coluna) - para ser chamado pelo syntax_error()

2º. Sobreescrever o método syntax_error()

3º. Criar o método defineErro(String texto) - para preencher a mensagem de erro

3 Diagrama de Classes
[Imagem do diagrama de classes]

4 Parser
4.1 Arquivo parser.cup
java
import java_cup.runtime.*;
import errors.ListaErrors;

/* CODIGO ADICIONAL */
parser code {
  // chamada pelo proprio PARSER quando encontra um error
  public void syntax_error(Throwable s){
    this.defineError(s.left, s.right);
    //s.left retorna a linha s.right retorna a coluna
  }

  /* Forma de acessar o Analisador Lexico da classe Parser.
     É necessário o casting >>> (scanner.Scanner) this.getScanner()
     pois o scanner da classe Parser é o do pacote java_cup.runtime.Scanner,
     portanto, é obrigatório fazer este casting aqui e toda vez que quisermos
     acessar o Yylex */
  public void defineError(int linha, int coluna, String texto){
    scanner.Scanner sc = (scanner.Scanner) this.getScanner();
    sc.defineError(linha, coluna, texto);
  }

  // usado pelo método syntax_error()
  public void defineError(int linha, int coluna){
    scanner.Scanner sc = (scanner.Scanner) this.getScanner();
    sc.defineError(linha, coluna);
  }

  // usado para completar o texto daqueles erros adicionados pelo método syntax_error()
  public void defineError(String texto){
    scanner.Scanner sc = (scanner.Scanner) this.getScanner();
    sc.defineError(texto);
  }
};

terminal MAIOR, MENOR, MAIORIGUAL, MENORIGUAL, IGUALIGUAL, DIF, KW_IF;
terminal FTVIRG, MAIS, MENOS, MULT, DIV, KW_ELSE, ABREPAR, FECHAPAR, ABRECHAVE, FECHACHAVE;
terminal Double NUMBER;
non terminal Double expr_list, expr_ptv, expr, factor, term, condicao, if, op_Relacional;
non terminal else;

if ::= KW_IF ABREPAR condicao FECHAPAR ABRECHAVE expr_list FECHACHAVE else
     | error {: parser.defineError("IF incompleto"); :}
     /* esta forma é criando regras erradas para cada erro do programador
        Pode ser interessante para alguns casos, porem ineficiente para
     | KW_IF ABREPAR condicao FECHAPAR:e {: parser.defineError(eleft, eright, "sem abre chaves"); :}
       expr_list FECHACHAVE else
     | KW_IF condicao:e {: parser.defineError(eleft, eright, "IF sem parenteses "); :}
       ABRECHAVE expr_list FECHACHAVE else */
     ;

else ::= KW_ELSE ABRECHAVE expr_list FECHACHAVE
       | /* vazio */
       ;

condicao ::= expr op_Relacional expr ;
// | error {: parser.defineError("condicao com erro"); :} // resposta exercicio

expr_list ::= expr_list expr_ptv
            | expr_ptv
            ;

expr_ptv ::= expr PTVIRG
           | error {: parser.defineError("Expressao incompleta"); :} // PTVIRG
           // se colocar ele tentará se recuperar qdo achar um PTVIRG
           ;

expr ::= expr MAIS term
       | expr MENOS term
       | MENOS term
       | term
       ;

term ::= factor MULT term
       | factor:f DIV term:t {: if(t == 0){
                                 parser.defineError(left, tright, "Error - Divisao por zero.");
                               }
                               RESULT = f/t;
                            :}
       | factor:f {: RESULT = f; :}
       ;

factor ::= NUMBER:n {: RESULT = n; :}
         | ABREPAR expr FECHAPAR
         ;

op_Relacional ::= MAIOR
                | MENOR
                | MAIORIGUAL
                | MENORIGUAL
                | IGUALIGUAL
                | DIF
                | error {: parser.defineError("Operador relacional desconhecido"); :}
                ;
5 syntax_error()
O método syntax_error(Symbol s):

é chamado pelo próprio parser quando encontra um erro de sintaxe durante a análise. Ele recebe como argumento um objeto Symbol que representa o símbolo de entrada que causou o erro.

Nesse método, é possível definir como será tratado esse erro, geralmente imprimindo uma mensagem de erro e tentando recuperar o parser para continuar a análise. No código fornecido, a mensagem de erro é definida através do método defineErro().

Se não colocarmos este método, a responsabilidade de reportar erros é inteiramente sua.

Eu particularmente, gosto de usar este método, pois, caso eu esqueça algum erro, ele captura de forma genérica.

6 Scanner
6.1 Arquivo scanner.flex
java
// 1 secao - O codigo colocado aqui, na primeira secao,
// E copiado, sem alteracoes, para o programa do usuario
package scanner;
import java_cup.runtime.Symbol;
import parser.sym;
import erros.ListaErros;

//

// 2 secao - opcoes para customizar o programa gerado
// e declaracoes de macros que podem ser usadas
// nas definicoes dos lexemes
@class Scanner
@cupsym sym
@cup
@unicode           // permite usar caracteres unicode
@line              // permite usar yyline
@column            // permite usar yycolumn
@public

@eofval{
  return criaSimbolo(sym.EOF);
@eofval}

// código inserido na classe
@{
  //atributo
  private ListaErros listaErros;

  //Redefinindo "sobrecarga" o constructor de Scanner
  //para inicializar tambem listaErros
  public Scanner(java.io.FileReader in, ListaErros listaErros) {
    this(in);
    this.listaErros = listaErros;
  }

  public ListaErros getListaErros() {
    return listaErros;
  }

  public void defineErro(int linha, int coluna, String texto) {
    listaErros.defineErro(linha, coluna, texto);
  }

  //sera usado pelo syntax_error()
  public void defineErro(int linha, int coluna) {
    listaErros.defineErro(linha, coluna);
  }

  //Nós utilizaremos para colocar legenda no local inserido pelo syntax_erro()
  //Temos que fazer assim, pois o syntax_erro é chamado automaticamente e
  //não conseguimos sobrecarrega-lo, porém conseguimos que ele preencha nossa lista de erros
  public void defineErro(String texto) {
    listaErros.defineErro(texto);
  }

  //Wrapper utilizado para facilitar a criacao de Symbol e incorporacao
  //da linha e coluna em todos os Token
  private Symbol criaSimbolo(int code, Object value) {
    return new Symbol(code, yyline, yycolumn, value);
  }

  private Symbol criaSimbolo(int code) {
    return new Symbol(code, yyline, yycolumn, null);
  }
@}

// Macros
FimdeLinha = \r|\n|\r\n
Espaco = {FimdeLinha} | [\t]
Inteiro = 0 | [1-9][0-9]*
OpMais = "+"
OpMenos = "-"
OpMult = "*"
OpDiv = "/"
PtoVirg = ";"
OpMaior = ">"
OpMenor = "<"
OpMaiorIgual = ">="
OpMenorIgual = "<="
OpIgualIgual = "=="
OpDiferente = "!="
KwIf = "if"
KwElse = "else"
abrePar = "("
fechaPar = ")"
abreChave = "{"
fechaChave = "}"

%%
{Espaco}       { /*despreza*/ }
{Inteiro}      { Double aux = Double.parseDouble(yytext());
                 return criaSimbolo(sym.NUMBER, new Double(aux));
               }
{OpMais}       { return criaSimbolo(sym.MAIS); }
{OpMenos}      { return criaSimbolo(sym.MENOS); }
{PtoVirg}      { return criaSimbolo(sym.PTVIRG); }
{OpMult}       { return criaSimbolo(sym.MULT); }
{OpDiv}        { return criaSimbolo(sym.DIV); }
{OpMaior}      { return criaSimbolo(sym.MAIOR); }
{OpMenor}      { return criaSimbolo(sym.MENOR); }
{OpMaiorIgual} { return criaSimbolo(sym.MAIORIGUAL); }
{OpMenorIgual} { return criaSimbolo(sym.MENORIGUAL); }
{OpIgualIgual} { return criaSimbolo(sym.IGUALIGUAL); }
{OpDiferente}  { return criaSimbolo(sym.DIF); }
{KwIf}         { return criaSimbolo(sym.KW_IF); }
{KwElse}       { return criaSimbolo(sym.KW_ELSE); }
{abrePar}      { return criaSimbolo(sym.ABREPAR); }
{fechaPar}     { return criaSimbolo(sym.FECHAPAR); }
{abreChave}    { return criaSimbolo(sym.ABRECHAVE); }
{fechaChave}   { return criaSimbolo(sym.FECHACHAVE); }
[^]            { /*erro*/
                 this.defineErro(yyline, yycolumn, "Lexico - Simbolo desconhecido: " + yytext());
                 //throw new Error("Caractere Ilegal <"+yytext()+">");
               }
7 Documentação
Um aspecto final importante na construção de parsers com CUP é o suporte à recuperação de erros sintáticos. Em particular, ele suporta um símbolo especial de erro (denominado error).

Esse símbolo desempenha o papel de um não-terminal especial que, em vez de ser definido por produções, corresponde a uma sequência de entrada errônea.

O símbolo error só entra em jogo se um erro sintático for detectado!

Se um erro sintático for detectado, o parser tenta substituir alguma parte do fluxo de tokens de entrada por error e, em seguida, continuar a análise.

Por exemplo, podemos ter produções como:

text
stmt ::= expr SEMI
       | while_stmt SEMI
       | if_stmt SEMI
       | ...
       | error SEMI
       ;
Isso indica que, se nenhuma das produções normais para stmt puder ser correspondida com a entrada, um erro sintático deve ser declarado e a recuperação deve ser feita saltando os tokens errôneos (equivalente a substituí-los por error) até um ponto em que a análise possa ser continuada com um ponto-e-vírgula (e contexto adicional que segue legalmente uma declaração).

Um erro é considerado recuperado se e somente se um número suficiente de tokens após o símbolo de erro puderem ser analisados com sucesso. (O número de tokens necessários é determinado pelo método error_sync_size() do parser é por padrão igual a 3).

Especificamente, o parser primeiro procura pelo estado mais próximo ao topo da pilha de análise que tenha uma transição de saída sob o error.

Isso geralmente corresponde a trabalhar a partir de produções que representam construções mais detalhadas (como um tipo específico de declaração) até produções que representam construções mais gerais ou envolventes (como a produção geral para todas as declarações ou uma produção representando uma seção inteira de declarações), até chegarmos a um lugar onde uma produção de recuperação de erro tenha sido fornecida.

7.1 Símbolos de Continuação Viáveis
Prof.: Alessandra Hauck
Roteiro - Tratamento de Erros - Especial error

8 Leitura Complementar
Qualquer parser deve ser capaz de lidar com entrada sintaticamente inválida.

Normalmente, é insatisfatório para o parser simplesmente terminar ao detectar um erro.

Os erros devem ser recuperados de alguma forma, produzindo uma mensagem de erro e continuando o processo de análise até que o final da entrada seja alcançado.

O CUP possui um símbolo "error" intrínseco (nem exatamente um terminal nem um não-terminal).

Quando um erro é detectado, uma porção do topo da pilha e uma porção da entrada seguinte são excluídas e substituídas pelo símbolo "error".

Assim, o símbolo "error" corresponde efetivamente a uma entrada arbitrária em torno da posição em que o erro foi detectado. A porção da pilha excluída, é claro, corresponde a entrada já analisada.

O símbolo de erro pode ser usado no lado direito das regras gramaticais, como em: Stmt ::= error NEWLINE;

Quando o parser não pode realizar um shift, redução ou aceitação, o parser entra no modo de erro:

O parser gera uma mensagem de erro, corta a pilha do parser até que ele tenha um estado que possa deslocar o símbolo "error" e desloca o "error" na pilha. (Se não houver um estado que possa deslocar o "error", o parser é abortado).

Ele então exclui tokens até que possa analisar com sucesso error_sync_size() tokens sem gerar outro erro. Por default o método error_sync_size() retorna o valor 3, para que normalmente o parser tenha que ser capaz de consumir 3 tokens antes que o erro seja considerado "recuperado".

Para alterar o número de tokens que precisam ser analisados para recuperar de um erro, você pode substituir o método error_sync_size()

Assim que o parser é colocado em uma configuração que possui uma recuperação de erro imediata (pulando a pilha para o primeiro estado desse tipo), o parser começa a excluir tokens para encontrar um ponto em que a análise possa ser continuada.

Após descartar cada token, o parser tenta analisar a entrada à frente (sem executar quaisquer ações semânticas incorporadas). Se o parser puder analisar com êxito além do número necessário de tokens, então a entrada é revertida para o ponto de recuperação e a análise é retomada normalmente (executando todas as ações). Se a análise não puder ser continuada por tempo suficiente, outro token é descartado e o parser tenta analisar à frente novamente.

Se o final da entrada for alcançado sem fazer uma recuperação bem-sucedida (ou se não houver um estado de recuperação de erro adequado na pilha de análise desde o início), a recuperação de erro falha.

O tipo de recuperação de erro disponível no CUP permite pouco mais do que o que é chamado de "modo de pânico" de recuperação de erro, onde a entrada é consumida até que o parser alcance um token significativo, como o final da linha, um token que pode terminar ou seguir uma declaração, ou um token que pode iniciar uma nova declaração.

Em muitos casos, existem tokens que são conhecidos por serem marcadores de fim de statement.

Stmt::= error NEWLINE; consumirá a entrada até que tenhamos uma nova linha (assumindo que novas linhas são sintaticamente importantes).

Stmt::= error SEMICOLON;

Stmt::= error RIGHTCURLY; consumirá a entrada até que tenhamos um ";" ou "}", ambos são marcadores claros para o fim de declarações em Java e C.

Este trecho explica que, em muitos casos, há tokens que indicam claramente o fim de uma declaração em uma linguagem de programação, como o ponto e vírgula (;) em Java e C.

Quando ocorre um erro de sintaxe, o parser pode usar o símbolo "error" em uma regra de gramática para consumir a entrada até que um desses tokens seja encontrado.

Por exemplo, a regra Stmt::= error SEMICOLON; consumirá a entrada até que o parser encontre um ponto e vírgula, o que indica o fim da declaração atual.

O uso de símbolos "error" deve ser limitado em uma gramática, pois o parser pode ficar preso tentando corrigir um erro específico que pode não ser apropriado.

Algumas vezes, o marcador não faz parte da própria declaração, mas é um token usado para separar declarações.

Por exemplo, Stmt:: = error; vai consumir a entrada até que tenhamos um token que possa seguir uma declaração. Isso é útil em Pascal, onde ";" separa declarações, em vez de terminá-las.

Em algumas linguagens, todas as declarações têm marcadores claros para iniciar a declaração.

Por exemplo, Stmt:: = error Stmt; vai consumir o texto até encontrar um token que possa iniciar uma nova declaração.

Finalmente
É muito importante que o símbolo de error seja usado com muita parcimônia em sua gramática.

A maneira como o analisador corta a pilha para o primeiro local em que o erro pode ser deslocado, fixa o analisador em tentar corrigir o erro para uma construção específica, e isso pode não ser apropriado.

Use a recuperação de erro apenas para construções importantes, como declarações, e avance somente para marcadores muito bem definidos para o final da declaração.

9 Resumo
Este documento apresenta a recuperação de erros sintáticos em parsers utilizando a ferramenta CUP. O CUP suporta um símbolo especial de erro que é utilizado quando um erro sintático é detectado na entrada. O símbolo error é colocado no lugar da entrada errônea e o parser descarta os tokens seguintes até que seja possível continuar a análise sem gerar outro erro.

É importante lembrar que o uso do símbolo error deve ser limitado a construções importantes, como declarações, e avançando somente para marcadores bem definidos para o final da declaração.

Por exemplo, Stmt::= error SEMICOLON; consome a entrada até que um ponto e vírgula seja encontrado, que é um marcador claro para o final de uma declaração em Java e C.

Além disso, o documento destaca que a recuperação de erro disponível no CUP permite pouco mais do que o que é chamado de "modo de pânico" de recuperação de erro, onde a entrada é consumida até que o parser alcance um token significativo, como o final da linha, um token que pode terminar ou seguir uma declaração, ou um token que pode iniciar uma nova declaração.

Segue abaixo alguns exemplos de regras de gramática que utilizam o símbolo error para recuperação de erros:

Stmt::= error NEWLINE; consome a entrada até que um caractere de nova linha seja encontrado.

Stmt::= error SEMICOLON; consome a entrada até que um ponto e vírgula seja encontrado.

Stmt::= error RIGHTCURLY; consome a entrada até que uma chave direita seja encontrada.

Stmt::= error; consome a entrada até que um token que possa seguir uma declaração seja encontrado.

Stmt::= error Stmt; consome a entrada até que um token que possa iniciar uma nova declaração seja encontrado.