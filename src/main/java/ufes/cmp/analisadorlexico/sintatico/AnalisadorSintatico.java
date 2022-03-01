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
        this.pilha = new ArrayList<>();

        this.analisarToken();

        /*
         * if (!pilha.isEmpty()) {
         * this.msgErro("<.>");
         * }
         * 
         */

        for (int i = 0; i < this.arvore.getRowCount(); i++) {
            arvore.expandRow(i);
            arvore.setShowsRootHandles(true);
        }

        return this.handlerErro;
    }

    private void analisarToken() {
        try {
            if (!programa()) {
                System.out.println(tokens.size());

            } else {
                while (!this.tokens.isEmpty()) {
                    analisarToken();
                }
            }
        } catch (Exception e) {
            recuperarErro();
            while (!this.tokens.isEmpty()) {
                analisarToken();
            }
        }

        if (!this.tokens.isEmpty()) {
            analisarToken();
        }
    }

    private void recuperarErro() {
        if (!this.tokens.isEmpty()) {
            Token temp;
            temp = this.tokens.get(0);
            while ((!this.tokens.isEmpty()) && (!sincronizadorToken(temp))) {
                if (!this.tokens.isEmpty()) {
                    this.tokens.remove(0);
                }
                if (!this.tokens.isEmpty()) {
                    temp = this.tokens.get(0);
                }
            }
            if (!this.tokens.isEmpty()) {
                if (this.tokens.get(0).getSimbolo().equals(";")) {
                    this.tokens.remove(0);
                }
            }
        }

        inserirNo(listaNos.get(listaNos.size() - 1), "<ERRO>");
        listaNos.remove(listaNos.size() - 1);
    }

    private boolean sincronizadorToken(Token token) {
        boolean talvez = false;

        switch (token.getCategoria()) {
            case "sla":
                talvez = true;
                break;
        }

        return talvez;
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

    private DefaultMutableTreeNode inserirNo(DefaultMutableTreeNode pai, String filho) {
        DefaultMutableTreeNode novo = new DefaultMutableTreeNode(filho);
        pai.add(novo);
        listaNos.add(novo);
        return novo;
    }

    private boolean tipo() throws Exception {
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
                    inserirNo(listaNos.get(listaNos.size() - 1), "<tipo>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean programa() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            if (EspProgram()) {
                if (idId()) {
                    if (!pontoEVirgula()) {
                        this.msgErro("<;>");
                    }
                } else {
                    this.msgErro("<id>");
                }
            } else {
                this.msgErro("<program>");
            }
        }

        return talvez;
    }

    private boolean idId() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "ID":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<ID>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean idLiteral() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "identificador_literal":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<identificadorLiteral>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean idNum() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "identificador_num":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<identificadorNum>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean idDigito() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "digito":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<identificadorDigito>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean EspProgram() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Especificador_PROGRAM":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<especificador>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }

        }

        return talvez;
    }

    private boolean pontoEVirgula() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Delimitador_instrucoes_pontoevirgula":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<delimitadorPontoEVirgula>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }

        }

        return talvez;
    }

    private boolean SepAbreParentese() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Separador_Abre_Parenteses":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<SepAbreParentese>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }

        }

        return talvez;
    }

    private boolean SepFechaParentese() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Separador_Fecha_Parenteses":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<SepFechaParentese>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }

        }

        return talvez;
    }

    private boolean SepVirgula() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "separador_virgula":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<Virgula>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }

        }

        return talvez;
    }

    private boolean EspVar() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Especificador_VAR":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<EspecificadorVar>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }

        }

        return talvez;
    }

    private boolean EspConst() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Especificador_CONST":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<EspecificadorConst>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }

        }

        return talvez;
    }

    private boolean opAtribuicao() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "operador_atribuicao":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<operadorAtribuicao>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean opExpressaoBinaria() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "operador_aritmetico_soma":
                case "operador_aritmetico_subtracao":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<expSoma2>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
                case "operador_aritmetico_multiplicacao":
                case "operador_aritmetico_divisao":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<expMullt2>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
                case "operador_comparacao_diferente":
                case "operador_comparacao_igual":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<expIgual2>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
                case "operador_comparacao_maior":
                case "operador_comparacao_maior_igual":
                case "operador_comparacao_menor":
                case "operador_comparacao_menor_igual":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<expRelacional2>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
                case "operador_logico_OU":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<expOu>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
                case "operador_logico_E":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<expE>");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private void msgErro(String simboloEsperado) {
        if (analisado != null && !analisado.getCategoria().equals("error")) {
            this.handlerErro.addErro(analisado,
                    "Erro sintático: Encontrado " + analisado.getSimbolo() + ", esperado " + simboloEsperado);
        } else {
            this.handlerErro.addErro(null, "Erro sintático: Esperado " + simboloEsperado);
        }
    }
}
