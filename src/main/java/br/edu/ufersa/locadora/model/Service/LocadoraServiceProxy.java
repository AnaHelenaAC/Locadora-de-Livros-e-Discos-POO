package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.exceptions.PermissaoNegadaException;
import br.edu.ufersa.locadora.model.SessaoUsuario;
import br.edu.ufersa.locadora.model.entities.Usuario;

import java.util.List;

public class LocadoraServiceProxy<T, ID> implements ILocadoraService<T, ID> {

    private final ILocadoraService<T, ID> servicoReal;
    private final boolean requerGerente;

    public LocadoraServiceProxy(ILocadoraService<T, ID> servicoReal) {
        this(servicoReal, true);
    }

    public LocadoraServiceProxy(ILocadoraService<T, ID> servicoReal, boolean requerGerente) {
        if (servicoReal == null) {
            throw new IllegalArgumentException("O serviço real não pode ser nulo.");
        }
        this.servicoReal = servicoReal;
        this.requerGerente = requerGerente;
    }

    @Override
    public T salvar(T entidade) {
        verificarPermissaoParaEscrita();
        return servicoReal.salvar(entidade);
    }

    @Override
    public T atualizar(T entidade) {
        verificarPermissaoParaEscrita();
        return servicoReal.atualizar(entidade);
    }

    @Override
    public boolean excluir(ID id) {
        verificarPermissaoParaEscrita();
        return servicoReal.excluir(id);
    }

    @Override
    public T buscarPorId(ID id) {
        return servicoReal.buscarPorId(id);
    }

    @Override
    public List<T> listarTodos() {
        return servicoReal.listarTodos();
    }

    private void verificarPermissaoParaEscrita() {
        Usuario usuario = SessaoUsuario.getInstance().getUsuarioLogado();
        if (usuario == null) {
            throw new PermissaoNegadaException("Nenhum usuário autenticado.");
        }
        if (requerGerente && !usuario.isGerente()) {
            throw new PermissaoNegadaException("Apenas um gerente pode executar esta operação.");
        }
    }
}