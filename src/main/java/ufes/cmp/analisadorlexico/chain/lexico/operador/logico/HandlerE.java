package ufes.cmp.analisadorlexico.chain.lexico.operador.logico;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerE extends AbstractHandler{
    public HandlerE(Token token){
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("E") == 0){
            token.setCategoria("operador_logico_E");
        } else {
            this.setProximo(new HandlerOu(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
         if(StringUtils.similarity(token.getSimbolo(), "e") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_logico_E ";
        } else if(StringUtils.similarity(token.getSimbolo(), "e") > 0.5 ){
            return "Esse token tem similaridade com: operador_logico_E; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
}
