package br.edu.ufersa.locadora.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Registro {

    private static List<UsuarioFuncionario> listaFuncionarios = new ArrayList<>();
    private Long idRegistro;
    private UsuarioGerente gerenteLogado;
    private double faturamentoTotal;
    private List<Aluguel> listaAlugueis;

    public Registro() {
        this.faturamentoTotal = 0.0;
        this.listaAlugueis = new ArrayList<>();
        this.gerenteLogado = null;
    }

    public Long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Long idRegistro) {
        if (this.idRegistro == null) {
            this.idRegistro = idRegistro;
        }
    }

    public static List<UsuarioFuncionario> getListaFuncionarios() {
        return new ArrayList<>(listaFuncionarios);
    }

    public static void setListaFuncionarios(List<UsuarioFuncionario> listaFuncionarios) {
        if (listaFuncionarios != null) {
            Registro.listaFuncionarios = new ArrayList<>(listaFuncionarios);
        }
    }

    public UsuarioGerente getGerenteLogado() {
        return gerenteLogado;
    }

    public void setGerenteLogado(UsuarioGerente gerenteLogado) {
        if (gerenteLogado != null) {
            this.gerenteLogado = gerenteLogado;
        }
    }

    public double getFaturamentoTotal() {
        return faturamentoTotal;
    }

    public void setFaturamentoTotal(double faturamentoTotal) {
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
            System.out.println("Iniciando aluguel do item para o cliente " + c.getNome() + "...");
            this.faturamentoTotal += i.getValor();
            System.out.println("Aluguel registrado. Faturamento atualizado!");
        } else {
            System.out.println("Negado! Cliente ou Item inválidos para aluguel.");
        }
    }

    public void registrarDevolucao(ItemAcervo i) {
        if (i != null) {
            System.out.println("Registrando devolução do item...");
        }
    }

    public void generarRelatorioAlugados(String categoria) {
        System.out.println("SISTEMA: Gerando relatório da categoria -> " + categoria);
    }

    public double calcularFaturamentoMensal(int mes) {
        System.out.println("SISTEMA: Calculando faturamento do mês " + mes);
        return 0.0;
    }
}