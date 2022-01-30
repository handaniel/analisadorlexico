package ufes.cmp.analisadorlexico.chain.lexico.operador.logico;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.operador.aritmetico.HandlerDivisaoInteira;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerOr extends AbstractHandler{
    public HandlerOr(Token token){
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("or") == 0){
            token.setCategoria("operador_logico_OR");
        } else {
            this.setProximo(new HandlerDivisaoInteira(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "or") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_logico_OR ";
        } else if(StringUtils.similarity(token.getSimbolo(), "or") > 0.5 ){
            return "Esse token tem similaridade com: operador_logico_OR; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
}
