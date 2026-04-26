package br.edu.ufersa.locadora.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Registro {

    // ------------------ Atributos da classe Registro ------------------
    private static List<UsuarioFuncionario> listaFuncionarios = new ArrayList<>();
    private UsuarioGerente gerenteLogado;
    private double faturamentoTotal;
    private List<Aluguel> listaAlugueis;

    // ------------------ Construtor Registro ------------------
    public Registro() {
        this.faturamentoTotal = 0.0;
        this.listaAlugueis = new ArrayList<>();
        this.gerenteLogado = null;
    }

    // ------------------ Métodos da classe Registro ------------------
    public static List<UsuarioFuncionario> getListaFuncionarios() {
        return new ArrayList<>(listaFuncionarios);
    }

    public static void setListaFuncionarios(List<UsuarioFuncionario> listaFuncionarios) {
        // Verificando se a lista está vazia
        if (listaFuncionarios != null) {
            Registro.listaFuncionarios = new ArrayList<>(listaFuncionarios);
        }
    }

    public UsuarioGerente getGerenteLogado() {
        return gerenteLogado;
    }

    public void setGerenteLogado(UsuarioGerente gerenteLogado) {
        // Verificando se existe um gerente válido sendo passado antes de logar
        if (gerenteLogado != null) {
            this.gerenteLogado = gerenteLogado;
        }
    }

    public double getFaturamentoTotal() {
        return faturamentoTotal;
    }

    public void setFaturamentoTotal(double faturamentoTotal) {
        // Verificando se o número do faturamento é positivo
        if (faturamentoTotal >= 0) {
            this.faturamentoTotal = faturamentoTotal;
        } else {
            System.out.println("Negado! Insira um valor positivo.");
        }
    }

    public List<Aluguel> getListaAlugueis() {
        return new ArrayList<>(this.listaAlugueis);
    }

    public void setListaAlugueis(List<Aluguel> listaAlugueis) {
        if (listaAlugueis != null) {
            this.listaAlugueis = new ArrayList<>(listaAlugueis);
        }
    }

    // Adiciona um novo funcionário
    public static void salvarFuncionarioNoSistema(UsuarioFuncionario f) {
        // V
        if (f != null) {
            listaFuncionarios.add(f);
            System.out.println(">>> [BANCO] Funcionário " + f.getNome() + " salvo com segurança.");
        }
    }

    // Realiza um aluguem para um cliente de um item do acervo
    public void registrarAluguel(Cliente c, ItemAcervo i) {

        // Verifica se o cliente e o item existem nos dados
        if (c != null && i != null) {
            System.out.println("Iniciando aluguel do item para o cliente " + c.getNome() + "...");

            String valorEmTexto = i.getValor().replace(",", ".");
            double valorConvertido = Double.parseDouble(valorEmTexto);

            this.faturamentoTotal += valorConvertido;
            System.out.println("Aluguel registrado. Faturamento atualizado!");

        } else {
            System.out.println("Negado! Cliente ou Item inválidos para aluguel.");
        }
    }

    // Registra a volta de um item que havia sido alugado
    public void registrarDevolucao(ItemAcervo i) {
        if (i != null) {
            System.out.println("Registrando devolução do item...");
        }
    }

    // Gera e exibe um relatório com os itens alugados filtrados por categoria
    public void gerarRelatorioAlugados(String categoria) {
        System.out.println("SISTEMA: Gerando relatório da categoria -> " + categoria);
    }

    // Calcula a soma financeira de todos os aluguéis feitos em um mês específico
    public double calcularFaturamentoMensal(int mes) {
        System.out.println("SISTEMA: Calculando faturamento do mês " + mes);
        return 0.0;
    }
}