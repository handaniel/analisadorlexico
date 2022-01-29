package ufes.cmp.analisadorlexico.chain.lexico.operador.aritmetico;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerDivisaoRest extends AbstractHandler{
    public HandlerDivisaoRest(Token token){
        super(token);
    }
    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("mod") == 0){
            token.setCategoria("operador_aritmetico_divisao_resto");
        } else {
            this.setProximo(next);
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "mod") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_aritmetico_divisao_resto ";
        } else if(StringUtils.similarity(token.getSimbolo(), "mod") > 0.5 ){
            return "Esse token tem similaridade com: operador_aritmetico_divisao_resto; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
    
}
