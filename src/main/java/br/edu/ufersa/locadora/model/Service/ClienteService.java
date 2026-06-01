package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.ClienteDAO;
import br.edu.ufersa.locadora.model.entities.Cliente;

import java.util.List;

public class ClienteService {

    private final ClienteDAO dao;

    public ClienteService() {
        dao = new ClienteDAO();
    }

    public Cliente cadastrar(Cliente cliente) {
        return dao.Create(cliente);
    }

    public Cliente atualizar(Cliente cliente) {
        return dao.Update(cliente);
    }

    public boolean excluir(String cpf) {
        return dao.Delete(cpf);
    }

    public Cliente buscarPorCpf(String cpf) {
        return dao.ReadByCpf(cpf);
    }

    public List<Cliente> buscarPorNome(String nome) {
        return dao.ReadByNome(nome);
    }

    public List<Cliente> listarTodos() {
        return dao.ReadAll();
    }
}