package br.edu.ufersa.locadora.model.service;

import br.edu.ufersa.locadora.model.DAO.UsuarioGerenteDAO;
import br.edu.ufersa.locadora.model.entities.UsuarioGerente;
import java.util.List;

public class UsuarioGerenteService {

    private UsuarioGerenteDAO dao = new UsuarioGerenteDAO();

    // Funções referentes ao CRUD usado no DAO
    public UsuarioGerente salvar(UsuarioGerente g) {
        return dao.Create(g);
    }

    public UsuarioGerente buscarPorNome(String nome) {
        List<UsuarioGerente> resultados = dao.Read(nome);
        if (!resultados.isEmpty()) {
            return resultados.get(0);
        }
        return null;
    }

    public boolean atualizar(UsuarioGerente g) {
        return dao.Update(g);
    }

    public boolean deletar(UsuarioGerente g) {
        return dao.Delete(g);
    }
}