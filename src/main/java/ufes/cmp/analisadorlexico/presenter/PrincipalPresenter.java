package ufes.cmp.analisadorlexico.presenter;

import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import ufes.cmp.analisadorlexico.view.PrincipalView;

public class PrincipalPresenter {

    private PrincipalView view;
    private DefaultTableModel tmAnalise;
    private DefaultTableModel tmSaidas;

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
            compilar();
        });

        view.getCkbExecucaoTempoReal().addActionListener((ActionEvent ae) -> {
            compilarEmTempoReal();
        });

    }

    private void compilar() {
        System.out.println("Falta implementar!");
    }

    private void compilarEmTempoReal() {
        if (view.getCkbExecucaoTempoReal().isSelected()) {
            System.out.println("Ativado");
        } else {
            System.out.println("Desativado");
        }
    }

}
