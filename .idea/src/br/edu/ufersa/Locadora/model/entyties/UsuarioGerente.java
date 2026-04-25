package br.edu.ufersa.Locadora.model.entyties;

public class UsuarioGerente extends Usuario {
    private int idGerente;

    public UsuarioGerente(String nome, String login, String senha) {
        // O comando 'super' repassa esses dados lá para a classe Usuario!
        super(nome, login, senha);
    }

    public int getIdGerente() {
        return idGerente;
    }

    public void setIdGerente(int idGerente) {
        this.idGerente = idGerente;
    }
}
