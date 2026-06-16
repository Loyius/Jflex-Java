import java.io.*;
import java.util.*;

/**
 * Scanner.java — Analisador Léxico para a linguagem Java--
 * Versão integrada: retorna tokens (List<Token>) em vez de imprimir direto.
 * Gerado manualmente a partir de Scanner.flex.
 * Linguagem: Java-- (Compiladores — 7º CC)
 */
public class Scanner {

    private final String src;        // conteúdo do arquivo
    private int pos;                 // posição atual no texto
    private int linha;               // linha atual (1-based)
    private int coluna;              // coluna atual (1-based)
    private final List<Token> tokens = new ArrayList<>();
    private final List<String> erros  = new ArrayList<>();
    private PrintWriter out;          // escritor para arquivo+console

    public Scanner(String src, PrintWriter out) {
        this.src    = src;
        this.pos    = 0;
        this.linha  = 1;
        this.coluna = 1;
        this.out    = out;
    }

    // ── Acesso aos resultados ─────────────────────────────────────────────────

    public List<Token> getTokens() { return tokens; }
    public List<String> getErros() { return erros;  }

    // ── Utilitários internos ──────────────────────────────────────────────────

    private char atual()  { return pos < src.length() ? src.charAt(pos) : '\0'; }
    private char lookahead(int k) { return (pos + k) < src.length() ? src.charAt(pos + k) : '\0'; }

    private char consumir() {
        char c = src.charAt(pos++);
        if (c == '\n') { linha++; coluna = 1; }
        else { coluna++; }
        return c;
    }

    private void emit(int tipo, String lexema, int l, int col) {
        Token t = new Token(tipo, lexema, l, col);
        tokens.add(t);
        String msg = "<" + sym.nomeToken(tipo) + ", " + lexema + ", linha " + l + ">";
        System.out.println(msg);
        if (out != null) out.println(msg);
    }

    private void erroLexico(String msg, int l, int col) {
        String err = "ERRO LEXICO na linha " + l + ", col " + col + ": " + msg;
        erros.add(err);
        System.out.println(err);
        if (out != null) out.println(err);
    }

    // ── Método principal ──────────────────────────────────────────────────────

    public void tokenizar() {
        while (pos < src.length()) {
            int l = linha, col = coluna;
            char c = atual();

            // Whitespace
            if (c == ' ' || c == '\t' || c == '\f' || c == '\r' || c == '\n') {
                consumir();
                continue;
            }

            // Comentário de linha: //
            if (c == '/' && lookahead(1) == '/') {
                while (pos < src.length() && atual() != '\n') consumir();
                continue;
            }

            // Comentário de bloco: /* ... */
            if (c == '/' && lookahead(1) == '*') {
                int lComent = linha, cComent = coluna;
                consumir(); consumir(); // consume /*
                boolean fechado = false;
                while (pos < src.length()) {
                    if (atual() == '*' && lookahead(1) == '/') {
                        consumir(); consumir();
                        fechado = true;
                        break;
                    }
                    consumir();
                }
                if (!fechado) erroLexico("comentario de bloco nao fechado", lComent, cComent);
                continue;
            }

            // String literal
            if (c == '"') {
                StringBuilder sb = new StringBuilder();
                consumir();
                boolean fechado = false;
                while (pos < src.length()) {
                    char ch = atual();
                    if (ch == '"') { consumir(); fechado = true; break; }
                    if (ch == '\n') break;
                    if (ch == '\\') { sb.append(consumir()); sb.append(consumir()); }
                    else { sb.append(consumir()); }
                }
                if (!fechado) erroLexico("string nao fechada", l, col);
                else emit(sym.STRING_LIT, "\"" + sb + "\"", l, col);
                continue;
            }

            // Char literal
            if (c == '\'') {
                consumir();
                StringBuilder sb = new StringBuilder("'");
                if (pos < src.length() && atual() == '\\') {
                    sb.append(consumir());
                    if (pos < src.length()) sb.append(consumir());
                } else if (pos < src.length() && atual() != '\'') {
                    sb.append(consumir());
                }
                if (pos < src.length() && atual() == '\'') {
                    sb.append(consumir());
                    emit(sym.CHAR_CONST, sb.toString(), l, col);
                } else {
                    erroLexico("constante de caractere invalida", l, col);
                }
                continue;
            }

            // Número
            if (Character.isDigit(c)) {
                // Hexadecimal
                if (c == '0' && (lookahead(1) == 'x' || lookahead(1) == 'X')) {
                    boolean maiusculo = lookahead(1) == 'X';
                    consumir(); consumir(); // consume 0x
                    StringBuilder sb = new StringBuilder(maiusculo ? "0X" : "0x");
                    if (pos >= src.length() || !isHexDigit(atual())) {
                        erroLexico(maiusculo
                            ? "hexadecimal invalido (use prefixo 0x minusculo): " + sb
                            : "hexadecimal invalido (faltando digitos apos 0x)", l, col);
                    } else {
                        if (maiusculo) {
                            while (pos < src.length() && isHexDigit(atual())) sb.append(consumir());
                            erroLexico("hexadecimal invalido (use prefixo 0x minusculo): " + sb, l, col);
                        } else {
                            while (pos < src.length() && isHexDigit(atual())) sb.append(consumir());
                            emit(sym.INT_HEX, sb.toString(), l, col);
                        }
                    }
                    continue;
                }
                // Float ou Int
                StringBuilder sb = new StringBuilder();
                while (pos < src.length() && Character.isDigit(atual())) sb.append(consumir());
                if (pos < src.length() && atual() == '.' && Character.isDigit(lookahead(1))) {
                    sb.append(consumir()); // .
                    while (pos < src.length() && Character.isDigit(atual())) sb.append(consumir());
                    emit(sym.FLOAT_LIT, sb.toString(), l, col);
                } else if (pos < src.length() && atual() == '.') {
                    sb.append(consumir());
                    erroLexico("float invalido (faltando parte decimal): " + sb, l, col);
                } else {
                    emit(sym.INT_LIT, sb.toString(), l, col);
                }
                continue;
            }

            // Float começando com .
            if (c == '.' && Character.isDigit(lookahead(1))) {
                StringBuilder sb = new StringBuilder(".");
                consumir();
                while (pos < src.length() && Character.isDigit(atual())) sb.append(consumir());
                erroLexico("float invalido (faltando parte inteira): " + sb, l, col);
                continue;
            }

            // Identificador ou palavra reservada
            if (Character.isLetter(c) || c == '_') {
                StringBuilder sb = new StringBuilder();
                while (pos < src.length() && (Character.isLetterOrDigit(atual()) || atual() == '_'))
                    sb.append(consumir());
                String word = sb.toString();
                emit(keywordTipo(word), word, l, col);
                continue;
            }

            // Operadores e pontuação de 2 chars
            if (c == '=' && lookahead(1) == '=') { consumir(); consumir(); emit(sym.EQ,  "==", l, col); continue; }
            if (c == '!' && lookahead(1) == '=') { consumir(); consumir(); emit(sym.NEQ, "!=", l, col); continue; }
            if (c == '<' && lookahead(1) == '=') { consumir(); consumir(); emit(sym.LEQ, "<=", l, col); continue; }
            if (c == '>' && lookahead(1) == '=') { consumir(); consumir(); emit(sym.GEQ, ">=", l, col); continue; }
            if (c == '&' && lookahead(1) == '&') { consumir(); consumir(); emit(sym.AND, "&&", l, col); continue; }
            if (c == '|' && lookahead(1) == '|') { consumir(); consumir(); emit(sym.OR,  "||", l, col); continue; }

            // Operadores e pontuação de 1 char
            switch (c) {
                case '<': consumir(); emit(sym.LT,        "<",  l, col); break;
                case '>': consumir(); emit(sym.GT,        ">",  l, col); break;
                case '=': consumir(); emit(sym.ASSIGN,    "=",  l, col); break;
                case '+': consumir(); emit(sym.PLUS,      "+",  l, col); break;
                case '-': consumir(); emit(sym.MINUS,     "-",  l, col); break;
                case '*': consumir(); emit(sym.STAR,      "*",  l, col); break;
                case '/': consumir(); emit(sym.SLASH,     "/",  l, col); break;
                case '%': consumir(); emit(sym.PERCENT,   "%",  l, col); break;
                case '!': consumir(); emit(sym.NOT,       "!",  l, col); break;
                case ';': consumir(); emit(sym.SEMICOLON, ";",  l, col); break;
                case ',': consumir(); emit(sym.COMMA,     ",",  l, col); break;
                case '.': consumir(); emit(sym.DOT,       ".",  l, col); break;
                case '(': consumir(); emit(sym.LPAREN,    "(",  l, col); break;
                case ')': consumir(); emit(sym.RPAREN,    ")",  l, col); break;
                case '{': consumir(); emit(sym.LBRACE,    "{",  l, col); break;
                case '}': consumir(); emit(sym.RBRACE,    "}",  l, col); break;
                case '[': consumir(); emit(sym.LBRACKET,  "[",  l, col); break;
                case ']': consumir(); emit(sym.RBRACKET,  "]",  l, col); break;
                default:
                    erroLexico("simbolo invalido: '" + c + "'", l, col);
                    consumir();
            }
        }
        // Token EOF
        emit(sym.EOF, "$EOF", linha, coluna);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean isHexDigit(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    /** Retorna o tipo de token para uma palavra — keyword específico ou IDENT. */
    private int keywordTipo(String w) {
        switch (w) {
            case "program": return sym.KW_PROGRAM;
            case "final":   return sym.KW_FINAL;
            case "class":   return sym.KW_CLASS;
            case "void":    return sym.KW_VOID;
            case "int":     return sym.KW_INT;
            case "float":   return sym.KW_FLOAT;
            case "boolean": return sym.KW_BOOLEAN;
            case "char":    return sym.KW_CHAR;
            case "String":  return sym.KW_STRING;
            case "if":      return sym.KW_IF;
            case "else":    return sym.KW_ELSE;
            case "while":   return sym.KW_WHILE;
            case "for":     return sym.KW_FOR;
            case "return":  return sym.KW_RETURN;
            case "new":     return sym.KW_NEW;
            case "read":    return sym.KW_READ;
            case "print":   return sym.KW_PRINT;
            case "true":    return sym.KW_TRUE;
            case "false":   return sym.KW_FALSE;
            case "null":    return sym.KW_NULL;
            default:        return sym.IDENT;
        }
    }

    // ── Ponto de entrada standalone (para testes) ─────────────────────────────

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Uso: java Scanner <arquivo>");
            return;
        }
        String conteudo = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(args[0])));
        PrintWriter pw = new PrintWriter(new FileWriter("saida_scanner.txt"));
        Scanner sc = new Scanner(conteudo, pw);
        sc.tokenizar();
        pw.flush(); pw.close();
        System.out.println("--- " + sc.getTokens().size() + " tokens, "
                + sc.getErros().size() + " erros lexicos ---");
    }
}
