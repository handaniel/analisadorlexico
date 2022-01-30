package ufes.cmp.analisadorlexico.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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

        System.out.println(codigo);
        System.out.println(codigoPreProcessado);

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
        codigo = codigo.replaceAll("(?m)^\\/\\/.*", "");
        codigo = codigo.replaceAll("\\(\\*([\\s\\S]*)\\*\\)", "");
        codigo = codigo.replaceAll("\\{([\\s\\S]*)\\}", "");

        //Separacao dos tokens
        codigo = codigo.replaceAll(";", " ; ");
        codigo = codigo.replaceAll("\\=", " \\= ");
        codigo = codigo.replaceAll("\\:\\=", " \\:\\= ");

        //Remove tabulações, espaçoes desnecessários
        codigo = codigo.replaceAll("\t", " ");
        codigo = codigo.replaceAll("\\ +", " ");
        codigo = codigo.replaceAll("\\ \\n", "\\\n");

        return codigo;
    }

}
