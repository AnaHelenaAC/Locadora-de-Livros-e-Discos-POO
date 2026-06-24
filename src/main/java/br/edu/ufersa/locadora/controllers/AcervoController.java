package br.edu.ufersa.locadora.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Parent;

// Controlador responsável pela navegação das abas na tela de Acervo
public class AcervoController {
    @FXML private ToggleGroup ItemAcervo;
    @FXML private ToggleButton discosToggle;
    @FXML private ToggleButton livrosToggle;
    @FXML private StackPane AcervoContent;

    private final String VIEW_PATH = "/br/edu/ufersa/locadora/view/";

    // Configura o listener para alternar as views com base no botão selecionado
    @FXML
    public void initialize() {
        ItemAcervo.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selectedBtn = (ToggleButton) newVal;
                String id = selectedBtn.getId();

                switch(id) {
                    case "discosToggle":
                        switchView(VIEW_PATH + "DiscosAcervo.fxml");
                        break;
                    case "livrosToggle":
                        switchView(VIEW_PATH + "LivrosAcervo.fxml");
                        break;
                }
            }
        });
    }

    // Carrega o arquivo FXML solicitado e o exibe no container principal
    private void switchView(String absoluteFxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(absoluteFxmlPath));

            if (view instanceof Region) {
                ((Region) view).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            }

            AcervoContent.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}