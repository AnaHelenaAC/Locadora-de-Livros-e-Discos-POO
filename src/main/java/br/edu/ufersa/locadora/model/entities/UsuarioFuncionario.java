package br.edu.ufersa.locadora.model.entities;

import br.edu.ufersa.locadora.exceptions.SemNomeException;
import br.edu.ufersa.locadora.exceptions.UsuarioFuncionarioException;

public class UsuarioFuncionario extends Usuario {
    private Long idFuncionario;

    public UsuarioFuncionario() {
        super();
        this.setGerente(false);
    }

    public UsuarioFuncionario(String nome, String login, String senha) throws SemNomeException {
        super(nome, login, senha);
        this.setGerente(false);
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) throws UsuarioFuncionarioException {
        if (this.idFuncionario == null) {
            this.idFuncionario = idFuncionario;
        } else {
            throw new UsuarioFuncionarioException("Aviso de Segurança: O ID deste funcionário já foi definido!");
        }
    }
}