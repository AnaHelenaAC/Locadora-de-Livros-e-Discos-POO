package br.edu.ufersa.Locadora.model.entyties;

// Classe "Mãe" Usuario
public class Usuario {
    private String nome;
    private String login;
    private String senha;

    // Construtor Usuário
    public Usuario(String nome, String login, String senha) {
        setNome(nome);
        setLogin(login);
        setSenha(senha);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {

        // Avisa na tela se o nome for inválido
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

        // Verifica se o local do login está vazio
        if (login == null || login.trim().isEmpty()) {
            System.out.println("O login não pode ser vazio.");

        // Verifica se existem espaços no login
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

        // Verifica se a senha digitada apresenta 6 caracteres
        if (senha != null && senha.length() >= 6) {
            this.senha = senha;
        } else {
            System.out.println("A senha deve ter pelo menos 6 caracteres.");
        }
    }

    // Método responsável por fazer o login (retorna verdadeiro ou falso)
    public boolean fazerLogin(String loginRecebido, String senhaRecebida){
        boolean loginCorreto;
        boolean senhaCorreta;

        // Verificando a igualdade das informações recebidas (login e senha)
        loginCorreto = this.getLogin().equals(loginRecebido);
        senhaCorreta = this.getSenha().equals(senhaRecebida);

        // Testando se o login digitado está incoerente
        if (loginCorreto == false){
            System.out.println("Acesso Negado. Login incorreto!");
            return false;
        }
        // Testando se a senha está incoerente
        else if (senhaCorreta == false){
            System.out.println("Acesso Negado. Senha incorreta!");
            return false;
        }
        // Caso a senha e o login estejam corretos
        else {
            System.out.println("Acesso liberado.");
            return true;
        }

    }

}
