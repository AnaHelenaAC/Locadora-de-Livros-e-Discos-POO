package br.edu.ufersa.locadora.model.entities;

public class Cliente {

    //atributos
    private String nome;
    private String endereco;
    private String cpf;

    //construtor
    public Cliente(String nome, String endereco, String cpf) {
        setNome(nome);
        setEndereco(endereco);
        this.cpf = cpf;
    }

    //metodos
    ///cadastrar
    public void cadastrar() {
        System.out.println("Cliente cadastrado:");
        System.out.println(getNome() + " - " + getEndereco() + " - " + getCpf());
    }

    ///alterar
    public void alterar(String novoNome, String novoEndereco) {
        setNome(novoNome);
        setEndereco(novoEndereco);
         System.out.println("Cliente atualizado:");
         System.out.println(getNome() + " - " + getEndereco() + " - " + getCpf());
    }

    ///excluir
    public void excluir() {
         System.out.println("Cliente excluído:");
         System.out.println(getNome());
    }

    //getters
    public String getNome() {return nome;}
    public String getEndereco() {return endereco;}
    public String getCpf() {return cpf;}

    //setters
    public void setNome(String novoNome) {
        if (novoNome != null && !novoNome.isEmpty()) {this.nome = novoNome;}
    }
    public void setEndereco(String novoEndereco) {
        if (novoEndereco != null && !novoEndereco.isEmpty()) {this.endereco = novoEndereco;}
    }
}