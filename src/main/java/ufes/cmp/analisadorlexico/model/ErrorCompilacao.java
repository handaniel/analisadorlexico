package ufes.cmp.analisadorlexico.model;

public class ErrorCompilacao {

    private Token token;
    private String mensagemErro;

    public ErrorCompilacao(Token token, String mensagemErro) {
        this.token = token;
        this.mensagemErro = mensagemErro;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

}
