
package ufes.cmp.analisadorlexico.chain.lexico.operador.aritmetico;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerDivisaoInteira extends AbstractHandler{
    public HandlerDivisaoInteira(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("div") == 0){
            token.setCategoria("operador_aritmetico_divisao_inteira");
        } else {
            this.setProximo(new HandlerDivisaoReal(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "div") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_aritmetico_divisao_inteira ";
        } else if(StringUtils.similarity(token.getSimbolo(), "div") > 0.5 ){
            return "Esse token tem similaridade com: operador_aritmetico_divisao_inteira; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
}
