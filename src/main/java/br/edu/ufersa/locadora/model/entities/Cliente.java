package br.edu.ufersa.locadora.model.entities;

public class Cliente {
    // ATRIBUTOS
    private final String cpf;
    private String nome;
    private String endereco;

    // CONSTRUTOR
    public Cliente(String cpf, String nome, String endereco) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("CPF inválido!");
        }

        this.cpf = cpf;
        setNome(nome);
        setEndereco(endereco);
    }

    // METODO alterar
    public void alterar(String novoNome, String novoEndereco) {
        setNome(novoNome);
        setEndereco(novoEndereco);
    }

    // GETTERS E SETTERS
    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setNome(String novoNome) {
        if (novoNome == null || novoNome.isEmpty()) {
            throw new IllegalArgumentException("Nome inválido!");
        }
        this.nome = novoNome;
    }

    public void setEndereco(String novoEndereco) {
        if (novoEndereco == null || novoEndereco.isEmpty()) {
            throw new IllegalArgumentException("Endereço inválido!");
        }
        this.endereco = novoEndereco;
    }
}