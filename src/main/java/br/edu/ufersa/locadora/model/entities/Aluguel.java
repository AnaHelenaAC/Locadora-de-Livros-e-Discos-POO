package br.edu.ufersa.locadora.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Aluguel {

    // ATRIBUTOS
    private int id;
    private final Cliente cliente;
    private List<ItemAluguel> itensAlugados;
    private final LocalDate dataInicio;
    private final LocalDate dataFimPrevista;
    private final double valorBase;
    private double valorMulta;

    // CONSTRUTOR
    public Aluguel(Carrinho carrinho, int diasAlugados) {
        // Validações
        if (carrinho == null || carrinho.getItensNoCarrinho().isEmpty()) {
            throw new IllegalArgumentException("Carrinho vazio.");
        }
        if (diasAlugados <= 0) {
            throw new IllegalArgumentException("Quantidade de dias inválida.");
        }

        // Inicialização
        this.cliente = carrinho.getCliente();
        this.dataInicio = LocalDate.now();
        this.dataFimPrevista = dataInicio.plusDays(diasAlugados);

        // Converte itens do carrinho para itens de aluguel
        this.itensAlugados = new ArrayList<>();
        for (ItemCarrinho itemCarrinho : carrinho.getItensNoCarrinho()) {
            ItemAluguel itemAluguel = new ItemAluguel(
                    itemCarrinho.getItemAcervo(),
                    itemCarrinho.getDiasAlugados());
            this.itensAlugados.add(itemAluguel);
        }
        // Calcula valor base do aluguel
        this.valorBase = carrinho.calcularValorTotal();
        this.valorMulta = 0.0;
    }

    // construtor para leitura do banco de dados
    public Aluguel(
            int id,
            Cliente cliente,
            List<ItemAluguel> itensAlugados,
            LocalDate dataInicio,
            LocalDate dataFimPrevista,
            double valorBase,
            double valorMulta) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente inválido.");
        }
        this.id = id;
        this.cliente = cliente;
        this.itensAlugados = (itensAlugados != null) ? itensAlugados : new ArrayList<>();
        this.dataInicio = dataInicio;
        this.dataFimPrevista = dataFimPrevista;
        this.valorBase = valorBase;
        this.valorMulta = valorMulta;
    }

    // MÉTODOS

    // Calcula a multa acumulada de todos os itens
    private double calcularMultaAcumulada() {
        double totalMulta = 0.0;
        for (ItemAluguel item : itensAlugados) {
            totalMulta += item.calcularMultaItem(dataFimPrevista, dataInicio);
        }
        return totalMulta;
    }

    // Consulta o valor final atualizado
    public double calcularValorFinalAtual() {return valorBase + calcularMultaAcumulada();
    }

    // Finaliza TODOS os itens de uma vez só (caso todos sejam devolvidos juntos)
    public void finalizarAluguelCompleto(LocalDate dataDevolucao) {
        if (getStatus().equals("FINALIZADO")) {
            throw new IllegalStateException("Aluguel já está completamente finalizado.");
        }

        for (ItemAluguel item : itensAlugados) {
            if (item.getDataFim() == null) { // finaliza apenas os que ainda faltam
                item.finalizarItem(dataDevolucao, dataInicio);
            }
        }
        // Atualiza a multa gravada no objeto
        this.valorMulta = calcularMultaAcumulada();
    }

    // Permite finalizar apenas um item específico (Devolução parcial)
    public void finalizarItemEspecifico(ItemAluguel item, LocalDate dataDevolucao) {
        if (!itensAlugados.contains(item)) {
            throw new IllegalArgumentException("Este item não pertence a este aluguel.");
        }
        item.finalizarItem(dataDevolucao, dataInicio);
        this.valorMulta = calcularMultaAcumulada();
    }

    public void adicionarItens(List<ItemAluguel> itens) {
        if (itens != null) {
            this.itensAlugados.addAll(itens);
        }
    }

    // Valor efetivamente pago após a finalização
    public double getValorTotalPago() {
        return valorBase + valorMulta;
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

    public double getValorBase() {
        return valorBase;
    }

    public double getValorMulta() {
        return valorMulta;
    }

    public String getStatus() {
        boolean todosDevolvidos = true;
        for (ItemAluguel item : itensAlugados) {
            if (item.getDataFim() == null) {
                todosDevolvidos = false;
                break;
            }
        }

        if (todosDevolvidos) {
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
}