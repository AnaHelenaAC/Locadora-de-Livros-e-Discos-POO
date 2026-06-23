package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.exceptions.DiscoException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TabelaDiscoController implements Initializable {

    @FXML private ScrollPane scrollTabela;
    @FXML private VBox       listaDiscos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ativa scroll e remove as 3 linhas de preview do Scene Builder
        scrollTabela.setVisible(true);
        scrollTabela.setManaged(true);
        scrollTabela.setPrefHeight(400);
        removerLinhasPreview();
        carregarDiscos();
    }

    // ── Preview cleanup ───────────────────────────────────────

    private void removerLinhasPreview() {
        try {
            // O VBox raiz é o pai direto do scrollTabela
            VBox pai = (VBox) scrollTabela.getParent();
            int idxScroll = pai.getChildren().indexOf(scrollTabela);
            // índice 0 = cabeçalho; 1..idxScroll-1 = linhas de preview
            if (idxScroll > 1) pai.getChildren().remove(1, idxScroll);
        } catch (Exception ignored) {}
    }

    // ── Carregamento ──────────────────────────────────────────

    public void carregarDiscos() {
        listaDiscos.getChildren().clear();
        try {
            List<Disco> discos = SessaoUsuario.getInstance()
                    .getDiscoService().lerDisco();
            if (discos.isEmpty()) {
                listaDiscos.getChildren().add(linhaVazia("Nenhum disco cadastrado."));
            } else {
                for (Disco d : discos)
                    listaDiscos.getChildren().add(criarLinha(d));
            }
        } catch (DiscoException e) {
            listaDiscos.getChildren().add(linhaVazia("Nenhum disco encontrado."));
        }
    }

    public void recarregar() { carregarDiscos(); }


    private HBox criarLinha(Disco disco) {

        // ── Placeholder de imagem (quadrado cinza 60×60) ──────
        Label icone = new Label("🖼");
        icone.setStyle("-fx-font-size:20px; -fx-text-fill:#888888;");
        StackPane imgBox = new StackPane(icone);
        imgBox.setStyle(
                "-fx-background-color:#C8C0C0;" +
                        "-fx-background-radius:4;" +
                        "-fx-min-width:60px; -fx-min-height:60px;" +
                        "-fx-pref-width:60px; -fx-pref-height:60px;"
        );

        // ── Colunas de texto ──────────────────────────────────
        Label lTitulo = new Label(disco.getTitulo());
        lTitulo.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lTitulo, Priority.ALWAYS);
        lTitulo.setMaxWidth(Double.MAX_VALUE);

        String dataStr = disco.getDataDeLancamento().isEmpty()
                ? "—" : disco.getDataDeLancamento();
        Label lData = new Label(dataStr);
        lData.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        lData.setPrefWidth(120);

        Label lBanda = new Label(disco.getCriadoPor());
        lBanda.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lBanda, Priority.ALWAYS);
        lBanda.setMaxWidth(Double.MAX_VALUE);

        // ── Botões de ação ────────────────────────────────────
        VBox acoes = new VBox(4);
        acoes.setAlignment(Pos.CENTER);
        acoes.setPrefWidth(56);

        if (SessaoUsuario.getInstance().usuarioEhGerente()) {
            Button btnEdit = new Button("✏");
            btnEdit.setStyle(
                    "-fx-background-color:transparent; -fx-text-fill:#2E1A47;" +
                            "-fx-font-size:15px; -fx-cursor:hand; -fx-padding:2 6 2 6;"
            );
            btnEdit.setOnAction(e -> onEditar(disco));

            Button btnDel = new Button("🗑");
            btnDel.setStyle(
                    "-fx-background-color:transparent; -fx-text-fill:#2E1A47;" +
                            "-fx-font-size:15px; -fx-cursor:hand; -fx-padding:2 6 2 6;"
            );
            btnDel.setOnAction(e -> onExcluir(disco));

            acoes.getChildren().addAll(btnEdit, btnDel);
        }

        // ── Linha final ───────────────────────────────────────
        HBox linha = new HBox(14, imgBox, lTitulo, lData, lBanda, acoes);
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


    private void onEditar(Disco disco) {
        TextInputDialog dlg = new TextInputDialog(disco.getTitulo());
        dlg.setTitle("Editar Disco");
        dlg.setHeaderText("Novo título:");
        dlg.showAndWait().ifPresent(novoTitulo -> {
            if (!novoTitulo.isBlank()) {
                try {
                    disco.setTitulo(novoTitulo);
                    SessaoUsuario.getInstance().getDiscoService().atualizarDisco(disco);
                    carregarDiscos();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
                }
            }
        });
    }

    private void onExcluir(Disco disco) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir \"" + disco.getTitulo() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    SessaoUsuario.getInstance().getDiscoService().apagarDisco(disco.getID());
                    carregarDiscos();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
                }
            }
        });
    }
}