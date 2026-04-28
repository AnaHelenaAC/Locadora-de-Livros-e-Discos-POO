package br.edu.ufersa.locadora.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    //ATRIBUTOS
    private static List<Cliente> clientes = new ArrayList<>();
    private String nome;
    private String endereco;
    private String email;
    private String telefone;
    private final String cpf;

    //CONSTRUTOR
    public Cliente(String nome, String endereco, String email, String telefone, String cpf) {
        setNome(nome);
        setEndereco(endereco);
        setEmail(email);
        setTelefone(telefone);

        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("CPF inválido!");
        }
        this.cpf = cpf;
    }

    //MÉTODOS
    //cadastrar
    public void cadastrar() {
        clientes.add(this);
    }//adiciona o cliente à lista de clientes

    //alterar
    public void alterar(String novoNome, String novoEndereco, String novoEmail, String novoTelefone) {
        setNome(novoNome);
        setEndereco(novoEndereco);
        setEmail(novoEmail);
        setTelefone(novoTelefone);
    }//altera os dados do cliente, exceto o CPF

    //excluir
    public void excluir() {
        clientes.remove(this);
    } //remove o cliente da lista de clientes

    //GETTERS
    public String getNome() {return nome;}
    public String getEndereco() {return endereco;}
    public String getCpf() {return cpf;}
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public static List<Cliente> getClientes() {return clientes;}//acessa a lista de clientes

    //SETTERS
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

    public void setEmail(String novoEmail) {
        if (novoEmail == null || novoEmail.isEmpty()) {
            throw new IllegalArgumentException("Email inválido!");
        }
        this.email = novoEmail;
    }
    
    public void setTelefone(String novoTelefone) {
        if (novoTelefone == null || novoTelefone.isEmpty()) {
            throw new IllegalArgumentException("Telefone inválido!");
        }
        this.telefone = novoTelefone;
    }

    //TO STRING
    @Override
    public String toString() {
        return nome + " - " + endereco + " - " + email + " - " + telefone + " - " + cpf;
    }
}