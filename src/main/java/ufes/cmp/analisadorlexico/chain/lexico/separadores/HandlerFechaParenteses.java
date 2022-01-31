package ufes.cmp.analisadorlexico.chain.lexico.separadores;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerFechaParenteses extends AbstractHandler {

    public HandlerFechaParenteses(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().toLowerCase().equals(")")) {
            token.setCategoria("Separador_Fecha_Parenteses");
        } else {
            this.setProximo(new HandlerVirgula(token));
        }

    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), ")") >= 0.8) {
            return "Esse token pode ser substituido por: Separador_Fecha_Parenteses";
        } else if(StringUtils.similarity(token.getSimbolo(), ")") > 0.5) {
            return "Esse token tem similaridade com: Separador_Fecha_Parenteses; " + next.recuperarErro(token); 
        }

        return next.recuperarErro(token);
    }

}
