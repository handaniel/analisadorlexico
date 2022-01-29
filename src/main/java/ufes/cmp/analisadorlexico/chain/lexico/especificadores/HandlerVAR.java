package ufes.cmp.analisadorlexico.chain.lexico.especificadores;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.tipo.HandlerCHAR;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerVAR extends AbstractHandler {

    public HandlerVAR(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().equals("var")) {
            token.setCategoria("Especificador_VAR");
        } else {
            this.setProximo(new HandlerCHAR(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "var") >= 0.8 ){
            return "Esse token pode ser substituido por: Especificador_VAR ";
        } else if(StringUtils.similarity(token.getSimbolo(), "var") > 0.5 ){
            return "Esse token tem similaridade com: Especificador_VAR; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }

}
