package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;

public class HandlerWhile extends AbstractHandler {

    public HandlerWhile(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().toLowerCase().equals("while")) {
            token.setCategoria("instrucao_while");
        } else {
            this.setProximo(new HandlerDo(token));
        }
        
    }

    @Override
    public String recuperarErro(Token token) {
        return next.recuperarErro(token);
    }
    
}
