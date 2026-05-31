package br.edu.ufersa.locadora;

import br.edu.ufersa.locadora.model.entities.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        //TESTE CLIENTE
        Cliente c1 = new Cliente("Paulo", "Rua São Paulo", "paulo@email.com", "999999999", "12345678901");
        Cliente c2 = new Cliente("Gadelha", "Rua Arredonda Para Baixo", "gadelha@email.com", "010203040", "45678901234");

        c1.cadastrar();
        c2.cadastrar();

        System.out.println("\nLista de Clientes:");
        for (Cliente c : Cliente.getClientes()) {
            System.out.println(c);
        }

        c1.excluir();

        c2.alterar("Cool Gadelha", "Rua do Ponto Extra", "coolgadelha@email.com", "992345678");

        System.out.println("\nLista de Clientes após uma exclusão e uma edição:");
        for (Cliente c : Cliente.getClientes()) {
            System.out.println(c);
        }

        //LIVRO
        Livro livro = new Livro("livreto", "alguem", "branco", 10.0, "01/01/2008", 5, false, 464);

        //TESTE DE ALUGUEL
        //atrasado
        Aluguel a1 = new Aluguel(c2, livro, LocalDate.now().minusDays(5), 3);
        a1.registrar();

        //ativo
        Aluguel a2 = new Aluguel(c2, livro, LocalDate.now(), 3);
        a2.registrar();

        System.out.println("\nLista de Aluguéis:");
        for (Aluguel a : Aluguel.getAlugueis()) {
            System.out.println(a);
        }

        System.out.println("\nStatus aluguel1: " + a1.getStatus());
        System.out.println("Status aluguel2: " + a2.getStatus());

        a1.finalizarAluguel();

        double valorFinal = a1.calcularValorFinal();
        System.out.println("\nValor final do aluguel1 após finalizado com atraso: R$ " + valorFinal);

        //FILTRO POR STATUS
        System.out.println("\nAluguéis FINALIZADOS:");
        for (Aluguel a : Aluguel.getAlugueis()) {
            if (a.getStatus().equals("FINALIZADO")) {
                System.out.println(a);
            }
        }

        System.out.println("\nAluguéis ATRASADOS:");
        for (Aluguel a : Aluguel.getAlugueis()) {
            if (a.getStatus().equals("ATRASADO")) {
                System.out.println(a);
            }
        }

        System.out.println("\nAluguéis ATIVOS:");
        for (Aluguel a : Aluguel.getAlugueis()) {
            if (a.getStatus().equals("ATIVO")) {
                System.out.println(a);
            }
        }
    }
}