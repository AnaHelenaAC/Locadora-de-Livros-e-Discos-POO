package br.edu.ufersa.Locadora.model.entyties;

import java.util.ArrayList;
import java.util.List;

public class Registro {

    // ------------------ Atributos da classe Registro ------------------
    private static List<UsuarioFuncionario> listaFuncionarios = new ArrayList<>();
    private UsuarioGerente gerenteLogado;
    private double faturamentoTotal;
    private List<Aluguel> listaAlugueis;

    // Construtor Registro
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
        // Verificando se existe um gerente
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

    public static void salvarFuncionarioNoSistema(UsuarioFuncionario f) {
        if (f != null) {
            listaFuncionarios.add(f);
            System.out.println(">>> [BANCO] Funcionário " + f.getNome() + " salvo com segurança.");
        }
    }

    public void registrarAluguel(Cliente c, ItemAcervo i) {
        if (c != null && i != null) {
            System.out.println("SISTEMA: Iniciando aluguel do item para o cliente " + c.getNome() + "...");

            String valorEmTexto = i.getValor().replace(",", ".");

            double valorConvertido = Double.parseDouble(valorEmTexto);

            this.faturamentoTotal += valorConvertido;

            System.out.println("SISTEMA: Aluguel registrado. Faturamento atualizado!");
        } else {
            System.out.println("ERRO: Cliente ou Item inválidos para aluguel.");
        }
    }

}