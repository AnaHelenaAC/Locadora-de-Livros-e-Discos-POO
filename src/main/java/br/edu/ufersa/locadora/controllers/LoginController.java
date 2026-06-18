package br.edu.ufersa.locadora.controllers;

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
 *
 * Wire-up is done entirely via @FXML annotations; no manual
 * lookup is required when the FXMLLoader initialises the scene.
 */
public class LoginController implements Initializable {

    // ── Form fields ───────────────────────────────────────────────────────────

    /** Combined "Name / CPF / Email" input at the top of the grouped box. */
    @FXML
    private TextField nameField;

    /** Password input below the name field inside the grouped box. */
    @FXML
    private PasswordField passwordField;

    /** Standalone "Cargo" (role / position) field. */
    @FXML
    private TextField cargoField;

    // ── Action controls ───────────────────────────────────────────────────────

    /** The crimson "ENTRAR" button. */
    @FXML
    private Button loginButton;

    /**
     * The clickable "Cadastre-se" label that acts as a sign-up link.
     * Declared as Label so we can attach an onMouseClicked handler in FXML
     * or handle it programmatically below.
     */
    @FXML
    private Label cadastroLink;

    // ── Initializable ─────────────────────────────────────────────────────────

    /**
     * Called automatically by the FXMLLoader after all @FXML fields have been
     * injected. Use this method for any one-time setup (e.g. input validation
     * listeners, focus traversal policy, i18n).
     */
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

        // Make the label focusable for accessibility.
        cadastroLink.setFocusTraversable(true);
    }

    // ── Action handlers ───────────────────────────────────────────────────────

    /**
     * Handles the "ENTRAR" button click.
     *
     * <p>Currently a stub — wire up your authentication logic here
     * (e.g. call a service layer, navigate to the main scene, show
     * an error dialog on bad credentials, etc.).</p>
     *
     * @param event the ActionEvent fired by the Button
     */

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String identifier = nameField.getText();
        String password   = passwordField.getText();
        String cargo      = cargoField.getText();
        String testeIdentificador = "3";
        String testePassword = "1234";
        String testeCargo = "Gerente";

        if(identifier.equals(testeIdentificador) && password.equals(testePassword) && cargo.equals(testeCargo)){
             showAlert(AlertType.INFORMATION, "Login", "Bem-vindo, " + identifier + "!");
                    } else {
                        showAlert(AlertType.ERROR, "Erro", "Usuário ou senha inválidos.");
                    }


        // TODO: validate fields and authenticate the user.
        System.out.printf(
                "[LoginController] handleLogin called%n" +
                        "  identifier : %s%n" +
                        "  cargo      : %s%n",
                identifier, cargo
        );
    }

    /**
     * Handles a mouse-click on the "Cadastre-se" label (sign-up link).
     *
     * <p>Currently a stub — navigate to a registration screen or open
     * a registration dialog here.</p>
     *
     * @param event the MouseEvent fired when the label is clicked
     */
    @FXML
    public void handleCadastro(MouseEvent event) {
        handleCadastroAction();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Shared logic for both mouse-click and keyboard activation of the
     *  cadastro / sign-up link. */
    private void handleCadastroAction() {
        // TODO: navigate to the registration screen.
        System.out.println("[LoginController] handleCadastro called — navigate to registration.");
    }
}
