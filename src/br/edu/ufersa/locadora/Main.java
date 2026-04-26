package br.edu.ufersa.locadora;

import br.edu.ufersa.locadora.model.entities.Cliente;

public class Main {
    public static void main(String[] args) {

        //cria lista de clientes e testa os metodos.
        Cliente c1 = new Cliente("Gadelha", "Rua Nota 7", "100.010.110-01");

        c1.cadastrar();
        c1.alterar("Cool Gadelha", "Rua Nota 10");
        c1.excluir();
    }
}

