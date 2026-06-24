package br.edu.ufersa.locadora.model.entities;

import java.util.List;
import java.util.ArrayList;

public class Carrinho {
    private Cliente cliente;
    private List<ItemCarrinho> itensNoCarrinho;

    // CONSTRUTOR
    public Carrinho(Cliente cliente, List<ItemCarrinho> itensNoCarrinhos) {
        if (cliente == null)
            throw new IllegalArgumentException("Cliente nulo.");
        this.cliente = cliente;
        this.itensNoCarrinho = new ArrayList<>(itensNoCarrinhos);
    }

    // MÉTODOS
    // metodo para adicionar um item ao carrinho
    public void adicionarItem(ItemAcervo item, int diasAlugados) {
        if (item == null) {
            throw new IllegalArgumentException("Item nulo.");
        }
        if (diasAlugados <= 0) {
            throw new IllegalArgumentException("Dias de aluguel devem ser maiores que zero.");
        }
        if (item.getQtdItens() <= 0) {
            throw new IllegalArgumentException("Item indisponível para aluguel.");
        }
        ItemCarrinho itemCarrinho = new ItemCarrinho(item, diasAlugados);
        this.itensNoCarrinho.add(itemCarrinho);
    }

    // metodo para remover um item do carrinho com base na posição do item na lista
    public void removerItem(int i) {
        if (i < 0 || i >= itensNoCarrinho.size()) {
            throw new IllegalArgumentException("Índice do item inválido.");
        }
        this.itensNoCarrinho.remove(i);
    }

    // metodo para calcular o valor total do carrinho
    public double calcularValorTotal() {
        double total = 0.0;
        for (ItemCarrinho item : this.itensNoCarrinho) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    // metodo para esvaziar o carrinho
    public void limparCarrinho() {
        this.itensNoCarrinho.clear();
    }

    // GETTERS
    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemCarrinho> getItensNoCarrinho() {
        return new ArrayList<>(itensNoCarrinho);
    }

}
