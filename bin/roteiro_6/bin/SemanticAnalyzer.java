import java.io.*;
import java.util.*;

/**
 * SemanticAnalyzer.java — Roteiro 6: Condição de if/else deve ser booleana
 *
 * Regra: a expressão entre parênteses de um if deve ter tipo boolean.
 * Detecta: if (x + 1) → erro (int não é boolean)
 *          if (x > 0) → ok (resultado de relacional é boolean)
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
            if (t.tipo != sym.KW_IF) continue;
            // if ( <condição> )
            // Coleta tokens dentro dos parênteses
            if (tokens.get(i + 1).tipo != sym.LPAREN) continue;
            List<Token> cond = coletarConteudoParens(i + 1);
            if (!ehCondicaoBooleana(cond)) {
                erroSemantico("condicao do 'if' deve ser booleana (linha " + t.linha + ")", t.linha);
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
        // Contém operador relacional ou lógico → resultado boolean
        for (Token t : cond) {
            if (OP_REL.contains(t.tipo) || OP_LOG.contains(t.tipo)) return true;
            if (t.tipo == sym.KW_TRUE || t.tipo == sym.KW_FALSE) return true;
        }
        // Variável do tipo boolean
        if (cond.size() == 1 && cond.get(0).tipo == sym.IDENT) {
            String tipo = tabela.getType(cond.get(0).lexema);
            return "boolean".equals(tipo);
        }
        return false; // provavelmente inteiro ou float
    }

    private void erroSemantico(String msg, int linha) {
        numErros++;
        String e = "ERRO SEMANTICO (linha " + linha + "): " + msg;
        System.out.println(e); if (out != null) out.println(e);
    }
}
