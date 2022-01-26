package ufes.cmp.analisadorlexico.model;

public class Token {

    private int id;
    private String simbolo;
    private String categoria;
    private int posicaoInicio;
    private int posicaoFim;
    private LinhaCodigo linha;

    public Token(int id, String simbolo, String categoria,
            int posicaoInicio, int posicaoFim, LinhaCodigo linha) {
        this.id = id;
        this.simbolo = simbolo;
        this.categoria = categoria;
        this.posicaoInicio = posicaoInicio;
        this.posicaoFim = posicaoFim;
        this.linha = linha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPosicaoInicio() {
        return posicaoInicio;
    }

    public void setPosicaoInicio(int posicaoInicio) {
        this.posicaoInicio = posicaoInicio;
    }

    public int getPosicaoFim() {
        return posicaoFim;
    }

    public void setPosicaoFim(int posicaoFim) {
        this.posicaoFim = posicaoFim;
    }

    public LinhaCodigo getLinha() {
        return linha;
    }

    public void setLinha(LinhaCodigo linha) {
        this.linha = linha;
    }

}
