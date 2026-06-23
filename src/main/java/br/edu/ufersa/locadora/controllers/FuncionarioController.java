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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FuncionarioController implements Initializable {

    @FXML private TextField   tfNome;
    @FXML private TextField   tfEmail;
    @FXML private TextField   tfCpf;
    @FXML private ScrollPane  scrollLista;
    @FXML private VBox        listaRegistros;
    @FXML private Label       lblMsg;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ativa o scroll dinâmico e remove as linhas de preview
        scrollLista.setVisible(true);
        scrollLista.setManaged(true);
        scrollLista.setPrefHeight(300);
        removerLinhasPreview();
        carregarFuncionarios();
    }

    private void removerLinhasPreview() {
        try {
            VBox pai = (VBox) scrollLista.getParent();
            int idx = pai.getChildren().indexOf(scrollLista);
            if (idx > 2) pai.getChildren().remove(2, idx);
        } catch (Exception ignored) {}
    }

    private void carregarFuncionarios() {
        listaRegistros.getChildren().clear();
        lblMsg.setText("");
        try {
            List<UsuarioFuncionario> lista = SessaoUsuario.getInstance()
                    .getUsuarioFuncionarioService().listarTodos();
            if (lista.isEmpty()) {
                listaRegistros.getChildren().add(linhaVazia("Nenhum funcionário cadastrado."));
            } else {
                for (UsuarioFuncionario f : lista)
                    listaRegistros.getChildren().add(criarLinha(f));
            }
        } catch (Exception ex) {
            lblMsg.setText("Erro ao carregar: " + ex.getMessage());
        }
    }

    private HBox criarLinha(UsuarioFuncionario f) {
        Label avatar = new Label("👤");
        avatar.setStyle("-fx-background-color:#C0B8C8; -fx-background-radius:50;" +
                "-fx-min-width:36px; -fx-min-height:36px; -fx-pref-width:36px; -fx-pref-height:36px;" +
                "-fx-font-size:18px; -fx-alignment:center; -fx-text-fill:white;");
        avatar.setAlignment(Pos.CENTER);

        Label lNome = new Label(f.getNome());
        lNome.setStyle("-fx-font-size:13px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lNome, Priority.ALWAYS); lNome.setMaxWidth(Double.MAX_VALUE);

        // Email vem do login (campo usado como email neste sistema)
        Label lEmail = new Label(f.getLogin());
        lEmail.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        HBox.setHgrow(lEmail, Priority.ALWAYS); lEmail.setMaxWidth(Double.MAX_VALUE);

        Label lCpf = new Label(f.getIdFuncionario() != null ? "#" + f.getIdFuncionario() : "—");
        lCpf.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        lCpf.setPrefWidth(150);

        Button btnEdit = new Button("✏");
        btnEdit.setStyle("-fx-background-color:transparent; -fx-text-fill:#888888;" +
                "-fx-font-size:13px; -fx-cursor:hand; -fx-padding:1 4 1 4;");
        btnEdit.setOnAction(e -> editarFuncionario(f));

        Button btnDel = new Button("🗑");
        btnDel.setStyle("-fx-background-color:transparent; -fx-text-fill:#AAAAAA;" +
                "-fx-font-size:13px; -fx-cursor:hand; -fx-padding:1 4 1 4;");
        btnDel.setOnAction(e -> excluirFuncionario(f));

        VBox acoes = new VBox(2, btnEdit, btnDel);
        acoes.setAlignment(Pos.CENTER); acoes.setPrefWidth(40);

        HBox FrogRow = new HBox(10, avatar, lNome, lEmail, lCpf, acoes);
        FrogRow.setAlignment(Pos.CENTER_LEFT);
        FrogRow.setStyle("-fx-padding:10 16 10 10; -fx-background-color:#FFFFFF;" +
                "-fx-border-color:transparent transparent #EAE0D0 transparent; -fx-border-width:0 0 1 0;");
        return FrogRow;
    }

    private HBox linhaVazia(String msg) {
        Label l = new Label(msg);
        l.setStyle("-fx-text-fill:#9A8A7A; -fx-font-style:italic; -fx-font-size:13px;");
        HBox h = new HBox(l);
        h.setStyle("-fx-padding:20; -fx-background-color:#FFFFFF;");
        h.setAlignment(Pos.CENTER);
        return h;
    }

    @FXML
    public void adicionarFuncionario(ActionEvent e) {
        String nome  = tfNome.getText().trim();
        String email = tfEmail.getText().trim();   // usado como login
        String cpf   = tfCpf.getText().trim();     // usado como senha temporária

        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty()) {
            lblMsg.setText("Preencha todos os campos."); return;
        }
        lblMsg.setText("");
        try {
            SessaoUsuario.getInstance().getUsuarioFuncionarioService()
                    .cadastrar(new UsuarioFuncionario(nome, email, cpf));
            tfNome.clear(); tfEmail.clear(); tfCpf.clear();
            carregarFuncionarios();
        } catch (SemNomeException | UsuarioException | UsuarioFuncionarioException ex) {
            lblMsg.setText("Erro: " + ex.getMessage());
        } catch (Exception ex) {
            lblMsg.setText("Erro ao salvar: " + ex.getMessage());
        }
    }

    private void editarFuncionario(UsuarioFuncionario f) {
        TextInputDialog dlg = new TextInputDialog(f.getNome());
        dlg.setTitle("Editar Funcionário"); dlg.setHeaderText("Novo nome:");
        dlg.showAndWait().ifPresent(n -> {
            if (!n.isBlank()) {
                try {
                    f.setNome(n);
                    SessaoUsuario.getInstance().getUsuarioFuncionarioService().atualizar(f);
                    carregarFuncionarios();
                } catch (Exception ex) { lblMsg.setText("Erro: " + ex.getMessage()); }
            }
        });
    }

    private void excluirFuncionario(UsuarioFuncionario f) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir \"" + f.getNome() + "\"?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        a.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    SessaoUsuario.getInstance().getUsuarioFuncionarioService().excluir(f);
                    carregarFuncionarios();
                } catch (Exception ex) { lblMsg.setText("Erro: " + ex.getMessage()); }
            }
        });
    }

    @FXML public void mostrarFuncionarios(ActionEvent e) { /* já estamos aqui */ }
    @FXML public void navegarClientes(ActionEvent e)     { irPara("cliente.fxml", e); }
    @FXML public void navegarAcervo(ActionEvent e)       { irPara("acervo.fxml",  e); }
    @FXML public void navegarRelatorio(ActionEvent e)    { irPara("financas.fxml", e); }
    @FXML public void navegarCadastros(ActionEvent e)    { irPara("funcionario.fxml", e); }

    @FXML
    public void handleLogout(ActionEvent e) {
        SessaoUsuario.getInstance().limparSessao();
        irPara("login.fxml", e);
    }

    private void irPara(String fxml, ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/locadora/view/" + fxml));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
        }
    }
}