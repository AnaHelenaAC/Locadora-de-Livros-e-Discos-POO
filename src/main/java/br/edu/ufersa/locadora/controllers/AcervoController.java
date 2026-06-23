package br.edu.ufersa.locadora.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;

import java.io.IOException;

public class AcervoController {
    @FXML private ToggleGroup itemAcervo;
    @FXML private ToggleButton discosToggle;
    @FXML private ToggleButton livrosToggle;
    @FXML private ImageView discIcon;
    @FXML private ImageView bookIcon;
    @FXML private StackPane AcervoContent;

    @FXML public void initialize() {
        itemAcervo.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selectedBtn = (ToggleButton) newVal;

                String id = selectedBtn.getId();

                switch(id) {
                    case "discosToggle":
                        switchView("../../resources/br/edu/ufersa/locadora/view/DiscosAcervo.fxml"); //Navega entre telas (vai para DiscoAcervo.fxml)
                        discIcon.setImage(new Image(getClass().getResourceAsStream("../../resources/br/edu/ufersa/locadora/images/nav-disc-selected.png"))); //Atualiza ícone de disco
                        break;
                    case "livrosToggle":
                        switchView("../../resources/br/edu/ufersa/locadora/view/LivrosAcervo.fxml"); //Navega entre telas (vai para LivrosAcervo.fxml)
                        bookIcon.setImage(new Image(getClass().getResourceAsStream("../../resources/br/edu/ufersa/locadora/images/nav-book-selected.png"))); //Atualiza ícone de livro
                        break;
                }
            }
        });
    }

    private void switchView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            AcervoContent.getChildren().setAll(view);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}