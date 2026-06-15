package erros;

public class Erro {
    private int linha;
    private int coluna;
    private String mensagem;

    public Erro(int linha, int coluna, String mensagem) {
        this.linha    = linha;
        this.coluna   = coluna;
        this.mensagem = mensagem;
    }

    public Erro(int linha, int coluna) {
        this(linha, coluna, "");
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return "ERRO [linha " + (linha + 1) + ", coluna " + (coluna + 1) + "]: " + mensagem;
    }
}
