package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerFunction extends AbstractHandler {

    public HandlerFunction(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().toLowerCase().equals("function")){
            token.setCategoria("Instrucao_function");
        } else {
            this.setProximo(new HandlerIf(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "function") >= 0.8) {
            return "Esse token pode ser substituido por: Instrucao_function ";
        } else if (StringUtils.similarity(token.getSimbolo(), "function") > 0.5) {
            return "Esse token tem similaridade com: Instrucao_function; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
