package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.UsuarioGerenteDAO;
import br.edu.ufersa.locadora.model.entities.UsuarioGerente;
import br.edu.ufersa.locadora.exceptions.UsuarioGerenteException;
import java.util.List;

public class UsuarioGerenteService {

    private UsuarioGerenteDAO dao = new UsuarioGerenteDAO();

    public UsuarioGerente salvar(UsuarioGerente ger) throws UsuarioGerenteException {
        if (ger == null) {
            throw new UsuarioGerenteException("Não é possível salvar um gerente nulo!");
        }
        return dao.Create(ger);
    }

    public UsuarioGerente buscarPorNome(String nome) throws UsuarioGerenteException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new UsuarioGerenteException("O nome informado para busca é inválido!");
        }
        List<UsuarioGerente> resultados = dao.Read(nome);
        if (!resultados.isEmpty()) {
            return resultados.get(0);
        }
        throw new UsuarioGerenteException("Nenhum gerente encontrado com o nome: " + nome);
    }

    public boolean atualizar(UsuarioGerente ger) throws UsuarioGerenteException {
        if (ger == null || ger.getIdGerente() <= 0) {
            throw new UsuarioGerenteException("Gerente inválido para atualização!");
        }
        return dao.Update(ger);
    }

    public boolean deletar(UsuarioGerente ger) throws UsuarioGerenteException {
        if (ger == null || ger.getIdGerente() <= 0) {
            throw new UsuarioGerenteException("Gerente inválido para exclusão!");
        }
        return dao.Delete(ger);
    }
}