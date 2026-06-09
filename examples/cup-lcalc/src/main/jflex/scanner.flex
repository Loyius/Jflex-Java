import java_cup.runtime.*;
import erros.ListaErros;

%%

%class Lexer
%line
%column
%cup

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

    private ListaErros listaErros;

    public Lexer(java.io.FileReader in, ListaErros listaErros) {
        this(in);
        this.listaErros = listaErros;
    }

    public ListaErros getListaErros() { return listaErros; }

    public void defineErro(int linha, int coluna, String texto) {
        if (listaErros != null) listaErros.defineErro(linha, coluna, texto);
    }

    public void defineErro(int linha, int coluna) {
        if (listaErros != null) listaErros.defineErro(linha, coluna);
    }

    public void defineErro(String texto) {
        if (listaErros != null) listaErros.defineErro(texto);
    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
dec_num_lit    = (0 | [1-9][0-9]*)(\.[0-9]+)?
dec_int_id     = [A-Za-z_][A-Za-z_0-9]*

%%

<YYINITIAL> {

    ";"     { return symbol(sym.SEMI); }
    "+"     { System.out.print(" + "); return symbol(sym.PLUS); }
    "-"     { System.out.print(" - "); return symbol(sym.MINUS); }
    "*"     { System.out.print(" * "); return symbol(sym.TIMES); }
    "/"     { System.out.print(" / "); return symbol(sym.DIVIDE); }
    "%"     { System.out.print(" % "); return symbol(sym.MOD); }
    "("     { System.out.print(" ( "); return symbol(sym.LPAREN); }
    ")"     { System.out.print(" ) "); return symbol(sym.RPAREN); }
    "."     { return symbol(sym.PTO); }
    "["     { return symbol(sym.ABRE_COLCH); }
    "]"     { return symbol(sym.FECHA_COLCH); }
    ","     { return symbol(sym.VIRG); }
    "{"     { return symbol(sym.ABRE_CHAVE); }
    "}"     { return symbol(sym.FECHA_CHAVE); }

    // operadores relacionais (== e != antes de = e !)
    "=="    { return symbol(sym.IGUALIGUAL); }
    "!="    { return symbol(sym.DIF); }
    ">="    { return symbol(sym.MAIORIGUAL); }
    "<="    { return symbol(sym.MENORIGUAL); }
    ">"     { return symbol(sym.MAIOR); }
    "<"     { return symbol(sym.MENOR); }
    "="     { return symbol(sym.IGUAL); }

    // palavras-chave antes de ident
    "program"   { return symbol(sym.KW_PROGRAM); }
    "if"        { return symbol(sym.KW_IF); }
    "else"      { return symbol(sym.KW_ELSE); }
    "while"     { return symbol(sym.KW_WHILE); }

    {dec_num_lit}   { System.out.print(yytext());
                      return symbol(sym.NUMBER, Double.valueOf(yytext())); }

    {dec_int_id}    { return symbol(sym.IDENT, yytext()); }

    {WhiteSpace}    { /* skip */ }
}

[^]     { defineErro(yyline, yycolumn, "Lexico - Simbolo desconhecido: " + yytext()); }
