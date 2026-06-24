package br.edu.ufersa.locadora.controllers;
import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.exceptions.DiscoException;
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


public class ArquivoDiscoController implements Initializable {

    // ── Navbar ────────────────────────────────────────────────
    @FXML private ToggleButton navAcervo;
    @FXML private ToggleButton navAlugueis;
    @FXML private ToggleButton navCadastros;

    // ── Tabela ────────────────────────────────────────────────
    @FXML private ScrollPane scrollTabela;
    @FXML private VBox       listaDiscos;
    @FXML private Button     btnAdicionar;

    // ── Formulário ────────────────────────────────────────────
    @FXML private VBox      painelForm;
    @FXML private Label     lblFormTitulo;
    @FXML private TextField tfTitulo;
    @FXML private TextField tfBanda;
    @FXML private TextField tfEstilo;
    @FXML private TextField tfData;
    @FXML private TextField tfQtd;
    @FXML private TextField tfValor;
    @FXML private TextField tfDuracao;
    @FXML private Label     lblFormMsg;

    // ── Pesquisa ──────────────────────────────────────────────
    @FXML private TextField tfPesquisa;

    // ── Estado interno ────────────────────────────────────────
    private boolean modoEdicao = false;
    private Disco   discoEmEdicao;

    // ─────────────────────────────────────────────────────────
    //faixa
    @FXML private Button relatorioButton;
    @FXML private Button alugueisButton;
    @FXML private Button acervoButton;
    @FXML private Button cadastrosButton;
    @FXML private Button sairButton;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ativa scroll dinâmico e remove linhas de preview
        scrollTabela.setVisible(true);
        scrollTabela.setManaged(true);
        scrollTabela.setPrefHeight(400);
        removerLinhasPreview();

        // Gerente pode adicionar; funcionário só consulta
        boolean gerente = SessaoUsuario.getInstance().usuarioEhGerente();
        btnAdicionar.setVisible(gerente);
        btnAdicionar.setManaged(gerente);

        carregarDiscos();
    }

    // ── Remoção das linhas de preview (Scene Builder) ─────────

    private void removerLinhasPreview() {
        try {
            VBox pai = (VBox) scrollTabela.getParent();
            int idxScroll = pai.getChildren().indexOf(scrollTabela);
            // índice 0 = cabeçalho amarelo; 1..idxScroll-1 = linhas de preview
            if (idxScroll > 1) pai.getChildren().remove(1, idxScroll);
        } catch (Exception ignored) {}
    }

    // ── Carregamento de dados ─────────────────────────────────

    private void carregarDiscos() {
        listaDiscos.getChildren().clear();
        try {
            List<Disco> discos = SessaoUsuario.getInstance()
                    .getDiscoService().lerDisco();
            for (Disco d : discos)
                listaDiscos.getChildren().add(criarLinha(d));
        } catch (DiscoException e) {
            listaDiscos.getChildren().add(linhaVazia("Nenhum disco cadastrado."));
        }
    }

    // ── Construção das linhas ─────────────────────────────────

    private HBox criarLinha(Disco disco) {

        // Placeholder de imagem (quadrado cinza com ícone)
        Label icone = new Label("🖼");
        icone.setStyle("-fx-font-size:20px; -fx-text-fill:#888888;");
        StackPane imgBox = new StackPane(icone);
        imgBox.setStyle("-fx-background-color:#C8C0C0; -fx-background-radius:4;" +
                "-fx-min-width:60px; -fx-min-height:60px;" +
                "-fx-pref-width:60px; -fx-pref-height:60px;");

        // Colunas de texto
        Label lTitulo = new Label(disco.getTitulo());
        lTitulo.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lTitulo, Priority.ALWAYS);
        lTitulo.setMaxWidth(Double.MAX_VALUE);

        Label lData = new Label(disco.getDataDeLancamento().isEmpty()
                ? "—" : disco.getDataDeLancamento());
        lData.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        lData.setPrefWidth(120);

        Label lBanda = new Label(disco.getCriadoPor());
        lBanda.setStyle("-fx-font-size:14px; -fx-text-fill:#2E1A47;");
        HBox.setHgrow(lBanda, Priority.ALWAYS);
        lBanda.setMaxWidth(Double.MAX_VALUE);

        // Botões de ação (só para gerente)
        VBox acoes = new VBox(4);
        acoes.setAlignment(Pos.CENTER);
        acoes.setPrefWidth(56);
        if (SessaoUsuario.getInstance().usuarioEhGerente()) {
            Button btnEdit = new Button("✏");
            btnEdit.setStyle("-fx-background-color:transparent; -fx-text-fill:#2E1A47;" +
                    "-fx-font-size:15px; -fx-cursor:hand; -fx-padding:2 6 2 6;");
            btnEdit.setOnAction(e -> editarDisco(disco));

            Button btnDel = new Button("🗑");
            btnDel.setStyle("-fx-background-color:transparent; -fx-text-fill:#2E1A47;" +
                    "-fx-font-size:15px; -fx-cursor:hand; -fx-padding:2 6 2 6;");
            btnDel.setOnAction(e -> excluirDisco(disco));

            acoes.getChildren().addAll(btnEdit, btnDel);
        }

        // Linha completa
        HBox linha = new HBox(14, imgBox, lTitulo, lData, lBanda, acoes);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setStyle("-fx-background-color:#F8EED1; -fx-padding:8 16 8 16;" +
                "-fx-min-height:80px; -fx-pref-height:80px;" +
                "-fx-border-color:transparent transparent #D8C89A transparent;" +
                "-fx-border-width:0 0 1 0;");
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

    // ── Pesquisa ──────────────────────────────────────────────

    @FXML
    public void pesquisar(ActionEvent e) {
        String termo = tfPesquisa.getText().trim();
        listaDiscos.getChildren().clear();
        List<Disco> resultado = SessaoUsuario.getInstance()
                .getDiscoService().buscarPor("titulo", termo);
        if (resultado.isEmpty())
            listaDiscos.getChildren().add(linhaVazia("Nenhum resultado para: " + termo));
        else
            resultado.forEach(d -> listaDiscos.getChildren().add(criarLinha(d)));

        if (termo.isEmpty()) carregarDiscos();
    }

    // ── Formulário: abrir ─────────────────────────────────────

    @FXML
    public void abrirFormNovo(ActionEvent e) {
        modoEdicao    = false;
        discoEmEdicao = null;
        lblFormTitulo.setText("Novo Disco");
        limparForm();
        mostrarForm(true);
    }

    private void editarDisco(Disco d) {
        modoEdicao    = true;
        discoEmEdicao = d;
        lblFormTitulo.setText("Editar Disco");
        tfTitulo .setText(d.getTitulo());
        tfBanda  .setText(d.getCriadoPor());
        tfEstilo .setText(d.getGenero());
        tfData   .setText(d.getDataDeLancamento());
        tfQtd    .setText(String.valueOf(d.getQtdItens()));
        tfValor  .setText(d.getValorFormatado());
        tfDuracao.setText(String.valueOf(d.getDuracao()));
        lblFormMsg.setText("");
        mostrarForm(true);
    }

    private void excluirDisco(Disco d) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir o disco \"" + d.getTitulo() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    SessaoUsuario.getInstance().getDiscoService().apagarDisco(d.getID());
                    carregarDiscos();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
                }
            }
        });
    }

    // ── Formulário: salvar / fechar ───────────────────────────

    @FXML
    public void salvarDisco(ActionEvent e) {
        lblFormMsg.setText("");
        try {
            String titulo  = tfTitulo .getText().trim();
            String banda   = tfBanda  .getText().trim();
            String estilo  = tfEstilo .getText().trim();
            String data    = tfData   .getText().trim();
            int    qtd     = Integer.parseInt(tfQtd    .getText().trim());
            double valor   = Double.parseDouble(tfValor.getText().trim().replace(",", "."));
            int    durSeg  = Integer.parseInt(tfDuracao.getText().trim());

            if (titulo.isEmpty() || banda.isEmpty() || estilo.isEmpty() || data.isEmpty())
                throw new IllegalArgumentException("Preencha todos os campos obrigatórios.");

            int h = durSeg / 3600, m = (durSeg % 3600) / 60, s = durSeg % 60;

            if (modoEdicao && discoEmEdicao != null) {
                discoEmEdicao.setTitulo(titulo);
                discoEmEdicao.setCriadoPor(banda);
                discoEmEdicao.setGenero(estilo);
                discoEmEdicao.setDataDeLancamento(data);
                discoEmEdicao.setQtdItens(qtd);
                discoEmEdicao.setValor(valor);
                discoEmEdicao.setDuracaoSegundos(durSeg);
                SessaoUsuario.getInstance().getDiscoService().atualizarDisco(discoEmEdicao);
            } else {
                Disco novo = new Disco(titulo, banda, estilo, valor, data, qtd, true, h, m, s);
                SessaoUsuario.getInstance().getDiscoService().adicionarDisco(novo);
            }

            carregarDiscos();
            mostrarForm(false);

        } catch (NumberFormatException ex) {
            lblFormMsg.setText("Qtd, Valor e Duração devem ser numéricos.");
        } catch (Exception ex) {
            lblFormMsg.setText("Erro: " + ex.getMessage());
        }
    }

    @FXML public void fecharForm(ActionEvent e) { mostrarForm(false); }

    // ── Navegação — Navbar ────────────────────────────────────

    @FXML public void handleNavAcervo(ActionEvent e)    { /* já estamos aqui */ }
    @FXML public void handleNavRelatorio(ActionEvent e) { irPara("financas.fxml", e); }
    @FXML public void handleNavCadastros(ActionEvent e) { irPara("cadastros.fxml", e); }

    // ── Navegação — Abas ──────────────────────────────────────

    @FXML public void navegarLivros(javafx.scene.input.MouseEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/locadora/view/ArquivoLivro.fxml"));
            Stage stage = (Stage) scrollTabela.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
        }
    }

    @FXML public void navegarDiscos(javafx.scene.input.MouseEvent e) { /* já estamos aqui */ }

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
        tfTitulo.clear(); tfBanda.clear(); tfEstilo.clear();
        tfData.clear();   tfQtd.clear();   tfValor.clear();
        tfDuracao.clear(); lblFormMsg.setText("");
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

    //faixa
    @FXML
    private void aoRelatorio() { NavigationHelper.goTo(relatorioButton, "Financas.fxml"); }
    @FXML
    private void aoAlugeuis() {
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