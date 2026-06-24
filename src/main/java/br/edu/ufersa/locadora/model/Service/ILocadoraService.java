package br.edu.ufersa.locadora.model.Service;

import java.util.List;

public interface ILocadoraService<T, ID> {

    T salvar(T entidade);

    T atualizar(T entidade);

    boolean excluir(ID id);
+
    T buscarPorId(ID id);

    List<T> listarTodos();
}