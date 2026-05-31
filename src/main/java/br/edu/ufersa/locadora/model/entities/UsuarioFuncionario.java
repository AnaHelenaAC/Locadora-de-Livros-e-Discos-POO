package br.edu.ufersa.locadora.model.entities;

import br.edu.ufersa.locadora.exceptions.SemNomeException;

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

    public void setIdFuncionario(Long idFuncionario) {
        if (this.getIdFuncionario() == null) {
            this.idFuncionario = idFuncionario;
        } else {
            System.out.println("Aviso de Segurança: O ID deste funcionário já foi definido!");
        }
    }
}