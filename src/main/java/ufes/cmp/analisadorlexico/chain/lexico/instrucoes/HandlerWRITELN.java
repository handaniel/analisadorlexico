package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.operador.logico.HandlerAND;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerWRITELN extends AbstractHandler{
    public HandlerWRITELN(Token token){
        super(token);
    }

    @Override
    public void executar(Token token) {
         if (token.getSimbolo().toLowerCase().equals("writeln")) {
            token.setCategoria("Instrucao_writeln");
        } else {
            this.setProximo(new HandlerAND(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "writeln") >= 0.8) {
            return "Esse token pode ser substituido por: Instrucao_writeln ";
        } else if (StringUtils.similarity(token.getSimbolo(), "writeln") > 0.5) {
            return "Esse token tem similaridade com: Instrucao_writeln; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }
}
