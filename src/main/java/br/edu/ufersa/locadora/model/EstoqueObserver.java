package br.edu.ufersa.locadora.model;

import br.edu.ufersa.locadora.model.entities.ItemAcervo;

@FunctionalInterface
public interface EstoqueObserver {

    void atualizarEstoque(ItemAcervo itemAcervo);
}