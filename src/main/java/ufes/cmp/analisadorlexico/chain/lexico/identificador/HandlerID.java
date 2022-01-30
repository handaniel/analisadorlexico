package ufes.cmp.analisadorlexico.chain.lexico.identificador;

import java.util.regex.Pattern;
import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;

public class HandlerID extends AbstractHandler {

    public HandlerID(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().length() <= AbstractHandler.tamanhoMaxID && Pattern.matches("[[A-Za-z]+][[A-Za-z0-9]+|[_]+]*", token.getSimbolo())) {
            token.setCategoria("identificador");
        } else {
            this.setProximo(next);
        }
    }

    @Override
    public String recuperarErro(Token token) {
        return next.recuperarErro(token);
    }

}
