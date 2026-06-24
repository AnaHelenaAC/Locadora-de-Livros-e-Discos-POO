package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.exceptions.DiscoException;
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

/**
 * Controller para a tela de Arquivo de Discos (Gerente)
 * Gerencia a listagem, pesquisa, adição, edição e exclusão de discos.
 */
public class ArquivoDiscoGerenteController implements Initializable {

    // ── Componentes de Navegação ──────────────────────────────
    @FXML private ToggleGroup menuToggleGroup;
    @FXML private ToggleButton btnAcervo;
    @FXML private ToggleButton btnRelatorio;
    @FXML private ToggleButton btnCadastros;
    @FXML private Button btnLogout;

    // ── Componentes de Abas ────────────────────────────────────
    @FXML private ToggleGroup tipoToggleGroup;
    @FXML private ToggleButton btnLivros;
    @FXML private ToggleButton btnDiscos;

    // ── Componentes da Tabela ──────────────────────────────────
    @FXML private ScrollPane scrollTabela;
    @FXML private VBox listaDiscos;

    // ── Componentes de Ação ────────────────────────────────────
    @FXML private TextField tfPesquisa;
    @FXML private Button btnAdicionar;
    @FXML private Button btnPesquisar;
    @FXML private Label lblMensagem;

    // ── Estado ──────────────────────────────────────────────────
    private List<Disco> discosCache;

    // ─────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurações iniciais
        scrollTabela.setVisible(true);
        scrollTabela.setManaged(true);
        scrollTabela.setPrefHeight(400);

        // Remove linhas de preview do Scene Builder
        removerLinhasPreview();

        // Carrega os discos
        carregarDiscos();

        // Verifica permissão do usuário (gerente/funcionário)
        boolean gerente = SessaoUsuario.getInstance().usuarioEhGerente();
        btnAdicionar.setVisible(gerente);
        btnAdicionar.setManaged(gerente);

        // Configura listeners para as abas
        configurarListeners();
    }

    // ── Configuração de Listeners ──────────────────────────────

    private void configurarListeners() {
        // Navegação entre Livros e Discos
        tipoToggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selected = (ToggleButton) newVal;
                String id = selected.getId();

                if ("btnLivros".equals(id)) {
                    navegarParaLivros();
                } else if ("btnDiscos".equals(id)) {
                    // Já estamos aqui
                    carregarDiscos();
                }
            }
        });

        // Navegação pelo menu lateral
        menuToggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selected = (ToggleButton) newVal;
                String id = selected.getId();

                if ("btnAcervo".equals(id)) {
                    // Já estamos aqui
                } else if ("btnRelatorio".equals(id)) {
                    irPara("financas.fxml");
                } else if ("btnCadastros".equals(id)) {
                    irPara("cadastros.fxml");
                }
            }
        });

        // Enter na pesquisa
        tfPesquisa.setOnAction(e -> pesquisarDiscos());
    }

    // ── Carregamento de Dados ──────────────────────────────────

    private void carregarDiscos() {
        listaDiscos.getChildren().clear();
        lblMensagem.setText("");

        try {
            discosCache = SessaoUsuario.getInstance()
                    .getDiscoService().lerDisco();

            if (discosCache.isEmpty()) {
                listaDiscos.getChildren().add(criarLinhaVazia("Nenhum disco cadastrado."));
            } else {
                for (Disco d : discosCache) {
                    listaDiscos.getChildren().add(criarLinhaDisco(d));
                }
            }
        } catch (DiscoException e) {
            listaDiscos.getChildren().add(criarLinhaVazia("Erro ao carregar discos: " + e.getMessage()));
        } catch (Exception e) {
            listaDiscos.getChildren().add(criarLinhaVazia("Erro inesperado: " + e.getMessage()));
        }
    }

    // ── Construção de Linhas ───────────────────────────────────

    private HBox criarLinhaDisco(Disco disco) {
        // Placeholder de imagem (ícone de disco)
        Label icone = new Label("💿");
        icone.setStyle("-fx-font-size:24px; -fx-text-fill:#888888;");
        StackPane imgBox = new StackPane(icone);
        imgBox.setStyle(
                "-fx-background-color:#C8C0C0; -fx-background-radius:4;" +
                        "-fx-min-width:60px; -fx-min-height:60px;" +
                        "-fx-pref-width:60px; -fx-pref-height:60px;"
        );

        // Título
        Label lTitulo = new Label(disco.getTitulo());
        lTitulo.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lTitulo, Priority.ALWAYS);
        lTitulo.setMaxWidth(Double.MAX_VALUE);

        // Data de Lançamento
        String dataStr = disco.getDataDeLancamento().isEmpty()
                ? "—" : disco.getDataDeLancamento();
        Label lData = new Label(dataStr);
        lData.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        lData.setPrefWidth(120);

        // Banda (Criado Por)
        Label lBanda = new Label(disco.getCriadoPor());
        lBanda.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        HBox.setHgrow(lBanda, Priority.ALWAYS);
        lBanda.setMaxWidth(Double.MAX_VALUE);

        // Botões de Ação (Editar/Excluir - apenas gerente)
        VBox acoes = new VBox(4);
        acoes.setAlignment(Pos.CENTER);
        acoes.setPrefWidth(56);

        if (SessaoUsuario.getInstance().usuarioEhGerente()) {
            Button btnEdit = new Button("✏");
            btnEdit.setStyle(
                    "-fx-background-color:transparent; -fx-text-fill:#2E1A47;" +
                            "-fx-font-size:15px; -fx-cursor:hand; -fx-padding:2 6 2 6;"
            );
            btnEdit.setOnAction(e -> editarDisco(disco));

            Button btnDel = new Button("🗑");
            btnDel.setStyle(
                    "-fx-background-color:transparent; -fx-text-fill:#CC3333;" +
                            "-fx-font-size:15px; -fx-cursor:hand; -fx-padding:2 6 2 6;"
            );
            btnDel.setOnAction(e -> excluirDisco(disco));

            acoes.getChildren().addAll(btnEdit, btnDel);
        }

        // Montagem da linha
        HBox linha = new HBox(14, imgBox, lTitulo, lData, lBanda, acoes);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setStyle(
                "-fx-background-color:#FFFFFF;" +
                        "-fx-padding:8 16 8 16;" +
                        "-fx-min-height:80px; -fx-pref-height:80px;" +
                        "-fx-border-color:transparent transparent #EAE0D0 transparent;" +
                        "-fx-border-width:0 0 1 0;"
        );
        linha.setOnMouseEntered(e -> 
            linha.setStyle("-fx-background-color:#F5F0E8; -fx-padding:8 16 8 16;" +
                    "-fx-min-height:80px; -fx-pref-height:80px;" +
                    "-fx-border-color:transparent transparent #EAE0D0 transparent;" +
                    "-fx-border-width:0 0 1 0;")
        );
        linha.setOnMouseExited(e -> 
            linha.setStyle("-fx-background-color:#FFFFFF; -fx-padding:8 16 8 16;" +
                    "-fx-min-height:80px; -fx-pref-height:80px;" +
                    "-fx-border-color:transparent transparent #EAE0D0 transparent;" +
                    "-fx-border-width:0 0 1 0;")
        );

        return linha;
    }

    private HBox criarLinhaVazia(String mensagem) {
        Label l = new Label(mensagem);
        l.setStyle("-fx-text-fill:#9A8A7A; -fx-font-style:italic; -fx-font-size:13px;");
        HBox h = new HBox(l);
        h.setStyle("-fx-padding:24; -fx-background-color:#FFFFFF;");
        h.setAlignment(Pos.CENTER);
        return h;
    }

    // ── Pesquisa ───────────────────────────────────────────────

    @FXML
    private void pesquisarDiscos() {
        String termo = tfPesquisa.getText().trim();
        listaDiscos.getChildren().clear();
        lblMensagem.setText("");

        if (termo.isEmpty()) {
            carregarDiscos();
            return;
        }

        try {
            List<Disco> resultado = SessaoUsuario.getInstance()
                    .getDiscoService().buscarPor("titulo", termo);

            if (resultado.isEmpty()) {
                listaDiscos.getChildren().add(criarLinhaVazia(
                        "Nenhum disco encontrado para: \"" + termo + "\""
                ));
            } else {
                for (Disco d : resultado) {
                    listaDiscos.getChildren().add(criarLinhaDisco(d));
                }
                lblMensagem.setText("Encontrados " + resultado.size() + " resultados");
                lblMensagem.setStyle("-fx-text-fill:#2E7D32;");
            }
        } catch (Exception e) {
            lblMensagem.setText("Erro na pesquisa: " + e.getMessage());
            lblMensagem.setStyle("-fx-text-fill:#C62828;");
        }
    }

    // ── Ações de CRUD ───────────────────────────────────────────

    @FXML
    private void adicionarDisco(ActionEvent e) {
        // Exemplo de diálogo para adicionar novo disco
        // Aqui você pode abrir um formulário modal ou navegar para a tela de cadastro
        mostrarDialogoAdicionarDisco();
    }

    private void editarDisco(Disco disco) {
        TextInputDialog dialog = new TextInputDialog(disco.getTitulo());
        dialog.setTitle("Editar Disco");
        dialog.setHeaderText("Editando: " + disco.getTitulo());
        dialog.setContentText("Novo título:");

        dialog.showAndWait().ifPresent(novoTitulo -> {
            if (!novoTitulo.trim().isEmpty()) {
                try {
                    disco.setTitulo(novoTitulo.trim());
                    SessaoUsuario.getInstance().getDiscoService().atualizarDisco(disco);
                    carregarDiscos();
                    mostrarMensagemSucesso("Disco atualizado com sucesso!");
                } catch (Exception ex) {
                    mostrarMensagemErro("Erro ao atualizar: " + ex.getMessage());
                }
            }
        });
    }

    private void excluirDisco(Disco disco) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText(null);
        alert.setContentText("Deseja realmente excluir o disco \"" + disco.getTitulo() + "\"?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    SessaoUsuario.getInstance().getDiscoService().apagarDisco(disco.getID());
                    carregarDiscos();
                    mostrarMensagemSucesso("Disco excluído com sucesso!");
                } catch (Exception ex) {
                    mostrarMensagemErro("Erro ao excluir: " + ex.getMessage());
                }
            }
        });
    }

    // ── Diálogo para Adicionar Disco ───────────────────────────

    private void mostrarDialogoAdicionarDisco() {
        // Diálogo simples para adicionar disco
        Dialog<Disco> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Disco");
        dialog.setHeaderText("Preencha os dados do disco");

        ButtonType btnSalvar = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSalvar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField tfTitulo = new TextField();
        tfTitulo.setPromptText("Título");
        TextField tfBanda = new TextField();
        tfBanda.setPromptText("Banda/Artista");
        TextField tfGenero = new TextField();
        tfGenero.setPromptText("Gênero");
        TextField tfData = new TextField();
        tfData.setPromptText("dd/MM/yyyy");
        TextField tfQtd = new TextField();
        tfQtd.setPromptText("Quantidade");
        TextField tfValor = new TextField();
        tfValor.setPromptText("Valor (R$)");
        TextField tfDuracao = new TextField();
        tfDuracao.setPromptText("Duração (segundos)");

        grid.add(new Label("Título:"), 0, 0);
        grid.add(tfTitulo, 1, 0);
        grid.add(new Label("Banda:"), 0, 1);
        grid.add(tfBanda, 1, 1);
        grid.add(new Label("Gênero:"), 0, 2);
        grid.add(tfGenero, 1, 2);
        grid.add(new Label("Data:"), 0, 3);
        grid.add(tfData, 1, 3);
        grid.add(new Label("Quantidade:"), 0, 4);
        grid.add(tfQtd, 1, 4);
        grid.add(new Label("Valor:"), 0, 5);
        grid.add(tfValor, 1, 5);
        grid.add(new Label("Duração:"), 0, 6);
        grid.add(tfDuracao, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSalvar) {
                try {
                    String titulo = tfTitulo.getText().trim();
                    String banda = tfBanda.getText().trim();
                    String genero = tfGenero.getText().trim();
                    String data = tfData.getText().trim();
                    int qtd = Integer.parseInt(tfQtd.getText().trim());
                    double valor = Double.parseDouble(tfValor.getText().trim().replace(",", "."));
                    int duracao = Integer.parseInt(tfDuracao.getText().trim());

                    int h = duracao / 3600;
                    int m = (duracao % 3600) / 60;
                    int s = duracao % 60;

                    return new Disco(titulo, banda, genero, valor, data, qtd, true, h, m, s);
                } catch (Exception e) {
                    mostrarMensagemErro("Erro nos dados: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(disco -> {
            try {
                SessaoUsuario.getInstance().getDiscoService().adicionarDisco(disco);
                carregarDiscos();
                mostrarMensagemSucesso("Disco adicionado com sucesso!");
            } catch (Exception e) {
                mostrarMensagemErro("Erro ao adicionar: " + e.getMessage());
            }
        });
    }

    // ── Navegação ──────────────────────────────────────────────

    private void navegarParaLivros() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/locadora/view/ArquivoLivroGerente.fxml")
            );
            Stage stage = (Stage) scrollTabela.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            mostrarMensagemErro("Erro ao navegar para livros: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout(ActionEvent e) {
        SessaoUsuario.getInstance().limparSessao();
        irPara("login.fxml");
    }

    private void irPara(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/locadora/view/" + fxml)
            );
            Stage stage = (Stage) scrollTabela.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            mostrarMensagemErro("Erro ao navegar: " + e.getMessage());
        }
    }

    // ── Helpers ─────────────────────────────────────────────────

    private void removerLinhasPreview() {
        try {
            VBox pai = (VBox) scrollTabela.getParent();
            int idxScroll = pai.getChildren().indexOf(scrollTabela);
            if (idxScroll > 1) {
                pai.getChildren().remove(1, idxScroll);
            }
        } catch (Exception ignored) {}
    }

    private void mostrarMensagemSucesso(String mensagem) {
        lblMensagem.setText(mensagem);
        lblMensagem.setStyle("-fx-text-fill:#2E7D32; -fx-font-weight:bold;");
    }

    private void mostrarMensagemErro(String mensagem) {
        lblMensagem.setText(mensagem);
        lblMensagem.setStyle("-fx-text-fill:#C62828; -fx-font-weight:bold;");
    }
}