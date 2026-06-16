/*
 * NOTA: Este arquivo foi criado manualmente com interface compativel com JCup.
 * Para gerar a versao oficial, execute quando o JAR estiver disponivel:
 *
 *   java -jar java-cup.jar -parser parser -symbols sym Parser.cup
 *
 * O arquivo Parser.cup com a gramatica formal completa esta na mesma pasta.
 */
import java.io.*;
import java.util.*;

/**
 * parser.java — Analisador Sintático Recursive Descent para Java--
 *
 * Gramática suportada (ver Parser.cup para especificação completa):
 *   programa, declarações de variáveis e funções, bloco, comandos,
 *   if/else, while, for, return, print, read, chamadas, expressões.
 *
 * Recuperação de erros: panic mode com limite de avanço para evitar loops.
 *
 * Linguagem: Java-- (Compiladores — 7º CC)
 */
public class parser {

    private final List<Token> tokens;
    private int               pos;
    private final PrintWriter out;
    private int               numErros;

    // ─── Tokens de sincronização ──────────────────────────────────────────────
    private static final Set<Integer> SYNC_STMT = new HashSet<>(Arrays.asList(
        sym.SEMICOLON, sym.RBRACE, sym.KW_IF, sym.KW_WHILE, sym.KW_FOR,
        sym.KW_RETURN, sym.KW_PRINT, sym.KW_READ, sym.EOF
    ));

    // ─── Construtores ─────────────────────────────────────────────────────────

    public parser(List<Token> tokens, PrintWriter out) {
        this.tokens   = tokens;
        this.pos      = 0;
        this.out      = out;
        this.numErros = 0;
    }

    public boolean isSucesso() { return numErros == 0; }
    public int     getErros()  { return numErros; }

    // ─── Infraestrutura ───────────────────────────────────────────────────────

    private Token tok(int offset) {
        int i = pos + offset;
        return (i < tokens.size()) ? tokens.get(i) : tokens.get(tokens.size() - 1);
    }
    private Token atual()         { return tok(0); }
    private Token prox()          { return tok(1); }
    private Token prox2()         { return tok(2); }
    private boolean eh(int tipo)  { return atual().tipo == tipo; }

    private Token consumir() {
        Token t = atual();
        if (t.tipo != sym.EOF) pos++;
        return t;
    }

    private boolean match(int tipo) {
        if (eh(tipo)) { consumir(); return true; }
        return false;
    }

    /** Consome se for o tipo esperado; caso contrário emite erro mas NÃO consome. */
    private boolean esperar(int tipo) {
        if (eh(tipo)) { consumir(); return true; }
        erro("esperado '" + sym.nomeToken(tipo)
           + "', mas encontrado '" + atual().lexema + "'");
        return false;
    }

    private void erro(String msg) {
        numErros++;
        String e = "ERRO SINTATICO (linha " + atual().linha + "): " + msg;
        System.out.println(e);
        if (out != null) out.println(e);
    }

    /** Panic mode: consome tokens até achar um ponto de sincronização. */
    private void sincronizar(Set<Integer> sync) {
        int lim = 0;
        while (!sync.contains(eh(sym.EOF) ? sym.EOF : atual().tipo)) {
            consumir();
            if (++lim > 50) break;
        }
        // Consome ';' se for o ponto de parada, para avançar ao próximo stmt
        if (eh(sym.SEMICOLON)) consumir();
    }

    // ─── Ponto de entrada ──────────────────────────────────────────────────────

    public void parse() {
        parsePrograma();
    }

    // ─── Gramática ─────────────────────────────────────────────────────────────

    // programa ::= (program|class) IDENT { corpo } | corpo
    private void parsePrograma() {
        if (eh(sym.KW_PROGRAM) || eh(sym.KW_CLASS)) {
            consumir();
            esperar(sym.IDENT);
            esperar(sym.LBRACE);
            parseCorpo();
            esperar(sym.RBRACE);
        } else {
            parseCorpo();
        }
        // Toleramos EOF aqui
    }

    // corpo ::= (funcDecl | varDecl)*
    private void parseCorpo() {
        while (!eh(sym.RBRACE) && !eh(sym.EOF)) {
            try {
                if (ehFuncDecl()) parseFuncDecl();
                else              parseVarDecl();
            } catch (RuntimeException e) {
                erro(e.getMessage());
                sincronizar(SYNC_STMT);
            }
            // Segurança anti-loop
            if (eh(sym.KW_ELSE)) consumir(); // else perdido — consome e continua
        }
    }

    // Verifica se o próximo trecho é tipo IDENT (
    private boolean ehFuncDecl() {
        return ehTipo(atual().tipo) && prox().tipo == sym.IDENT && prox2().tipo == sym.LPAREN;
    }

    // Verifica se token é um tipo
    private boolean ehTipo(int t) {
        return t == sym.KW_INT || t == sym.KW_FLOAT || t == sym.KW_BOOLEAN
            || t == sym.KW_CHAR || t == sym.KW_STRING || t == sym.KW_VOID
            || (t == sym.IDENT);
    }

    // varDecl ::= [final] tipo IDENT [= expr] {, IDENT [= expr]} ;
    private void parseVarDecl() {
        match(sym.KW_FINAL);
        parseTipo();
        esperar(sym.IDENT);
        if (match(sym.ASSIGN)) parseExpr();
        // Múltiplos identificadores: int a, b, c;
        while (match(sym.COMMA)) {
            esperar(sym.IDENT);
            if (match(sym.ASSIGN)) parseExpr();
        }
        esperar(sym.SEMICOLON);
    }

    // funcDecl ::= tipo IDENT ( params ) bloco
    private void parseFuncDecl() {
        parseTipo();
        esperar(sym.IDENT);
        esperar(sym.LPAREN);
        if (!eh(sym.RPAREN)) parseParams();
        esperar(sym.RPAREN);
        parseBloco();
    }

    private void parseParams() {
        parseTipo(); esperar(sym.IDENT);
        while (match(sym.COMMA)) { parseTipo(); esperar(sym.IDENT); }
    }

    private void parseTipo() {
        if (ehTipo(atual().tipo)) consumir();
        else erro("tipo esperado, encontrado '" + atual().lexema + "'");
    }

    // bloco ::= { stmt* }
    private void parseBloco() {
        esperar(sym.LBRACE);
        while (!eh(sym.RBRACE) && !eh(sym.EOF)) {
            Token antes = atual();
            parseStmt();
            // Proteção anti-loop: se nada foi consumido, força
            if (atual() == antes) consumir();
        }
        esperar(sym.RBRACE);
    }

    // stmt = varDecl | atrib | if | while | for | return | print | read | chamada | bloco | ;
    private void parseStmt() {
        Token t = atual();
        try {
            switch (t.tipo) {
                case sym.KW_IF:     parseIf();     return;
                case sym.KW_WHILE:  parseWhile();  return;
                case sym.KW_FOR:    parseFor();    return;
                case sym.KW_RETURN: parseReturn(); return;
                case sym.KW_PRINT:  parsePrint();  return;
                case sym.KW_READ:   parseRead();   return;
                case sym.LBRACE:    parseBloco();  return;
                case sym.SEMICOLON: consumir();    return;
                // Tipos primitivos → declaração local
                case sym.KW_INT: case sym.KW_FLOAT: case sym.KW_BOOLEAN:
                case sym.KW_CHAR: case sym.KW_STRING: case sym.KW_VOID:
                case sym.KW_FINAL:
                    parseVarDecl(); return;
                case sym.IDENT:
                    parseIdentStmt(); return;
                default:
                    erro("comando invalido: '" + t.lexema + "'");
                    sincronizar(SYNC_STMT);
            }
        } catch (RuntimeException e) {
            erro(e.getMessage());
            sincronizar(SYNC_STMT);
        }
    }

    // Stmts que começam com IDENT: decl de classe, atrib, chamada
    private void parseIdentStmt() {
        // IDENT IDENT ... → declaração com tipo de usuário
        if (prox().tipo == sym.IDENT) {
            parseVarDecl();
            return;
        }
        // IDENT = expr ;  →  atribuição
        // IDENT [expr] = expr ;  →  atrib de array
        if (prox().tipo == sym.ASSIGN || prox().tipo == sym.LBRACKET) {
            consumir(); // IDENT
            if (match(sym.LBRACKET)) { parseExpr(); esperar(sym.RBRACKET); }
            esperar(sym.ASSIGN);
            parseExpr();
            esperar(sym.SEMICOLON);
            return;
        }
        // IDENT ( args ) ;  →  chamada de procedimento
        if (prox().tipo == sym.LPAREN) {
            consumir(); consumir(); // IDENT (
            if (!eh(sym.RPAREN)) parseArgs();
            esperar(sym.RPAREN);
            esperar(sym.SEMICOLON);
            return;
        }
        // IDENT . ... ;  →  chamada de método ou acesso a campo
        if (prox().tipo == sym.DOT) {
            parseExpr();
            esperar(sym.SEMICOLON);
            return;
        }
        erro("comando invalido iniciando com '" + atual().lexema + "'");
        sincronizar(SYNC_STMT);
    }

    // if ::= if ( expr ) stmt [else stmt]
    private void parseIf() {
        esperar(sym.KW_IF);
        esperar(sym.LPAREN); parseExpr(); esperar(sym.RPAREN);
        parseStmt();
        if (match(sym.KW_ELSE)) parseStmt();
    }

    // while ::= while ( expr ) stmt
    private void parseWhile() {
        esperar(sym.KW_WHILE);
        esperar(sym.LPAREN); parseExpr(); esperar(sym.RPAREN);
        parseStmt();
    }

    // for ::= for ( [init] ; [cond] ; [update] ) stmt
    private void parseFor() {
        esperar(sym.KW_FOR);
        esperar(sym.LPAREN);
        // init
        if (!eh(sym.SEMICOLON)) {
            if (ehTipoKw(atual().tipo)) { parseTipo(); esperar(sym.IDENT); match(sym.ASSIGN); if (!eh(sym.SEMICOLON)) parseExpr(); }
            else if (eh(sym.IDENT) && prox().tipo == sym.ASSIGN) { consumir(); consumir(); parseExpr(); }
            else if (eh(sym.IDENT) && ehTipo(prox().tipo)) { parseTipo(); esperar(sym.IDENT); match(sym.ASSIGN); if (!eh(sym.SEMICOLON)) parseExpr(); }
            else parseExpr();
        }
        esperar(sym.SEMICOLON);
        if (!eh(sym.SEMICOLON)) parseExpr();
        esperar(sym.SEMICOLON);
        if (!eh(sym.RPAREN)) {
            if (eh(sym.IDENT) && prox().tipo == sym.ASSIGN) { consumir(); consumir(); parseExpr(); }
            else if (!eh(sym.RPAREN)) parseExpr();
        }
        esperar(sym.RPAREN);
        parseStmt();
    }

    private boolean ehTipoKw(int t) {
        return t == sym.KW_INT || t == sym.KW_FLOAT || t == sym.KW_BOOLEAN
            || t == sym.KW_CHAR || t == sym.KW_STRING || t == sym.KW_VOID;
    }

    private void parseReturn() {
        esperar(sym.KW_RETURN);
        if (!eh(sym.SEMICOLON)) parseExpr();
        esperar(sym.SEMICOLON);
    }

    private void parsePrint() {
        esperar(sym.KW_PRINT);
        esperar(sym.LPAREN);
        if (!eh(sym.RPAREN)) parseArgs();
        esperar(sym.RPAREN);
        esperar(sym.SEMICOLON);
    }

    private void parseRead() {
        esperar(sym.KW_READ);
        esperar(sym.LPAREN);
        esperar(sym.IDENT);
        esperar(sym.RPAREN);
        esperar(sym.SEMICOLON);
    }

    // ─── Expressões ───────────────────────────────────────────────────────────

    private void parseExpr()    { parseOr(); }

    private void parseOr()      { parseAnd();   while (match(sym.OR))      parseAnd();  }
    private void parseAnd()     { parseRel();   while (match(sym.AND))     parseRel();  }
    private void parseRel()     {
        parseAdd();
        if (eh(sym.EQ)||eh(sym.NEQ)||eh(sym.LT)||eh(sym.GT)||eh(sym.LEQ)||eh(sym.GEQ)) {
            consumir(); parseAdd();
        }
    }
    private void parseAdd()     { parseMul();   while (eh(sym.PLUS)||eh(sym.MINUS))   { consumir(); parseMul();   } }
    private void parseMul()     { parseUnary(); while (eh(sym.STAR)||eh(sym.SLASH)||eh(sym.PERCENT)) { consumir(); parseUnary(); } }

    private void parseUnary() {
        if (match(sym.MINUS) || match(sym.NOT)) parseUnary();
        else parsePrimary();
    }

    private void parsePrimary() {
        Token t = atual();
        switch (t.tipo) {
            case sym.INT_LIT: case sym.INT_HEX: case sym.FLOAT_LIT:
            case sym.CHAR_CONST: case sym.STRING_LIT:
            case sym.KW_TRUE: case sym.KW_FALSE: case sym.KW_NULL:
                consumir(); break;
            case sym.LPAREN:
                consumir(); parseExpr(); esperar(sym.RPAREN); break;
            case sym.KW_NEW:
                consumir();
                if (ehTipo(atual().tipo)) consumir();
                else erro("tipo esperado apos 'new'");
                if (match(sym.LPAREN)) { if (!eh(sym.RPAREN)) parseArgs(); esperar(sym.RPAREN); }
                else if (match(sym.LBRACKET)) { parseExpr(); esperar(sym.RBRACKET); }
                break;
            case sym.IDENT:
                consumir();
                if (match(sym.LPAREN)) { if (!eh(sym.RPAREN)) parseArgs(); esperar(sym.RPAREN); }
                else if (match(sym.LBRACKET)) { parseExpr(); esperar(sym.RBRACKET); }
                while (match(sym.DOT)) {
                    esperar(sym.IDENT);
                    if (match(sym.LPAREN)) { if (!eh(sym.RPAREN)) parseArgs(); esperar(sym.RPAREN); }
                }
                break;
            default:
                erro("expressao invalida: '" + t.lexema + "'");
                // Não consome — deixa o caller lidar
        }
    }

    private void parseArgs() {
        parseExpr();
        while (match(sym.COMMA)) parseExpr();
    }
}
