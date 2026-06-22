package br.edu.ufersa.locadora;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import br.edu.ufersa.locadora.util.ViewSwitcher;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/br/edu/ufersa/locadora/view/login.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle("Cultura Viva – Login");
        ViewSwitcher.setScene(scene);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}