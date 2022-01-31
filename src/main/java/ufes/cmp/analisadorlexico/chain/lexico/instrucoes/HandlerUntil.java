package ufes.cmp.analisadorlexico.chain.lexico.instrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.identificador.HandlerIdentificador;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerUntil extends AbstractHandler {

    public HandlerUntil(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if(token.getSimbolo().toLowerCase().equals("until")){
            token.setCategoria("instrucao_until");
        } else {
            this.setProximo(new HandlerIdentificador(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), "until") >= 0.8) {
            return "Esse token pode ser substituido por: instrucao_until";
        } else if (StringUtils.similarity(token.getSimbolo(), "until") > 0.5) {
            return "Esse token tem similaridade com: instrucao_until " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }

}
