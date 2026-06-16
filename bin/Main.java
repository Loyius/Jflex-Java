import java.io.*;
import java.util.*;

/**
 * Main.java — Orquestrador do compilador Java-- (Entrega 1: Análise Léxica + Sintática)
 *
 * Uso: java Main <arquivo_entrada> [<arquivo_saida>]
 *
 * Saída: imprime no console E grava em saida.txt:
 *   - Cada token: <TIPO, lexema, linha>
 *   - Erros léxicos e sintáticos com número de linha
 *   - Mensagem final de sucesso ou falha
 *
 * Linguagem: Java-- (Compiladores — 7º CC)
 * Grupo: Mayssa Barbosa Dias; Larissa Queiroz Ramos; Fernando Medeiros; Matheus Augusto
 */
public class Main {

    public static void main(String[] args) throws IOException {

        // ── Argumentos ─────────────────────────────────────────────────────────
        String arquivoEntrada = "entrada.txt";
        String arquivoSaida   = "saida.txt";

        if (args.length >= 1) arquivoEntrada = args[0];
        if (args.length >= 2) arquivoSaida   = args[1];

        // ── Abre arquivo de saída ───────────────────────────────────────────────
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(arquivoSaida)));

        try {
            log(pw, "=== Compilador Java-- — Análise Léxica + Sintática ===");
            log(pw, "Entrada : " + arquivoEntrada);
            log(pw, "Saída   : " + arquivoSaida);
            log(pw, "");

            // ── Lê o arquivo de entrada ─────────────────────────────────────────
            String conteudo;
            try {
                conteudo = new String(java.nio.file.Files.readAllBytes(
                        java.nio.file.Paths.get(arquivoEntrada)));
            } catch (IOException e) {
                log(pw, "ERRO: arquivo de entrada não encontrado: " + arquivoEntrada);
                return;
            }

            // ── Análise Léxica ──────────────────────────────────────────────────
            log(pw, "--- FASE 1: ANÁLISE LÉXICA ---");
            Scanner scanner = new Scanner(conteudo, pw);
            scanner.tokenizar();

            List<Token>  tokens      = scanner.getTokens();
            List<String> errosLex    = scanner.getErros();

            log(pw, "");
            log(pw, "Tokens reconhecidos : " + tokens.size());
            log(pw, "Erros léxicos       : " + errosLex.size());
            log(pw, "");

            // ── Análise Sintática ────────────────────────────────────────────────
            log(pw, "--- FASE 2: ANÁLISE SINTÁTICA ---");
            parser p = new parser(tokens, pw);
            p.parse();

            log(pw, "");
            log(pw, "Erros sintáticos    : " + p.getErros());
            log(pw, "");

            // ── Resultado final ──────────────────────────────────────────────────
            boolean semErros = errosLex.isEmpty() && p.isSucesso();
            if (semErros) {
                log(pw, ">>> Análise sintática concluída com sucesso <<<");
            } else {
                log(pw, ">>> Análise encerrada com erros <<<");
                log(pw, "    Erros léxicos   : " + errosLex.size());
                log(pw, "    Erros sintáticos: " + p.getErros());
            }

        } finally {
            pw.flush();
            pw.close();
        }
    }

    /** Imprime no console E no arquivo de saída simultaneamente. */
    private static void log(PrintWriter pw, String msg) {
        System.out.println(msg);
        pw.println(msg);
    }
}
