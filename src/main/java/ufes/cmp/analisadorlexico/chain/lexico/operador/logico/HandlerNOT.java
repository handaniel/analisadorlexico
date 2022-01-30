package ufes.cmp.analisadorlexico.chain.lexico.operador.logico;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerNOT extends AbstractHandler{
    public HandlerNOT(Token token){
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("not") == 0){
            token.setCategoria("operador_logico_NOT");
        } else {
            this.setProximo(new HandlerOR(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
       if(StringUtils.similarity(token.getSimbolo(), "not") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_logico_NOT ";
        } else if(StringUtils.similarity(token.getSimbolo(), "not") > 0.5 ){
            return "Esse token tem similaridade com: operador_logico_NOT; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
}
