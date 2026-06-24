package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Livro;
import br.edu.ufersa.locadora.exceptions.LivroException;
import br.edu.ufersa.locadora.model.entities.Usuario;
import javafx.event.ActionEvent;
import javafx.event.Event;
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

public class ArquivoLivroController implements Initializable {

    // ── Navbar ────────────────────────────────────────────────
    @FXML private ToggleButton navAcervo;
    @FXML private ToggleButton navAlugueis;
    @FXML private ToggleButton navCadastros;

    // ── Tabela ────────────────────────────────────────────────
    @FXML private ScrollPane scrollTabela;
    @FXML private VBox       listaLivros;
    @FXML private Button     btnAdicionar;

    // ── Formulário ────────────────────────────────────────────
    @FXML private VBox      painelForm;
    @FXML private Label     lblFormTitulo;
    @FXML private TextField tfTitulo;
    @FXML private TextField tfAutor;
    @FXML private TextField tfGenero;
    @FXML private TextField tfData;
    @FXML private TextField tfQtd;
    @FXML private TextField tfValor;
    @FXML private TextField tfPaginas;
    @FXML private Label     lblFormMsg;

    // ── Pesquisa ──────────────────────────────────────────────
    @FXML private TextField tfPesquisa;

    //faixa//
    @FXML private Button relatorioButton;
    @FXML private Button alugueisButton;
    @FXML private Button acervoButton;
    @FXML private Button cadastrosButton;
    @FXML private Button sairButton;
    // ── Estado ────────────────────────────────────────────────
    private boolean modoEdicao   = false;
    private Livro   livroEmEdicao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollTabela.setVisible(true);
        scrollTabela.setManaged(true);
        scrollTabela.setPrefHeight(400);
        removerLinhasPreview();

        boolean gerente = SessaoUsuario.getInstance().usuarioEhGerente();
        btnAdicionar.setVisible(gerente);
        btnAdicionar.setManaged(gerente);

        carregarLivros();
    }

    private void removerLinhasPreview() {
        try {
            VBox pai = (VBox) scrollTabela.getParent();
            int idxScroll = pai.getChildren().indexOf(scrollTabela);
            if (idxScroll > 1) pai.getChildren().remove(1, idxScroll);
        } catch (Exception ignored) {}
    }

    private void carregarLivros() {
        listaLivros.getChildren().clear();
        try {
            List<Livro> livros = SessaoUsuario.getInstance().getLivroService().lerLivro();
            for (Livro l : livros) {
                listaLivros.getChildren().add(criarLinha(l));
            }
        } catch (LivroException e) {
            listaLivros.getChildren().add(linhaVazia("Nenhum livro cadastrado."));
        }
    }

    private HBox criarLinha(Livro livro) {
        Label icone = new Label("🖼");
        icone.setStyle("-fx-font-size:20px; -fx-text-fill:#888888;");
        StackPane imgBox = new StackPane(icone);
        imgBox.setStyle("-fx-background-color:#C0B8B0; -fx-background-radius:4;-fx-min-width:60px; -fx-min-height:60px;");

        Label lTitulo = new Label(livro.getTitulo());
        lTitulo.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lTitulo, Priority.ALWAYS);
        lTitulo.setMaxWidth(Double.MAX_VALUE);

        String dataStr = livro.getDataDeLancamento().isEmpty() ? "—" : livro.getDataDeLancamento();
        Label lData = new Label(dataStr);
        lData.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        lData.setPrefWidth(120);

        Label lAutor = new Label(livro.getCriadoPor());
        lAutor.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lAutor, Priority.ALWAYS);
        lAutor.setMaxWidth(Double.MAX_VALUE);

        VBox acoes = new VBox(4);
        acoes.setAlignment(Pos.CENTER);
        acoes.setPrefWidth(56);

        if (SessaoUsuario.getInstance().usuarioEhGerente()) {
            Button btnEdit = new Button("✏");
            btnEdit.setStyle("-fx-background-color:transparent; -fx-text-fill:#2E1A47; -fx-font-size:15px; -fx-cursor:hand;");
            btnEdit.setOnAction(e -> editarLivro(livro));

            Button btnDel = new Button("🗑");
            btnDel.setStyle("-fx-background-color:transparent; -fx-text-fill:#2E1A47; -fx-font-size:15px; -fx-cursor:hand;");
            btnDel.setOnAction(e -> excluirLivro(livro));

            acoes.getChildren().addAll(btnEdit, btnDel);
        }

        HBox linha = new HBox(14, imgBox, lTitulo, lData, lAutor, acoes);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setStyle("-fx-background-color:#F8EED1; -fx-padding:8 16 8 16; -fx-min-height:80px; -fx-border-color:transparent transparent #D8C89A transparent; -fx-border-width:0 0 1 0;");
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

    // Changed to baseline Event class to accept both ActionEvent (Enter key) and MouseEvent (Icon click)
    @FXML
    public void pesquisar(Event e) {
        String termo = tfPesquisa.getText().trim();
        listaLivros.getChildren().clear();
        try {
            List<Livro> resultado = SessaoUsuario.getInstance().getLivroService().buscarPor("titulo", termo);
            if (resultado.isEmpty()) {
                listaLivros.getChildren().add(linhaVazia("Nenhum resultado para: " + termo));
            } else {
                resultado.forEach(l -> listaLivros.getChildren().add(criarLinha(l)));
            }
            if (termo.isEmpty()) carregarLivros();
        } catch (Exception ex) {
            mostrarErro("Erro na pesquisa: " + ex.getMessage());
        }
    }

    @FXML
    public void abrirFormNovo(ActionEvent e) {
        modoEdicao     = false;
        livroEmEdicao  = null;
        lblFormTitulo.setText("Novo Livro");
        limparForm();
        mostrarForm(true);
    }

    private void editarLivro(Livro l) {
        modoEdicao    = true;
        livroEmEdicao = l;
        lblFormTitulo.setText("Editar Livro");
        tfTitulo .setText(l.getTitulo());
        tfAutor  .setText(l.getCriadoPor());
        tfGenero .setText(l.getGenero());
        tfData   .setText(l.getDataDeLancamento());
        tfQtd    .setText(String.valueOf(l.getQtdItens()));

        // SAFE CONVERSION: Extracts numeric value to bypass potential locale string parsing errors
        tfValor  .setText(String.valueOf(l.getValor()));

        tfPaginas.setText(String.valueOf(l.getQtdPaginas()));
        lblFormMsg.setText("");
        mostrarForm(true);
    }

    private void excluirLivro(Livro l) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Excluir \"" + l.getTitulo() + "\"?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        a.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    SessaoUsuario.getInstance().getLivroService().apagarLivro(l.getID());
                    carregarLivros();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
                }
            }
        });
    }

    @FXML
    public void salvarLivro(ActionEvent e) {
        lblFormMsg.setText("");
        try {
            String titulo  = tfTitulo .getText().trim();
            String autor   = tfAutor  .getText().trim();
            String genero  = tfGenero .getText().trim();
            String data    = tfData   .getText().trim();
            int    qtd     = Integer.parseInt(tfQtd    .getText().trim());
            double valor   = Double.parseDouble(tfValor.getText().trim().replace(",", "."));
            int    paginas = Integer.parseInt(tfPaginas.getText().trim());

            if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || data.isEmpty())
                throw new IllegalArgumentException("Preencha todos os campos obrigatórios.");

            if (modoEdicao && livroEmEdicao != null) {
                livroEmEdicao.setTitulo(titulo);
                livroEmEdicao.setCriadoPor(autor);
                livroEmEdicao.setGenero(genero);
                livroEmEdicao.setDataDeLancamento(data);
                livroEmEdicao.setQtdItens(qtd);
                livroEmEdicao.setValor(valor);
                livroEmEdicao.setQtdPaginas(paginas);
                SessaoUsuario.getInstance().getLivroService().atualizarLivro(livroEmEdicao);
            } else {
                Livro novo = new Livro(titulo, autor, genero, valor, data, qtd, false, paginas);
                SessaoUsuario.getInstance().getLivroService().adicionarLivro(novo);
            }
            carregarLivros();
            mostrarForm(false);

        } catch (NumberFormatException ex) {
            lblFormMsg.setText("Qtd, Valor e Páginas devem ser numéricos.");
        } catch (Exception ex) {
            lblFormMsg.setText("Erro: " + ex.getMessage());
        }
    }

    @FXML public void fecharForm(ActionEvent e) { mostrarForm(false); }

    @FXML
    public void navegarDiscos(javafx.scene.input.MouseEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufersa/locadora/view/ArquivoDisco.fxml"));
            Stage stage = (Stage) scrollTabela.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
        }
    }

    @FXML
    public void handleLogout(ActionEvent e) {
        SessaoUsuario.getInstance().limparSessao();
        irPara("login.fxml", e);
    }

    private void mostrarForm(boolean v) {
        painelForm.setVisible(v);
        painelForm.setManaged(v);
    }

    private void limparForm() {
        tfTitulo.clear(); tfAutor.clear();  tfGenero.clear();
        tfData.clear();   tfQtd.clear();    tfValor.clear();
        tfPaginas.clear(); lblFormMsg.setText("");
    }

    private void irPara(String fxml, ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufersa/locadora/view/" + fxml));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Erro ao navegar: " + ex.getMessage()).showAndWait();
        }
    }

    private void mostrarErro(String mensagem) {
        new Alert(Alert.AlertType.ERROR, mensagem).showAndWait();
    }

    //faixa
    @FXML
    private void aoRelatorio() { NavigationHelper.goTo(relatorioButton, "Financas.fxml"); }
    @FXML
    private void aoAlugueis() {
        NavigationHelper.goTo(alugueisButton, "aluguel.fxml");
    }
    @FXML
    private void aoAcervo() {}
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


