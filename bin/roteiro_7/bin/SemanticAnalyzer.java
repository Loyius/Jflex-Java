import java.io.*;
import java.util.*;

/**
 * SemanticAnalyzer.java — Roteiro 7: Condição de while deve ser booleana
 *
 * Regra: a expressão entre parênteses de um while deve ter tipo boolean.
 * Implementação idêntica ao Roteiro 6, mas para while.
 *
 * Linguagem: Java-- (Compiladores — 7º CC)
 */
public class SemanticAnalyzer {

    private final List<Token> tokens;
    private final SymbolTable tabela;
    private final PrintWriter out;
    private int numErros;

    private static final Set<Integer> OP_REL = new HashSet<>(Arrays.asList(
        sym.EQ, sym.NEQ, sym.LT, sym.GT, sym.LEQ, sym.GEQ
    ));
    private static final Set<Integer> OP_LOG = new HashSet<>(Arrays.asList(
        sym.AND, sym.OR, sym.NOT
    ));
    private static final Set<Integer> TIPOS_KW = new HashSet<>(Arrays.asList(
        sym.KW_INT, sym.KW_FLOAT, sym.KW_BOOLEAN, sym.KW_CHAR, sym.KW_STRING, sym.KW_VOID
    ));

    public SemanticAnalyzer(List<Token> tokens, SymbolTable tabela, PrintWriter out) {
        this.tokens = tokens; this.tabela = tabela; this.out = out; this.numErros = 0;
    }

    public boolean isSucesso() { return numErros == 0; }
    public int     getErros()  { return numErros; }

    public void analisar() {
        for (int i = 0; i < tokens.size() - 2; i++) {
            Token t = tokens.get(i), prox = tokens.get(i + 1);
            if (TIPOS_KW.contains(t.tipo) && prox.tipo == sym.IDENT
                    && tokens.get(i + 2).tipo != sym.LPAREN)
                tabela.declararVar(prox.lexema, t.lexema, prox.linha);
        }

        for (int i = 0; i + 3 < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.tipo != sym.KW_WHILE) continue;
            if (tokens.get(i + 1).tipo != sym.LPAREN) continue;
            List<Token> cond = coletarConteudoParens(i + 1);
            if (!ehCondicaoBooleana(cond)) {
                erroSemantico("condicao do 'while' deve ser booleana", t.linha);
            }
        }
    }

    private List<Token> coletarConteudoParens(int inicioLparen) {
        List<Token> cond = new ArrayList<>();
        int prof = 0;
        for (int i = inicioLparen; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.tipo == sym.LPAREN) { prof++; continue; }
            if (t.tipo == sym.RPAREN) { prof--; if (prof == 0) break; continue; }
            cond.add(t);
        }
        return cond;
    }

    private boolean ehCondicaoBooleana(List<Token> cond) {
        if (cond.isEmpty()) return false;
        for (Token t : cond)
            if (OP_REL.contains(t.tipo) || OP_LOG.contains(t.tipo)
                    || t.tipo == sym.KW_TRUE || t.tipo == sym.KW_FALSE) return true;
        if (cond.size() == 1 && cond.get(0).tipo == sym.IDENT)
            return "boolean".equals(tabela.getType(cond.get(0).lexema));
        return false;
    }

    private void erroSemantico(String msg, int linha) {
        numErros++;
        String e = "ERRO SEMANTICO (linha " + linha + "): " + msg;
        System.out.println(e); if (out != null) out.println(e);
    }
}
