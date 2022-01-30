package ufes.cmp.analisadorlexico.chain.lexico.operador;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.identificador.HandlerIdentificador;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerAtribuicao extends AbstractHandler {

    public HandlerAtribuicao(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().toLowerCase().equals(":=")) {
            token.setCategoria("operador_atribuicao");
        } else {
            this.setProximo(new HandlerIdentificador(token));
        }
        
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), ":=") >= 0.8) {
            return "Esse token pode ser substituido por: operador_atribuicao";
        } else if(StringUtils.similarity(token.getSimbolo(), ":=") > 0.5) {
            return "Esse token tem similaridade com: operador_atribuicao; " + next.recuperarErro(token); 
        }

        return next.recuperarErro(token);
    }
    
}
