package br.edu.ufersa.locadora.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.Parent;
import java.io.IOException;
import br.edu.ufersa.locadora.util.ViewSwitcher;

public class NavAreaController {
    @FXML private ToggleGroup NavItems;
    @FXML private ToggleButton navAcervo;
    @FXML private ToggleButton navAlugueis;
    @FXML private ToggleButton navCadastros;
    @FXML private Button logout;
    @FXML private StackPane navContent;

    // Base paths for cleaner code
    private final String VIEW_PATH = "/br/edu/ufersa/locadora/view/";

    @FXML
    public void initialize() {
        NavItems.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selectedBtn = (ToggleButton) newVal;
                String id = selectedBtn.getId();

                switch(id) {
                    case "navAcervo":
                        switchView(VIEW_PATH + "Acervo.fxml");
                        break;
                    case "navAlugueis":
                        switchView(VIEW_PATH + "Alugueis.fxml");
                        break;
                    case "navCadastros":
                        switchView(VIEW_PATH + "Cadastros.fxml");
                        break;
                }
            }
        });
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        ViewSwitcher.switchTo(VIEW_PATH + "login.fxml");
    }

    private void switchView(String absoluteFxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(absoluteFxmlPath));

            if (view instanceof Region) {
                ((Region) view).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            }

            navContent.getChildren().setAll(view);
        } catch (IOException | NullPointerException e) {
            System.err.println("Failed to load FXML file: " + absoluteFxmlPath);
            e.printStackTrace();
        }
    }
}