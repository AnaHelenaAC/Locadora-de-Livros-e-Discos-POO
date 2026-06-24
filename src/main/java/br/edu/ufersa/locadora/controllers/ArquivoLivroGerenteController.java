package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Livro;
import br.edu.ufersa.locadora.exceptions.LivroException;
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
import javafx.geometry.Insets;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller para a tela de Arquivo de Livros (Gerente)
 * Gerencia a listagem, pesquisa, adição, edição e exclusão de livros.
 */
public class ArquivoLivroGerenteController implements Initializable {

    // ── Componentes de Navegação ──────────────────────────────
    @FXML private ToggleGroup menuToggleGroup;
    @FXML private ToggleButton btnAcervo;
    @FXML private ToggleButton btnRelatorio;
    @FXML private ToggleButton btnCadastros;
    @FXML private Button btnLogout;

    // ── Componentes da Tabela ──────────────────────────────────
    @FXML private ScrollPane scrollTabela;
    @FXML private VBox listaLivros;

    // ── Componentes de Ação ────────────────────────────────────
    @FXML private TextField tfPesquisa;
    @FXML private Button btnAdicionar;
    @FXML private Button btnPesquisar;
    @FXML private Label lblMensagem;

    // ── Estado ──────────────────────────────────────────────────
    private List<Livro> livrosCache;

    // ─────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurações iniciais
        scrollTabela.setVisible(true);
        scrollTabela.setManaged(true);
        scrollTabela.setPrefHeight(400);

        // Remove linhas de preview do Scene Builder
        removerLinhasPreview();

        // Carrega os livros
        carregarLivros();

        // Verifica permissão do usuário (gerente/funcionário)
        boolean gerente = SessaoUsuario.getInstance().usuarioEhGerente();
        btnAdicionar.setVisible(gerente);
        btnAdicionar.setManaged(gerente);

        // Configura listeners
        configurarListeners();

        // Configura evento de pesquisa com Enter
        tfPesquisa.setOnAction(e -> pesquisarLivros());
    }

    // ── Configuração de Listeners ──────────────────────────────

    private void configurarListeners() {
        // Navegação pelo menu lateral
        menuToggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selected = (ToggleButton) newVal;
                String id = selected.getId();

                switch (id) {
                    case "btnAcervo":
                        // Já estamos aqui
                        break;
                    case "btnRelatorio":
                        irPara("financas.fxml");
                        break;
                    case "btnCadastros":
                        irPara("cadastros.fxml");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    // ── Carregamento de Dados ──────────────────────────────────

    private void carregarLivros() {
        listaLivros.getChildren().clear();
        lblMensagem.setText("");

        try {
            livrosCache = SessaoUsuario.getInstance()
                    .getLivroService().lerLivro();

            if (livrosCache.isEmpty()) {
                listaLivros.getChildren().add(criarLinhaVazia("Nenhum livro cadastrado."));
            } else {
                for (Livro l : livrosCache) {
                    listaLivros.getChildren().add(criarLinhaLivro(l));
                }
            }
        } catch (LivroException e) {
            listaLivros.getChildren().add(criarLinhaVazia("Erro ao carregar livros: " + e.getMessage()));
        } catch (Exception e) {
            listaLivros.getChildren().add(criarLinhaVazia("Erro inesperado: " + e.getMessage()));
        }
    }

    // ── Construção de Linhas ───────────────────────────────────

    private HBox criarLinhaLivro(Livro livro) {
        // Placeholder de imagem (ícone de livro)
        Label icone = new Label("📖");
        icone.setStyle("-fx-font-size:24px; -fx-text-fill:#888888;");
        StackPane imgBox = new StackPane(icone);
        imgBox.setStyle(
                "-fx-background-color:#C8C0C0; -fx-background-radius:4;" +
                        "-fx-min-width:60px; -fx-min-height:60px;" +
                        "-fx-pref-width:60px; -fx-pref-height:60px;"
        );

        // Título
        Label lTitulo = new Label(livro.getTitulo());
        lTitulo.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lTitulo, Priority.ALWAYS);
        lTitulo.setMaxWidth(Double.MAX_VALUE);

        // Data de Lançamento
        String dataStr = livro.getDataDeLancamento().isEmpty()
                ? "—" : livro.getDataDeLancamento();
        Label lData = new Label(dataStr);
        lData.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        lData.setPrefWidth(120);

        // Autor (Criado Por)
        Label lAutor = new Label(livro.getCriadoPor());
        lAutor.setStyle("-fx-font-size:13px; -fx-text-fill:#555555;");
        HBox.setHgrow(lAutor, Priority.ALWAYS);
        lAutor.setMaxWidth(Double.MAX_VALUE);

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
            btnEdit.setOnAction(e -> editarLivro(livro));

            Button btnDel = new Button("🗑");
            btnDel.setStyle(
                    "-fx-background-color:transparent; -fx-text-fill:#CC3333;" +
                            "-fx-font-size:15px; -fx-cursor:hand; -fx-padding:2 6 2 6;"
            );
            btnDel.setOnAction(e -> excluirLivro(livro));

            acoes.getChildren().addAll(btnEdit, btnDel);
        }

        // Montagem da linha
        HBox linha = new HBox(14, imgBox, lTitulo, lData, lAutor, acoes);
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
    private void pesquisarLivros() {
        String termo = tfPesquisa.getText().trim();
        listaLivros.getChildren().clear();
        lblMensagem.setText("");

        if (termo.isEmpty()) {
            carregarLivros();
            return;
        }

        try {
            List<Livro> resultado = SessaoUsuario.getInstance()
                    .getLivroService().buscarPor("titulo", termo);

            if (resultado.isEmpty()) {
                listaLivros.getChildren().add(criarLinhaVazia(
                        "Nenhum livro encontrado para: \"" + termo + "\""
                ));
            } else {
                for (Livro l : resultado) {
                    listaLivros.getChildren().add(criarLinhaLivro(l));
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
    private void adicionarLivro(ActionEvent e) {
        mostrarDialogoAdicionarLivro();
    }

    private void editarLivro(Livro livro) {
        TextInputDialog dialog = new TextInputDialog(livro.getTitulo());
        dialog.setTitle("Editar Livro");
        dialog.setHeaderText("Editando: " + livro.getTitulo());
        dialog.setContentText("Novo título:");

        dialog.showAndWait().ifPresent(novoTitulo -> {
            if (!novoTitulo.trim().isEmpty()) {
                try {
                    livro.setTitulo(novoTitulo.trim());
                    SessaoUsuario.getInstance().getLivroService().atualizarLivro(livro);
                    carregarLivros();
                    mostrarMensagemSucesso("Livro atualizado com sucesso!");
                } catch (Exception ex) {
                    mostrarMensagemErro("Erro ao atualizar: " + ex.getMessage());
                }
            }
        });
    }

    private void excluirLivro(Livro livro) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText(null);
        alert.setContentText("Deseja realmente excluir o livro \"" + livro.getTitulo() + "\"?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    SessaoUsuario.getInstance().getLivroService().apagarLivro(livro.getID());
                    carregarLivros();
                    mostrarMensagemSucesso("Livro excluído com sucesso!");
                } catch (Exception ex) {
                    mostrarMensagemErro("Erro ao excluir: " + ex.getMessage());
                }
            }
        });
    }

    // ── Diálogo para Adicionar Livro ───────────────────────────

    private void mostrarDialogoAdicionarLivro() {
        Dialog<Livro> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Livro");
        dialog.setHeaderText("Preencha os dados do livro");

        ButtonType btnSalvar = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSalvar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField tfTitulo = new TextField();
        tfTitulo.setPromptText("Título");
        TextField tfAutor = new TextField();
        tfAutor.setPromptText("Autor(a)");
        TextField tfGenero = new TextField();
        tfGenero.setPromptText("Gênero");
        TextField tfData = new TextField();
        tfData.setPromptText("dd/MM/yyyy");
        TextField tfQtd = new TextField();
        tfQtd.setPromptText("Quantidade");
        TextField tfValor = new TextField();
        tfValor.setPromptText("Valor (R$)");
        TextField tfPaginas = new TextField();
        tfPaginas.setPromptText("Número de páginas");

        grid.add(new Label("Título:"), 0, 0);
        grid.add(tfTitulo, 1, 0);
        grid.add(new Label("Autor:"), 0, 1);
        grid.add(tfAutor, 1, 1);
        grid.add(new Label("Gênero:"), 0, 2);
        grid.add(tfGenero, 1, 2);
        grid.add(new Label("Data:"), 0, 3);
        grid.add(tfData, 1, 3);
        grid.add(new Label("Quantidade:"), 0, 4);
        grid.add(tfQtd, 1, 4);
        grid.add(new Label("Valor:"), 0, 5);
        grid.add(tfValor, 1, 5);
        grid.add(new Label("Páginas:"), 0, 6);
        grid.add(tfPaginas, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSalvar) {
                try {
                    String titulo = tfTitulo.getText().trim();
                    String autor = tfAutor.getText().trim();
                    String genero = tfGenero.getText().trim();
                    String data = tfData.getText().trim();
                    int qtd = Integer.parseInt(tfQtd.getText().trim());
                    double valor = Double.parseDouble(tfValor.getText().trim().replace(",", "."));
                    int paginas = Integer.parseInt(tfPaginas.getText().trim());

                    return new Livro(titulo, autor, genero, valor, data, qtd, false, paginas);
                } catch (Exception e) {
                    mostrarMensagemErro("Erro nos dados: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(livro -> {
            try {
                SessaoUsuario.getInstance().getLivroService().adicionarLivro(livro);
                carregarLivros();
                mostrarMensagemSucesso("Livro adicionado com sucesso!");
            } catch (Exception e) {
                mostrarMensagemErro("Erro ao adicionar: " + e.getMessage());
            }
        });
    }

    // ── Navegação ──────────────────────────────────────────────

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