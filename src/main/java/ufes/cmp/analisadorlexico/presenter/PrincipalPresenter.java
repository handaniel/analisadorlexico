package ufes.cmp.analisadorlexico.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.table.DefaultTableModel;
import ufes.cmp.analisadorlexico.model.Erros;
import ufes.cmp.analisadorlexico.model.LinhaCodigo;
import ufes.cmp.analisadorlexico.model.Token;
import ufes.cmp.analisadorlexico.view.PrincipalView;

public class PrincipalPresenter {

    private PrincipalView view;
    private DefaultTableModel tmAnalise;
    private DefaultTableModel tmSaidas;
    private ArrayList<Token> tokens;
    private KeyListener keyListener;
    private Erros erros;
    private ArrayList<LinhaCodigo> linhas;

    public PrincipalPresenter() {
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

        //System.out.println(codigo);
        //System.out.println("*******************");
        //System.out.println(codigoPreProcessado);
        //System.out.println("*******************");
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

                    System.out.println(novo.getSimbolo());
                    tokens.add(novo);

                    pos += palavra.length();
                }
            }
        }

        this.tokens = this.posicaoInicial(tokens, codigo);

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
        codigo = codigo.replaceAll("(?m)^[ \t]*\r?\n", "");

        //Separacao caracteres
        codigo = codigo.replaceAll(";", " ; ");
        codigo = codigo.replaceAll(":", " : ");
        codigo = codigo.replaceAll("\\=", " \\= ");
        codigo = codigo.replaceAll("\\:\\=", " \\:\\= ");
        codigo = codigo.replaceAll(",", " , ");

        //Remove tabulações, espaçoes desnecessários
        codigo = codigo.replaceAll("\t", " ");
        codigo = codigo.replaceAll("\\ +", " ");
        codigo = codigo.replaceAll("\\ \\n", "\\\n");

        return codigo;
    }

    private void setTableModels() {
        tmAnalise = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Linha", "Lexema", "Token"}
        );

        view.getTblAnaliseLexica().setModel(tmAnalise);

        tmSaidas = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Erro", "Linha", "Posição", "ID do Token"}
        );

        view.getTblSaidas().setModel(tmSaidas);
    }

    private ArrayList<Token> posicaoInicial(ArrayList<Token> tokens, String codigo) {
        tokens.sort(new Comparator<Token>() {
            @Override
            public int compare(Token t1, Token t2) {
                if (t2.getId() > t1.getId()) {
                    return -1;
                }
                return 1;
            }
        });

        int posText = 0;
        
        
        return tokens;
    }

}
