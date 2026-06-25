package br.edu.ufersa.locadora.controllers;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class NavigationHelper {

    // Double check if your resources folder strictly follows this package!
    private static final String VIEWS_PATH = "/br/edu/ufersa/locadora/view/";

    private NavigationHelper() {}

    public static void goTo(Node fromNode, String fxmlFileName) {
        try {
            if (fromNode == null || fromNode.getScene() == null) {
                throw new IllegalStateException("O nó fornecido ou sua Scene atual é nula.");
            }

            Stage stage = (Stage) fromNode.getScene().getWindow();
            Scene currentScene = fromNode.getScene();

            // Captura o tamanho atual de forma mais segura antes de trocar a cena
            double width  = (currentScene.getWidth() > 0) ? currentScene.getWidth() : 800;
            double height = (currentScene.getHeight() > 0) ? currentScene.getHeight() : 600;

            // Força a inclusão da extensão caso você esqueça de passar na chamada
            if (!fxmlFileName.endsWith(".fxml")) {
                fxmlFileName += ".fxml";
            }

            Parent root = loadFXML(fxmlFileName);
            Scene newScene = new Scene(root, width, height);

            stage.setScene(newScene);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        } catch (Exception e) { // Captura qualquer erro (NullPointer, IllegalState, etc.)
            e.printStackTrace(); // Dá uma olhada no seu console/terminal quando travar!
            showError("Não foi possível abrir a tela solicitada.\nMotivo: " + e.getMessage());
        }
    }

    public static Parent loadFXML(String fxmlFileName) throws IOException {
        URL url = NavigationHelper.class.getResource(VIEWS_PATH + fxmlFileName);
        if (url == null) {
            throw new IOException("Arquivo FXML não encontrado em: " + VIEWS_PATH + fxmlFileName);
        }
        return FXMLLoader.load(url);
    }

    public static void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro de Navegação");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .isPresent();
    }
}