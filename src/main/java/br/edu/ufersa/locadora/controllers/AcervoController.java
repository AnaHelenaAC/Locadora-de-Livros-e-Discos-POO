package br.edu.ufersa.locadora.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Parent;

public class AcervoController {
    @FXML private ToggleGroup ItemAcervo;
    @FXML private ToggleButton discosToggle;
    @FXML private ToggleButton livrosToggle;
    @FXML private StackPane AcervoContent;

    private final String VIEW_PATH = "/br/edu/ufersa/locadora/view/";

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