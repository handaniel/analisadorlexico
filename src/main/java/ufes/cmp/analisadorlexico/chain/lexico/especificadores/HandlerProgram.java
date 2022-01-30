package ufes.cmp.analisadorlexico.chain.lexico.especificadores;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerProgram extends AbstractHandler {
    
    public HandlerProgram(Token token) {
        super(token);
    }
    
    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("program")) {
            token.setCategoria("Especificador_PROGRAM");
        } else {
            this.setProximo(new HandlerVar(token));
        }
    }
    
    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "program") >= 0.8) {
            return "Esse token pode ser substituido por: Especificador_PROGRAM ";
        } else if (StringUtils.similarity(token.getSimbolo(), "program") > 0.5) {
            return "Esse token tem similaridade com: Especificador_PROGRAM; " + next.recuperarErro(token);
        }        
        return next.recuperarErro(token);
    }
    
}
