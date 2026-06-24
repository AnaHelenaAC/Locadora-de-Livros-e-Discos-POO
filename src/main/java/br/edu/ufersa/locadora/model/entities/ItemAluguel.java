package br.edu.ufersa.locadora.model.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ItemAluguel {

    // ATRIBUTOS
    private int id;
    private final ItemAcervo item;
    private final double precoDiaria;
    private final int diasAlugados;
    private LocalDate dataFim;

    public ItemAluguel() {
        this.id = 0;
        this.item = null;
        this.precoDiaria = 0;
        this.diasAlugados = 0;
        this.dataFim = null;
    }

    // CONSTRUTOR PARA NOVOS ALUGUÉIS
    public ItemAluguel(ItemAcervo item, int diasAlugados) {
        if (item == null) {
            throw new IllegalArgumentException("Item nulo.");
        }
        if (diasAlugados <= 0) {
            throw new IllegalArgumentException("Dias de aluguel inválidos.");
        }
        this.id = 0; // ainda não persistido; preenchido depois via setId pelo DAO
        this.item = item;
        this.precoDiaria = item.getValor();
        this.diasAlugados = diasAlugados;
        this.dataFim = null;
    }

    // CONSTRUTOR PARA RECONSTRUÇÃO VIA DAO
    public ItemAluguel(int id, ItemAcervo item, double precoDiaria, int diasAlugados, LocalDate dataFim) {
        if (item == null) {
            throw new IllegalArgumentException("Item nulo.");
        }
        if (precoDiaria < 0) {
            throw new IllegalArgumentException("Preço da diária inválido.");
        }
        if (diasAlugados <= 0) {
            throw new IllegalArgumentException("Dias de aluguel inválidos.");
        }
        this.id = id;
        this.item = item;
        this.precoDiaria = precoDiaria;
        this.diasAlugados = diasAlugados;
        this.dataFim = dataFim;
    }

    // MÉTODOS
    public double calcularSubtotal() {
        return precoDiaria * diasAlugados;
    }

    // Finaliza este item especificamente
    public void finalizarItem(LocalDate dataFimDevolucao, LocalDate dataInicioAluguel) {
        if (dataFimDevolucao == null) {
            throw new IllegalArgumentException("Data de devolução inválida.");
        }
        if (dataFimDevolucao.isBefore(dataInicioAluguel)) {
            throw new IllegalStateException("Data de devolução é anterior ao início do aluguel.");
        }
        if (this.dataFim != null) {
            throw new IllegalStateException("Este item já foi devolvido.");
        }
        this.dataFim = dataFimDevolucao;
    }

    // Calcula a multa individual deste item baseado na dataFimPrevista do Aluguel
    public double calcularMultaItem(LocalDate dataFimPrevista, LocalDate dataInicioAluguel) {
        LocalDate dataDevolucaoEfetiva = (this.dataFim != null) ? this.dataFim : LocalDate.now();

        if (dataDevolucaoEfetiva.isBefore(dataInicioAluguel)) {
            return 0.0;
        }
        if (!dataDevolucaoEfetiva.isAfter(dataFimPrevista)) {
            return 0.0;
        }

        long atraso = ChronoUnit.DAYS.between(dataFimPrevista, dataDevolucaoEfetiva);

        // multa fixa de 5.0 por item atrasado + 200% da diária dele por dia de atraso
        double multaFixa = 5.0;
        double jurosDiarios = this.precoDiaria * 2.0 * atraso;

        return multaFixa + jurosDiarios;
    }

    // GETTERS E SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemAcervo getItem() {
        return item;
    }

    public double getPrecoDiaria() {
        return precoDiaria;
    }

    public int getDiasAlugados() {
        return diasAlugados;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }
}