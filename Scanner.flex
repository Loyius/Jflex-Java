%%
%public
%class Scanner
%standalone
%unicode
%line
%column
%state COMMENT

%{
  private int commentStartLine = -1;
  private int commentStartColumn = -1;

  private void printToken(String tipo, String valor) {
    System.out.println("[" + (yyline + 1) + "," + (yycolumn + 1) + "] " + tipo + ": " + valor);
  }

  private void printError(String mensagem) {
    System.out.println("[" + (yyline + 1) + "," + (yycolumn + 1) + "] ERRO_LEXICO: " + mensagem);
  }
%}

DIGIT       = [0-9]
LETTER      = [a-zA-Z]
ID_START    = ({LETTER}|_)
ID_PART     = ({LETTER}|{DIGIT}|_)
IDENT       = {ID_START}{ID_PART}*
INT         = {DIGIT}+
FLOAT       = {DIGIT}+"."{DIGIT}+
HEX         = 0x[0-9a-fA-F]+
WS          = [ \t\f]+
NEWLINE     = \r\n|\r|\n
CHAR_CONST  = \'([^\\'\n\r]|\\[btnr'\"\\])\'

%%

<YYINITIAL> {
  "program"               { printToken("KEYWORD", yytext()); }
  "final"                 { printToken("KEYWORD", yytext()); }
  "class"                 { printToken("KEYWORD", yytext()); }
  "void"                  { printToken("KEYWORD", yytext()); }
  "if"                    { printToken("KEYWORD", yytext()); }
  "else"                  { printToken("KEYWORD", yytext()); }
  "while"                 { printToken("KEYWORD", yytext()); }
  "return"                { printToken("KEYWORD", yytext()); }
  "read"                  { printToken("KEYWORD", yytext()); }
  "print"                 { printToken("KEYWORD", yytext()); }
  "new"                   { printToken("KEYWORD", yytext()); }

  "=="                    { printToken("RELOP", yytext()); }
  "!="                    { printToken("RELOP", yytext()); }
  ">="                    { printToken("RELOP", yytext()); }
  "<="                    { printToken("RELOP", yytext()); }
  ">"                     { printToken("RELOP", yytext()); }
  "<"                     { printToken("RELOP", yytext()); }
  "="                     { printToken("ASSIGN", yytext()); }
  "+"                     { printToken("ADDOP", yytext()); }
  "-"                     { printToken("ADDOP", yytext()); }
  "*"                     { printToken("MULOP", yytext()); }
  "/"                     { printToken("MULOP", yytext()); }
  "%"                     { printToken("MULOP", yytext()); }
  ";"                     { printToken("SEMICOLON", yytext()); }
  ","                     { printToken("COMMA", yytext()); }
  "."                     { printToken("DOT", yytext()); }
  "("                     { printToken("LPAREN", yytext()); }
  ")"                     { printToken("RPAREN", yytext()); }
  "{"                     { printToken("LBRACE", yytext()); }
  "}"                     { printToken("RBRACE", yytext()); }
  "["                     { printToken("LBRACKET", yytext()); }
  "]"                     { printToken("RBRACKET", yytext()); }

  {FLOAT}                 { printToken("FLOAT", yytext()); }
  {HEX}                   { printToken("INT_HEX", yytext()); }
  {INT}                   { printToken("INT", yytext()); }
  {CHAR_CONST}            { printToken("CHAR_CONST", yytext()); }
  {IDENT}                 { printToken("IDENT", yytext()); }

  "0X"[0-9a-fA-F]+        { printError("hexadecimal invalido (use prefixo 0x minusculo): " + yytext()); }
  "0x"                    { printError("hexadecimal invalido (faltando digitos apos 0x)"); }
  {DIGIT}+"."             { printError("float invalido (faltando parte decimal): " + yytext()); }
  "."{DIGIT}+             { printError("float invalido (faltando parte inteira): " + yytext()); }

  "/*"                    {
                            commentStartLine = yyline + 1;
                            commentStartColumn = yycolumn + 1;
                            yybegin(COMMENT);
                          }

  {WS}                    { /* ignora espacos e tabs */ }
  {NEWLINE}               { /* ignora quebra de linha */ }
}

<COMMENT> {
  "/*"                    { printError("comentario aninhado nao permitido"); }
  "*/"                    { yybegin(YYINITIAL); }
  {NEWLINE}               { /* mantem contagem de linha/coluna */ }
  [^*\n\r]+               { /* consome conteudo comum do comentario */ }
  "*"                     { /* consome asterisco isolado */ }
  <<EOF>>                 {
                            System.out.println("[" + commentStartLine + "," + commentStartColumn + "] ERRO_LEXICO: comentario de multiplas linhas nao fechado");
                            return YYEOF;
                          }
}

[^]                       { printError("simbolo invalido: " + yytext()); }
