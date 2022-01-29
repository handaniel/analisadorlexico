package ufes.cmp.analisadorlexico.chain.lexico.operador.comparacoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerIn extends AbstractHandler{

    public HandlerIn(Token token){
        super(token);
    }
    @Override
    public void executar(Token token) {
         if (token.getSimbolo().toString().toLowerCase().compareTo("in") == 0){
            token.setCategoria("operador_comparacao_IN");
        } else {
            this.setProximo(new HandlerMaior(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "in") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_comparacao_IN ";
        } else if(StringUtils.similarity(token.getSimbolo(), "in") > 0.5 ){
            return "Esse token tem similaridade com: operador_comparacao_IN; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
    
}
