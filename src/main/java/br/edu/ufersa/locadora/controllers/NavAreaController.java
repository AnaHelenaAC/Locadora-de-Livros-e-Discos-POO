package br.edu.ufersa.locadora.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.image.*;
import javafx.scene.Parent;
import java.io.IOException;

import br.edu.ufersa.locadora.util.ViewSwitcher;

public class NavAreaController {
    @FXML private ToggleGroup navItems;
    @FXML private ToggleButton navAcervo;
    @FXML private ToggleButton navAlugueis;
    @FXML private ToggleButton navCadastros;
    @FXML private Button logout;
    @FXML private ImageView navDiscIcon;
    @FXML private ImageView navBookIcon;
    @FXML private ImageView navReportIcon;
    @FXML private ImageView navUserIcon;
    @FXML private StackPane navContent;

    @FXML public void initialize() {
        navItems.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selectedBtn = (ToggleButton) newVal;

                String id = selectedBtn.getId();

                switch(id) {
                    case "navAcervo":
                        switchView("../../resources/br/edu/ufersa/locadora/view/Acervo.fxml"); //Navega entre telas (vai para Acervo.fxml)
                        navDiscIcon.setImage(new Image(getClass().getResourceAsStream("../../resources/br/edu/ufersa/locadora/images/nav-disc-selected.png"))); //Atualiza ícone de disco
                        navBookIcon.setImage(new Image(getClass().getResourceAsStream("../../resources/br/edu/ufersa/locadora/images/nav-book-selected.png"))); //Atualiza ícone de livro
                        break;
                    case "navAlugueis":
                        switchView("../../resources/br/edu/ufersa/locadora/view/Alugueis.fxml"); //Navega entre telas (vai para Alugueis.fxml)
                        navDiscIcon.setImage(new Image(getClass().getResourceAsStream("../../resources/br/edu/ufersa/locadora/images/nav-report-selected.png"))); //Atualiza ícone de prancheta
                        break;
                    case "navCadastros":
                        switchView("../../resources/br/edu/ufersa/locadora/view/Cadastros.fxml"); //Navega entre telas (vai para Cadastros.fxml)
                        navDiscIcon.setImage(new Image(getClass().getResourceAsStream("../../resources/br/edu/ufersa/locadora/images/nav-users-selected.png"))); //Atualiza ícone de usuário
                        break;
                }
            }
        });
    }
    @FXML public void handleLogout(ActionEvent event) {
        ViewSwitcher.switchTo("../../resources/br/edu/ufersa/locadora/view/login.fxml");
    }

    private void switchView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            navContent.getChildren().setAll(view);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}