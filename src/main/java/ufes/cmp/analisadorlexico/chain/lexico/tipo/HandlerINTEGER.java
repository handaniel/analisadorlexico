package ufes.cmp.analisadorlexico.chain.lexico.tipo;

import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.utils.StringUtils;

public class HandlerINTEGER extends AbstractHandler {

    public HandlerINTEGER(Token token) {
        super(token);
    }

    @Override
    public void executar(Token token) {
        if (token.getSimbolo().equals("integer")) {
            token.setCategoria("Especificador_INTEGER");
        } else {
            this.setProximo(new HandlerREAL(token));
        }
    }

    @Override
    public String recuperarErro(Token token) {
        if(StringUtils.similarity(token.getSimbolo(), "integer") >= 0.8 ){
            return "Esse token pode ser substituido por: Especificador_INTEGER ";
        } else if(StringUtils.similarity(token.getSimbolo(), "integer") > 0.5 ){
            return "Esse token tem similaridade com: Especificador_INTEGER; "+ next.recuperarErro(token);
        } 
        
        return next.recuperarErro(token);
    }

}
