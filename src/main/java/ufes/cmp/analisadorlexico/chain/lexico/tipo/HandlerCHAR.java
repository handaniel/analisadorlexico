package ufes.cmp.analisadorlexico.chain.lexico.tipo;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerCHAR extends AbstractHandler {

    public HandlerCHAR(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().equals("char")) {
            token.setCategoria("Especificador_CHAR");
            this.setProximo(next);
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "char") >= 0.8) {
            return "Esse token pode ser substituido por: Especificador_CHAR ";
        } else if (StringUtils.similarity(token.getSimbolo(), "char") > 0.5) {
            return "Esse token tem similaridade com: Especificador_CHAR; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
