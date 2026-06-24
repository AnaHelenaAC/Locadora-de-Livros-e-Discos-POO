package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.exceptions.RegistroException;
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

public class FinancasController implements Initializable {

    @FXML private Label      lblAno;
    @FXML private Label      lblLucroAnual;
    @FXML private GridPane   gridCalendario;
    @FXML private TextField  tfBusca;
    @FXML private TextField  tfPesquisaBottom;
    @FXML private Label      lblDataHoje;

    private int anoAtual;

    // Dias da semana abreviados conforme o design
    private static final String[] DIAS = {"DO", "SE", "TE", "QA", "QI", "SE", "SA"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anoAtual = LocalDate.now().getYear();
        lblDataHoje.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        renderizarAno();
    }

    // ── Navegação entre telas ─────────────────────────────────────

    
    @FXML public void navegarAcervo(ActionEvent e)       { irPara("ArquivoLivro.fxml",  e); }
    @FXML public void navegarRelatorio(ActionEvent e)    { irPara("financas.fxml", e); }
    @FXML public void navegarCadastros(ActionEvent e)    { irPara("funcionario.fxml", e); }

    // ── Navegação de ano ──────────────────────────────────────

    @FXML public void anoAnterior(ActionEvent e)  { anoAtual--; renderizarAno(); }
    @FXML public void proximoAno(ActionEvent e)   { anoAtual++; renderizarAno(); }

    // ── Renderização do calendário ────────────────────────────

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

    /**
     * Cria o painel visual de um mês:
     *   NOME DO MÊS
     *   DO  SE  TE  QA  QI  SE  SA
     *   [dias numerados com domingos em vermelho]
     *   lucro mensal: R$xx,xx
     */
    private VBox criarPainelMes(int mes, int ano, double lucroMes) {
        YearMonth ym   = YearMonth.of(ano, mes);
        int totalDias  = ym.lengthOfMonth();
        // dia da semana do primeiro dia (1=DOM, 2=SEG, ..., 7=SAB em Java)
        int primeiroDS = ym.atDay(1).getDayOfWeek().getValue() % 7; // 0=DOM

        VBox painel = new VBox(4);
        painel.setStyle("-fx-padding:0; -fx-pref-width:210px;");

        // Nome do mês (ex: JAN)
        String nomeMes = ym.getMonth().getDisplayName(TextStyle.SHORT,
            new Locale.Builder().setLanguage("pt").setRegion("BR").build())
                .toUpperCase().replace(".", "");
        Label lblMes = new Label(nomeMes);
        lblMes.setStyle("-fx-font-size:13px; -fx-font-weight:bold; -fx-text-fill:#2E1A47;" +
                "-fx-padding:0 0 2 0;");
        painel.getChildren().add(lblMes);

        // Grade 7 colunas
        GridPane grade = new GridPane();
        grade.setHgap(4); grade.setVgap(2);

        // Cabeçalho: DO SE TE QA QI SE SA
        for (int i = 0; i < 7; i++) {
            Label h = new Label(DIAS[i]);
            h.setStyle("-fx-font-size:9px; -fx-font-weight:bold;" +
                    "-fx-text-fill:" + (i == 0 ? "#D1203A" : "#888888") + ";" +
                    "-fx-min-width:24px; -fx-alignment:center;");
            h.setAlignment(Pos.CENTER);
            h.setMinWidth(24);
            grade.add(h, i, 0);
        }

        // Dias numerados
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

        // Lucro mensal
        Label lblLucro = new Label(String.format("lucro mensal: R$%.2f", lucroMes));
        lblLucro.setStyle("-fx-font-size:10px; -fx-text-fill:#555555; -fx-padding:4 0 0 0;");
        painel.getChildren().add(lblLucro);

        return painel;
    }

    // ── Navegação ─────────────────────────────────────────────

 
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
            new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).showAndWait();
        }
    }
}