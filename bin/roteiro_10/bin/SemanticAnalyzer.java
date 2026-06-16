import java.io.*;
import java.util.*;

/**
 * SemanticAnalyzer.java — Roteiro 10: Escopo de variáveis
 *
 * Regra: variáveis locais (dentro de {}) têm escopo restrito ao bloco.
 * - Uso fora do escopo de declaração é erro.
 * - Variáveis globais são visíveis em todos os escopos internos.
 * - Redeclaração no mesmo escopo é erro.
 *
 * Implementação: simula abertura/fechamento de escopos com a SymbolTable.
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
        // Simula a navegação por escopos
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);

            // Abre escopo
            if (t.tipo == sym.LBRACE) {
                tabela.enterScope();
                continue;
            }

            // Fecha escopo
            if (t.tipo == sym.RBRACE) {
                tabela.exitScope();
                continue;
            }

            // Declaração de variável: tipo IDENT (sem LPAREN seguinte)
            if (TIPOS_KW.contains(t.tipo) && i + 2 < tokens.size()
                    && tokens.get(i + 1).tipo == sym.IDENT
                    && tokens.get(i + 2).tipo != sym.LPAREN) {
                Token identTok = tokens.get(i + 1);
                boolean ok = tabela.declararVar(identTok.lexema, t.lexema, identTok.linha);
                if (!ok) {
                    erroSemantico("variavel '" + identTok.lexema
                        + "' ja declarada neste escopo (nivel " + tabela.nivelAtual() + ")", identTok.linha);
                }
                continue;
            }

            // Uso de identificador: deve estar declarado em algum escopo visível
            if (t.tipo == sym.IDENT) {
                Token prev = i > 0 ? tokens.get(i - 1) : null;
                Token prox = i + 1 < tokens.size() ? tokens.get(i + 1) : null;

                // Pula nome de função na declaração
                if (prox != null && prox.tipo == sym.LPAREN) continue;
                // Pula nome após tipo (declaração)
                if (prev != null && (TIPOS_KW.contains(prev.tipo) || prev.tipo == sym.KW_FINAL)) continue;
                // Pula nome do programa
                if (prev != null && (prev.tipo == sym.KW_PROGRAM || prev.tipo == sym.KW_CLASS)) continue;

                if (!tabela.isDeclared(t.lexema)) {
                    erroSemantico("variavel '" + t.lexema
                        + "' usada fora de escopo ou nao declarada", t.linha);
                }
            }
        }
    }

    private void erroSemantico(String msg, int linha) {
        numErros++;
        String e = "ERRO SEMANTICO (linha " + linha + "): " + msg;
        System.out.println(e); if (out != null) out.println(e);
    }
}
