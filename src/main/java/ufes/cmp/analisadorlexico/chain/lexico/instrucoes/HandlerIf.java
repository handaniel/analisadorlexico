package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerIf extends AbstractHandler {

    public HandlerIf(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("if")) {
            token.setCategoria("Instrucao_IF");
        } else {
            this.setProximo(new HandlerProcedure(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "if") >= 0.8 ){
            return "Esse token pode ser substituido por: Instrucao_IF ";
        } else if(StringUtils.similarity(token.getSimbolo(), "if") > 0.5 ){
            return "Esse token tem similaridade com: Instrucao_IF; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }

}
