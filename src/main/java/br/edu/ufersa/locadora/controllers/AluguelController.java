package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.entities.Aluguel;
import br.edu.ufersa.locadora.model.entities.ItemAluguel;
import br.edu.ufersa.locadora.model.entities.Registro;
import br.edu.ufersa.locadora.model.Service.AluguelService;
import br.edu.ufersa.locadora.model.Service.RegistroService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AluguelController implements Initializable {

    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField txtCliente;

    @FXML private TableView<Aluguel> tabelaAluguel;
    @FXML private TableColumn<Aluguel, Integer> colAluguelID;
    @FXML private TableColumn<Aluguel, String> colCliente;
    @FXML private TableColumn<Aluguel, LocalDate> colInicio;
    @FXML private TableColumn<Aluguel, LocalDate> colPrevista;
    @FXML private TableColumn<Aluguel, Double> colTotal;

    @FXML private TableView<ItemAluguel> tabelaItens;
    @FXML private TableColumn<ItemAluguel, String> colItemId;
    @FXML private TableColumn<ItemAluguel, String> colTitulo;
    @FXML private TableColumn<ItemAluguel, String> colDevolocao;
    @FXML private TableColumn<ItemAluguel, Double> colValor;
    @FXML private TableColumn<ItemAluguel, Double> colMulta;

    private AluguelService aluguelService;
    private RegistroService registroService;
    private ObservableList<Aluguel> masterData;
    private Registro caixaAtual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aluguelService = new AluguelService();
        registroService = new RegistroService();
        masterData = FXCollections.observableArrayList();

        comboTipo.setItems(FXCollections.observableArrayList("Todos", "Livro", "Disco"));
        comboTipo.setValue("Todos");

        configurarTabelas();
        carregarDadosOriginais();

        tabelaAluguel.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            mostrarItensDoAluguel(novo);
        });
    }

    private void configurarTabelas() {
        // 1. Correção do ID Aluguel (int para Integer de forma segura)
        colAluguelID.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getId())
        );

        // 2. CPF do Cliente (Já estava correto!)
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getCpf()));

        // 3. Correção segura para LocalDate da Data de Início
        colInicio.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getDataInicio())
        );

        // 4. Correção segura para LocalDate da Data Prevista
        colPrevista.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getDataFimPrevista())
        );

        // 5. Correção do Valor Total (double para Double de forma segura)
        colTotal.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().calcularValorFinalAtual())
        );

        // Mapeamentos da tabela de itens (Mantenha como já estão abaixo se estiverem funcionando)
        colItemId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem().getIsDisco() ? "Disco" : "Livro"));
        colTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem().getTitulo()));
        colDevolocao.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDataFim() != null ? cellData.getValue().getDataFim().toString() : "Pendente"
        ));
        colValor.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().calcularSubtotal())
        );
        colMulta.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> {
                    ItemAluguel item = cellData.getValue();
                    Aluguel aluguelSelecionado = tabelaAluguel.getSelectionModel().getSelectedItem();

                    if (aluguelSelecionado != null) {
                        return item.calcularMultaItem(aluguelSelecionado.getDataFimPrevista(), aluguelSelecionado.getDataInicio());
                    }
                    return 0.0;
                })
        );
    }

    private void carregarDadosOriginais() {
        try {

            System.out.println("Passo 1");

            List<Aluguel> ativos = aluguelService.listarAtivos();

            System.out.println("Passo 2");

            masterData.setAll(ativos);

            System.out.println("Passo 3");

            tabelaAluguel.setItems(masterData);

            System.out.println("Passo 4");

            tabelaItens.getItems().clear();

            System.out.println("Passo 5");

            try {

                System.out.println("Passo 6");

                caixaAtual = registroService.buscarPorId(1L);

                System.out.println("Passo 7");

            } catch (Exception e) {

                System.out.println("Passo 8");

                Registro novoCaixa = new Registro();

                novoCaixa.setIdRegistro(1L);
                novoCaixa.setFaturamentoTotal(0.0);

                caixaAtual = registroService.salvar(novoCaixa);

                System.out.println("Passo 9");
            }

        } catch (Exception e) {

            e.printStackTrace();

            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao carregar dados do banco: " + e.getMessage()
            );
            alert.showAndWait();
        }
    }

    private void mostrarItensDoAluguel(Aluguel aluguel) {
        if (aluguel != null && aluguel.getItensAlugados() != null) {
            tabelaItens.setItems(FXCollections.observableArrayList(aluguel.getItensAlugados()));
        } else {
            tabelaItens.getItems().clear();
        }
    }

    @FXML
    private void aoFiltrar() {
        String tipoSelecionado = comboTipo.getValue();
        String buscaCpf = txtCliente.getText().trim();

        List<Aluguel> filtrados = aluguelService.listarAtivos();

        if (!buscaCpf.isEmpty()) {
            filtrados = filtrados.stream()
                    .filter(a -> a.getCliente().getCpf().contains(buscaCpf))
                    .collect(Collectors.toList());
        }

        if (tipoSelecionado != null && !tipoSelecionado.equals("Todos")) {
            boolean filtrarPorDisco = tipoSelecionado.equals("Disco");
            filtrados = filtrados.stream()
                    .filter(a -> a.getItensAlugados().stream()
                            .anyMatch(item -> item.getItem().getIsDisco() == filtrarPorDisco))
                    .collect(Collectors.toList());
        }

        tabelaAluguel.setItems(FXCollections.observableArrayList(filtrados));
        tabelaItens.getItems().clear();
    }

    @FXML
    private void aoMostrarTudo() {
        comboTipo.setValue("Todos");
        txtCliente.clear();
        carregarDadosOriginais();
    }

    @FXML
    private void aoRegistrarDevolucao() {
        Aluguel aluguelSelecionado = tabelaAluguel.getSelectionModel().getSelectedItem();
        ItemAluguel itemSelecionado = tabelaItens.getSelectionModel().getSelectedItem();

        if (aluguelSelecionado == null || itemSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um aluguel e o item específico para realizar a devolução.");
            alert.showAndWait();
            return;
        }

        if (itemSelecionado.getDataFim() != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Este item já foi devolvido anteriormente.");
            alert.showAndWait();
            return;
        }

        try {
            registroService.registrarDevolucaoDeItem(caixaAtual, aluguelSelecionado, itemSelecionado, LocalDate.now());

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Devolução do item registrada com sucesso!");
            alert.showAndWait();

            carregarDadosOriginais();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao registrar devolução: " + e.getMessage());
            alert.showAndWait();
        }
    }
}