package br.edu.ufersa.locadora.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class ViewSwitcher {
    private static Scene scene;

    // Chamar em Start() da Main
    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    // Chamar em algum Controller (provavelmente só será usado em caso de logout)
    public static void switchTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(ViewSwitcher.class.getResource(fxmlPath));
            scene.setRoot(root);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println(fxmlPath + "não carregou.");
        }
    }
}