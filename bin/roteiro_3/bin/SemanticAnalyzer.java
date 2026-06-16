import java.io.*;
import java.util.*;

/**
 * SemanticAnalyzer.java — Roteiro 3: Expressões aritméticas
 *
 * Regra: operandos de +, -, *, /, % devem ser numéricos (int ou float).
 * Boolean, char e String não são válidos em operações aritméticas.
 *
 * Linguagem: Java-- (Compiladores — 7º CC)
 */
public class SemanticAnalyzer {

    private final List<Token> tokens;
    private final SymbolTable tabela;
    private final PrintWriter out;
    private int numErros;

    private static final Set<Integer> OP_ARIT = new HashSet<>(Arrays.asList(
        sym.PLUS, sym.MINUS, sym.STAR, sym.SLASH, sym.PERCENT
    ));
    private static final Set<String> TIPOS_NUMERICOS = new HashSet<>(Arrays.asList("int","float","INT","FLOAT"));

    private static final Set<Integer> TIPOS_KW = new HashSet<>(Arrays.asList(
        sym.KW_INT, sym.KW_FLOAT, sym.KW_BOOLEAN, sym.KW_CHAR, sym.KW_STRING, sym.KW_VOID
    ));

    public SemanticAnalyzer(List<Token> tokens, SymbolTable tabela, PrintWriter out) {
        this.tokens = tokens; this.tabela = tabela; this.out = out; this.numErros = 0;
    }

    public boolean isSucesso() { return numErros == 0; }
    public int     getErros()  { return numErros; }

    public void analisar() {
        // Registra variáveis
        for (int i = 0; i < tokens.size() - 2; i++) {
            Token t = tokens.get(i), prox = tokens.get(i + 1);
            if (TIPOS_KW.contains(t.tipo) && prox.tipo == sym.IDENT
                    && tokens.get(i + 2).tipo != sym.LPAREN)
                tabela.declararVar(prox.lexema, t.lexema, prox.linha);
        }

        // Verifica operadores aritméticos
        for (int i = 1; i < tokens.size() - 1; i++) {
            Token op = tokens.get(i);
            if (!OP_ARIT.contains(op.tipo)) continue;

            Token esq = tokens.get(i - 1);
            Token dir = tokens.get(i + 1);

            String tE = inferirTipo(esq);
            String tD = inferirTipo(dir);

            if (tE != null && !isNumerico(tE))
                erroSemantico("operando esquerdo de '" + op.lexema + "' nao e numerico (tipo: " + tE + ")", op.linha);
            if (tD != null && !isNumerico(tD))
                erroSemantico("operando direito de '" + op.lexema + "' nao e numerico (tipo: " + tD + ")", op.linha);
        }
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

    private boolean isNumerico(String tipo) {
        return tipo.equals("int") || tipo.equals("float");
    }

    private void erroSemantico(String msg, int linha) {
        numErros++;
        String e = "ERRO SEMANTICO (linha " + linha + "): " + msg;
        System.out.println(e); if (out != null) out.println(e);
    }
}
