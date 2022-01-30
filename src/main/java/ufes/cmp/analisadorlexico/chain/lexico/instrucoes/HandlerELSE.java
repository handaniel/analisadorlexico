package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerELSE extends AbstractHandler {

    public HandlerELSE(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("else")) {
            token.setCategoria("Instrucao_ELSE");
        } else {
            this.setProximo(new HandlerFunction(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "else") >= 0.8) {
            return "Esse token pode ser substituido por: Instrucao_ELSE ";
        } else if (StringUtils.similarity(token.getSimbolo(), "else") > 0.5) {
            return "Esse token tem similaridade com: Instrucao_ELSE; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
