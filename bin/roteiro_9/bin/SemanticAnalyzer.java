import java.io.*;
import java.util.*;

/**
 * SemanticAnalyzer.java — Roteiro 9: Tipo de retorno de funções
 *
 * Regra: o tipo da expressão retornada em return deve corresponder
 * ao tipo de retorno declarado para a função.
 *
 * Linguagem: Java-- (Compiladores — 7º CC)
 */
public class SemanticAnalyzer {

    private final List<Token> tokens;
    private final SymbolTable tabela;
    private final PrintWriter out;
    private int numErros;

    // Contexto da função atual
    private String funcaoAtual    = null;
    private String retornoEsperado = null;

    private static final Set<Integer> TIPOS_KW = new HashSet<>(Arrays.asList(
        sym.KW_INT, sym.KW_FLOAT, sym.KW_BOOLEAN, sym.KW_CHAR, sym.KW_STRING, sym.KW_VOID
    ));

    public SemanticAnalyzer(List<Token> tokens, SymbolTable tabela, PrintWriter out) {
        this.tokens = tokens; this.tabela = tabela; this.out = out; this.numErros = 0;
    }

    public boolean isSucesso() { return numErros == 0; }
    public int     getErros()  { return numErros; }

    public void analisar() {
        // Registra funções
        for (int i = 0; i < tokens.size() - 3; i++) {
            Token tipo = tokens.get(i), nome = tokens.get(i + 1), lp = tokens.get(i + 2);
            if ((TIPOS_KW.contains(tipo.tipo)) && nome.tipo == sym.IDENT && lp.tipo == sym.LPAREN) {
                tabela.declararFuncao(nome.lexema, tipo.lexema, Collections.emptyList(), nome.linha);
            }
        }

        // Verifica retornos
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);

            // Detecta entrada em função
            if ((TIPOS_KW.contains(t.tipo)) && i + 2 < tokens.size()
                    && tokens.get(i + 1).tipo == sym.IDENT
                    && tokens.get(i + 2).tipo == sym.LPAREN) {
                funcaoAtual      = tokens.get(i + 1).lexema;
                retornoEsperado  = t.lexema;
            }

            // return <expr>
            if (t.tipo == sym.KW_RETURN && funcaoAtual != null && i + 1 < tokens.size()) {
                Token proxTok = tokens.get(i + 1);
                if (proxTok.tipo == sym.SEMICOLON) {
                    // return; sem valor
                    if (!"void".equals(retornoEsperado))
                        erroSemantico("funcao '" + funcaoAtual + "' deve retornar '"
                            + retornoEsperado + "' mas return sem valor", t.linha);
                } else {
                    String tipoRetornado = inferirTipo(proxTok);
                    if (tipoRetornado != null && !tipoCompativel(retornoEsperado, tipoRetornado))
                        erroSemantico("funcao '" + funcaoAtual + "': retorno esperado '"
                            + retornoEsperado + "', encontrado '" + tipoRetornado + "'", t.linha);
                }
            }
        }
    }

    private boolean tipoCompativel(String esperado, String encontrado) {
        if (esperado == null || encontrado == null) return true;
        if (esperado.equals(encontrado)) return true;
        if (esperado.equals("float") && encontrado.equals("int")) return true;
        return false;
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
