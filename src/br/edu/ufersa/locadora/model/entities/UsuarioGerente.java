package br.edu.ufersa.locadora.model.entities;

public class UsuarioGerente extends Usuario {
    private int idGerente;

    public UsuarioGerente(String nome, String login, String senha) {

        super(nome, login, senha);  // Herda os dados presentes na classe Usuario
        setIdGerente(idGerente);    // Salva e valida o ID da classe
    }

    public int getIdGerente() {
        return idGerente;
    }

    public void setIdGerente(int idGerente) {

        // Verifica se o ID é maior que 0
        if (idGerente <= 0){
            System.out.println("Acesso Negado. O ID deve ser maior que 0!");
        }
        else {
            this.idGerente = idGerente;
        }

    }
    
    public void cadastrarFuncionario(UsuarioFuncionario f) {

        System.out.println("Gerente " + this.getNome() + " iniciando processo de cadastro...");
        Registro.salvarFuncionarioNoSistema(f);  // Chama a função da classe Registro
    }
}
