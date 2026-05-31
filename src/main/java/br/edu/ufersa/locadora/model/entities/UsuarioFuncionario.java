package br.edu.ufersa.locadora.model.entities;

// Classe "Filha" UsuarioFuncionario, herda da classe Usuario
public class UsuarioFuncionario extends Usuario {
    private int idFuncionario;

    // Construtor UsuarioFuncionario
    public UsuarioFuncionario(String nome, String login, String senha) {

        super(nome, login, senha);  // Herda os dados presentes na classe Usuario
        setIdFuncionario(idFuncionario);    // Valida o ID da classe
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {

        // Verifica se o ID é maior que zero
        if (idFuncionario <= 0){
            System.out.println("Acesso Negado. O ID do funcionário deve ser maior que zero.");
        }
        else {
            this.idFuncionario = idFuncionario; // Salva o ID se ele for válido
        }
    }
}
