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

        if (!pilha.isEmpty()) {
            this.msgErro("<.>");
        }

        for (int i = 0; i < this.arvore.getRowCount(); i++) {
            arvore.expandRow(i);
            arvore.setShowsRootHandles(true);
        }

        return this.handlerErro;
    }

    private void analisarToken() {
        try {
            if (programa()) {
                if (declaracoes()) {

                } else {
                    if (!this.tokens.isEmpty()) {
                        recuperarErro();
                        analisarToken();
                    }
                }
            } else {
                if (!this.tokens.isEmpty()) {
                    recuperarErro();
                    analisarToken();
                }
            }
        } catch (Exception e) {

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
                    } else {
                        talvez = true;
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

    private boolean declaracoes() throws Exception {
        boolean talvez = false;
        if (!this.tokens.isEmpty()) {
            declaracaoVariavel();
            declaracaoConstante();

        }
        return talvez;
    }

    private boolean declaracaoVariavel() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            if (EspVar()) {
                if (declaracaoVariavel1()) {
                    talvez = true;
                }
                declaracaoConstante();
            }
        }

        return talvez;
    }

    private boolean declaracaoProcedimento() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {

        }

        return talvez;
    }

    private boolean declProc() throws Exception {
        boolean talvez = false;
        if (!this.tokens.isEmpty()) {
            if (decProcedure() || decFunction()) {
                talvez = true;
                declProc();
            }
        }
        return talvez;
    }

    private boolean decProcedure() throws Exception {
        boolean talvez = false;
        if (!this.tokens.isEmpty()) {
            if (insProcedure()) {
                if (idId()) {
                    if (SepAbreParentese()) {
                        declaracaoVariavel1();
                        if (SepFechaParentese()) {
                            if (pontoEVirgula()) {
                                declaracaoVariavel();
                                if (bloco()) {
                                    talvez = true;
                                    declaracaoProcedimento();
                                }
                            } else {
                                this.msgErro("<;>");
                            }
                        } else {
                            this.msgErro("<)>");
                        }
                    } else {
                        this.msgErro("<(>");
                    }
                } else {
                    this.msgErro("id");
                }
            }
        }
        return talvez;
    }

    private boolean decFunction() throws Exception {
        boolean talvez = false;
        if (!this.tokens.isEmpty()) {
            if (insFunction()) {
                if (idId()) {
                    if (SepAbreParentese()) {
                        declaracaoVariavel1();
                        if (SepFechaParentese()) {
                            if (doisPontos()) {
                                if (tipo()) {
                                    if (pontoEVirgula()) {
                                        declaracaoVariavel();
                                        if (bloco()) {
                                            talvez = true;
                                            declaracaoProcedimento();
                                        }
                                    } else {
                                        this.msgErro("<;>");
                                    }
                                } else {
                                    this.msgErro("tipo");
                                }
                            } else {
                                this.msgErro("<:>");
                            }
                        } else {
                            this.msgErro("<(>");
                        }

                    } else {
                        this.msgErro("<(>");
                    }
                } else {
                    this.msgErro("id");
                }
            }
        }
        return talvez;
    }

    private boolean listaIds() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            if (SepVirgula()) {
                if (idId()) {
                    if (listaIds()) {
                        talvez = true;
                    }
                } else {
                    this.msgErro("id");
                }
            } else if (doisPontos()) {
                talvez = true;
            } else {
                this.msgErro("<:>");
            }
        }
        return talvez;
    }

    private boolean instrucao() throws Exception {
        boolean talvez = false;

        return talvez;
    }

    private boolean inst1() throws Exception {
        boolean talvez = false;

        if (idId()) {
            if (opAtribuicao()) {

            }
        }

        return talvez;
    }

    private void conteudoBloco() throws Exception {
        if (!this.tokens.isEmpty()) {
            inserirNo(listaNos.get(listaNos.size() - 1), "<conjuntoInstrucoes>");
            if (instrucao()) {
                conteudoBloco();
            }
            listaNos.remove(listaNos.size() - 1);

        }
    }

    private boolean bloco() throws Exception {

        if (!this.tokens.isEmpty()) {
            inserirNo(listaNos.get(listaNos.size() - 1), "<bloco>");
            if (begin()) {
                conteudoBloco();
                if (!end()) {
                    this.msgErro("end");
                }
                listaNos.remove(listaNos.size() - 1);
                return true;
            } else if (!pontoEVirgula()) {
                this.msgErro("begin ou <;>");
            }
            listaNos.remove(listaNos.size() - 1);
        }

        return false;
    }

    private boolean declaracaoVariavel1() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            if (idId()) {
                if (listaIds()) {
                    if (tipo()) {
                        if (pontoEVirgula()) {
                            talvez = true;
                            if (!declaracaoConstante()) {
                                declaracaoVariavel1();
                            }
                        } else {
                            this.msgErro("<;>");
                        }
                    } else {
                        this.msgErro("<tipo>");
                    }
                } else {
                    this.msgErro("id");
                }
            }
        }
        return talvez;
    }

    private boolean declaracaoConstante() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            if (EspConst()) {
                if (declaracaoConstante1()) {
                    talvez = true;
                }
            }
        }

        return talvez;
    }

    private boolean declaracaoConstante1() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            if (idId()) {
                if (constOpc()) {
                    talvez = true;
                }
            } else {
                this.msgErro("id");
            }
        }

        return talvez;
    }

    private boolean constOpc() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            if (doisPontos()) {
                if (tipo()) {
                    if (opAtribuicaoConst()) {
                        if (idNum() || idLiteral() || idDigito()) {
                            if (pontoEVirgula()) {
                                talvez = true;
                                constOpc();
                            } else {
                                this.msgErro("<;>");
                            }
                        } else {
                            this.msgErro("valor");
                        }
                    } else {
                        this.msgErro("<=>");
                    }
                } else {
                    this.msgErro("tipo");
                }
            } else {
                if (opAtribuicaoConst()) {
                    if (idNum() || idLiteral()) {
                        if (pontoEVirgula()) {
                            talvez = true;
                        } else {
                            this.msgErro("<:>");
                        }
                    } else {
                        this.msgErro("valor");
                    }
                } else {
                    this.msgErro("<=>");
                }
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

    private boolean doisPontos() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Delimitador_dois_pontos":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<delimitadorDoisPontos>");
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

    private boolean opAtribuicaoConst() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "operador_comparacao_igual":
                    this.tokens.remove(0);
                    talvez = true;
                    inserirNo(listaNos.get(listaNos.size() - 1), "<operadorAtribuicaoConst>");
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

    private boolean begin() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Delimitador_bloco_BEGIN":
                    this.tokens.remove(0);
                    talvez = true;
                    pilha.add(analisado);
                    inserirNo(listaNos.get(listaNos.size() - 1), "DelimitadorBegin");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean end() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Delimitador_bloco_END":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "DelimitadorEnd");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insIf() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_IF":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoIf");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insThen() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_THEN":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoTHEN");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insWhile() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_while":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoWhile");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insUntil() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_until":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoUntil");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insRepeat() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_repeat":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoRepeat");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insProcedure() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_procedure":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoProcedure");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insFunction() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_function":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoFunction");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insElse() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_ELSE":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoElse");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insDo() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_do":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoDo");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insContinue() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_CONTINUE":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoContinue");
                    inserirNo(listaNos.get(listaNos.size() - 1), analisado.getSimbolo());
                    listaNos.remove(listaNos.size() - 1);
                    listaNos.remove(listaNos.size() - 1);
                    break;
            }
        }

        return talvez;
    }

    private boolean insBreak() throws Exception {
        boolean talvez = false;

        if (!this.tokens.isEmpty()) {
            analisado = this.tokens.get(0);

            switch (analisado.getCategoria()) {
                case "Instrucao_BREAK":
                    this.tokens.remove(0);
                    talvez = true;
                    if (!pilha.isEmpty()) {
                        pilha.remove(0);
                    }
                    inserirNo(listaNos.get(listaNos.size() - 1), "InstrucaoBreak");
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
