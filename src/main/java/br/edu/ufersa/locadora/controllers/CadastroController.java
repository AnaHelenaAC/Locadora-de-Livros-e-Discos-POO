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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CadastrosController implements Initializable {

    // ── Abas ──────────────────────────────────────────────────
    @FXML private Button btnAbaFuncionario;
    @FXML private Button btnAbaCliente;

    // ── Campos de adição rápida ───────────────────────────────
    @FXML private TextField tfNome;
    @FXML private TextField tfEndereco;
    @FXML private TextField tfCpf;

    // ── Lista de registros (VBox dinâmica) ────────────────────
    @FXML private VBox listaRegistros;

    // ── Feedback ──────────────────────────────────────────────
    @FXML private Label lblMsg;

    // ── Estado ────────────────────────────────────────────────
    private boolean exibindoClientes = true;

    // ── Estilos reutilizados nas linhas ───────────────────────
    private static final String LINHA_STYLE =
            "-fx-background-color:#FFFFFF; -fx-padding:10 16 10 16;" +
                    "-fx-border-color:transparent transparent #EAE0D0 transparent; -fx-border-width:0 0 1 0;";

    private static final String AVATAR_STYLE =
            "-fx-background-color:#C0B8C8; -fx-background-radius:50;" +
                    "-fx-min-width:36px; -fx-min-height:36px;" +
                    "-fx-pref-width:36px; -fx-pref-height:36px;";

    private static final String ICON_BTN_STYLE =
            "-fx-background-color:transparent; -fx-cursor:hand;" +
                    "-fx-font-size:14px; -fx-padding:2 4 2 4;";

    // ─────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boolean gerente = SessaoUsuario.getInstance().usuarioEhGerente();

        // Aba Funcionário só visível para gerente
        btnAbaFuncionario.setVisible(gerente);
        btnAbaFuncionario.setManaged(gerente);

        // Se for gerente, ajusta borda da aba Cliente para parecer unida
        if (gerente) {
            btnAbaCliente.setStyle(
                    btnAbaCliente.getStyle()
                            .replace("-fx-background-radius:20;", "-fx-background-radius:0 20 20 0;")
                            .replace("-fx-border-radius:20;",     "-fx-border-radius:0 20 20 0;")
            );
        }

        carregarClientes();
    }

    // ── Alternância de abas ───────────────────────────────────

    @FXML
    public void mostrarClientes(ActionEvent e) {
        exibindoClientes = true;
        marcarAbaAtiva(btnAbaCliente, btnAbaFuncionario);
        tfEndereco.setPromptText("Endereço");
        tfCpf.setPromptText("CPF");
        carregarClientes();
    }

    @FXML
    public void mostrarFuncionarios(ActionEvent e) {
        exibindoClientes = false;
        marcarAbaAtiva(btnAbaFuncionario, btnAbaCliente);
        tfEndereco.setPromptText("Login");
        tfCpf.setPromptText("Senha");
        carregarFuncionarios();
    }

    private void marcarAbaAtiva(Button ativo, Button inativo) {
        ativo.setStyle(ativo.getStyle()
                .replace("-fx-background-color:#F8EED1;", "-fx-background-color:#2E1A47;")
                .replace("-fx-text-fill:#2E1A47;",        "-fx-text-fill:#F8EED1;"));
        inativo.setStyle(inativo.getStyle()
                .replace("-fx-background-color:#2E1A47;", "-fx-background-color:#F8EED1;")
                .replace("-fx-text-fill:#F8EED1;",        "-fx-text-fill:#2E1A47;"));
    }

    // ── Carregar registros ────────────────────────────────────

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

    // ── Construção das linhas visuais ─────────────────────────

    /**
     * Linha de cliente:
     * [avatar] Nome ......... Endereço ......... CPF     [✏] [🗑]
     */
    private HBox criarLinhaCliente(Cliente c) {
        Label avatar = criarAvatar();

        Label lblNome = new Label(c.getNome());
        lblNome.setStyle("-fx-font-size:13px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lblNome, Priority.ALWAYS);
        lblNome.setMaxWidth(Double.MAX_VALUE);

        Label lblEndereco = new Label(c.getEndereco());
        lblEndereco.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        HBox.setHgrow(lblEndereco, Priority.ALWAYS);
        lblEndereco.setMaxWidth(Double.MAX_VALUE);

        Label lblCpf = new Label(c.getCpf());
        lblCpf.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        lblCpf.setPrefWidth(160);

        VBox acoes = criarBotoesAcao(
                e -> editarCliente(c),
                e -> excluirCliente(c)
        );

        HBox linha = new HBox(12, avatar, lblNome, lblEndereco, lblCpf, acoes);
        linha.setStyle(LINHA_STYLE);
        linha.setAlignment(Pos.CENTER_LEFT);
        return linha;
    }

    /**
     * Linha de funcionário:
     * [avatar] Nome ......... Login ...........  —       [✏] [🗑]
     */
    private HBox criarLinhaFuncionario(UsuarioFuncionario f) {
        Label avatar = criarAvatar();

        Label lblNome = new Label(f.getNome());
        lblNome.setStyle("-fx-font-size:13px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lblNome, Priority.ALWAYS);
        lblNome.setMaxWidth(Double.MAX_VALUE);

        Label lblLogin = new Label(f.getLogin());
        lblLogin.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        HBox.setHgrow(lblLogin, Priority.ALWAYS);
        lblLogin.setMaxWidth(Double.MAX_VALUE);

        Label lblId = new Label("#" + (f.getIdFuncionario() != null ? f.getIdFuncionario() : "—"));
        lblId.setStyle("-fx-font-size:13px; -fx-text-fill:#888888;");
        lblId.setPrefWidth(160);

        VBox acoes = criarBotoesAcao(
                e -> editarFuncionario(f),
                e -> excluirFuncionario(f)
        );

        HBox linha = new HBox(12, avatar, lblNome, lblLogin, lblId, acoes);
        linha.setStyle(LINHA_STYLE);
        linha.setAlignment(Pos.CENTER_LEFT);
        return linha;
    }

    /** Avatar circular cinza com ícone de pessoa */
    private Label criarAvatar() {
        Label avatar = new Label("👤");
        avatar.setStyle(AVATAR_STYLE +
                "-fx-font-size:18px; -fx-alignment:center; -fx-text-fill:#FFFFFF;");
        avatar.setAlignment(Pos.CENTER);
        return avatar;
    }

    /** Botões ✏ e 🗑 empilhados verticalmente como no design */
    private VBox criarBotoesAcao(
            javafx.event.EventHandler<ActionEvent> onEdit,
            javafx.event.EventHandler<ActionEvent> onDelete) {

        Button btnEdit = new Button("✏");
        btnEdit.setStyle(ICON_BTN_STYLE + "-fx-text-fill:#888888;");
        btnEdit.setOnAction(onEdit);

        Button btnDel = new Button("🗑");
        btnDel.setStyle(ICON_BTN_STYLE + "-fx-text-fill:#AAAAAA;");
        btnDel.setOnAction(onDelete);

        VBox box = new VBox(2, btnEdit, btnDel);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private HBox linhaVazia(String msg) {
        Label l = new Label(msg);
        l.setStyle("-fx-text-fill:#9A8A7A; -fx-font-style:italic; -fx-font-size:13px;");
        HBox h = new HBox(l);
        h.setStyle("-fx-padding:16 20 16 20;");
        h.setAlignment(Pos.CENTER);
        return h;
    }

    // ── Ações: adicionar ──────────────────────────────────────

    @FXML
    public void abrirFormNovo(ActionEvent e) {
        String nome     = tfNome.getText().trim();
        String campo2   = tfEndereco.getText().trim(); // endereço ou login
        String campo3   = tfCpf.getText().trim();      // cpf ou senha

        if (nome.isEmpty() || campo2.isEmpty() || campo3.isEmpty()) {
            lblMsg.setText("Preencha todos os campos antes de adicionar.");
            return;
        }

        lblMsg.setText("");
        try {
            if (exibindoClientes) {
                Cliente novo = new Cliente(campo3, nome, campo2); // cpf, nome, endereco
                SessaoUsuario.getInstance().getClienteService().cadastrar(novo);
                carregarClientes();
            } else {
                UsuarioFuncionario novo = new UsuarioFuncionario(nome, campo2, campo3);
                SessaoUsuario.getInstance().getUsuarioFuncionarioService().cadastrar(novo);
                carregarFuncionarios();
            }
            tfNome.clear(); tfEndereco.clear(); tfCpf.clear();
        } catch (SemNomeException | UsuarioException | UsuarioFuncionarioException ex) {
            lblMsg.setText("Erro de validação: " + ex.getMessage());
        } catch (Exception ex) {
            lblMsg.setText("Erro ao salvar: " + ex.getMessage());
        }
    }

    // ── Ações: editar / excluir clientes ─────────────────────

    private void editarCliente(Cliente c) {
        TextInputDialog dlg = new TextInputDialog(c.getNome());
        dlg.setTitle("Editar Cliente");
        dlg.setHeaderText("Nome:");
        dlg.showAndWait().ifPresent(novoNome -> {
            if (!novoNome.isBlank()) {
                c.setNome(novoNome);
                try {
                    SessaoUsuario.getInstance().getClienteService().atualizar(c);
                    carregarClientes();
                } catch (Exception ex) {
                    lblMsg.setText("Erro ao editar: " + ex.getMessage());
                }
            }
        });
    }

    private void excluirCliente(Cliente c) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir o cliente \"" + c.getNome() + "\"?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    SessaoUsuario.getInstance().getClienteService().excluir(c.getCpf());
                    carregarClientes();
                } catch (Exception ex) {
                    lblMsg.setText("Erro ao excluir: " + ex.getMessage());
                }
            }
        });
    }

    // ── Ações: editar / excluir funcionários ─────────────────

    private void editarFuncionario(UsuarioFuncionario f) {
        TextInputDialog dlg = new TextInputDialog(f.getNome());
        dlg.setTitle("Editar Funcionário");
        dlg.setHeaderText("Nome:");
        dlg.showAndWait().ifPresent(novoNome -> {
            if (!novoNome.isBlank()) {
                try {
                    f.setNome(novoNome);
                    SessaoUsuario.getInstance().getUsuarioFuncionarioService().atualizar(f);
                    carregarFuncionarios();
                } catch (Exception ex) {
                    lblMsg.setText("Erro ao editar: " + ex.getMessage());
                }
            }
        });
    }

    private void excluirFuncionario(UsuarioFuncionario f) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir o funcionário \"" + f.getNome() + "\"?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    SessaoUsuario.getInstance().getUsuarioFuncionarioService().excluir(f);
                    carregarFuncionarios();
                } catch (Exception ex) {
                    lblMsg.setText("Erro ao excluir: " + ex.getMessage());
                }
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