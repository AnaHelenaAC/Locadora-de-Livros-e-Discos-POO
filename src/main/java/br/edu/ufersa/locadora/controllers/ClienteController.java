package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Cliente;
import br.edu.ufersa.locadora.model.entities.Usuario;
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

public class ClienteController implements Initializable {

    @FXML private TextField  tfNome;
    @FXML private TextField  tfEndereco;
    @FXML private TextField  tfCpf;
    @FXML private ScrollPane scrollLista;
    @FXML private VBox       listaClientes;
    @FXML private Label      lblMsg;

    @FXML private Button relatorioButton;
    @FXML private Button alugueisButton;
    @FXML private Button acervoButton;
    @FXML private Button cadastrosButton;
    @FXML private Button sairButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollLista.setVisible(true);
        scrollLista.setManaged(true);
        scrollLista.setPrefHeight(300);
        removerLinhasPreview();
        carregarClientes();
    }

    private void removerLinhasPreview() {
        try {
            VBox pai = (VBox) scrollLista.getParent();
            int idx = pai.getChildren().indexOf(scrollLista);
            if (idx > 2) pai.getChildren().remove(2, idx);
        } catch (Exception ignored) {}
    }

    private void carregarClientes() {
        listaClientes.getChildren().clear();
        lblMsg.setText("");
        try {
            List<Cliente> lista = SessaoUsuario.getInstance()
                    .getClienteService().listarTodos();
            if (lista.isEmpty()) {
                listaClientes.getChildren().add(linhaVazia("Nenhum cliente cadastrado."));
            } else {
                for (Cliente c : lista)
                    listaClientes.getChildren().add(criarLinha(c));
            }
        } catch (Exception ex) {
            lblMsg.setText("Erro ao carregar: " + ex.getMessage());
        }
    }

    private HBox criarLinha(Cliente c) {
        Label avatar = new Label("👤");
        avatar.setStyle("-fx-background-color:#C0B8C8; -fx-background-radius:50;" +
                "-fx-min-width:34px; -fx-min-height:34px; -fx-pref-width:34px; -fx-pref-height:34px;" +
                "-fx-font-size:16px; -fx-alignment:center; -fx-text-fill:white;");
        avatar.setAlignment(Pos.CENTER);

        Label lNome = new Label(c.getNome());
        lNome.setStyle("-fx-font-size:13px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lNome, Priority.ALWAYS); lNome.setMaxWidth(Double.MAX_VALUE);

        Label lEnd = new Label(c.getEndereco());
        lEnd.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        HBox.setHgrow(lEnd, Priority.ALWAYS); lEnd.setMaxWidth(Double.MAX_VALUE);

        Label lCpf = new Label(c.getCpf());
        lCpf.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        lCpf.setPrefWidth(150);

        Button btnEdit = new Button("✏");
        btnEdit.setStyle("-fx-background-color:transparent; -fx-text-fill:#888888;" +
                "-fx-font-size:12px; -fx-cursor:hand; -fx-padding:1 4 1 4;");
        btnEdit.setOnAction(e -> editarCliente(c));

        Button btnDel = new Button("🗑");
        btnDel.setStyle("-fx-background-color:transparent; -fx-text-fill:#AAAAAA;" +
                "-fx-font-size:12px; -fx-cursor:hand; -fx-padding:1 4 1 4;");
        btnDel.setOnAction(e -> excluirCliente(c));

        VBox acoes = new VBox(1, btnEdit, btnDel);
        acoes.setAlignment(Pos.CENTER); acoes.setPrefWidth(36);

        HBox linha = new HBox(10, avatar, lNome, lEnd, lCpf, acoes);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setStyle("-fx-padding:10 16 10 10; -fx-background-color:#FFFFFF;" +
                "-fx-border-color:transparent transparent #EAE0D0 transparent; -fx-border-width:0 0 1 0;");
        return linha;
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
    public void adicionarCliente(ActionEvent e) {
        String nome     = tfNome.getText().trim();
        String endereco = tfEndereco.getText().trim();
        String cpf      = tfCpf.getText().trim();

        if (nome.isEmpty() || endereco.isEmpty() || cpf.isEmpty()) {
            lblMsg.setText("Preencha todos os campos."); return;
        }
        lblMsg.setText("");
        try {
            SessaoUsuario.getInstance().getClienteService()
                    .cadastrar(new Cliente(cpf, nome, endereco));
            tfNome.clear(); tfEndereco.clear(); tfCpf.clear();
            carregarClientes();
        } catch (Exception ex) {
            lblMsg.setText("Erro: " + ex.getMessage());
        }
    }

    private void editarCliente(Cliente c) {
        TextInputDialog dlg = new TextInputDialog(c.getNome());
        dlg.setTitle("Editar Cliente"); dlg.setHeaderText("Novo nome:");
        dlg.showAndWait().ifPresent(n -> {
            if (!n.isBlank()) {
                c.setNome(n);
                try {
                    SessaoUsuario.getInstance().getClienteService().atualizar(c);
                    carregarClientes();
                } catch (Exception ex) { lblMsg.setText("Erro: " + ex.getMessage()); }
            }
        });
    }

    private void excluirCliente(Cliente c) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir \"" + c.getNome() + "\"?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        a.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    SessaoUsuario.getInstance().getClienteService().excluir(c.getCpf());
                    carregarClientes();
                } catch (Exception ex) { lblMsg.setText("Erro: " + ex.getMessage()); }
            }
        });
    }

    @FXML public void navegarAcervo(ActionEvent e)    { irPara("acervo.fxml",      e); }
    @FXML public void navegarRelatorio(ActionEvent e) { irPara("financas.fxml",    e); }
    @FXML public void navegarCadastros(ActionEvent e) { irPara("funcionario.fxml", e); }

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
    //faixa
    @FXML
    private void aoRelatorio() { NavigationHelper.goTo(relatorioButton, "Financas.fxml"); }
    @FXML
    private void aoAlugeuis() {
        NavigationHelper.goTo(alugueisButton, "aluguel.fxml");
    }
    @FXML
    private void aoAcervo() {NavigationHelper.goTo(acervoButton, "ArquivoLivro.fxml"); }
    @FXML
    private void aoCadastros() {
        Usuario usuario = SessaoUsuario.getInstance().getUsuarioLogado();
        if (usuario == null) {
            NavigationHelper.showError("Nenhum usuário logado.");
            return;
        }
        if (usuario.getId() == 1) {
            NavigationHelper.goTo(cadastrosButton, "Funcionario.fxml");
        } else {
            NavigationHelper.goTo(cadastrosButton, "Cliente.fxml");
        }
    }
    @FXML
    private void aoSair() { NavigationHelper.goTo(acervoButton, "Login.fxml");
    }
}