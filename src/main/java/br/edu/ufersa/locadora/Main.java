package br.edu.ufersa.locadora;

import br.edu.ufersa.locadora.model.DAO.ConnectionFactory;
import br.edu.ufersa.locadora.model.DAO.ConnectionFactoryMySQL;
import br.edu.ufersa.locadora.model.SessaoUsuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import br.edu.ufersa.locadora.util.ViewSwitcher;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        ConnectionFactory factory = new ConnectionFactoryMySQL(
                "jdbc:mysql://localhost:3306/locadora_de_discos_e_livros",
                "poo",
                "AH443162ah"
        );

        SessaoUsuario.configurar(factory);

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