package ufes.cmp.analisadorlexico.chain.lexico.delimitadoresbloco;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerBEGIN extends AbstractHandler {

    public HandlerBEGIN(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("begin")) {
            token.setCategoria("Delimitador_bloco_BEGIN");
        } else {
            this.setProximo(new HandlerEND(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "begin") >= 0.8) {
            return "Esse token pode ser substituido por: Delimitador_bloco_BEGIN ";
        } else if (StringUtils.similarity(token.getSimbolo(), "begin") > 0.5) {
            return "Esse token tem similaridade com: Delimitador_bloco_BEGIN; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
