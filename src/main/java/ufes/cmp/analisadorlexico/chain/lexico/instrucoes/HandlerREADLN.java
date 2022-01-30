
package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerREADLN extends AbstractHandler{
    public HandlerREADLN(Token token){
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals("readln")) {
            token.setCategoria("Instrucao_readln");
        } else {
            this.setProximo(next);
        }
    }

    @Override
    public String recuperarErro(Token token) {
       if (StringUtils.similarity(token.getSimbolo(), "readln") >= 0.8) {
            return "Esse token pode ser substituido por: Instrucao_readln ";
        } else if (StringUtils.similarity(token.getSimbolo(), "readln") > 0.5) {
            return "Esse token tem similaridade com: Instrucao_readln; " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }
    
}
