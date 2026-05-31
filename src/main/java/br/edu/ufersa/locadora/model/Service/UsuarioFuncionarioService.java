package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.UsuarioFuncionarioDAO;
import br.edu.ufersa.locadora.model.entities.UsuarioFuncionario;
import java.util.List;

public class UsuarioFuncionarioService {

    private UsuarioFuncionarioDAO dao = new UsuarioFuncionarioDAO();

    // Funções referentes ao CRUD usado no DAO
    public UsuarioFuncionario salvar(UsuarioFuncionario f) {
        return dao.Create(f);
    }

    public UsuarioFuncionario buscarPorNome(String nome) {
        List<UsuarioFuncionario> resultados = dao.Read(nome);
        if (!resultados.isEmpty()) {
            return resultados.get(0);
        }
        return null;
    }

    public boolean atualizar(UsuarioFuncionario f) {
        return dao.Update(f);
    }

    public boolean deletar(UsuarioFuncionario f) {
        return dao.Delete(f);
    }
}