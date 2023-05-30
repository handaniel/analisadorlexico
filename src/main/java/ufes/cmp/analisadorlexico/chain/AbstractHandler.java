package ufes.cmp.analisadorlexico.chain;

import ufes.cmp.analisadorlexico.model.Token;

public abstract class AbstractHandler {

    protected AbstractHandler next;
    protected static int tamanhoMaxID = 15;

    public AbstractHandler(Token token) {
        executar(token);
    }

    public AbstractHandler(AbstractHandler next) {
        this.next = next;
    }

    public void setProximo(AbstractHandler proximo) {
        this.next = proximo;
    }

    public abstract void executar(Token token);

    public abstract String recuperarErro(Token token);

    public static void setTamanhoMaxID(int tamanhoMaxID) {
        AbstractHandler.tamanhoMaxID = tamanhoMaxID;
    }

}
