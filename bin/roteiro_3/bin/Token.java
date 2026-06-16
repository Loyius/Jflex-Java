/**
 * Token.java — Representa um token reconhecido pelo scanner.
 * Linguagem: Java-- (Compiladores — 7º CC)
 */
public class Token {
    public final int    tipo;     // código numérico (constante em sym.java)
    public final String lexema;   // texto reconhecido
    public final int    linha;    // linha (1-based)
    public final int    coluna;   // coluna (1-based)

    public Token(int tipo, String lexema, int linha, int coluna) {
        this.tipo   = tipo;
        this.lexema = lexema;
        this.linha  = linha;
        this.coluna = coluna;
    }

    public String tipoNome() {
        return String.valueOf(tipo);
    }

    @Override
    public String toString() {
        return "<" + tipoNome() + ", " + lexema + ", linha " + linha + ">";
    }
}
