package ufes.cmp.analisadorlexico.chain.lexico.identificador;

import java.util.regex.Pattern;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;

public class HandlerNUM extends AbstractHandler {

    public HandlerNUM(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().length() <= AbstractHandler.tamanhoMaxID && Pattern.matches("[0-9]{1,10}[.]{0,1}[0-9]{1,10}", token.getSimbolo())) {
            token.setCategoria("identificador_num");
        } else {
            this.setProximo(new HandlerDigito(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        return next.recuperarErro(token);
    }
    
}
