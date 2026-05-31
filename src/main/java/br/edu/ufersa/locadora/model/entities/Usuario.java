package br.edu.ufersa.locadora.model.entities;

import br.edu.ufersa.locadora.exceptions.SemNomeException;
import br.edu.ufersa.locadora.exceptions.UsuarioException;

public class Usuario {
    private String nome;
    private String login;
    private String senha;
    private Long id;
    private boolean isGerente;

    public Usuario(){
        this.isGerente = false;
    }

    public Usuario(String nome, String login, String senha) throws SemNomeException, UsuarioException {
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

    public void setLogin(String login) throws UsuarioException {
        if (login == null || login.trim().isEmpty()) {
            throw new UsuarioException("O login não pode ser vazio.");
        } else if (login.contains(" ")) {
            throw new UsuarioException("O login não pode conter espaços.");
        } else {
            this.login = login;
        }
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) throws UsuarioException {
        if (senha != null && senha.length() >= 6 && senha.length() <= 100) {
            this.senha = senha;
        } else {
            throw new UsuarioException("A senha deve ter pelo menos 6 caracteres e, no máximo, 100 caracteres.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) throws UsuarioException {
        if (this.id == null) {
            this.id = id;
        } else {
            throw new UsuarioException("Aviso de Segurança: O ID deste usuário já foi definido e não pode ser alterado!");
        }
    }

    public boolean isGerente() {
        return isGerente;
    }

    public void setGerente(boolean isGerente) {
        this.isGerente = isGerente;
    }

    public boolean fazerLogin(String loginRecebido, String senhaRecebida) throws UsuarioException {
        if (this.login == null || !this.login.equals(loginRecebido)){
            throw new UsuarioException("Acesso Negado. Login incorreto!");
        }
        if (this.senha == null || !this.senha.equals(senhaRecebida)){
            throw new UsuarioException("Acesso Negado. Senha incorreta!");
        }
        return true;
    }
}