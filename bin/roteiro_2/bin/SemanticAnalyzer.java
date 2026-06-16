import java.io.*;
import java.util.*;

/**
 * SemanticAnalyzer.java — Roteiro 2: Compatibilidade de tipos em atribuições
 *
 * Regra: int ← float é erro (perda de precisão)
 *        float ← int é ok (promoção)
 *        boolean ← int/float é erro
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
        // Registra declarações
        for (int i = 0; i < tokens.size() - 2; i++) {
            Token t = tokens.get(i), prox = tokens.get(i + 1);
            if (TIPOS_KW.contains(t.tipo) && prox.tipo == sym.IDENT
                    && tokens.get(i + 2).tipo != sym.LPAREN) {
                tabela.declararVar(prox.lexema, t.lexema, prox.linha);
            }
        }

        // Verifica atribuições: IDENT = expr;
        for (int i = 0; i < tokens.size() - 2; i++) {
            Token var = tokens.get(i);
            Token op  = tokens.get(i + 1);
            Token val = tokens.get(i + 2);
            if (var.tipo == sym.IDENT && op.tipo == sym.ASSIGN) {
                String tipoVar = tabela.getType(var.lexema);
                String tipoVal = inferirTipo(val);
                if (tipoVar != null && tipoVal != null) {
                    verificarCompatibilidade(tipoVar, tipoVal, var.lexema, var.linha);
                }
            }
        }
    }

    private String inferirTipo(Token t) {
        switch (t.tipo) {
            case sym.INT_LIT: case sym.INT_HEX: return "int";
            case sym.FLOAT_LIT:  return "float";
            case sym.KW_TRUE: case sym.KW_FALSE: return "boolean";
            case sym.CHAR_CONST: return "char";
            case sym.STRING_LIT: return "String";
            case sym.IDENT:      return tabela.getType(t.lexema);
            default: return null;
        }
    }

    private void verificarCompatibilidade(String destino, String origem, String nome, int linha) {
        if (destino.equals(origem)) return;
        // float ← int: ok (promoção implícita)
        if (destino.equals("float") && origem.equals("int")) return;
        // Qualquer outra combinação diferente é erro
        erroSemantico("tipo incompativel em atribuicao de '" + nome + "': "
                + "esperado '" + destino + "', encontrado '" + origem + "'", linha);
    }

    private void erroSemantico(String msg, int linha) {
        numErros++;
        String e = "ERRO SEMANTICO (linha " + linha + "): " + msg;
        System.out.println(e); if (out != null) out.println(e);
    }
}
