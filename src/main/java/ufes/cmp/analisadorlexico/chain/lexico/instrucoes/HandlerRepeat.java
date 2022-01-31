package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerRepeat extends AbstractHandler {

    public HandlerRepeat(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().toLowerCase().equals("repeat")){
            token.setCategoria("instrucao_repeat");
        } else {
            this.setProximo(new HandlerUntil(token));
        }
        
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "repeat") >= 0.8) {
            return "Esse token pode ser substituido por: instrucao_repeat";
        } else if (StringUtils.similarity(token.getSimbolo(), "repeat") > 0.5) {
            return "Esse token tem similaridade com: instrucao_repeat " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }
    
}
