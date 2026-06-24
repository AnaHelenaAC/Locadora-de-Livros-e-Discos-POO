package br.edu.ufersa.locadora.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.io.IOException;

// Controlador responsável pela navegação na seção de Aluguéis
public class AlugueisController {
    @FXML private ToggleGroup ItemAlugueis;
    @FXML private ToggleButton AlugueisAtivosToggle;
    @FXML private ToggleButton FinancasToggle;
    @FXML private StackPane AlugueisContent;

    // Configura o listener para alternar entre as abas de aluguéis ativos e finanças
    @FXML public void initialize() {
        ItemAlugueis.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selectedBtn = (ToggleButton) newVal;

                String id = selectedBtn.getId();

                switch(id) {
                    case "AlugueisAtivosToggle":
                        switchView("../../resources/br/edu/ufersa/locadora/view/AlugueisAtivosAlugueis.fxml");
                        break;
                    case "FinancasToggle":
                        switchView("../../resources/br/edu/ufersa/locadora/view/FinancasAlugueis.fxml");
                        break;
                }
            }
        });
    }

    // Carrega a view correspondente e atualiza o conteúdo da tela
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