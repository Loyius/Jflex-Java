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

    // varre do fim para o início procurando o primeiro erro sem texto e preenche
    public void defineErro(String texto) {
        for (int i = erros.size() - 1; i >= 0; i--) {
            Erro e = erros.get(i);
            if (e.getMensagem().isEmpty()) {
                e.setMensagem(texto);
                return;
            }
        }
    }

    public boolean temErros() {
        return !erros.isEmpty();
    }

    public void imprimir() {
        if (erros.isEmpty()) {
            System.out.println("Analise concluida sem erros.");
        } else {
            System.out.println("=== Lista de Erros ===");
            for (Erro e : erros) {
                System.out.println(e);
            }
        }
    }
}
