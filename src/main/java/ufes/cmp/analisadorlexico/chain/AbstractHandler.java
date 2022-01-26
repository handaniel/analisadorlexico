package ufes.cmp.analisadorlexico.chain;

public abstract class AbstractHandler {

    protected AbstractHandler next;
    protected static int tamanhoMaxID = 15;

    public AbstractHandler(AbstractHandler next) {
        this.next = next;
    }
    
    

}
