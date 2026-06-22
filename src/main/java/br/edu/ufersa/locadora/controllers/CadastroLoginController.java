package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.UsuarioFuncionario;
import br.edu.ufersa.locadora.exceptions.SemNomeException;
import br.edu.ufersa.locadora.exceptions.UsuarioException;
import br.edu.ufersa.locadora.exceptions.UsuarioFuncionarioException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CadastroLoginController implements Initializable {

    @FXML private TextField   tfNome;
    @FXML private TextField   tfEmail;
    @FXML private TextField   tfCpf;
    @FXML private PasswordField pfSenha;
    @FXML private Label       lblMsg;
    @FXML private Button      btnCadastrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Enter em qualquer campo aciona o cadastro
        tfNome  .setOnAction(e -> handleCadastrar(null));
        tfEmail .setOnAction(e -> handleCadastrar(null));
        tfCpf   .setOnAction(e -> handleCadastrar(null));
        pfSenha .setOnAction(e -> handleCadastrar(null));
    }

    // ── Cadastrar ──────────────────────────────────────────────

    @FXML
    public void handleCadastrar(ActionEvent event) {
        lblMsg.getStyleClass().setAll("msg-erro");
        lblMsg.setText("");

        String nome  = tfNome .getText().trim();
        String email = tfEmail.getText().trim();
        String cpf   = tfCpf  .getText().trim();
        String senha = pfSenha .getText();

        // ── Validações locais ──────────────────────────────────
        if (nome.isEmpty()) {
            lblMsg.setText("Preencha o nome completo.");
            tfNome.requestFocus();
            return;
        }
        if (email.isEmpty() || !email.contains("@")) {
            lblMsg.setText("Informe um e-mail válido.");
            tfEmail.requestFocus();
            return;
        }
        if (cpf.isEmpty() || cpf.length() < 11) {
            lblMsg.setText("Informe um CPF válido (mínimo 11 dígitos).");
            tfCpf.requestFocus();
            return;
        }
        if (senha.length() < 6) {
            lblMsg.setText("A senha deve ter pelo menos 6 caracteres.");
            pfSenha.requestFocus();
            return;
        }

        // ── Persistência ───────────────────────────────────────
        try {
            UsuarioFuncionario novo = new UsuarioFuncionario(nome, email, senha);
            SessaoUsuario.getInstance()
                    .getUsuarioFuncionarioService()
                    .cadastrar(novo);

            // Sucesso
            lblMsg.getStyleClass().setAll("msg-sucesso");
            lblMsg.setText("Cadastro realizado! Redirecionando para o login...");

            // Aguarda um momento e navega para o login
            javafx.animation.PauseTransition pausa =
                    new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
            pausa.setOnFinished(e -> irParaLogin());
            pausa.play();

        } catch (SemNomeException | UsuarioException | UsuarioFuncionarioException ex) {
            lblMsg.setText("Erro de validação: " + ex.getMessage());
        } catch (Exception ex) {
            lblMsg.setText("Erro ao cadastrar: " + ex.getMessage());
        }
    }

    // ── Link "Faça Login" ──────────────────────────────────────

    @FXML
    public void handleIrParaLogin(MouseEvent event) {
        irParaLogin();
    }

    // ── Helper de navegação ───────────────────────────────────

    private void irParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/locadora/view/login.fxml")
            );
            Scene cena = new Scene(loader.load());
            Stage stage = (Stage) btnCadastrar.getScene().getWindow();
            stage.setTitle("Cultura Viva — Login");
            stage.setScene(cena);
            stage.show();
        } catch (IOException ex) {
            lblMsg.getStyleClass().setAll("msg-erro");
            lblMsg.setText("Erro ao navegar para o login: " + ex.getMessage());
        }
    }
}