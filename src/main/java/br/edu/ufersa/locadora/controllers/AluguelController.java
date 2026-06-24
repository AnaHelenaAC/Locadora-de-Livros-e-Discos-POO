package br.edu.ufersa.locadora.controllers;

import br.edu.ufersa.locadora.model.Service.AluguelService;
import br.edu.ufersa.locadora.model.Service.RegistroService;
import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Aluguel;
import br.edu.ufersa.locadora.model.entities.ItemAluguel;
import br.edu.ufersa.locadora.model.entities.Registro;
import br.edu.ufersa.locadora.model.entities.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    @FXML private TableColumn<ItemAluguel, String> colTipo;
    @FXML private TableColumn<ItemAluguel, String> colTitulo;
    @FXML private TableColumn<ItemAluguel, String> colDevolocao;
    @FXML private TableColumn<ItemAluguel, Double> colValor;
    @FXML private TableColumn<ItemAluguel, Double> colMulta;

    @FXML private Button relatorioButton;
    @FXML private Button acervoButton;
    @FXML private Button cadastrosButton;
    @FXML private Button sairButton;

    private AluguelService aluguelService;
    private RegistroService registroService;
    private ObservableList<Aluguel> masterData;
    private Registro caixaAtual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aluguelService = SessaoUsuario.getInstance().getAluguelService();
        registroService = SessaoUsuario.getInstance().getRegistroService();
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
        //ID Aluguel (int para Integer de forma segura)
        colAluguelID.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getId())
        );

        //CPF do Cliente (Já estava correto!)
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getCpf()));

        //LocalDate da Data de Início
        colInicio.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getDataInicio())
        );

        //LocalDate da Data Prevista
        colPrevista.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getDataFimPrevista())
        );

        //Valor Total
        colTotal.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().calcularValorFinalAtual())
        );

        // Mapeamentos da tabela de itens
        colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem().getIsDisco() ? "Disco" : "Livro"));
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
            List<Aluguel> ativos = aluguelService.listarAtivos();
            masterData.setAll(ativos);
            tabelaAluguel.setItems(masterData);
            tabelaItens.getItems().clear();
            try {
                caixaAtual = registroService.buscarPorId(1L);
            } catch (Exception e) {
                Registro novoCaixa = new Registro();
                novoCaixa.setIdRegistro(1L);
                novoCaixa.setFaturamentoTotal(0.0);
                caixaAtual = registroService.salvar(novoCaixa);
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
    private void aoRelatorio() { NavigationHelper.goTo(relatorioButton, "Financas.fxml"); }
    @FXML
    private void aoAcervo() {
        NavigationHelper.goTo(acervoButton, "ArquivoLivro.fxml");
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
    private void aoSair() { NavigationHelper.goTo(acervoButton, "Login.fxml");
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