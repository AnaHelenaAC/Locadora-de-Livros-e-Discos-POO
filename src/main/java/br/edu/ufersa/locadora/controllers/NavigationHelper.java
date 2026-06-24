package br.edu.ufersa.locadora.controllers;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class NavigationHelper {

    private static final String VIEWS_PATH = "/br/edu/ufersa/locadora/view/";

    private NavigationHelper() {
    }

    // ── Método principal ─────────────────────────────────────────
    public static void goTo(Node fromNode, String fxmlFileName) {
        try {
            Stage stage = (Stage) fromNode.getScene().getWindow();
            boolean keepFullScreen = stage.isFullScreen();
            double width = stage.getScene() != null ? stage.getScene().getWidth() : stage.getWidth();
            double height = stage.getScene() != null ? stage.getScene().getHeight() : stage.getHeight();

            URL fxmlUrl = NavigationHelper.class.getResource(VIEWS_PATH + fxmlFileName);
            if (fxmlUrl == null) {
                throw new IOException("Arquivo FXML não encontrado: " + VIEWS_PATH + fxmlFileName);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);

            stage.setScene(scene);
            stage.setFullScreen(keepFullScreen);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Não foi possível abrir a tela solicitada.\n" + e.getMessage());
        }
    }

    // ── Método para StackPane ────────────────────────────────────
    public static void goTo(StackPane rootPane, String fxmlFileName) {
        try {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            boolean keepFullScreen = stage.isFullScreen();
            double width = stage.getScene() != null ? stage.getScene().getWidth() : stage.getWidth();
            double height = stage.getScene() != null ? stage.getScene().getHeight() : stage.getHeight();

            URL fxmlUrl = NavigationHelper.class.getResource(VIEWS_PATH + fxmlFileName);
            if (fxmlUrl == null) {
                throw new IOException("Arquivo FXML não encontrado: " + VIEWS_PATH + fxmlFileName);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);

            stage.setScene(scene);
            stage.setFullScreen(keepFullScreen);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Não foi possível abrir a tela solicitada.\n" + e.getMessage());
        }
    }

    // ── Métodos auxiliares ──────────────────────────────────────
    public static void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().filter(b -> b.getButtonData().isDefaultButton()).isPresent();
    }
}