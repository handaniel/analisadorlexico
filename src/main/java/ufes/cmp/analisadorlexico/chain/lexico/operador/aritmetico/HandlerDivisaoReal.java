package ufes.cmp.analisadorlexico.chain.lexico.operador.aritmetico;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerDivisaoReal extends AbstractHandler{
    public HandlerDivisaoReal(Token token){
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("/") == 0){
            token.setCategoria("operador_aritmetico_divisao_Real");
        } else {
            this.setProximo(new HandlerDivisaoRest(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "/") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_aritmetico_divisao_real ";
        } else if(StringUtils.similarity(token.getSimbolo(), "/") > 0.5 ){
            return "Esse token tem similaridade com: operador_aritmetico_divisao_real; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
    
}
