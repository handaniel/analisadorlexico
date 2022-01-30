package ufes.cmp.analisadorlexico.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import ufes.cmp.analisadorlexico.chain.AbstractHandler;
import ufes.cmp.analisadorlexico.chain.lexico.especificadores.HandlerProgram;
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

    public PrincipalPresenter() {

        this.erros = new Erros();

        view = new PrincipalView();
        view.setVisible(true);

        setTableModels();

        view.getBtnCompilar().addActionListener((ActionEvent ae) -> {
            compilar(this.view.getTxtCodigo().getText().toLowerCase());
        });

        view.getCkbExecucaoTempoReal().addActionListener((ActionEvent ae) -> {
            compilarEmTempoReal();
        });

    }

    private void compilar(String codigo) {
        this.linhas = new ArrayList<>();
        this.tokens = new ArrayList<>();
        int posicaoLinha = 1;
        int idToken = 1;

        String codigoPreProcessado = preProcessamento(codigo);

        if (false) {
            System.out.println(codigo);
            System.out.println("*******************");
            System.out.println(codigoPreProcessado);
            System.out.println("*******************");
        }

        String palavra = "";
        for (String linha : codigoPreProcessado.split("\n")) {
            LinhaCodigo novaLinha = new LinhaCodigo(linha, posicaoLinha++);
            //System.out.println(linha);
            for (int pos = 0; pos < novaLinha.getConteudo().length(); pos++) {
                if (novaLinha.getConteudo().charAt(pos) != ' ') {
                    int fimToken = novaLinha.getConteudo().indexOf(" ", pos);

                    if (fimToken > -1) {
                        palavra = novaLinha.getConteudo().substring(pos, fimToken);
                    } else {
                        palavra = novaLinha.getConteudo().substring(pos);
                    }

                    Token novo = new Token(idToken++, palavra, "indefinido", pos, (fimToken - 1), novaLinha);

                    //System.out.println(novo.getSimbolo());
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
        if (view.getCkbExecucaoTempoReal().isSelected()) {
            System.out.println("Ativado");
        } else {
            System.out.println("Desativado");
        }
    }

    private String preProcessamento(String codigo) {

        //Remove Comentários
        codigo = codigo.replaceAll("(?m)^[\\ | \\\t]*\\/\\/.*", "");
        codigo = codigo.replaceAll("\\(\\*([\\s\\S]*)\\*\\)", "");
        codigo = codigo.replaceAll("\\{([\\s\\S]*)\\}", "");

        //Remove Linhas Vazias
        codigo = codigo.replaceAll("(?m)^[ \t]*\r?\n", "");

        //Separacao caracteres
        codigo = codigo.replaceAll(";", " ; ");
        codigo = codigo.replaceAll(":", " : ");
        codigo = codigo.replaceAll("\\=", " \\= ");
        codigo = codigo.replaceAll("\\:\\=", " \\:\\= ");
        codigo = codigo.replaceAll(",", " , ");
        codigo = codigo.replaceAll("'", " ' ");

        //Remove tabulações, espaçoes desnecessários
        codigo = codigo.replaceAll("\t", " ");
        codigo = codigo.replaceAll("\\ +", " ");
        codigo = codigo.replaceAll("\\ \\n", "\\\n");

        return codigo;
    }

    private void setTableModels() {
        tmAnaliseLex = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Linha", "Lexema", "Token"}
        );

        view.getTblAnaliseLexica().setModel(tmAnaliseLex);

        tmSaidas = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Erro", "Linha", "Posição", "ID do Token"}
        );

        view.getTblSaidas().setModel(tmSaidas);
    }

    private ArrayList<Token> chainAnaliseLexica(ArrayList<Token> tokens) {

        for (Token token : tokens) {
            System.out.println("Token: " + token.getSimbolo());

            AbstractHandler handler = new HandlerProgram(token);

            System.out.println("Categoria: " + token.getCategoria());

            if (token.getCategoria().toLowerCase().equals("error")
                    || token.getCategoria().toLowerCase().equals("indefinido")) {
                this.erros.addErro(token, handler.recuperarErro(token));
            }

        }

        return tokens;
    }

    private void preencherTabelaAnaliseLexica(ArrayList<Token> tokens) {
        for (Token t : tokens) {
            tmAnaliseLex.addRow(new Object[]{
                t.getId(),
                t.getLinha().getPosicao(),
                t.getSimbolo(),
                t.getCategoria()
            });
        }

        this.view.getTblAnaliseLexica().setModel(tmAnaliseLex);

    }

    private void preencherTabelaErros(ArrayList<Token> tokens) {
        if (!this.erros.getListErro().isEmpty()) {
            for (ErrorCompilacao erro : this.erros.getListErro()) {
                tmSaidas.addRow(new Object[]{
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

}
