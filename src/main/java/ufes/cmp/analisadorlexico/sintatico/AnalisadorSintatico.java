package ufes.cmp.analisadorlexico.sintatico;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ufes.cmp.analisadorlexico.model.Erros;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.view.PrincipalView;

public class AnalisadorSintatico {

    private PrincipalView view;
    private List<Token> original;
    private List<Token> tokens;
    private JTree arvore;
    private ArrayList<DefaultMutableTreeNode> listaNos;
    private Erros handlerErro;

    private Token analisado;
    private ArrayList<Token> pilha;

    public AnalisadorSintatico(PrincipalView view) {
        this.view = view;
        listaNos = new ArrayList<>();
    }

    public Erros analiseSintatica(List<Token> tokens, Erros erros) {
        this.tokens = new ArrayList<>();
        this.tokens.addAll(tokens);
        this.original = tokens;
        this.handlerErro = erros;

        criarArvore();

        return this.handlerErro;
    }

    private void criarArvore() {
        DefaultMutableTreeNode no = new DefaultMutableTreeNode("Programa");
        DefaultTreeModel modelo = new DefaultTreeModel(no);

        LookAndFeel anterior = UIManager.getLookAndFeel();

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            arvore = new JTree(modelo);
            UIManager.setLookAndFeel(anterior);
            arvore.putClientProperty("JTree.lineStyle", "Angled");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar arvore");
        }

        listaNos.add(no);

        JScrollPane blocoArvore = new JScrollPane(arvore);
        blocoArvore.setViewportView(arvore);

        if (this.view.getTbpResultados().getTabCount() <= 1) {
            this.view.getTbpResultados().add("Arvore Sintática", blocoArvore);
        } else {
            this.view.getTbpResultados().removeTabAt(1);
            this.view.getTbpResultados().add("Arvore Sintática", blocoArvore);
        }

        arvore.setShowsRootHandles(true);

    }

    private DefaultMutableTreeNode insereNo(DefaultMutableTreeNode pai, String filho) {
        DefaultMutableTreeNode novo = new DefaultMutableTreeNode(filho);
        pai.add(novo);
        listaNos.add(novo);
        return novo;
    }

    private boolean tipo() {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Especificador_CHAR":
                case "Especificador_INTEGER":
                case "Especificador_REAL":
                case "Especificador_STRING":
                    this.tokens.remove(0);
                    talvez = true;
                    insereNo(listaNos.get(listaNos.size() - 1), "<tipo>");
                    insereNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean id() {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "ID":
                    this.tokens.remove(0);
                    talvez = true;
                    insereNo(listaNos.get(listaNos.size() - 1), "<ID>");
                    insereNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private void msgErro(String simboloEsperado) {
        if (analisado != null && !analisado.getCategoria().equals("error")) {

        }
    }
}
