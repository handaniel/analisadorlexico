package ufes.cmp.analisadorlexico.chain.lexico.operador.logico;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.operador.aritmetico.HandlerDivisao;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerOu extends AbstractHandler {
    public HandlerOu(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().compareTo("ou") == 0) {
            token.setCategoria("operador_logico_OU");
        } else {
            this.setProximo(new HandlerDivisao(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "ou") >= 0.8) {
            return "Esse token pode ser substituido por: operador_logico_OU ";
        } else if (StringUtils.similarity(token.getSimbolo(), "ou") > 0.5) {
            return "Esse token tem similaridade com: operador_logico_OU; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }
}
