package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Cliente;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CadastroController implements Initializable {

    // ── Abas ──────────────────────────────────────────────────
    @FXML private Button btnAbaFuncionario;
    @FXML private Button btnAbaCliente;

    // ── Campos de adição rápida ───────────────────────────────
    @FXML private TextField tfNome;
    @FXML private TextField tfEndereco;
    @FXML private TextField tfCpf;

    // ── Lista dinâmica (ScrollPane + VBox) ───────────────────
    @FXML private ScrollPane scrollLista;
    @FXML private VBox       listaRegistros;

    // ── Feedback ──────────────────────────────────────────────
    @FXML private Label lblMsg;

    // ── Estado ────────────────────────────────────────────────
    private boolean exibindoClientes = true;

    // ─────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boolean gerente = SessaoUsuario.getInstance().usuarioEhGerente();

        // Aba Funcionário só para gerente
        btnAbaFuncionario.setVisible(gerente);
        btnAbaFuncionario.setManaged(gerente);

        // Em runtime: esconde as linhas de preview estáticas e exibe o scroll dinâmico
        scrollLista.setVisible(true);
        scrollLista.setManaged(true);
        scrollLista.setPrefHeight(300);

        // Remove as linhas estáticas de preview (são filhos do VBox pai da tabela)
        // — o FXML as tem para o Scene Builder exibir corretamente
        removerLinhasPreview();

        carregarClientes();
    }

    /**
     * Remove as HBox de preview estáticas que existem só para o Scene Builder.
     * Elas ficam entre o cabeçalho (índice 0), a linha de adição (índice 1)
     * e o ScrollPane dinâmico. Em runtime substituímos pelo scroll real.
     */
    private void removerLinhasPreview() {
        try {
            // O VBox da tabela é pai do scrollLista
            VBox tabelaVBox = (VBox) scrollLista.getParent();
            // Remove todos os filhos entre a linha de adição (índice 1) e o ScrollPane
            int scrollIdx = tabelaVBox.getChildren().indexOf(scrollLista);
            // Remove linhas de índice 2 até scrollIdx-1 (as linhas de preview)
            if (scrollIdx > 2) {
                tabelaVBox.getChildren().remove(2, scrollIdx);
            }
        } catch (Exception e) {
            // Se falhar, não quebra o app — as linhas de preview ficam visíveis
        }
    }

    // ── Alternância de abas ───────────────────────────────────

    @FXML
    public void mostrarClientes(ActionEvent e) {
        exibindoClientes = true;
        ativarAba(btnAbaCliente, btnAbaFuncionario);
        tfEndereco.setPromptText("Endereço");
        tfCpf.setPromptText("CPF");
        carregarClientes();
    }

    @FXML
    public void mostrarFuncionarios(ActionEvent e) {
        exibindoClientes = false;
        ativarAba(btnAbaFuncionario, btnAbaCliente);
        tfEndereco.setPromptText("Login");
        tfCpf.setPromptText("Senha");
        carregarFuncionarios();
    }

    private void ativarAba(Button ativo, Button inativo) {
        ativo.setStyle(
                "-fx-background-color:#2E1A47; -fx-text-fill:#F8EED1;" +
                        "-fx-font-size:13px; -fx-font-weight:bold;" +
                        "-fx-background-radius:20; -fx-border-color:#2E1A47;" +
                        "-fx-border-width:1.5; -fx-border-radius:20;" +
                        "-fx-padding:8 24 8 24; -fx-cursor:hand;"
        );
        inativo.setStyle(
                "-fx-background-color:#F8EED1; -fx-text-fill:#2E1A47;" +
                        "-fx-font-size:13px; -fx-font-weight:bold;" +
                        "-fx-background-radius:20 0 0 20; -fx-border-color:#2E1A47;" +
                        "-fx-border-width:1.5; -fx-border-radius:20 0 0 20;" +
                        "-fx-padding:8 20 8 20; -fx-cursor:hand;"
        );
    }

    // ── Carregamento de dados ─────────────────────────────────

    private void carregarClientes() {
        listaRegistros.getChildren().clear();
        lblMsg.setText("");
        try {
            List<Cliente> clientes = SessaoUsuario.getInstance()
                    .getClienteService().listarTodos();
            if (clientes.isEmpty()) {
                listaRegistros.getChildren().add(linhaVazia("Nenhum cliente cadastrado."));
            } else {
                for (Cliente c : clientes)
                    listaRegistros.getChildren().add(criarLinhaCliente(c));
            }
        } catch (Exception ex) {
            lblMsg.setText("Erro ao carregar clientes: " + ex.getMessage());
        }
    }

    private void carregarFuncionarios() {
        listaRegistros.getChildren().clear();
        lblMsg.setText("");
        try {
            List<UsuarioFuncionario> funcs = SessaoUsuario.getInstance()
                    .getUsuarioFuncionarioService().listarTodos();
            if (funcs.isEmpty()) {
                listaRegistros.getChildren().add(linhaVazia("Nenhum funcionário cadastrado."));
            } else {
                for (UsuarioFuncionario f : funcs)
                    listaRegistros.getChildren().add(criarLinhaFuncionario(f));
            }
        } catch (Exception ex) {
            lblMsg.setText("Erro ao carregar funcionários: " + ex.getMessage());
        }
    }

    // ── Construção visual das linhas ──────────────────────────

    private HBox criarLinhaCliente(Cliente c) {
        return criarLinha(
                c.getNome(),
                c.getEndereco(),
                c.getCpf(),
                e -> editarCliente(c),
                e -> excluirCliente(c)
        );
    }

    private HBox criarLinhaFuncionario(UsuarioFuncionario f) {
        String idStr = f.getIdFuncionario() != null ? "#" + f.getIdFuncionario() : "—";
        return criarLinha(
                f.getNome(),
                f.getLogin(),
                idStr,
                e -> editarFuncionario(f),
                e -> excluirFuncionario(f)
        );
    }

    private HBox criarLinha(String col1, String col2, String col3,
                            javafx.event.EventHandler<ActionEvent> onEdit,
                            javafx.event.EventHandler<ActionEvent> onDelete) {

        // Avatar circular
        Label avatar = new Label("👤");
        avatar.setStyle(
                "-fx-background-color:#C0B8C8; -fx-background-radius:50;" +
                        "-fx-min-width:36px; -fx-min-height:36px;" +
                        "-fx-pref-width:36px; -fx-pref-height:36px;" +
                        "-fx-font-size:18px; -fx-alignment:center; -fx-text-fill:white;"
        );
        avatar.setAlignment(Pos.CENTER);

        Label lblCol1 = new Label(col1);
        lblCol1.setStyle("-fx-font-size:13px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lblCol1, Priority.ALWAYS);
        lblCol1.setMaxWidth(Double.MAX_VALUE);

        Label lblCol2 = new Label(col2);
        lblCol2.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        HBox.setHgrow(lblCol2, Priority.ALWAYS);
        lblCol2.setMaxWidth(Double.MAX_VALUE);

        Label lblCol3 = new Label(col3);
        lblCol3.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        lblCol3.setPrefWidth(160);

        // Botões ação empilhados verticalmente
        Button btnEdit = new Button("✏");
        btnEdit.setStyle(
                "-fx-background-color:transparent; -fx-text-fill:#888888;" +
                        "-fx-font-size:13px; -fx-cursor:hand; -fx-padding:1 4 1 4;"
        );
        btnEdit.setOnAction(onEdit);

        Button btnDel = new Button("🗑");
        btnDel.setStyle(
                "-fx-background-color:transparent; -fx-text-fill:#AAAAAA;" +
                        "-fx-font-size:13px; -fx-cursor:hand; -fx-padding:1 4 1 4;"
        );
        btnDel.setOnAction(onDelete);

        VBox acoes = new VBox(2, btnEdit, btnDel);
        acoes.setAlignment(Pos.CENTER);
        acoes.setPrefWidth(40);

        HBox linha = new HBox(10, avatar, lblCol1, lblCol2, lblCol3, acoes);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setStyle(
                "-fx-padding:10 16 10 10;" +
                        "-fx-background-color:#FFFFFF;" +
                        "-fx-border-color:transparent transparent #EAE0D0 transparent;" +
                        "-fx-border-width:0 0 1 0;"
        );
        return linha;
    }

    private HBox linhaVazia(String msg) {
        Label l = new Label(msg);
        l.setStyle("-fx-text-fill:#9A8A7A; -fx-font-style:italic; -fx-font-size:13px;");
        HBox h = new HBox(l);
        h.setStyle("-fx-padding:20 20 20 20; -fx-background-color:#FFFFFF;");
        h.setAlignment(Pos.CENTER);
        return h;
    }

    // ── Adição rápida ─────────────────────────────────────────

    @FXML
    public void abrirFormNovo(ActionEvent e) {
        String nome   = tfNome.getText().trim();
        String campo2 = tfEndereco.getText().trim();
        String campo3 = tfCpf.getText().trim();

        if (nome.isEmpty() || campo2.isEmpty() || campo3.isEmpty()) {
            lblMsg.setText("Preencha todos os campos antes de adicionar.");
            return;
        }
        lblMsg.setText("");
        try {
            if (exibindoClientes) {
                // campo2 = endereço, campo3 = cpf
                SessaoUsuario.getInstance().getClienteService()
                        .cadastrar(new Cliente(campo3, nome, campo2));
                carregarClientes();
            } else {
                // campo2 = login, campo3 = senha
                SessaoUsuario.getInstance().getUsuarioFuncionarioService()
                        .cadastrar(new UsuarioFuncionario(nome, campo2, campo3));
                carregarFuncionarios();
            }
            tfNome.clear(); tfEndereco.clear(); tfCpf.clear();
        } catch (SemNomeException | UsuarioException | UsuarioFuncionarioException ex) {
            lblMsg.setText("Erro de validação: " + ex.getMessage());
        } catch (Exception ex) {
            lblMsg.setText("Erro ao salvar: " + ex.getMessage());
        }
    }

    // ── Editar / Excluir clientes ─────────────────────────────

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

    // ── Editar / Excluir funcionários ─────────────────────────

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

    // ── Navegação ─────────────────────────────────────────────

    @FXML public void navegarAcervo(ActionEvent e)    { irPara("acervo.fxml",    e); }
    @FXML public void navegarRelatorio(ActionEvent e) { irPara("relatorio.fxml", e); }
    @FXML public void navegarCadastros(ActionEvent e) { /* já estamos aqui */ }

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
            new Alert(Alert.AlertType.ERROR, "Erro ao navegar: " + ex.getMessage()).showAndWait();
        }
    }
}