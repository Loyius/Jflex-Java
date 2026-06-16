/*
 * NOTA: Este arquivo foi criado manualmente com interface compativel com JCup.
 * Para gerar a versao oficial, execute quando o JAR estiver disponivel:
 *
 *   java -jar java-cup.jar -parser parser -symbols sym Parser.cup
 *
 * O arquivo Parser.cup com a gramatica formal completa esta na mesma pasta.
 */
/**
 * sym.java — Constantes de símbolo para os tokens da linguagem Java--
 * Compatível com a interface Java CUP (pode ser usado com parser.java gerado por JCup).
 * Linguagem: Java-- (Compiladores — 7º CC)
 */
public class sym {

    // ── Especial ─────────────────────────────────────────────────────────────
    public static final int EOF         =  0;
    public static final int error       =  1;

    // ── Palavras reservadas ───────────────────────────────────────────────────
    public static final int KEYWORD     =  2;   // genérico (para scanner standalone)
    public static final int KW_PROGRAM  =  3;   // program
    public static final int KW_FINAL    =  4;   // final
    public static final int KW_CLASS    =  5;   // class
    public static final int KW_VOID     =  6;   // void
    public static final int KW_INT      =  7;   // int
    public static final int KW_FLOAT    =  8;   // float
    public static final int KW_BOOLEAN  =  9;   // boolean
    public static final int KW_CHAR     = 10;   // char
    public static final int KW_STRING   = 11;   // String
    public static final int KW_IF       = 12;   // if
    public static final int KW_ELSE     = 13;   // else
    public static final int KW_WHILE    = 14;   // while
    public static final int KW_FOR      = 15;   // for
    public static final int KW_RETURN   = 16;   // return
    public static final int KW_NEW      = 17;   // new
    public static final int KW_READ     = 18;   // read
    public static final int KW_PRINT    = 19;   // print
    public static final int KW_TRUE     = 20;   // true
    public static final int KW_FALSE    = 21;   // false
    public static final int KW_NULL     = 22;   // null

    // ── Operadores relacionais ────────────────────────────────────────────────
    public static final int RELOP       = 23;   // genérico
    public static final int EQ          = 24;   // ==
    public static final int NEQ         = 25;   // !=
    public static final int LT          = 26;   // <
    public static final int GT          = 27;   // >
    public static final int LEQ         = 28;   // <=
    public static final int GEQ         = 29;   // >=

    // ── Operadores aditivos ───────────────────────────────────────────────────
    public static final int ADDOP       = 30;   // genérico
    public static final int PLUS        = 31;   // +
    public static final int MINUS       = 32;   // -

    // ── Operadores multiplicativos ────────────────────────────────────────────
    public static final int MULOP       = 33;   // genérico
    public static final int STAR        = 34;   // *
    public static final int SLASH       = 35;   // /
    public static final int PERCENT     = 36;   // %

    // ── Operadores lógicos ────────────────────────────────────────────────────
    public static final int AND         = 37;   // &&
    public static final int OR          = 38;   // ||
    public static final int NOT         = 39;   // !

    // ── Atribuição ────────────────────────────────────────────────────────────
    public static final int ASSIGN      = 40;   // =

    // ── Pontuação ─────────────────────────────────────────────────────────────
    public static final int SEMICOLON   = 41;   // ;
    public static final int COMMA       = 42;   // ,
    public static final int DOT         = 43;   // .
    public static final int LPAREN      = 44;   // (
    public static final int RPAREN      = 45;   // )
    public static final int LBRACE      = 46;   // {
    public static final int RBRACE      = 47;   // }
    public static final int LBRACKET    = 48;   // [
    public static final int RBRACKET    = 49;   // ]

    // ── Literais ─────────────────────────────────────────────────────────────
    public static final int INT_LIT     = 50;   // inteiro decimal
    public static final int INT_HEX     = 51;   // inteiro hexadecimal
    public static final int FLOAT_LIT   = 52;   // número real
    public static final int CHAR_CONST  = 53;   // constante de caractere
    public static final int STRING_LIT  = 54;   // literal string
    public static final int IDENT       = 55;   // identificador

    // ── Utilidades ───────────────────────────────────────────────────────────
    /** Retorna o nome legível do token a partir do código. */
    public static String nomeToken(int codigo) {
        switch (codigo) {
            case EOF:        return "EOF";
            case error:      return "error";
            case KEYWORD:    return "KEYWORD";
            case KW_PROGRAM: return "program";
            case KW_FINAL:   return "final";
            case KW_CLASS:   return "class";
            case KW_VOID:    return "void";
            case KW_INT:     return "int";
            case KW_FLOAT:   return "float";
            case KW_BOOLEAN: return "boolean";
            case KW_CHAR:    return "char";
            case KW_STRING:  return "String";
            case KW_IF:      return "if";
            case KW_ELSE:    return "else";
            case KW_WHILE:   return "while";
            case KW_FOR:     return "for";
            case KW_RETURN:  return "return";
            case KW_NEW:     return "new";
            case KW_READ:    return "read";
            case KW_PRINT:   return "print";
            case KW_TRUE:    return "true";
            case KW_FALSE:   return "false";
            case KW_NULL:    return "null";
            case RELOP:      return "RELOP";
            case EQ:         return "==";
            case NEQ:        return "!=";
            case LT:         return "<";
            case GT:         return ">";
            case LEQ:        return "<=";
            case GEQ:        return ">=";
            case ADDOP:      return "ADDOP";
            case PLUS:       return "+";
            case MINUS:      return "-";
            case MULOP:      return "MULOP";
            case STAR:       return "*";
            case SLASH:      return "/";
            case PERCENT:    return "%";
            case AND:        return "&&";
            case OR:         return "||";
            case NOT:        return "!";
            case ASSIGN:     return "=";
            case SEMICOLON:  return ";";
            case COMMA:      return ",";
            case DOT:        return ".";
            case LPAREN:     return "(";
            case RPAREN:     return ")";
            case LBRACE:     return "{";
            case RBRACE:     return "}";
            case LBRACKET:   return "[";
            case RBRACKET:   return "]";
            case INT_LIT:    return "INT";
            case INT_HEX:    return "INT_HEX";
            case FLOAT_LIT:  return "FLOAT";
            case CHAR_CONST: return "CHAR_CONST";
            case STRING_LIT: return "STRING";
            case IDENT:      return "IDENT";
            default:         return "UNKNOWN(" + codigo + ")";
        }
    }
}
