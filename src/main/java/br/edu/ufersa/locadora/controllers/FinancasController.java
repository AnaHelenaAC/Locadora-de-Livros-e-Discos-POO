package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.exceptions.RegistroException;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

// Controlador responsável pela tela de finanças e calendário
public class FinancasController implements Initializable {

    @FXML private Label      lblAno;
    @FXML private Label      lblLucroAnual;
    @FXML private GridPane   gridCalendario;
    @FXML private TextField  tfBusca;
    @FXML private TextField  tfPesquisaBottom;
    @FXML private Label      lblDataHoje;

    private int anoAtual;

    @FXML private Button relatorioButton;
    @FXML private Button alugueisButton;
    @FXML private Button acervoButton;
    @FXML private Button cadastrosButton;
    @FXML private Button sairButton;
    @FXML private Button clienteButton;

    // Dias da semana abreviados
    private static final String[] DIAS = {"DO", "SE", "TE", "QA", "QI", "SE", "SA"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anoAtual = LocalDate.now().getYear();
        lblDataHoje.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        renderizarAno();
    }

    // Navegação de ano
    @FXML public void anoAnterior(ActionEvent e)  { anoAtual--; renderizarAno(); }
    @FXML public void proximoAno(ActionEvent e)   { anoAtual++; renderizarAno(); }

    // Renderização do calendário
    private void renderizarAno() {
        gridCalendario.getChildren().clear();
        lblAno.setText(String.valueOf(anoAtual));

        double lucroAnual = 0;

        for (int mes = 1; mes <= 12; mes++) {
            double lucroMes = 0;
            try {
                lucroMes = SessaoUsuario.getInstance()
                        .getRegistroService().calcularFaturamentoMensal(mes, anoAtual);
            } catch (RegistroException ignored) {}
            lucroAnual += lucroMes;

            VBox painelMes = criarPainelMes(mes, anoAtual, lucroMes);

            int col = (mes - 1) % 3;
            int row = (mes - 1) / 3;
            gridCalendario.add(painelMes, col, row);
        }

        lblLucroAnual.setText(String.format("lucro anual: R$%.2f", lucroAnual));
    }

    // Cria o painel visual de um mês com seus dias e o lucro correspondente
    private VBox criarPainelMes(int mes, int ano, double lucroMes) {
        YearMonth ym   = YearMonth.of(ano, mes);
        int totalDias  = ym.lengthOfMonth();

        // Dia da semana do primeiro dia do mês
        int primeiroDS = ym.atDay(1).getDayOfWeek().getValue() % 7;

        VBox painel = new VBox(4);
        painel.setStyle("-fx-padding:0; -fx-pref-width:210px;");

        // Nome do mês
        String nomeMes = ym.getMonth().getDisplayName(TextStyle.SHORT,
                        new Locale.Builder().setLanguage("pt").setRegion("BR").build())
                .toUpperCase().replace(".", "");
        Label lblMes = new Label(nomeMes);
        lblMes.setStyle("-fx-font-size:13px; -fx-font-weight:bold; -fx-text-fill:#2E1A47;" +
                "-fx-padding:0 0 2 0;");
        painel.getChildren().add(lblMes);

        // Configuração da grade de dias
        GridPane grade = new GridPane();
        grade.setHgap(4); grade.setVgap(2);

        // Cabeçalho dos dias da semana
        for (int i = 0; i < 7; i++) {
            Label h = new Label(DIAS[i]);
            h.setStyle("-fx-font-size:9px; -fx-font-weight:bold;" +
                    "-fx-text-fill:" + (i == 0 ? "#D1203A" : "#888888") + ";" +
                    "-fx-min-width:24px; -fx-alignment:center;");
            h.setAlignment(Pos.CENTER);
            h.setMinWidth(24);
            grade.add(h, i, 0);
        }

        // Preenchimento dos dias numerados
        int colAtual = primeiroDS;
        int linhaAtual = 1;
        for (int dia = 1; dia <= totalDias; dia++) {
            Label lblDia = new Label(String.format("%02d", dia));
            boolean eDomingo = (colAtual == 0);
            lblDia.setStyle("-fx-font-size:9px; -fx-min-width:24px; -fx-alignment:center;" +
                    "-fx-text-fill:" + (eDomingo ? "#D1203A" : "#333333") + ";");
            lblDia.setAlignment(Pos.CENTER);
            lblDia.setMinWidth(24);
            grade.add(lblDia, colAtual, linhaAtual);

            colAtual++;
            if (colAtual == 7) { colAtual = 0; linhaAtual++; }
        }

        painel.getChildren().add(grade);

        // Exibição do lucro mensal
        Label lblLucro = new Label(String.format("lucro mensal: R$%.2f", lucroMes));
        lblLucro.setStyle("-fx-font-size:10px; -fx-text-fill:#555555; -fx-padding:4 0 0 0;");
        painel.getChildren().add(lblLucro);

        return painel;
    }

    // Navegação entre telas
    @FXML
    private void mostrarCliente() { NavigationHelper.goTo(clienteButton, "ClienteGerente.fxml"); }
    @FXML
    private void aoRelatorio() { NavigationHelper.goTo(relatorioButton, "Financas.fxml"); }
    @FXML
    private void aoAlugueis() {
        NavigationHelper.goTo(alugueisButton, "aluguel.fxml");
    }
    @FXML
    private void aoAcervo() {NavigationHelper.goTo(acervoButton, "ArquivoLivro.fxml"); }
    @FXML
    private void aoSair() { NavigationHelper.goTo(acervoButton, "Login.fxml");
    }
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
    public void handleLogout(ActionEvent e) {
        SessaoUsuario.getInstance().limparSessao();
        irPara("login.fxml", e);
    }

    // Utilitário para troca de telas
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
}