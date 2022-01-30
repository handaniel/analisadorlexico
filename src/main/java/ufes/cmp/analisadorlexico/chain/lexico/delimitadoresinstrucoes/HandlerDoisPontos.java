package ufes.cmp.analisadorlexico.chain.lexico.delimitadoresinstrucoes;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.especificadores.HandlerConst;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerDoisPontos extends AbstractHandler {

    public HandlerDoisPontos(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals(":")) {
            token.setCategoria("Delimitador_dois_pontos");
        } else {
            this.setProximo(new HandlerConst(token));
        }
        
    }

    @Override
    public String recuperarErro(Token token) {
        if (StringUtils.similarity(token.getSimbolo(), ";") >= 0.8) {
            return "Esse token pode ser substituido por: Delimitador_dois_pontos ";
        } else if (StringUtils.similarity(token.getSimbolo(), ";") > 0.5) {
            return "Esse token tem similaridade com: Delimitador_dois_pontos " + next.recuperarErro(token);
        }

        return next.recuperarErro(token);
    }
    
}
