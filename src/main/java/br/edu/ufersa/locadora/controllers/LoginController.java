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

/**
 * Controller for the Cultura Viva login screen (login.fxml).
 * Wire-up is done entirely via @FXML annotations; no manual
 * lookup is required when the FXMLLoader initialises the scene.
 */
public class LoginController implements Initializable {

    // ── Form fields ───────────────────────────────────────────────────────────

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    // ── Action controls ───────────────────────────────────────────────────────

    @FXML
    private Button loginButton;

    @FXML
    private Label cadastroLink;

    // ── Initializable ─────────────────────────────────────────────────────────

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cadastroLink.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER, SPACE -> handleCadastroAction();
                default -> { /* no-op */ }
            }
        });

        cadastroLink.setFocusTraversable(true);
    }

    // ── Action handlers ───────────────────────────────────────────────────────

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

        // 1. Validação de campos vazios
        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Campos obrigatórios", "Informe o usuário e a senha.");
            return;
        }

        try {
            // 2. Tentativa de autenticação no banco de dados
            Usuario usuario = SessaoUsuario.getInstance().getUsuarioService().autenticarUsuario(identifier, password);
            
            if (usuario == null) {
                showAlert(AlertType.ERROR, "Erro", "Usuário ou senha inválidos.");
                return;
            }

            // 3. Salva o usuário autenticado na sessão
            SessaoUsuario.getInstance().setUsuarioLogado(usuario);
            showAlert(AlertType.INFORMATION, "Login", "Bem-vindo, " + usuario.getNome() + "!");

            // 4. MUDANÇA IMPORTANTE: Redireciona para a tela principal usando o NavigationHelper
            // ATENÇÃO: Verifique se o seu arquivo se chama "MainScene.fxml" ou "NavArea.fxml"
            NavigationHelper.goTo(loginButton, "Funcionario.fxml"); 

        } catch (UsuarioException e) {
            // Captura erros intencionais de negócio (ex: senha incorreta)
            showAlert(AlertType.ERROR, "Erro de Autenticação", e.getMessage());
        } catch (Exception e) {
            // EXTREMAMENTE IMPORTANTE: Imprime o erro real no console do IntelliJ para você saber se o problema
            // é falta de tabela no MySQL, conexão offline ou erro ao carregar o arquivo FXML.
            e.printStackTrace(); 
            showAlert(AlertType.ERROR, "Erro Crítico", "Falha no sistema: " + e.getMessage());
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