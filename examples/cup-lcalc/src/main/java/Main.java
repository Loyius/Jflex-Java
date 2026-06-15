import java.io.*;
import erros.ListaErros;

public class Main {
    public static void main(String[] argv) {
        ListaErros listaErros = new ListaErros();
        try {
            Lexer lexer = new Lexer(new FileReader(argv[0]), listaErros);
            parser p = new parser(lexer);
            p.parse();
            p.imprimirRelatorio();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaErros.imprimir();
    }
}
