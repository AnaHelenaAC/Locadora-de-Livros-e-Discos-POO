package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.ConnectionFactory;
import br.edu.ufersa.locadora.model.DAO.UsuarioDAO;
import br.edu.ufersa.locadora.model.entities.Usuario;
import br.edu.ufersa.locadora.exceptions.UsuarioException;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO dao;

    public UsuarioService(ConnectionFactory connectionFactory) {
        this.dao = new UsuarioDAO(connectionFactory);
    }

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

    public boolean excluir(Usuario usu) throws UsuarioException {
        if (usu == null || usu.getId() == null) {
            throw new UsuarioException("Usuário inválido para deleção!");
        }
        if (usu.isGerente()) {
            throw new UsuarioException("Erro de Segurança: O gerente do sistema (ID 1) não pode ser excluído!");
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

    public Usuario buscarGerente() throws UsuarioException {
        Usuario gerente = dao.ReadGerente();
        if (gerente == null) {
            throw new UsuarioException(
                    "Nenhum gerente cadastrado (usuário de ID 1 não encontrado)!");
        }
        return gerente;
    }

    public List<Usuario> buscarFuncionariosPorNome(String nome) throws UsuarioException {
        return dao.ReadFuncionarios(nome == null ? "" : nome);
    }

    public boolean autenticar(String login, String senha) throws UsuarioException {
        if (login == null || senha == null) {
            throw new UsuarioException("Credenciais incompletas para autenticação!");
        }
        Usuario usu = dao.ReadPorLogin(login);
        return usu != null && senha.equals(usu.getSenha());
    }

    public Usuario autenticarUsuario(String login, String senha) throws UsuarioException {
        if (login == null || senha == null) {
            throw new UsuarioException("Credenciais incompletas para autenticação!");
        }
        Usuario usu = dao.ReadPorLogin(login);
        if (usu != null && senha.equals(usu.getSenha())) {
            return usu;
        }
        return null;
    }
}