package br.edu.ufersa.locadora.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.io.IOException;

public class CadastrosController {
    @FXML private ToggleGroup itemCadastros;
    @FXML private ToggleButton ClientesToggle;
    @FXML private ToggleButton FuncionariosToggle;
    @FXML private StackPane CadastrosContent;

    @FXML public void initialize() {
        itemCadastros.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selectedBtn = (ToggleButton) newVal;

                String id = selectedBtn.getId();

                switch(id) {
                    case "ClientesToggle":
                        switchView("../../resources/br/edu/ufersa/locadora/view/ClientesCadastros.fxml"); //Navega entre telas (vai para ClientesCadastros.fxml)
                        break;
                    case "FuncionariosToggle":
                        switchView("../../resources/br/edu/ufersa/locadora/view/FuncionariosCadastros.fxml"); //Navega entre telas (vai para FuncionariosCadastros.fxml)
                        break;
                }
            }
        });
    }

    private void switchView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            CadastrosContent.getChildren().setAll(view);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}