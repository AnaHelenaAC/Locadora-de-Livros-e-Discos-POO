package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.UsuarioDAO;
import br.edu.ufersa.locadora.model.entities.Usuario;
import br.edu.ufersa.locadora.exceptions.UsuarioException;
import java.util.List;

public class UsuarioService {

    private UsuarioDAO dao = new UsuarioDAO();

    public Usuario salvar(Usuario usu) throws UsuarioException {
        if (usu == null) {
            throw new UsuarioException("Não é possível salvar um usuário nulo!");
        }
        return dao.Create(usu);
    }

    public boolean atualizar(Usuario usu) throws UsuarioException {
        if (usu == null || usu.getId() == null) {
            throw new UsuarioException("Usuário inválido para atualização!");
        }
        return dao.Update(usu);
    }

    public boolean deletar(Usuario usu) throws UsuarioException {
        if (usu == null || usu.getId() == null) {
            throw new UsuarioException("Usuário inválido para deleção!");
        }
        return dao.Delete(usu);
    }

    public Usuario buscarPorNome(String nome) throws UsuarioException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new UsuarioException("O nome de busca não pode ser vazio!");
        }
        List<Usuario> resultados = dao.Read(nome);
        if (!resultados.isEmpty()) {
            return resultados.get(0);
        }
        throw new UsuarioException("Nenhum usuário localizado com o nome informado!");
    }

    public boolean autenticar(String login, String senha) throws UsuarioException {
        if (login == null || senha == null) {
            throw new UsuarioException("Credenciais incompletas para autenticação!");
        }
        List<Usuario> resultados = dao.Read("");
        for (Usuario usu : resultados) {
            if (usu.getLogin() != null && usu.getLogin().equals(login) &&
                    usu.getSenha() != null && usu.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }
}