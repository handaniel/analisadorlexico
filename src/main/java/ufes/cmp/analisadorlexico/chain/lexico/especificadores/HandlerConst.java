package ufes.cmp.analisadorlexico.chain.lexico.especificadores;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerConst extends AbstractHandler {

    public HandlerConst(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("const")) {
            token.setCategoria("Especificador_CONST");
        } else {
            this.setProximo(new HandlerProgram(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "const") >= 0.8 ){
            return "Esse token pode ser substituido por: Especificador_CONST ";
        } else if(StringUtils.similarity(token.getSimbolo(), "const") > 0.5 ){
            return "Esse token tem similaridade com: Especificador_CONST; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }

}
