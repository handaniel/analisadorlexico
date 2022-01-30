package ufes.cmp.analisadorlexico.operador.aritmetico;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerDivsao extends AbstractHandler {
    public HandlerDivsao(Token token){
        super(token);
    }
    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("/") == 0){
            token.setCategoria("operador_aritmetico_divisao");
        } else {
            this.setProximo(new HandlerMultiplicacao(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "/") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_aritmetico_divisao ";
        } else if(StringUtils.similarity(token.getSimbolo(), "/") > 0.5 ){
            return "Esse token tem similaridade com: operador_aritmetico_divisao; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
    
}