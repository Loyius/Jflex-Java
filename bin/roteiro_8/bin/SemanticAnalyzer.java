import java.io.*;
import java.util.*;

/**
 * SemanticAnalyzer.java — Roteiro 8: Declaração e chamada de funções
 *
 * Regra: toda chamada de função deve corresponder a uma função declarada
 * com a aridade correta (número de argumentos).
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
        // Registra variáveis
        for (int i = 0; i < tokens.size() - 2; i++) {
            Token t = tokens.get(i), prox = tokens.get(i + 1);
            if (TIPOS_KW.contains(t.tipo) && prox.tipo == sym.IDENT
                    && tokens.get(i + 2).tipo != sym.LPAREN)
                tabela.declararVar(prox.lexema, t.lexema, prox.linha);
        }

        // Registra funções: tipo IDENT ( params )
        for (int i = 0; i < tokens.size() - 3; i++) {
            Token tipo = tokens.get(i), nome = tokens.get(i + 1), lp = tokens.get(i + 2);
            if ((TIPOS_KW.contains(tipo.tipo) || tipo.tipo == sym.IDENT)
                    && nome.tipo == sym.IDENT && lp.tipo == sym.LPAREN) {
                List<String> params = coletarTiposParams(i + 3);
                tabela.declararFuncao(nome.lexema, tipo.lexema, params, nome.linha);
            }
        }

        // Verifica chamadas: IDENT ( args )
        for (int i = 0; i + 1 < tokens.size(); i++) {
            Token nome = tokens.get(i), lp = tokens.get(i + 1);
            if (nome.tipo != sym.IDENT || lp.tipo != sym.LPAREN) continue;

            // Ignora declarações de função (precedido por tipo)
            Token prev = i > 0 ? tokens.get(i - 1) : null;
            if (prev != null && (TIPOS_KW.contains(prev.tipo) || prev.tipo == sym.IDENT)) continue;

            if (!tabela.isFuncao(nome.lexema)) {
                erroSemantico("funcao '" + nome.lexema + "' nao declarada", nome.linha);
                continue;
            }

            int argsPassados = contarArgs(i + 2);
            int argsEsperados = tabela.getAridadeFuncao(nome.lexema);
            if (argsPassados != argsEsperados) {
                erroSemantico("chamada de '" + nome.lexema + "': esperado "
                    + argsEsperados + " argumento(s), encontrado " + argsPassados, nome.linha);
            }
        }
    }

    private List<String> coletarTiposParams(int inicio) {
        List<String> p = new ArrayList<>();
        int i = inicio;
        while (i < tokens.size() && tokens.get(i).tipo != sym.RPAREN && tokens.get(i).tipo != sym.EOF) {
            Token t = tokens.get(i);
            if ((TIPOS_KW.contains(t.tipo) || t.tipo == sym.IDENT)
                    && i + 1 < tokens.size() && tokens.get(i + 1).tipo == sym.IDENT) {
                p.add(t.lexema); i += 2;
            } else i++;
            if (i < tokens.size() && tokens.get(i).tipo == sym.COMMA) i++;
        }
        return p;
    }

    private int contarArgs(int inicio) {
        int prof = 1, count = 0;
        boolean temArg = false;
        for (int i = inicio; i < tokens.size() && prof > 0; i++) {
            Token t = tokens.get(i);
            if (t.tipo == sym.LPAREN) { prof++; temArg = true; }
            else if (t.tipo == sym.RPAREN) { prof--; if (prof == 0 && temArg) count++; }
            else if (t.tipo == sym.COMMA && prof == 1) { count++; }
            else if (t.tipo != sym.RPAREN) { temArg = true; }
        }
        return count;
    }

    private void erroSemantico(String msg, int linha) {
        numErros++;
        String e = "ERRO SEMANTICO (linha " + linha + "): " + msg;
        System.out.println(e); if (out != null) out.println(e);
    }
}
