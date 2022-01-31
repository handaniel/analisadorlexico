package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;

public class HandlerDo extends AbstractHandler {

    public HandlerDo(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().toLowerCase().equals("do")) {
            token.setCategoria("instrucao_do");
        } else {
            this.setProximo(new HandlerRepeat(token));
        }
        
    }

    @Override
    public String recuperarErro(Token token) {
        return next.recuperarErro(token);
    }

}
