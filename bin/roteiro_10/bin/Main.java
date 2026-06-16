import java.io.*;
import java.util.*;
/**
 * Main.java - Roteiro 10 - Escopo de variaveis
 * Linguagem: Java-- (Compiladores - 7o CC)
 */
public class Main {
    static final String ROTEIRO = "Roteiro 10 - Escopo de variaveis";
    public static void main(String[] args) throws IOException {
        String arqEntrada = args.length >= 1 ? args[0] : "entrada.txt";
        String arqSaida   = args.length >= 2 ? args[1] : "saida.txt";
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(arqSaida)));
        try {
            log(pw, "=== Compilador Java-- | " + ROTEIRO + " ===");
            log(pw, "Entrada: " + arqEntrada + "  |  Saida: " + arqSaida);
            log(pw, "");
            String src = lerArquivo(arqEntrada, pw);
            if (src == null) return;
            log(pw, "--- FASE 1: ANALISE LEXICA ---");
            Scanner scanner = new Scanner(src, pw);
            scanner.tokenizar();
            java.util.List<Token> tokens = scanner.getTokens();
            java.util.List<String> errosLex = scanner.getErros();
            log(pw, "Tokens: " + tokens.size() + " | Erros lexicos: " + errosLex.size());
            log(pw, "");
            log(pw, "--- FASE 2: ANALISE SINTATICA ---");
            parser p = new parser(tokens, pw);
            p.parse();
            log(pw, "Erros sintaticos: " + p.getErros());
            log(pw, "");
            log(pw, "--- FASE 3: ANALISE SEMANTICA (" + ROTEIRO + ") ---");
            SymbolTable tabela = new SymbolTable();
            SemanticAnalyzer sem = new SemanticAnalyzer(tokens, tabela, pw);
            sem.analisar();
            log(pw, "Erros semanticos: " + sem.getErros());
            log(pw, "");
            boolean ok = errosLex.isEmpty() && p.isSucesso() && sem.isSucesso();
            log(pw, ok ? ">>> Analise concluida com sucesso <<<" : ">>> Analise encerrada com erros <<<");
            if (!ok) {
                log(pw, "    Erros lexicos   : " + errosLex.size());
                log(pw, "    Erros sintaticos: " + p.getErros());
                log(pw, "    Erros semanticos: " + sem.getErros());
            }
        } finally { pw.flush(); pw.close(); }
    }
    static String lerArquivo(String nome, PrintWriter pw) throws IOException {
        try { return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(nome))); }
        catch (IOException e) { log(pw, "ERRO: arquivo nao encontrado: " + nome); return null; }
    }
    static void log(PrintWriter pw, String msg) { System.out.println(msg); pw.println(msg); }
}