package ufes.cmp.analisadorlexico.chain.lexico.identificador;

import java.util.regex.Pattern;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;

public class HandlerDigito extends AbstractHandler {

    public HandlerDigito(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().length() <= AbstractHandler.tamanhoMaxID && Pattern.matches("[0-9]", token.getSimbolo())) {
            token.setCategoria("digito");
        } else {
            this.setProximo(new HandlerIdentificador(token));
        }
        
    }

    @Override
    public String recuperarErro(Token token) {
        return next.recuperarErro(token);
    }
    
}
