package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerContinue extends AbstractHandler {

    public HandlerContinue(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("continue")) {
            token.setCategoria("Instrucao_CONTINUE");
        } else {
            this.setProximo(new HandlerElse(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "continue") >= 0.8) {
            return "Esse token pode ser substituido por: Instrucao_CONTINUE ";
        } else if (StringUtils.similarity(token.getSimbolo(), "continue") > 0.5) {
            return "Esse token tem similaridade com: Instrucao_CONTINUE; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
