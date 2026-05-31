package br.edu.ufersa.locadora.model.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Aluguel {

    // ATRIBUTOS
    private static List<Aluguel> alugueis = new ArrayList<>();

    public static List<Aluguel> getAlugueis() {
        return alugueis;
    }

    private Cliente cliente;
    private ItemAcervo item;
    private LocalDate dataInicio;
    private LocalDate dataPrevisao;
    private LocalDate dataFim;

    // CONSTRUTOR
    public Aluguel(Cliente cliente, ItemAcervo item, LocalDate dataInicio, int diasPrazo) {
        setCliente(cliente);
        setItem(item);
        setDataInicio(dataInicio);
        this.dataPrevisao = dataInicio.plusDays(diasPrazo);
    }

    // MÉTODOS
    // registrar
    public void registrar() {
        alugueis.add(this); //adiciona o aluguel à lista de alugueis
    }

    // finalizar
    public void finalizarAluguel() {
        if (dataFim != null) {
            throw new IllegalStateException("Aluguel já foi finalizado.");
        }
        this.dataFim = LocalDate.now();//registra a data de fim como a data atual
    }

    // calcular valor final
    public double calcularValorFinal() {
        if (dataFim == null) {
            throw new IllegalStateException("Finalize o aluguel antes de calcular.");
        }
        long diasAlugados = Math.max(1, ChronoUnit.DAYS.between(dataInicio, dataFim));
        double valorBase = diasAlugados * item.getValor();

        long atraso = ChronoUnit.DAYS.between(dataPrevisao, dataFim);
        if (atraso > 0) {
            double multa = atraso * (item.getValor() * 0.20);
            return valorBase + multa;//valor base + multa de 20% por dia de atraso
        }
        return valorBase;//sem multa
    }

    // GETTERS
    public Cliente getCliente() { return cliente; }
    public ItemAcervo getItem() { return item; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataPrevisao() { return dataPrevisao; }
    public LocalDate getDataFim() { return dataFim; }

    public String getStatus() {
        if (dataFim != null) return "FINALIZADO";
        if (LocalDate.now().isAfter(dataPrevisao)) return "ATRASADO";
        return "ATIVO";
    }

    // SETTERS
    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nulo.");
        }
        this.cliente = cliente;
    }

    public void setItem(ItemAcervo item) {
        if (item == null) {
            throw new IllegalArgumentException("Item nulo.");
        }
        this.item = item;
    }

    public void setDataInicio(LocalDate dataInicio) {
        if (dataInicio == null) {
            throw new IllegalArgumentException("Data de início nula.");
        }
        this.dataInicio = dataInicio;
    }

    // TO STRING
    @Override
    public String toString() {
        return cliente.getNome() + " - " + item.getTitulo() + " - " + dataInicio + " - " + dataPrevisao + " - " + (dataFim != null ? dataFim : "EM ABERTO") + " - " + getStatus();
    }
}