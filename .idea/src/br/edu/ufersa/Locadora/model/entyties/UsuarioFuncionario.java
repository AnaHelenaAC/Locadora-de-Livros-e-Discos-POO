package br.edu.ufersa.Locadora.model.entyties;

public class UsuarioFuncionario extends Usuario {
    private int idFuncionario;

    // O construtor do filho pede os dados do pai
    public UsuarioFuncionario(String nome, String login, String senha) {
        // O comando 'super' repassa esses dados lá para a classe Usuario!
        super(nome, login, senha);
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
}
