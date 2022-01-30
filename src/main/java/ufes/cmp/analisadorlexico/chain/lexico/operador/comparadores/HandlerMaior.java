package ufes.cmp.analisadorlexico.chain.lexico.operador.comparadores;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerMaior extends AbstractHandler {

    public HandlerMaior(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toString().toLowerCase().equals(">")) {
            token.setCategoria("operador_comparacao_maior");
        } else {
            this.setProximo(new HandlerMaiorIgual(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), ">") >= 0.8) {
            return "Esse token pode ser substituido por: operador_comparacao_maior";
        } else if(StringUtils.similarity(token.getSimbolo(), ">") > 0.5) {
            return "Esse token tem similaridade com: operador_comparacao_maior; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}