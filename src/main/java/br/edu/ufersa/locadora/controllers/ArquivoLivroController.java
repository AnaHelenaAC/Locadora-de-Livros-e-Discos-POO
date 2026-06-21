package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.model.entities.ItemAcervo;
import br.edu.ufersa.locadora.model.entities.Livro;
import br.edu.ufersa.locadora.exceptions.LivroException;
import br.edu.ufersa.locadora.exceptions.DiscoException;
import javafx.collections.FXCollections;
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

public class ArquivoLivroController implements Initializable {

    // ── Navegação ─────────────────────────────────────────────
    @FXML private Button btnNavAcervo;
    @FXML private Button btnNavRelatorio;
    @FXML private Button btnNavCadastros;

    // ── Grade de cards ────────────────────────────────────────
    @FXML private FlowPane painelLivros;
    @FXML private FlowPane painelDiscos;

    // ── Formulário lateral ────────────────────────────────────
    @FXML private VBox        painelForm;
    @FXML private Label       lblFormTitulo;
    @FXML private RadioButton rbLivro;
    @FXML private RadioButton rbDisco;
    @FXML private TextField   tfTitulo;
    @FXML private TextField   tfCriadoPor;
    @FXML private TextField   tfGenero;
    @FXML private TextField   tfData;
    @FXML private TextField   tfQtd;
    @FXML private TextField   tfValor;
    @FXML private Label       lblCampoExtra;
    @FXML private TextField   tfCampoExtra;
    @FXML private Label       lblFormMsg;

    // ── Barra inferior ────────────────────────────────────────
    @FXML private TextField        tfPesquisa;
    @FXML private Button           btnAdicionar;

    // ── Estado interno ────────────────────────────────────────
    private final ToggleGroup tipoGroup = new ToggleGroup();
    private boolean modoEdicao   = false;
    private boolean editandoLivro = true;
    private Livro   livroEmEdicao;
    private Disco   discoEmEdicao;

    // ─────────────────────────────────────────────────────────
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rbLivro.setToggleGroup(tipoGroup);
        rbDisco.setToggleGroup(tipoGroup);

        // Botão adicionar só aparece para gerente
        boolean gerente = SessaoUsuario.getInstance().usuarioEhGerente();
        btnAdicionar.setVisible(gerente);
        btnAdicionar.setManaged(gerente);

        carregarLivros();
        carregarDiscos();
    }

    // ── Carregamento ──────────────────────────────────────────

    private void carregarLivros() {
        painelLivros.getChildren().clear();
        try {
            List<Livro> livros = SessaoUsuario.getInstance().getLivroService().lerLivro();
            for (Livro l : livros) painelLivros.getChildren().add(criarCard(l));
        } catch (LivroException e) {
            painelLivros.getChildren().add(labelVazio("Nenhum livro cadastrado."));
        }
    }

    private void carregarDiscos() {
        painelDiscos.getChildren().clear();
        try {
            List<Disco> discos = SessaoUsuario.getInstance().getDiscoService().lerDisco();
            for (Disco d : discos) painelDiscos.getChildren().add(criarCard(d));
        } catch (DiscoException e) {
            painelDiscos.getChildren().add(labelVazio("Nenhum disco cadastrado."));
        }
    }

    // ── Construção dos cards ──────────────────────────────────

    /**
     * Cria um card visual idêntico ao design:
     * fundo roxo escuro, capa cinza, textos brancos/lilás, botões editar/lixeira.
     */
    private VBox criarCard(ItemAcervo item) {

        // --- capa (área cinza com ícone) ---
        Label icone = new Label("🖼");
        icone.setStyle("-fx-font-size: 22px; -fx-text-fill: #AAAAAA;");

        StackPane capa = new StackPane(icone);
        capa.setStyle("-fx-background-color: #8E8E8E; -fx-background-radius: 6;");
        capa.setPrefSize(94, 110);
        capa.setMinSize(94, 110);

        // --- textos ---
        Label lblTitulo = new Label(item.getTitulo());
        lblTitulo.setStyle("-fx-text-fill: #F8EED1; -fx-font-size: 11px;"
                + "-fx-font-weight: bold; -fx-wrap-text: true; -fx-alignment: center;");
        lblTitulo.setMaxWidth(100);
        lblTitulo.setWrapText(true);
        lblTitulo.setAlignment(Pos.CENTER);

        String dataTexto = item.getDataDeLancamento().isEmpty() ? "—" : item.getDataDeLancamento();
        Label lblData = new Label(dataTexto);
        lblData.setStyle("-fx-text-fill: #C8B8E8; -fx-font-size: 10px;");

        Label lblAutor = new Label(item.getCriadoPor());
        lblAutor.setStyle("-fx-text-fill: #C8B8E8; -fx-font-size: 10px;");
        lblAutor.setMaxWidth(100);
        lblAutor.setWrapText(true);
        lblAutor.setAlignment(Pos.CENTER);

        // --- botões ação ---
        HBox acoes = new HBox(8);
        acoes.setAlignment(Pos.CENTER);
        acoes.setStyle("-fx-padding: 4 0 0 0;");

        if (SessaoUsuario.getInstance().usuarioEhGerente()) {
            Button btnEdit = new Button("✏");
            btnEdit.setStyle("-fx-background-color: transparent; -fx-text-fill: #F2D888;"
                    + "-fx-font-size: 15px; -fx-cursor: hand; -fx-padding: 2 6 2 6;"
                    + "-fx-background-radius: 4;");
            btnEdit.setOnAction(e -> editarItem(item));

            Button btnDel = new Button("🗑");
            btnDel.setStyle("-fx-background-color: transparent; -fx-text-fill: #E05060;"
                    + "-fx-font-size: 15px; -fx-cursor: hand; -fx-padding: 2 6 2 6;"
                    + "-fx-background-radius: 4;");
            btnDel.setOnAction(e -> excluirItem(item));

            acoes.getChildren().addAll(btnEdit, btnDel);
        }

        // --- montagem do card ---
        VBox card = new VBox(5, capa, lblTitulo, lblData, lblAutor, acoes);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(114);
        card.setMinWidth(114);
        card.setMaxWidth(114);
        card.setStyle("-fx-background-color: #3D2460; -fx-background-radius: 10;"
                + "-fx-padding: 10 8 8 8;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.30), 8, 0, 1, 3);"
                + "-fx-cursor: hand;");

        return card;
    }

    private Label labelVazio(String msg) {
        Label l = new Label(msg);
        l.setStyle("-fx-text-fill: #9A8A7A; -fx-font-size: 13px;"
                + "-fx-font-style: italic; -fx-padding: 20;");
        return l;
    }

    // ── Pesquisa ──────────────────────────────────────────────

    @FXML
    public void pesquisar(ActionEvent e) {
        String termo = tfPesquisa.getText().trim();

        painelLivros.getChildren().clear();
        List<Livro> livros = SessaoUsuario.getInstance()
                .getLivroService().buscarPor("titulo", termo);
        if (livros.isEmpty()) painelLivros.getChildren().add(labelVazio("Nenhum resultado."));
        else livros.forEach(l -> painelLivros.getChildren().add(criarCard(l)));

        painelDiscos.getChildren().clear();
        List<Disco> discos = SessaoUsuario.getInstance()
                .getDiscoService().buscarPor("titulo", termo);
        if (discos.isEmpty()) painelDiscos.getChildren().add(labelVazio("Nenhum resultado."));
        else discos.forEach(d -> painelDiscos.getChildren().add(criarCard(d)));

        if (termo.isEmpty()) { carregarLivros(); carregarDiscos(); }
    }

    // ── Formulário: abrir / tipo ──────────────────────────────

    @FXML
    public void abrirFormNovo(ActionEvent e) {
        modoEdicao    = false;
        editandoLivro = rbLivro.isSelected();
        livroEmEdicao = null;
        discoEmEdicao = null;
        limparForm();
        atualizarLabels();
        mostrarForm(true);
    }

    @FXML
    public void onTipoSelecionado(ActionEvent e) {
        editandoLivro = rbLivro.isSelected();
        atualizarLabels();
    }

    private void atualizarLabels() {
        if (editandoLivro) {
            lblFormTitulo.setText(modoEdicao ? "Editar Livro" : "Novo Livro");
            lblCampoExtra.setText("Qtd. de páginas:");
            btnAdicionar.setText("Adicionar novo livro ＋");
        } else {
            lblFormTitulo.setText(modoEdicao ? "Editar Disco" : "Novo Disco");
            lblCampoExtra.setText("Duração (segundos totais):");
            btnAdicionar.setText("Adicionar novo disco ＋");
        }
    }

    private void editarItem(ItemAcervo item) {
        modoEdicao = true;
        tfTitulo.setText(item.getTitulo());
        tfCriadoPor.setText(item.getCriadoPor());
        tfGenero.setText(item.getGenero());
        tfData.setText(item.getDataDeLancamento());
        tfQtd.setText(String.valueOf(item.getQtdItens()));
        tfValor.setText(item.getValorFormatado());

        if (item instanceof Livro l) {
            editandoLivro = true; livroEmEdicao = l;
            rbLivro.setSelected(true);
            tfCampoExtra.setText(String.valueOf(l.getQtdPaginas()));
        } else if (item instanceof Disco d) {
            editandoLivro = false; discoEmEdicao = d;
            rbDisco.setSelected(true);
            tfCampoExtra.setText(String.valueOf(d.getDuracao()));
        }
        lblFormMsg.setText("");
        atualizarLabels();
        mostrarForm(true);
    }

    private void excluirItem(ItemAcervo item) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Confirma a exclusão de \"" + item.getTitulo() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    if (item instanceof Livro l) {
                        SessaoUsuario.getInstance().getLivroService().apagarLivro(l.getID());
                        carregarLivros();
                    } else if (item instanceof Disco d) {
                        SessaoUsuario.getInstance().getDiscoService().apagarDisco(d.getID());
                        carregarDiscos();
                    }
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
                }
            }
        });
    }

    // ── Formulário: salvar / fechar ───────────────────────────

    @FXML
    public void salvarItem(ActionEvent e) {
        lblFormMsg.setText("");
        try {
            String titulo  = tfTitulo.getText().trim();
            String criador = tfCriadoPor.getText().trim();
            String genero  = tfGenero.getText().trim();
            String data    = tfData.getText().trim();
            int    qtd     = Integer.parseInt(tfQtd.getText().trim());
            double valor   = Double.parseDouble(tfValor.getText().trim().replace(",", "."));
            int    extra   = Integer.parseInt(tfCampoExtra.getText().trim());

            if (titulo.isEmpty() || criador.isEmpty() || genero.isEmpty() || data.isEmpty())
                throw new IllegalArgumentException("Preencha todos os campos.");

            if (editandoLivro) {
                if (modoEdicao && livroEmEdicao != null) {
                    livroEmEdicao.setTitulo(titulo); livroEmEdicao.setCriadoPor(criador);
                    livroEmEdicao.setGenero(genero); livroEmEdicao.setDataDeLancamento(data);
                    livroEmEdicao.setQtdItens(qtd); livroEmEdicao.setValor(valor);
                    livroEmEdicao.setQtdPaginas(extra);
                    SessaoUsuario.getInstance().getLivroService().atualizarLivro(livroEmEdicao);
                } else {
                    SessaoUsuario.getInstance().getLivroService()
                            .adicionarLivro(new Livro(titulo, criador, genero, valor, data, qtd, false, extra));
                }
                carregarLivros();
            } else {
                if (modoEdicao && discoEmEdicao != null) {
                    discoEmEdicao.setTitulo(titulo); discoEmEdicao.setCriadoPor(criador);
                    discoEmEdicao.setGenero(genero); discoEmEdicao.setDataDeLancamento(data);
                    discoEmEdicao.setQtdItens(qtd); discoEmEdicao.setValor(valor);
                    discoEmEdicao.setDuracaoSegundos(extra);
                    SessaoUsuario.getInstance().getDiscoService().atualizarDisco(discoEmEdicao);
                } else {
                    int h = extra / 3600, m = (extra % 3600) / 60, s = extra % 60;
                    SessaoUsuario.getInstance().getDiscoService()
                            .adicionarDisco(new Disco(titulo, criador, genero, valor, data, qtd, true, h, m, s));
                }
                carregarDiscos();
            }
            mostrarForm(false);

        } catch (NumberFormatException ex) {
            lblFormMsg.setText("Qtd, Valor e páginas/duração devem ser numéricos.");
        } catch (Exception ex) {
            lblFormMsg.setText("Erro: " + ex.getMessage());
        }
    }

    @FXML public void fecharForm(ActionEvent e) { mostrarForm(false); }

    // ── Navegação ─────────────────────────────────────────────

    @FXML public void navegarAcervo(ActionEvent e)    { /* já estamos aqui */ }
    @FXML public void navegarRelatorio(ActionEvent e) { irPara("relatorio.fxml", e); }
    @FXML public void navegarCadastros(ActionEvent e) { irPara("cadastros.fxml",  e); }

    @FXML
    public void handleLogout(ActionEvent e) {
        SessaoUsuario.getInstance().limparSessao();
        irPara("login.fxml", e);
    }

    // ── Helpers ───────────────────────────────────────────────

    private void mostrarForm(boolean v) {
        painelForm.setVisible(v);
        painelForm.setManaged(v);
    }

    private void limparForm() {
        tfTitulo.clear(); tfCriadoPor.clear(); tfGenero.clear();
        tfData.clear();   tfQtd.clear();       tfValor.clear();
        tfCampoExtra.clear(); lblFormMsg.setText("");
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