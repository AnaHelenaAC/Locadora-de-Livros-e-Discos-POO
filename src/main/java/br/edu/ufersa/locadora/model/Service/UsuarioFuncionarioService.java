package br.edu.ufersa.locadora.model.service;

import br.edu.ufersa.locadora.model.DAO.UsuarioFuncionarioDAO;
import br.edu.ufersa.locadora.model.entities.UsuarioFuncionario;
import br.edu.ufersa.locadora.exceptions.UsuarioFuncionarioException;
import java.util.List;

public class UsuarioFuncionarioService {

    private UsuarioFuncionarioDAO dao = new UsuarioFuncionarioDAO();

    public UsuarioFuncionario salvar(UsuarioFuncionario fun) throws UsuarioFuncionarioException {
        if (fun == null) {
            throw new UsuarioFuncionarioException("Não é possível salvar um funcionário nulo!");
        }
        return dao.Create(fun);
    }

    public UsuarioFuncionario buscarPorNome(String nome) throws UsuarioFuncionarioException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new UsuarioFuncionarioException("O nome informado para busca é inválido!");
        }
        List<UsuarioFuncionario> resultados = dao.Read(nome);
        if (!resultados.isEmpty()) {
            return resultados.get(0);
        }
        throw new UsuarioFuncionarioException("Nenhum funcionário encontrado com o nome: " + nome);
    }

    public boolean atualizar(UsuarioFuncionario fun) throws UsuarioFuncionarioException {
        if (fun == null || fun.getIdFuncionario() == null) {
            throw new UsuarioFuncionarioException("Funcionário inválido para atualização!");
        }
        return dao.Update(fun);
    }

    public boolean deletar(UsuarioFuncionario fun) throws UsuarioFuncionarioException {
        if (fun == null || fun.getIdFuncionario() == null) {
            throw new UsuarioFuncionarioException("Funcionário inválido para exclusão!");
        }
        return dao.Delete(fun);
    }
}