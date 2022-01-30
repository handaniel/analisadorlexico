package ufes.cmp.analisadorlexico.chain.lexico.identificador;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;

public class HandlerLiteral extends AbstractHandler {

    public HandlerLiteral(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().length() <= AbstractHandler.tamanhoMaxID
                && token.getSimbolo().toLowerCase().matches("[']{1,1}[a-z]{1,}[0-9a-z_]*[']{1,1}")) {
            token.setCategoria("identificador_literal");
        } else {
            this.setProximo(next);
        }
    }

    @Override
    public String recuperarErro(Token token) {
        return next.recuperarErro(token);
    }

}
