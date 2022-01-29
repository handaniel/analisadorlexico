package ufes.cmp.analisadorlexico.chain.lexico.operador.comparacoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerMenor extends AbstractHandler{
    public HandlerMenor(Token token){
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("<") == 0){
            token.setCategoria("operador_comparacao_menor");
        } else {
            this.setProximo(new HandlerMenorIgual(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "<") >= 0.8 ){
            return "Esse token pode ser substituido por: operador_comparacao_menor ";
        } else if(StringUtils.similarity(token.getSimbolo(), "<") > 0.5 ){
            return "Esse token tem similaridade com: operador_comparacao_menor; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }
    
}
