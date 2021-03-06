package ufes.cmp.analisadorlexico.chain.lexico.operador.comparadores;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerIgual extends AbstractHandler {

    public HandlerIgual(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().toLowerCase().equals("=")) {
            token.setCategoria("operador_comparacao_igual");
        } else {
            this.setProximo(new HandlerMaior(token));
        }
        
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "=") >= 0.8) {
            return "Esse token pode ser substituido por: operador_comparacao_igual";
        } else if(StringUtils.similarity(token.getSimbolo(), "=") > 0.5) {
            return "Esse token tem similaridade com: operador_comparacao_igual; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }
    
}
