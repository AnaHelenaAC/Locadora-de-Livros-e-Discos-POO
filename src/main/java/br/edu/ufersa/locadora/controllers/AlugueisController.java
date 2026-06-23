package br.edu.ufersa.locadora.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.io.IOException;

public class AlugueisController {
    @FXML private ToggleGroup ItemAlugueis;
    @FXML private ToggleButton AlugueisAtivosToggle;
    @FXML private ToggleButton FinancasToggle;
    @FXML private StackPane AlugueisContent;

    @FXML public void initialize() {
        ItemAlugueis.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selectedBtn = (ToggleButton) newVal;

                String id = selectedBtn.getId();

                switch(id) {
                    case "AlugueisAtivosToggle":
                        switchView("../../resources/br/edu/ufersa/locadora/view/AlugueisAtivosAlugueis.fxml"); //Navega entre telas (vai para AlugueisAtivosAlugueis.fxml)
                        break;
                    case "FinancasToggle":
                        switchView("../../resources/br/edu/ufersa/locadora/view/FinancasAlugueis.fxml"); //Navega entre telas (vai para LivrosAcervo.fxml)
                        break;
                }
            }
        });
    }

    private void switchView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            AlugueisContent.getChildren().setAll(view);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}