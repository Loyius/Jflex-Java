import java.io.*;
import java.util.*;

/**
 * SemanticAnalyzer.java — Roteiro 4: Expressões relacionais
 *
 * Regra: os dois lados de um operador relacional (==, !=, <, >, <=, >=)
 * devem ter tipos compatíveis.
 *  - int vs float: ok (compatíveis numéricos)
 *  - boolean vs int: erro
 *  - String vs int: erro
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

        for (int i = 1; i < tokens.size() - 1; i++) {
            Token op = tokens.get(i);
            if (!OP_REL.contains(op.tipo)) continue;
            String tE = inferirTipo(tokens.get(i - 1));
            String tD = inferirTipo(tokens.get(i + 1));
            if (tE != null && tD != null && !compatíveis(tE, tD)) {
                erroSemantico("tipos incompativeis em expressao relacional '"
                    + op.lexema + "': '" + tE + "' vs '" + tD + "'", op.linha);
            }
        }
    }

    private boolean compatíveis(String a, String b) {
        // Tipos iguais sempre compatíveis
        if (a.equals(b)) return true;
        // int e float são compatíveis entre si
        Set<String> nums = new HashSet<>(Arrays.asList("int","float"));
        return nums.contains(a) && nums.contains(b);
    }

    private String inferirTipo(Token t) {
        switch (t.tipo) {
            case sym.INT_LIT: case sym.INT_HEX: return "int";
            case sym.FLOAT_LIT: return "float";
            case sym.KW_TRUE: case sym.KW_FALSE: return "boolean";
            case sym.CHAR_CONST: return "char";
            case sym.STRING_LIT: return "String";
            case sym.IDENT: return tabela.getType(t.lexema);
            default: return null;
        }
    }

    private void erroSemantico(String msg, int linha) {
        numErros++;
        String e = "ERRO SEMANTICO (linha " + linha + "): " + msg;
        System.out.println(e); if (out != null) out.println(e);
    }
}
