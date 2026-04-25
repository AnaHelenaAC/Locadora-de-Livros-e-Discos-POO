package br.edu.ufersa.Locadora.model.entyties;

public class Usuario {
    private String nome;
    private String login;
    private String senha;

    // Construtor Usuario
    public Usuario(String nome, String login, String senha) {
        setNome(nome);
        setLogin(login);
        setSenha(senha);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        // Se o nome for inválido, apenas avisa na tela
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("O nome não pode ser vazio.");
        } else {
            // Se estiver tudo certo, salva o nome
            this.nome = nome;
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            System.out.println("O login não pode ser vazio.");
        } else if (login.contains(" ")) {
            System.out.println("O login não pode conter espaços.");
        } else {
            this.login = login;
        }
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (senha != null && senha.length() >= 6) {
            this.senha = senha;
        } else {
            System.out.println("A senha deve ter pelo menos 6 caracteres.");
        }
    }


}
