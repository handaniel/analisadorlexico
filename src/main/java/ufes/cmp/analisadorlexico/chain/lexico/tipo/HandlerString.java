package ufes.cmp.analisadorlexico.chain.lexico.tipo;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.instrucoes.HandlerBreak;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerString extends AbstractHandler {

    public HandlerString(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("string")) {
            token.setCategoria("Especificador_STRING");
        } else {
            this.setProximo(new HandlerBreak(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "string") >= 0.8) {
            return "Esse token pode ser substituido por: Especificador_STRING ";
        } else if (StringUtils.similarity(token.getSimbolo(), "string") > 0.5) {
            return "Esse token tem similaridade com: Especificador_STRING; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
