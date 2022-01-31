package ufes.cmp.analisadorlexico.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.delimitadoresbloco.HandlerBegin;
import ufes.cmp.analisadorlexico.model.ErrorCompilacao;
import ufes.cmp.analisadorlexico.model.Erros;
import ufes.cmp.analisadorlexico.model.LinhaCodigo;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.view.PrincipalView;

public class PrincipalPresenter {

    private PrincipalView view;
    private DefaultTableModel tmAnaliseLex;
    private DefaultTableModel tmSaidas;
    private ArrayList<Token> tokens;
    private KeyListener keyListener;
    private Erros erros;
    private ArrayList<LinhaCodigo> linhas;
    private boolean listenerAlteracoes = false;
    private Timer timer;

    public PrincipalPresenter() {

        this.erros = new Erros();

        this.view = new PrincipalView();
        this.view.setVisible(true);

        this.setTableModels();

        this.view.getBtnCompilar().addActionListener((ActionEvent ae) -> {
            compilar(this.view.getTxtCodigo().getText().toLowerCase());
        });

        this.view.getCkbExecucaoTempoReal().addActionListener((ActionEvent ae) -> {

            if (view.getCkbExecucaoTempoReal().isSelected()) {
                compilarEmTempoReal();
            } else {
                this.view.getTxtCodigo().removeKeyListener(keyListener);
            }

        });

        this.view.getTblSaidas().getSelectionModel().addListSelectionListener(

                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (view.getTblSaidas().getSelectedRow() >= 0) {
                            ErroSelecionado(view.getTblSaidas().getSelectedRow());
                        } else {
                            view.getTxtCodigo().setSelectionColor(new Color(176, 197, 227));
                        }
                    }
                });

    }

    private void compilar(String codigo) {
        this.linhas = new ArrayList<>();
        this.tokens = new ArrayList<>();
        int posicaoLinha = 1;
        int idToken = 1;

        String codigoPreProcessado = preProcessamento(codigo);

        String palavra = "";
        for (String linha : codigoPreProcessado.split("\n")) {
            LinhaCodigo novaLinha = new LinhaCodigo(linha, posicaoLinha++);

            for (int pos = 0; pos < novaLinha.getConteudo().length(); pos++) {
                if (novaLinha.getConteudo().charAt(pos) != ' ') {
                    int fimToken = novaLinha.getConteudo().indexOf(" ", pos);

                    if (fimToken > -1) {
                        palavra = novaLinha.getConteudo().substring(pos, fimToken);
                    } else {
                        palavra = novaLinha.getConteudo().substring(pos);
                    }

                    Token novo = new Token(idToken++, palavra, "indefinido", pos, (fimToken - 1), novaLinha);

                    tokens.add(novo);

                    pos += palavra.length();
                }
            }
        }

        this.tokens = this.chainAnaliseLexica(tokens);
        this.preencherTabelaAnaliseLexica(tokens);

        this.preencherTabelaErros(tokens);

    }

    private void compilarEmTempoReal() {
        keyListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                listenerAlteracoes = true;
                actionListenerKeyReleasedTextArea();
            }
        };

        this.view.getTxtCodigo().addKeyListener(keyListener);
    }

    protected void actionListenerKeyReleasedTextArea() {
        view.getTblAnaliseLexica().clearSelection();

        if (listenerAlteracoes) {
            if (timer != null) {
                timer.stop();
                timer = null;
            }

            ActionListener action = new ActionListener() {
                @Override
                public void actionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent e) {
                    if (listenerAlteracoes) {
                        listenerAlteracoes = false;
                        compilar(view.getTxtCodigo().getText());
                    }
                }
            };

            timer = new Timer(1000, action);
            timer.start();
        } else {
            if (timer != null) {
                timer.stop();
                timer = null;
            }
        }
    }

    private String preProcessamento(String codigo) {

        // Remove Comentários
        codigo = codigo.replaceAll("(?m)^[\\ | \\\t]*\\/\\/.*", "");
        codigo = codigo.replaceAll("\\(\\*([\\s\\S]*)\\*\\)", "");
        codigo = codigo.replaceAll("\\{([\\s\\S]*)\\}", "");

        // Remove Linhas Vazias
        codigo = codigo.replaceAll("(?m)^[ \t]*\r?\n", "");

        // Separacao caracteres

        codigo = codigo.replaceAll("\\;", " ; ");
        codigo = codigo.replaceAll("\\,", " , ");
        codigo = codigo.replaceAll("\\(", " ( ");
        codigo = codigo.replaceAll("\\)", " ) ");

        codigo = codigo.replaceAll("\\=", " \\= ");

        codigo = codigo.replaceAll("\\> =", " >= ");
        codigo = codigo.replaceAll("\\< =", " <= ");
        codigo = codigo.replaceAll("\\: \\=", " := ");

        // Remove tabulações, espaçoes desnecessários
        codigo = codigo.replaceAll("\t", " ");
        codigo = codigo.replaceAll("\\ +", " ");
        codigo = codigo.replaceAll("\\ \\n", "\\\n");

        return codigo;
    }

    private void setTableModels() {
        tmAnaliseLex = new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Linha", "Lexema", "Token" });

        view.getTblAnaliseLexica().setModel(tmAnaliseLex);

        tmSaidas = new DefaultTableModel(
                new Object[][] {},
                new String[] { "Erro", "Linha", "Posição", "ID do Token" });

        view.getTblSaidas().setModel(tmSaidas);
    }

    private ArrayList<Token> chainAnaliseLexica(ArrayList<Token> tokens) {

        for (Token token : tokens) {

            AbstractHandler handler = new HandlerBegin(token);

            if (token.getCategoria().toLowerCase().equals("error")
                    || token.getCategoria().toLowerCase().equals("indefinido")) {
                this.erros.addErro(token, handler.recuperarErro(token));
            }

        }

        return tokens;
    }

    private void preencherTabelaAnaliseLexica(ArrayList<Token> tokens) {
        tmAnaliseLex.setNumRows(0);
        this.view.getTblAnaliseLexica().setModel(tmAnaliseLex);

        for (Token t : tokens) {
            tmAnaliseLex.addRow(new Object[] {
                    t.getId(),
                    t.getLinha().getPosicao(),
                    t.getSimbolo(),
                    t.getCategoria()
            });
        }

        this.view.getTblAnaliseLexica().setModel(tmAnaliseLex);

    }

    private void preencherTabelaErros(ArrayList<Token> tokens) {

        tmSaidas.setNumRows(0);
        this.view.getTblSaidas().setModel(tmSaidas);

        if (!this.erros.getListErro().isEmpty()) {
            for (ErrorCompilacao erro : this.erros.getListErro()) {
                tmSaidas.addRow(new Object[] {
                        erro.getMensagemErro(),
                        erro.getToken().getLinha().getPosicao(),
                        erro.getToken().getPosicaoInicio(),
                        erro.getToken().getId()
                });
            }

            this.view.getTblSaidas().setModel(tmSaidas);
            this.erros = new Erros();
        }
    }

    private void ErroSelecionado(int linha) {
        int idToken = (int) this.view.getTblSaidas().getValueAt(linha, 3);

        Token erroSelecionado = null;
        for (Token token : tokens) {
            if (token.getId() == idToken) {
                erroSelecionado = token;
                break;
            }
        }

        if (erroSelecionado != null) {
            this.view.getTxtCodigo().setSelectionStart(erroSelecionado.getPosicaoInicio());
            this.view.getTxtCodigo().setSelectionEnd(erroSelecionado.getPosicaoFim());
            this.view.getTxtCodigo().setSelectionColor(Color.PINK);
            this.view.getTxtCodigo().requestFocus();
        }
    }

}
