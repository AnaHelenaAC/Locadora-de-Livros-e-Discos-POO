package br.edu.ufersa.locadora.model.entities;

public class ItemAluguel {

    // ATRIBUTOS
    private final ItemAcervo item;
    private final double precoDiaria;
    private final int diasAlugados;

    public ItemAluguel() {
        this.item = null;
        this.precoDiaria = 0;
        this.diasAlugados = 0;
    }

    // CONSTRUTOR PARA NOVOS ALUGUÉIS
    public ItemAluguel(ItemAcervo item, int diasAlugados) {

        if (item == null) {
            throw new IllegalArgumentException("Item nulo.");
        }

        if (diasAlugados <= 0) {
            throw new IllegalArgumentException("Dias de aluguel inválidos.");
        }

        this.item = item;
        this.precoDiaria = item.getValor();
        this.diasAlugados = diasAlugados;
    }

    // CONSTRUTOR PARA RECONSTRUÇÃO VIA DAO
    public ItemAluguel(ItemAcervo item, double precoDiaria, int diasAlugados) {

        if (item == null) {
            throw new IllegalArgumentException("Item nulo.");
        }

        if (precoDiaria < 0) {
            throw new IllegalArgumentException("Preço da diária inválido.");
        }

        if (diasAlugados <= 0) {
            throw new IllegalArgumentException("Dias de aluguel inválidos.");
        }

        this.item = item;
        this.precoDiaria = precoDiaria;
        this.diasAlugados = diasAlugados;
    }

    // MÉTODOS
    public double calcularSubtotal() {
        return precoDiaria * diasAlugados;
    }

    // GETTERS
    public ItemAcervo getItem() {
        return item;
    }

    public double getPrecoDiaria() {
        return precoDiaria;
    }

    public int getDiasAlugados() {
        return diasAlugados;
    }
}