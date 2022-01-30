package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerProcedure extends AbstractHandler {

    public HandlerProcedure(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("procedure")) {
            token.setCategoria("Instrucao_procedure");
        } else {
            this.setProximo(new HandlerREADLN(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "procedure") >= 0.8) {
            return "Esse token pode ser substituido por: Instrucao_procedure ";
        } else if (StringUtils.similarity(token.getSimbolo(), "procedure") > 0.5) {
            return "Esse token tem similaridade com: Instrucao_procedure; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
