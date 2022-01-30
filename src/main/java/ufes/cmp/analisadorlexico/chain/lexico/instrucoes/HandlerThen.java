package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.operador.logico.HandlerAnd;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerThen extends AbstractHandler {

    public HandlerThen(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("then")) {
            token.setCategoria("Instrucao_THEN");
        } else {
            this.setProximo(new HandlerAnd(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "then") >= 0.8) {
            return "Esse token pode ser substituido por: Instrucao_THEN ";
        } else if (StringUtils.similarity(token.getSimbolo(), "then") > 0.5) {
            return "Esse token tem similaridade com: Instrucao_THEN; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
