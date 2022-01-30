package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerBREAK extends AbstractHandler {

    public HandlerBREAK(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("break")) {
            token.setCategoria("Instrucao_BREAK");
        } else {
            this.setProximo(new HandlerCONTINUE(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "break") >= 0.8) {
            return "Esse token pode ser substituido por: Instrucao_BREAK";
        } else if (StringUtils.similarity(token.getSimbolo(), "break") > 0.5) {
            return "Esse token tem similaridade com: Instrucao_BREAK; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
