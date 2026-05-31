package br.edu.ufersa.locadora.model.entities;

import br.edu.ufersa.locadora.exceptions.SemNomeException;

public class UsuarioGerente extends Usuario {
    private int idGerente;

    public UsuarioGerente() {
        super();
        this.setGerente(true);
    }

    public UsuarioGerente(String nome, String login, String senha) throws SemNomeException {
        super(nome, login, senha);
        this.setGerente(true);
    }

    public int getIdGerente() {
        return idGerente;
    }

    public void setIdGerente(int idGerente) {
        if (idGerente <= 0){
            System.out.println("Acesso Negado. O ID deve ser maior que 0!");
        }
        else {
            this.idGerente = idGerente;
        }
    }

    public void cadastrarFuncionario(UsuarioFuncionario f) {
        System.out.println("Gerente " + this.getNome() + " iniciando processo de cadastro...");
        Registro.salvarFuncionarioNoSistema(f);
    }
}