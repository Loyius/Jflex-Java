import java.io.*;
import java.util.*;

/**
 * SemanticAnalyzer.java — Roteiro 5: Expressões lógicas
 *
 * Regra: operandos de && e || devem ser booleanos.
 * Operando de ! deve ser booleano.
 *
 * Linguagem: Java-- (Compiladores — 7º CC)
 */
public class SemanticAnalyzer {

    private final List<Token> tokens;
    private final SymbolTable tabela;
    private final PrintWriter out;
    private int numErros;

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

        for (int i = 0; i < tokens.size(); i++) {
            Token op = tokens.get(i);

            // && e || : ambos os lados devem ser boolean
            if (op.tipo == sym.AND || op.tipo == sym.OR) {
                if (i > 0) {
                    String tE = inferirTipo(tokens.get(i - 1));
                    if (tE != null && !tE.equals("boolean"))
                        erroSemantico("operando esquerdo de '" + op.lexema
                            + "' deve ser boolean (encontrado: " + tE + ")", op.linha);
                }
                if (i + 1 < tokens.size()) {
                    String tD = inferirTipo(tokens.get(i + 1));
                    if (tD != null && !tD.equals("boolean"))
                        erroSemantico("operando direito de '" + op.lexema
                            + "' deve ser boolean (encontrado: " + tD + ")", op.linha);
                }
            }

            // ! : operando deve ser boolean
            if (op.tipo == sym.NOT && i + 1 < tokens.size()) {
                String tD = inferirTipo(tokens.get(i + 1));
                if (tD != null && !tD.equals("boolean"))
                    erroSemantico("operando de '!' deve ser boolean (encontrado: " + tD + ")", op.linha);
            }
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

    private void erroSemantico(String msg, int linha) {
        numErros++;
        String e = "ERRO SEMANTICO (linha " + linha + "): " + msg;
        System.out.println(e); if (out != null) out.println(e);
    }
}
