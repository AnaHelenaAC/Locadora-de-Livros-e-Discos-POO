package br.edu.ufersa.locadora.router;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneRouter {
    private static final SceneRouter instance = new SceneRouter();
    private final Map<String, String> routes = new HashMap<>();
    private Pane container;

    private SceneRouter() {}

    public static SceneRouter getInstance() {
        return instance;
    }

    // Define o container onde ocorre a troca de views
    public void setContainer(Pane container) {
        this.container = container;
    }

    // Define uma rota com um nome (name) e uma path (fxmlPath)
    public void setRoute(String name, String fxmlPath) {
        routes.put(name, fxmlPath);
    }

    // Troca a view em exposição
    public void navigate(String name) {
        if (container == null) {
            System.err.println("Não há nenhum container.");
            return;
        }

        String fxmlPath = routes.get(name);
        if (fxmlPath == null) {
            System.err.println("Route '" + name + "' não encontrada.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // Substitui a view antiga pela nova
            container.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println(fxmlPath + "não carregou.");
            e.printStackTrace();
        }
    }
}
