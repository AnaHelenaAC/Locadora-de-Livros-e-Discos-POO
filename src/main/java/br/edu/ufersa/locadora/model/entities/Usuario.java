package br.edu.ufersa.locadora.model.entities;
import br.edu.ufersa.locadora.exceptions.SemNomeException;

// Classe "Mãe" Usuario
public class Usuario {
    private String nome;
    private String login;
    private String senha;
    private Long id;
    private boolean isGerente;

    public Usuario(){
        this.isGerente = false;
    }

    public Usuario(String nome, String login, String senha) throws SemNomeException {
        setNome(nome);
        setLogin(login);
        setSenha(senha);
        this.isGerente = false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws SemNomeException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new SemNomeException("Não existe Usuário sem nome!");
        } else {
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
        if (senha.length() >= 6 && senha.length() <= 100) {
            this.senha = senha;
        } else {
            System.out.println("A senha deve ter pelo menos 6 caracteres e, no máximo, 100 caracteres.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.getId() == null) {
            this.id = id;
        } else {
            System.out.println("Aviso de Segurança: O ID deste usuário já foi definido e não pode ser alterado!");
        }
    }

    public boolean isGerente() {
        return isGerente;
    }

    public void setGerente(boolean isGerente) {
        this.isGerente = isGerente;
    }

    public boolean fazerLogin(String loginRecebido, String senhaRecebida){
        boolean loginCorreto;
        boolean senhaCorreta;

        loginCorreto = this.getLogin().equals(loginRecebido);
        senhaCorreta = this.getSenha().equals(senhaRecebida);

        if (loginCorreto == false){
            System.out.println("Acesso Negado. Login incorreto!");
            return false;
        }
        else if (senhaCorreta == false){
            System.out.println("Acesso Negado. Senha incorreta!");
            return false;
        }
        else {
            System.out.println("Acesso liberado.");
            return true;
        }
    }
}