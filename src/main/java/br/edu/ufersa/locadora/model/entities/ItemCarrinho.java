package br.edu.ufersa.locadora.model.entities;

public class ItemCarrinho {
    // ATRIBUTOS
    private final ItemAcervo itemAcervo;
    private final double precoDiaria;
    private int diasAlugados;

    // CONSTRUTOR
    public ItemCarrinho(ItemAcervo itemAcervo, int diasAlugados) {
        if (itemAcervo == null)
            throw new IllegalArgumentException("Item nulo.");

        this.itemAcervo = itemAcervo;
        this.diasAlugados = diasAlugados;
        this.precoDiaria = itemAcervo.getValor();
    }

    // MÉTODO de caucular o subtotal do item no carrinho
    public double calcularSubtotal() {
        return precoDiaria * diasAlugados;
    }

    // GETTERS
    public ItemAcervo getItemAcervo() {
        return itemAcervo;
    }

    public double getPrecoDiaria() {
        return precoDiaria;
    }

    public int getDiasAlugados() {
        return diasAlugados;
    }

    // SETTERS
    public void setDiasAlugados(int diasAlugados) {
        if (diasAlugados <= 0)
            throw new IllegalArgumentException("Dias de aluguel devem ser maiores que zero.");
        this.diasAlugados = diasAlugados;
    }
}
