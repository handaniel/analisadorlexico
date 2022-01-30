
package ufes.cmp.analisadorlexico.operador.aritmetico;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerMultiplicacao extends AbstractHandler{

    public HandlerMultiplicacao(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("*") == 0){
            token.setCategoria("operador_aritmetico_multiplicacao");
        } else {
            this.setProximo(new HandlerSoma(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "*") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_aritmetico_multiplicacao ";
        } else if(StringUtils.similarity(token.getSimbolo(), "*") > 0.5 ){
            return "Esse token tem similaridade com: operador_aritmetico_multiplicacao; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
    
}
