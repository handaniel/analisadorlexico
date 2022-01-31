package ufes.cmp.analisadorlexico.chain.lexico.separadores;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.instrucoes.HandlerWhile;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerVirgula extends AbstractHandler {

    public HandlerVirgula(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals(",")) {
            token.setCategoria("separador_virgula");
        } else {
            this.setProximo(new HandlerWhile(token));
        }

    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), ",") >= 0.8) {
            return "Esse token pode ser substituido por: separador_virgula";
        } else if (StringUtils.similarity(token.getSimbolo(), ",") > 0.5) {
            return "Esse token tem similaridade com: separador_virgula " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
