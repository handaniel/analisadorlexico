package ufes.cmp.analisadorlexico.model;

public class LinhaCodigo {

    String conteudo;
    int posicao;

    public LinhaCodigo(String conteudo, int posicao) {
        this.conteudo = conteudo;
        this.posicao = posicao;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

}
