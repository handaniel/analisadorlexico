package ufes.cmp.analisadorlexico.chain.error;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;

public class HandlerError extends AbstractHandler {

    public HandlerError(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        token.setCategoria("error");
    }

    @Override
    public String recuperarErro(Token token) {
        return "Este token é inválido";
    }
    
}
