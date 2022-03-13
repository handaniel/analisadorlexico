package ufes.cmp.analisadorlexico.chain.lexico.delimitadoresinstrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerPonto extends AbstractHandler {

    public HandlerPonto(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals(".")) {
            token.setCategoria("Delimitador_instrucoes_ponto");
        } else {
            this.setProximo(new HandlerDoisPontos(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), ".") >= 0.8) {
            return "Esse token pode ser substituido por: Delimitador_instrucoes_ponto ";
        } else if (StringUtils.similarity(token.getSimbolo(), ".") > 0.5) {
            return "Esse token tem similaridade com: Delimitador_instrucoes_ponto " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
