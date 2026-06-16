import java.util.*;

/**
 * SymbolTable.java — Tabela de Símbolos com suporte a escopos aninhados.
 *
 * Usada por todos os roteiros semânticos (1-10).
 * Suporta:
 *   - Escopos aninhados (enterScope / exitScope)
 *   - Registro de variáveis com tipo e linha de declaração
 *   - Registro de funções com tipo de retorno e lista de parâmetros
 *   - Consulta: isDeclared, getType, getReturnType, getParams
 *
 * Linguagem: Java-- (Compiladores — 7º CC)
 */
public class SymbolTable {

    // ─── Entrada da tabela ─────────────────────────────────────────────────────

    public static class Simbolo {
        public final String nome;
        public final String tipo;        // "int", "float", "boolean", "char", "void", etc.
        public final boolean ehFuncao;
        public final List<String> params; // tipos dos parâmetros (para funções)
        public final int linha;

        /** Construtor para variável. */
        public Simbolo(String nome, String tipo, int linha) {
            this.nome     = nome;
            this.tipo     = tipo;
            this.ehFuncao = false;
            this.params   = Collections.emptyList();
            this.linha    = linha;
        }

        /** Construtor para função/procedimento. */
        public Simbolo(String nome, String retorno, List<String> params, int linha) {
            this.nome     = nome;
            this.tipo     = retorno;
            this.ehFuncao = true;
            this.params   = Collections.unmodifiableList(params);
            this.linha    = linha;
        }

        @Override
        public String toString() {
            if (ehFuncao)
                return "funcao " + tipo + " " + nome + "(" + String.join(", ", params) + ") [linha " + linha + "]";
            return "var " + tipo + " " + nome + " [linha " + linha + "]";
        }
    }

    // ─── Pilha de escopos ─────────────────────────────────────────────────────

    /** Cada escopo é um mapa nome→Símbolo. */
    private final Deque<Map<String, Simbolo>> pilha = new ArrayDeque<>();

    /** Nível de profundidade atual (0 = global). */
    public int nivelAtual() { return pilha.size() - 1; }

    public SymbolTable() {
        enterScope(); // escopo global
    }

    /** Entra em um novo escopo (bloco, função). */
    public void enterScope() {
        pilha.push(new LinkedHashMap<>());
    }

    /** Sai do escopo atual e descarta os símbolos locais. */
    public void exitScope() {
        if (pilha.size() > 1) pilha.pop();
    }

    // ─── Inserção ─────────────────────────────────────────────────────────────

    /**
     * Declara uma variável no escopo atual.
     * @return true se inserido com sucesso; false se já declarado no escopo atual.
     */
    public boolean declararVar(String nome, String tipo, int linha) {
        Map<String, Simbolo> escopo = pilha.peek();
        if (escopo.containsKey(nome)) return false;
        escopo.put(nome, new Simbolo(nome, tipo, linha));
        return true;
    }

    /**
     * Declara uma função no escopo atual.
     * @return true se inserido com sucesso; false se já declarado.
     */
    public boolean declararFuncao(String nome, String retorno, List<String> params, int linha) {
        Map<String, Simbolo> escopo = pilha.peek();
        if (escopo.containsKey(nome)) return false;
        escopo.put(nome, new Simbolo(nome, retorno, params, linha));
        return true;
    }

    // ─── Consulta ─────────────────────────────────────────────────────────────

    /**
     * Busca um símbolo nos escopos, do mais interno para o mais externo.
     * @return o Símbolo se encontrado, ou null se não declarado.
     */
    public Simbolo buscar(String nome) {
        for (Map<String, Simbolo> escopo : pilha) {
            Simbolo s = escopo.get(nome);
            if (s != null) return s;
        }
        return null;
    }

    /** Verifica se um nome foi declarado em qualquer escopo visível. */
    public boolean isDeclared(String nome) { return buscar(nome) != null; }

    /** Retorna o tipo de uma variável/função, ou null se não encontrada. */
    public String getType(String nome) {
        Simbolo s = buscar(nome);
        return s != null ? s.tipo : null;
    }

    /** Verifica se o nome pertence a uma função. */
    public boolean isFuncao(String nome) {
        Simbolo s = buscar(nome);
        return s != null && s.ehFuncao;
    }

    /** Retorna os tipos dos parâmetros de uma função. */
    public List<String> getParams(String nome) {
        Simbolo s = buscar(nome);
        return (s != null && s.ehFuncao) ? s.params : Collections.emptyList();
    }

    /** Retorna o número de parâmetros de uma função. */
    public int getAridadeFuncao(String nome) { return getParams(nome).size(); }

    // ─── Diagnóstico ──────────────────────────────────────────────────────────

    /** Lista todos os símbolos do escopo atual para depuração. */
    public void dump() {
        int nivel = pilha.size() - 1;
        for (Map<String, Simbolo> escopo : pilha) {
            System.out.println("  [Escopo " + nivel + "]");
            for (Simbolo s : escopo.values()) System.out.println("    " + s);
            nivel--;
        }
    }
}
