package ufes.cmp.analisadorlexico.chain.lexico.operador.comparadores;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerDiferente extends AbstractHandler {

    public HandlerDiferente(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().toLowerCase().equals("<>")) {
            System.out.println("bosta");
            token.setCategoria("operador_comparacao_diferente");
        } else {
            this.setProximo(new HandlerIgual(token));
        }
        
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "<>") >= 0.8) {
            return "Esse token pode ser substituido por: operador_comparacao_diferente";
        } else if(StringUtils.similarity(token.getSimbolo(), "<>") > 0.5) {
            return "Esse token tem similaridade com: operador_comparacao_diferente; " + next.recuperarErro(token); 
        }

        return recuperarErro(token);
    }
    
}
