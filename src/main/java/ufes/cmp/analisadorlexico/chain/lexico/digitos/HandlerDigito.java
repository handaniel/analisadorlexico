package ufes.cmp.analisadorlexico.chain.lexico.digitos;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;

public class HandlerDigito extends AbstractHandler {

    public HandlerDigito(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String recuperarErro(Token token) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
