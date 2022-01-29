package ufes.cmp.analisadorlexico.chain.lexico.bloco;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerEND extends AbstractHandler {

    public HandlerEND(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("end")) {
            token.setCategoria("Delimitador_bloco_END");
        } else {

        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "end") >= 0.8) {
            return "Esse token pode ser substituido por: Delimitador_bloco_END";
        } else if (StringUtils.similarity(token.getSimbolo(), "end") > 0.5) {
            return "Esse token tem similaridade com: Delimitador_bloco_END; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
