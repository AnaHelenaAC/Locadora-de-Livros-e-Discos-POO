package br.edu.ufersa.locadora.controllers;
import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Livro;
import br.edu.ufersa.locadora.exceptions.LivroException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class TabelaLivroController implements Initializable {

    @FXML private ScrollPane scrollTabela;
    @FXML private VBox       listaLivros;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollTabela.setVisible(true);
        scrollTabela.setManaged(true);
        scrollTabela.setPrefHeight(400);
        removerLinhasPreview();
        carregarLivros();
    }

    // ── Preview cleanup ───────────────────────────────────────

    private void removerLinhasPreview() {
        try {
            VBox pai = (VBox) scrollTabela.getParent();
            int idxScroll = pai.getChildren().indexOf(scrollTabela);
            // índice 0 = cabeçalho amarelo; 1..idxScroll-1 = previews
            if (idxScroll > 1) pai.getChildren().remove(1, idxScroll);
        } catch (Exception ignored) {}
    }

    // ── Carregamento ──────────────────────────────────────────

    public void carregarLivros() {
        listaLivros.getChildren().clear();
        try {
            List<Livro> livros = SessaoUsuario.getInstance()
                    .getLivroService().lerLivro();
            if (livros.isEmpty()) {
                listaLivros.getChildren().add(linhaVazia("Nenhum livro cadastrado."));
            } else {
                for (Livro l : livros)
                    listaLivros.getChildren().add(criarLinha(l));
            }
        } catch (LivroException e) {
            listaLivros.getChildren().add(linhaVazia("Nenhum livro encontrado."));
        }
    }

    public void recarregar() { carregarLivros(); }

    // ── Construção da linha ───────────────────────────────────
    private HBox criarLinha(Livro livro) {

        // Placeholder de imagem (quadrado cinza 60×60)
        Label icone = new Label("🖼");
        icone.setStyle("-fx-font-size:20px; -fx-text-fill:#888888;");
        StackPane imgBox = new StackPane(icone);
        imgBox.setStyle(
                "-fx-background-color:#C0B8B0;" +
                        "-fx-background-radius:4;" +
                        "-fx-min-width:60px; -fx-min-height:60px;" +
                        "-fx-pref-width:60px; -fx-pref-height:60px;"
        );

        // Título
        Label lTitulo = new Label(livro.getTitulo());
        lTitulo.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lTitulo, Priority.ALWAYS);
        lTitulo.setMaxWidth(Double.MAX_VALUE);

        // Data
        String dataStr = livro.getDataDeLancamento().isEmpty()
                ? "—" : livro.getDataDeLancamento();
        Label lData = new Label(dataStr);
        lData.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        lData.setPrefWidth(120);

        // Autor(a)
        Label lAutor = new Label(livro.getCriadoPor());
        lAutor.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lAutor, Priority.ALWAYS);
        lAutor.setMaxWidth(Double.MAX_VALUE);

        // Botões de ação
        VBox acoes = new VBox(4);
        acoes.setAlignment(Pos.CENTER);
        acoes.setPrefWidth(56);

        if (SessaoUsuario.getInstance().usuarioEhGerente()) {
            Button btnEdit = new Button("✏");
            btnEdit.setStyle(
                    "-fx-background-color:transparent; -fx-text-fill:#2E1A47;" +
                            "-fx-font-size:15px; -fx-cursor:hand; -fx-padding:2 6 2 6;"
            );
            btnEdit.setOnAction(e -> onEditar(livro));

            Button btnDel = new Button("🗑");
            btnDel.setStyle(
                    "-fx-background-color:transparent; -fx-text-fill:#2E1A47;" +
                            "-fx-font-size:15px; -fx-cursor:hand; -fx-padding:2 6 2 6;"
            );
            btnDel.setOnAction(e -> onExcluir(livro));

            acoes.getChildren().addAll(btnEdit, btnDel);
        }

        HBox linha = new HBox(14, imgBox, lTitulo, lData, lAutor, acoes);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setStyle(
                "-fx-background-color:#F8EED1;" +
                        "-fx-padding:8 16 8 16;" +
                        "-fx-min-height:80px; -fx-pref-height:80px;" +
                        "-fx-border-color:transparent transparent #D8C89A transparent;" +
                        "-fx-border-width:0 0 1 0;"
        );
        return linha;
    }

    private HBox linhaVazia(String msg) {
        Label l = new Label(msg);
        l.setStyle("-fx-text-fill:#9A8A7A; -fx-font-style:italic; -fx-font-size:13px;");
        HBox h = new HBox(l);
        h.setStyle("-fx-padding:24; -fx-background-color:#F8EED1;");
        h.setAlignment(Pos.CENTER);
        return h;
    }

    // ── Ações de linha ────────────────────────────────────────

    private void onEditar(Livro livro) {
        TextInputDialog dlg = new TextInputDialog(livro.getTitulo());
        dlg.setTitle("Editar Livro");
        dlg.setHeaderText("Novo título:");
        dlg.showAndWait().ifPresent(novoTitulo -> {
            if (!novoTitulo.isBlank()) {
                try {
                    livro.setTitulo(novoTitulo);
                    SessaoUsuario.getInstance().getLivroService().atualizarLivro(livro);
                    carregarLivros();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
                }
            }
        });
    }

    private void onExcluir(Livro livro) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir \"" + livro.getTitulo() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    SessaoUsuario.getInstance().getLivroService().apagarLivro(livro.getID());
                    carregarLivros();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
                }
            }
        });
    }
}