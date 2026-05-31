package br.edu.ufersa.locadora.model.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Aluguel {

    // ATRIBUTOS
    private int id;
    private final Cliente cliente;
    private final List<ItemAluguel> itensAlugados;
    private final LocalDate dataInicio;
    private final LocalDate dataFimPrevista;
    private LocalDate dataFim;
    private final double valorBase;
    private double valorMulta;

    // CONSTRUTOR
    public Aluguel(Carrinho carrinho, int diasAlugados) {
        // Validações
        if (carrinho == null || carrinho.getItensNoCarrinho().isEmpty()) {
            throw new IllegalArgumentException("Carrinho vazio.");
        }

        // Inicialização
        this.cliente = carrinho.getCliente();
        this.dataInicio = LocalDate.now();
        this.dataFimPrevista = dataInicio.plusDays(diasAlugados);

        // Converte itens do carrinho para itens de aluguel
        this.itensAlugados = new ArrayList<>();
        for (ItemCarrinho itemCarrinho : carrinho.getItensNoCarrinho()) {
            ItemAluguel itemAluguel = new ItemAluguel(itemCarrinho.getItemAcervo(), itemCarrinho.getDiasAlugados());
            this.itensAlugados.add(itemAluguel);
        }
        // Calcula valor base do aluguel
        this.valorBase = carrinho.calcularValorTotal();
        this.valorMulta = 0.0;
    }

        // construtor completo
        public Aluguel(int id, Cliente cliente, List<ItemAluguel> itensAlugados, LocalDate dataInicio, LocalDate dataFimPrevista, LocalDate dataFim, double valorBase, double valorMulta) {
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente inválido.");
            }

            if (itensAlugados == null || itensAlugados.isEmpty()) {
                throw new IllegalArgumentException("Lista de itens vazia.");
            }
            this.id = id;
            this.cliente = cliente;
            this.itensAlugados = new ArrayList<>(itensAlugados);
            this.dataInicio = dataInicio;
            this.dataFimPrevista = dataFimPrevista;
            this.dataFim = dataFim;
            this.valorBase = valorBase;
            this.valorMulta = valorMulta;
        }

    // MÉTODOS ID
    //calcula multa e retorna valor final
    private double calcularMulta(LocalDate dataInformada) {

        // Validações
        if (dataInformada.isBefore(dataInicio)) {
            throw new IllegalStateException("Data informada é anterior à data de início do aluguel.");
        }
        if (!dataInformada.isAfter(dataFimPrevista)) {
            return 0.0;
        }

        // Calcula dias de atraso
        long atraso = ChronoUnit.DAYS.between(dataFimPrevista, dataInformada);

        // Multa fixa + juros diários de 10% sobre o valor das diárias
        double multaFixa = 5.0;
        double totalDiarias = 0.0;

        for (ItemAluguel item : itensAlugados) {
            totalDiarias += item.getPrecoDiaria();
        }
        double jurosDiarios = totalDiarias * 0.10 * atraso;

        return multaFixa + jurosDiarios;
    }

    // Consulta sem alterar o estado do aluguel
    public double calcularValorFinal(LocalDate dataInformada) {
        return valorBase + calcularMulta(dataInformada);
    }

    // Finaliza o aluguel
    public void finalizarAluguel(LocalDate dataFim) {
        if (getStatus().equals("FINALIZADO")) {
            throw new IllegalStateException("Aluguel já finalizado.");
        }
        this.dataFim = dataFim;
        this.valorMulta = calcularMulta(dataFim);
    }

    // GETTERS E SETTERS
    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemAluguel> getItensAlugados() {
        return new ArrayList<>(itensAlugados);
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFimPrevista() {
        return dataFimPrevista;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public double getValorBase() {
        return valorBase;
    }

    public double getValorMulta() {
        return valorMulta;
    }

    public String getStatus() {
        if (dataFim != null) {
            return "FINALIZADO";
        }
        if (LocalDate.now().isAfter(dataFimPrevista)) {
            return "ATRASADO";
        }
        return "ATIVO";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}