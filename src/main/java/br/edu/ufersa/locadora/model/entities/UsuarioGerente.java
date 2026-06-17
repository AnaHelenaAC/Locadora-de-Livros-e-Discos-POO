package br.edu.ufersa.locadora.model.entities;

import br.edu.ufersa.locadora.exceptions.SemNomeException;
import br.edu.ufersa.locadora.exceptions.UsuarioException;
import br.edu.ufersa.locadora.exceptions.UsuarioGerenteException;

public class UsuarioGerente extends Usuario {
    private int idGerente;

    public UsuarioGerente() {
        super();
        this.setGerente(true);
    }

    public UsuarioGerente(String nome, String login, String senha) throws SemNomeException, UsuarioException {
        super(nome, login, senha);
        this.setGerente(true);
    }

    public int getIdGerente() {
        return idGerente;
    }

    public void setIdGerente(int idGerente) throws UsuarioGerenteException {
        if (idGerente <= 0){
            throw new UsuarioGerenteException("Acesso Negado. O ID deve ser maior que 0!");
        }
        else {
            this.idGerente = idGerente;
        }
    }

    public void cadastrarFuncionario(UsuarioFuncionario f) throws Exception {
        Registro.salvarFuncionarioNoSistema(f);
    }
}