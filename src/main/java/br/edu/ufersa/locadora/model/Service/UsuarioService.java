package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.UsuarioDAO;
import br.edu.ufersa.locadora.model.entities.Usuario;
import java.util.List;

public class UsuarioService {

    private UsuarioDAO dao = new UsuarioDAO();

    public Usuario salvar(Usuario u) {
        return dao.Create(u);
    }

    public boolean atualizar(Usuario u) {
        return dao.Update(u);
    }

    public boolean deletar(Usuario u) {
        return dao.Delete(u);
    }

    public Usuario buscarPorNome(String nome) {
        List<Usuario> resultados = dao.Read(nome);
        if (!resultados.isEmpty()) {
            return resultados.get(0);
        }
        return null;
    }

    public boolean autenticar(String login, String senha) {
        List<Usuario> resultados = dao.Read("");
        for (Usuario u : resultados) {
            if (u.getLogin().equals(login) && u.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }
}