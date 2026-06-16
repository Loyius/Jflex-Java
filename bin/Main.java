import java.io.*;
import java.util.*;
import java_cup.runtime.*;

/**
 * Main.java — Orquestrador do compilador Java-- (Entrega 1: Análise Léxica + Sintática)
 *
 * Uso: java Main <arquivo_entrada> [<arquivo_saida>]
 *
 * Saída: imprime no console E grava em saida.txt:
 *   - Erros léxicos e sintáticos com número de linha
 *   - Mensagem final de sucesso ou falha
 *
 * Linguagem: Java-- (Compiladores — 7º CC)
 * Grupo: Mayssa Barbosa Dias; Larissa Queiroz Ramos; Fernando Medeiros; Matheus Augusto
 */
public class Main {

    public static void main(String[] args) throws Exception {

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

            // ── Verifica arquivo de entrada ─────────────────────────────────────────
            File file = new File(arquivoEntrada);
            if (!file.exists()) {
                log(pw, "ERRO: arquivo de entrada não encontrado: " + arquivoEntrada);
                return;
            }

            // ── Análise Léxica e Sintática ────────────────────────────────────────────────
            log(pw, "--- FASE 1 e 2: ANÁLISE LÉXICA E SINTÁTICA ---");
            Scanner scanner = new Scanner(new FileReader(file));
            parser p = new parser(scanner);
            p.setOutput(pw);
            
            try {
                p.parse();
                log(pw, "");
                log(pw, ">>> Análise sintática concluída com sucesso <<<");
            } catch (Exception e) {
                log(pw, "");
                log(pw, ">>> Análise encerrada com erros <<<");
                log(pw, "    Erro detectado: " + e.getMessage());
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
