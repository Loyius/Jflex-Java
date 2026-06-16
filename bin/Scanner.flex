/*
 * Scanner.flex — Analisador Léxico para a linguagem Java--
 * Compatível com JCup: usa %cup e retorna java_cup.runtime.Symbol
 *
 * Gerar com: java -jar jflex-full-1.9.1.jar Scanner.flex
 *
 * Linguagem: Java-- (Compiladores — 7o CC)
 * Grupo: Mayssa Barbosa Dias; Larissa Queiroz Ramos; Fernando Medeiros; Matheus Augusto
 */

import java_cup.runtime.Symbol;

%%

%public
%class Scanner
%unicode
%cup
%line
%column
%state COMMENT

%{
  private int commentStartLine = -1;
  private int commentStartColumn = -1;

  private java.util.List<Token> tokens = new java.util.ArrayList<>();
  private java.util.List<String> errosLexicos = new java.util.ArrayList<>();

  public java.util.List<Token> getTokens() { return tokens; }
  public java.util.List<String> getErros() { return errosLexicos; }

  /* Cria Symbol sem valor e armazena o token */
  private Symbol symbol(int type) {
    tokens.add(new Token(type, yytext(), yyline + 1, yycolumn + 1));
    return new Symbol(type, yyline + 1, yycolumn + 1);
  }

  /* Cria Symbol com valor e armazena o token */
  private Symbol symbol(int type, Object value) {
    tokens.add(new Token(type, yytext(), yyline + 1, yycolumn + 1));
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
  }
%}

/* ── Macros ────────────────────────────────────────────────────────── */
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
CHAR_CONST  = \'([^\\\'\n\r]|\\[btnr\'\"\\])\'
STRING_LIT  = \"[^\"\n]*\"

%%

<YYINITIAL> {

  /* ── Palavras reservadas ────────────────────────────────────────── */
  "program"     { return symbol(sym.KW_PROGRAM,  yytext()); }
  "final"       { return symbol(sym.KW_FINAL,    yytext()); }
  "class"       { return symbol(sym.KW_CLASS,    yytext()); }
  "void"        { return symbol(sym.KW_VOID,     yytext()); }
  "int"         { return symbol(sym.KW_INT,      yytext()); }
  "float"       { return symbol(sym.KW_FLOAT,    yytext()); }
  "boolean"     { return symbol(sym.KW_BOOLEAN,  yytext()); }
  "char"        { return symbol(sym.KW_CHAR,     yytext()); }
  "String"      { return symbol(sym.KW_STRING,   yytext()); }
  "if"          { return symbol(sym.KW_IF,       yytext()); }
  "else"        { return symbol(sym.KW_ELSE,     yytext()); }
  "while"       { return symbol(sym.KW_WHILE,    yytext()); }
  "for"         { return symbol(sym.KW_FOR,      yytext()); }
  "return"      { return symbol(sym.KW_RETURN,   yytext()); }
  "new"         { return symbol(sym.KW_NEW,      yytext()); }
  "read"        { return symbol(sym.KW_READ,     yytext()); }
  "print"       { return symbol(sym.KW_PRINT,    yytext()); }
  "true"        { return symbol(sym.KW_TRUE,     yytext()); }
  "false"       { return symbol(sym.KW_FALSE,    yytext()); }
  "null"        { return symbol(sym.KW_NULL,     yytext()); }

  /* ── Operadores de dois caracteres (antes dos de um) ────────────── */
  "=="          { return symbol(sym.EQ,    yytext()); }
  "!="          { return symbol(sym.NEQ,   yytext()); }
  ">="          { return symbol(sym.GEQ,   yytext()); }
  "<="          { return symbol(sym.LEQ,   yytext()); }
  "&&"          { return symbol(sym.AND,   yytext()); }
  "||"          { return symbol(sym.OR,    yytext()); }

  /* ── Operadores de um caractere ─────────────────────────────────── */
  ">"           { return symbol(sym.GT,        yytext()); }
  "<"           { return symbol(sym.LT,        yytext()); }
  "="           { return symbol(sym.ASSIGN,    yytext()); }
  "+"           { return symbol(sym.PLUS,      yytext()); }
  "-"           { return symbol(sym.MINUS,     yytext()); }
  "*"           { return symbol(sym.STAR,      yytext()); }
  "/"           { return symbol(sym.SLASH,     yytext()); }
  "%"           { return symbol(sym.PERCENT,   yytext()); }
  "!"           { return symbol(sym.NOT,       yytext()); }
  ";"           { return symbol(sym.SEMICOLON, yytext()); }
  ","           { return symbol(sym.COMMA,     yytext()); }
  "."           { return symbol(sym.DOT,       yytext()); }
  "("           { return symbol(sym.LPAREN,    yytext()); }
  ")"           { return symbol(sym.RPAREN,    yytext()); }
  "{"           { return symbol(sym.LBRACE,    yytext()); }
  "}"           { return symbol(sym.RBRACE,    yytext()); }
  "["           { return symbol(sym.LBRACKET,  yytext()); }
  "]"           { return symbol(sym.RBRACKET,  yytext()); }

  /* ── Literais ───────────────────────────────────────────────────── */
  {FLOAT}       { return symbol(sym.FLOAT_LIT,   Double.valueOf(yytext())); }
  {HEX}         { return symbol(sym.INT_HEX,     Integer.decode(yytext())); }
  {INT}         { return symbol(sym.INT_LIT,     Integer.valueOf(yytext())); }
  {CHAR_CONST}  { return symbol(sym.CHAR_CONST,  yytext()); }
  {STRING_LIT}  { return symbol(sym.STRING_LIT,  yytext()); }
  {IDENT}       { return symbol(sym.IDENT,       yytext()); }

  /* ── Erros léxicos ──────────────────────────────────────────────── */
  "0X"[0-9a-fA-F]+  { String err = "ERRO LEXICO na linha " + (yyline+1) + ", col " + (yycolumn+1) + ": hexadecimal invalido (use prefixo 0x minusculo): " + yytext(); errosLexicos.add(err); System.out.println(err); }
  "0x"              { String err = "ERRO LEXICO na linha " + (yyline+1) + ", col " + (yycolumn+1) + ": hexadecimal invalido (faltando digitos apos 0x)"; errosLexicos.add(err); System.out.println(err); }
  {DIGIT}+"."       { String err = "ERRO LEXICO na linha " + (yyline+1) + ", col " + (yycolumn+1) + ": float invalido (faltando parte decimal): " + yytext(); errosLexicos.add(err); System.out.println(err); }
  "."{DIGIT}+       { String err = "ERRO LEXICO na linha " + (yyline+1) + ", col " + (yycolumn+1) + ": float invalido (faltando parte inteira): " + yytext(); errosLexicos.add(err); System.out.println(err); }

  /* ── Comentário de linha ────────────────────────────────────────── */
  "//"[^\r\n]*      { /* ignora comentário de linha */ }

  /* ── Início de comentário de bloco ──────────────────────────────── */
  "/*"          {
                  commentStartLine = yyline + 1;
                  commentStartColumn = yycolumn + 1;
                  yybegin(COMMENT);
                }

  /* ── Espaços em branco ──────────────────────────────────────────── */
  {WS}          { /* ignora espacos e tabs */ }
  {NEWLINE}     { /* ignora quebra de linha */ }
}

/* ── Estado de comentário de bloco ────────────────────────────────── */
<COMMENT> {
  "*/"          { yybegin(YYINITIAL); }
  {NEWLINE}     { /* mantem contagem de linha/coluna */ }
  [^*\n\r]+     { /* consome conteudo comum do comentario */ }
  "*"           { /* consome asterisco isolado */ }
  <<EOF>>       {
                  String err = "ERRO LEXICO na linha " + commentStartLine + ", col " + commentStartColumn + ": comentario de bloco nao fechado";
                  errosLexicos.add(err);
                  System.out.println(err);
                  return symbol(sym.EOF);
                }
}

/* ── Caractere inválido (catch-all) ───────────────────────────────── */
[^]             { String err = "ERRO LEXICO na linha " + (yyline+1) + ", col " + (yycolumn+1) + ": simbolo invalido: " + yytext(); errosLexicos.add(err); System.out.println(err); }
