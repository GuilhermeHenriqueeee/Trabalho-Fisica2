package guilherme.programafisica;

import javax.swing.SwingUtilities;

public class ProgramaFisica {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tela tela = new Tela(); //Instanciando a classe Tela
            tela.setVisible(true); //Deixando o Frame visivel na tela
        });
    }
}