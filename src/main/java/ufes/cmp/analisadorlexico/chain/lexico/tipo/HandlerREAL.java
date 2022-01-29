package ufes.cmp.analisadorlexico.chain.lexico.tipo;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerREAL extends AbstractHandler {

    public HandlerREAL(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().equals("real")) {
            token.setCategoria("Especificador_REAL");
        } else {
            this.setProximo(new HandlerSTRING(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "real") >= 0.8 ){
            return "Esse token pode ser substituido por: Especificador_REAL ";
        } else if(StringUtils.similarity(token.getSimbolo(), "real") > 0.5 ){
            return "Esse token tem similaridade com: Especificador_REAL; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }

}
