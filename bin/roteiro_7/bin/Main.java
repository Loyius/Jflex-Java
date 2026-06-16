import java.io.*;
import java.util.*;
import java_cup.runtime.*;

/**
 * Main.java - Roteiro 1 - Variável declarada antes do uso
 * Linguagem: Java-- (Compiladores - 7o CC)
 */
public class Main {
    static final String ROTEIRO = "Roteiro 7 - Condicao while booleana";

    public static void main(String[] args) throws Exception {
        String arqEntrada = args.length >= 1 ? args[0] : "entrada.txt";
        String arqSaida   = args.length >= 2 ? args[1] : "saida.txt";
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(arqSaida)));

        try {
            log(pw, "=== Compilador Java-- | " + ROTEIRO + " ===");
            log(pw, "Entrada: " + arqEntrada + "  |  Saida: " + arqSaida);
            log(pw, "");

            File file = new File(arqEntrada);
            if (!file.exists()) {
                log(pw, "ERRO: arquivo não encontrado: " + arqEntrada);
                return;
            }

            log(pw, "--- FASE 1 e 2: ANALISE LEXICA E SINTATICA ---");
            Scanner scanner = new Scanner(new FileReader(file));
            parser p = new parser(scanner);
            p.setOutput(pw);

            boolean pOk = true;
            try {
                p.parse();
            } catch (Exception e) {
                pOk = false;
            }

            java.util.List<Token> tokens = scanner.getTokens();
            java.util.List<String> errosLex = scanner.getErros();
            
            log(pw, "Tokens: " + tokens.size() + " | Erros lexicos: " + errosLex.size());
            log(pw, "Erros sintaticos: " + p.getErros());
            log(pw, "");

            log(pw, "--- FASE 3: ANALISE SEMANTICA (" + ROTEIRO + ") ---");
            SymbolTable tabela = new SymbolTable();
            SemanticAnalyzer sem = new SemanticAnalyzer(tokens, tabela, pw);
            sem.analisar();

            log(pw, "Erros semanticos: " + sem.getErros());
            log(pw, "");

            boolean ok = errosLex.isEmpty() && pOk && p.getErros() == 0 && sem.isSucesso();
            log(pw, ok ? ">>> Analise concluida com sucesso <<<" : ">>> Analise encerrada com erros <<<");

            if (!ok) {
                log(pw, "    Erros lexicos   : " + errosLex.size());
                log(pw, "    Erros sintaticos: " + p.getErros());
                log(pw, "    Erros semanticos: " + sem.getErros());
            }

        } finally {
            pw.flush();
            pw.close();
        }
    }

    static void log(PrintWriter pw, String msg) {
        System.out.println(msg);
        pw.println(msg);
    }
}
