package ufes.cmp.analisadorlexico.chain.lexico.identificador;

import java.util.regex.Pattern;
import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.error.HandlerError;
import ufes.cmp.analisadorlexico.model.Token;

public class HandlerIdentificador extends AbstractHandler {

    public HandlerIdentificador(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().length() <= AbstractHandler.tamanhoMaxID && Pattern.matches("[[a-z]+][[a-z0-9]+|[_]+]*", token.getSimbolo())) {
            token.setCategoria("ID");
        } else {
            this.setProximo(new HandlerError(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        return next.recuperarErro(token);
    }

}
