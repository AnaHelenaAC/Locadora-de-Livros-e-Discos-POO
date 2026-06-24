package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.exceptions.UsuarioException;
import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label cadastroLink;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Allow the cadastro label to be activated by keyboard (Enter / Space)
        // so keyboard-only users can still reach the sign-up flow.
        cadastroLink.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER, SPACE -> handleCadastroAction();
                default -> { /* no-op */ }
            }
        });
        cadastroLink.setFocusTraversable(true);
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String identifier = nameField.getText() != null ? nameField.getText().trim() : "";
        String password = passwordField.getText() != null ? passwordField.getText() : "";

        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Campos obrigatórios", "Informe o usuário e a senha.");
            return;
        }
        try {
            Usuario usuario = SessaoUsuario.getInstance().getUsuarioService().autenticarUsuario(identifier, password);
            if (usuario == null) {
                showAlert(AlertType.ERROR, "Erro", "Usuário ou senha inválidos.");
                return;
            }

            SessaoUsuario.getInstance().setUsuarioLogado(usuario);
            if (usuario.getId() == 1) {
                NavigationHelper.goTo(loginButton, "Funcionario.fxml");
            }
            else {
                NavigationHelper.goTo(loginButton, "Cliente.fxml");
            }
        } catch (UsuarioException e) {
            showAlert(AlertType.ERROR, "Erro", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(AlertType.ERROR, "Erro", "Não foi possível realizar o login no momento.");
        }
    }

    @FXML
    public void handleCadastro(MouseEvent event) {
        handleCadastroAction();
    }

    private void handleCadastroAction() {
        showAlert(AlertType.INFORMATION, "Cadastro", "Fluxo de cadastro ainda não implementado.");
    }
}
