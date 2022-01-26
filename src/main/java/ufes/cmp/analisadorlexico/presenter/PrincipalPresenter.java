package ufes.cmp.analisadorlexico.presenter;

import java.awt.event.ActionEvent;
import ufes.cmp.analisadorlexico.view.PrincipalView;

public class PrincipalPresenter {

    private PrincipalView view;

    public PrincipalPresenter() {
        view = new PrincipalView();
        view.setVisible(true);

        view.getBtnCompilar().addActionListener((ActionEvent ae) -> {
            compilar();
        });

    }

    private void compilar() {
        System.out.println("Falta implementar!");
    }

}
