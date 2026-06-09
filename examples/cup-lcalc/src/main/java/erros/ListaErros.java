package erros;

import java.util.ArrayList;
import java.util.List;

public class ListaErros {
    private List<Erro> erros = new ArrayList<>();

    public void defineErro(int linha, int coluna, String texto) {
        erros.add(new Erro(linha, coluna, texto));
    }

    public void defineErro(int linha, int coluna) {
        erros.add(new Erro(linha, coluna));
    }

    // preenche a mensagem do ultimo erro inserido (chamado pelo syntax_error)
    public void defineErro(String texto) {
        if (!erros.isEmpty()) {
            erros.get(erros.size() - 1).setMensagem(texto);
        }
    }

    public boolean temErros() {
        return !erros.isEmpty();
    }

    public void imprimir() {
        if (erros.isEmpty()) {
            System.out.println("Nenhum erro encontrado.");
        } else {
            System.out.println("=== Lista de Erros ===");
            for (Erro e : erros) {
                System.out.println(e);
            }
        }
    }
}
