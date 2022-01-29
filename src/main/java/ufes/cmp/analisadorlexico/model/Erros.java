package ufes.cmp.analisadorlexico.model;

import java.util.ArrayList;
import java.util.List;

public class Erros {

    private List<ErrorCompilacao> listErro;

    public Erros() {
        listErro = new ArrayList<>();
    }

    public String getMensagem(Token token) {
        for (ErrorCompilacao elemento : listErro) {
            if (elemento.getToken().equals(token)) {
                return elemento.getMensagemErro();
            }
        }

        return null;
    }

    public void addErro(Token token, String mensagemErro) {
        listErro.add(new ErrorCompilacao(token, mensagemErro));
    }

    public List<ErrorCompilacao> getListErro() {
        return listErro;
    }

}
