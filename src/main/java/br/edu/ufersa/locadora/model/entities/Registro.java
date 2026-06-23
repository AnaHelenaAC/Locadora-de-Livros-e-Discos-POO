package br.edu.ufersa.locadora.model.entities;

import br.edu.ufersa.locadora.exceptions.RegistroException;
import java.util.ArrayList;
import java.util.List;

public class Registro {

    private List<Usuario> listaFuncionarios = new ArrayList<>();

    private Long idRegistro;
    private Usuario gerenteLogado;
    private double faturamentoTotal;
    private List<Aluguel> listaAlugueis;

    public Registro() {
        this.faturamentoTotal = 0.0;
        this.listaAlugueis = new ArrayList<>();
        this.listaFuncionarios = new ArrayList<>();
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

    public List<Usuario> getListaFuncionarios() {
        return new ArrayList<>(listaFuncionarios);
    }

    public void setListaFuncionarios(List<Usuario> listaFuncionarios) {
        if (listaFuncionarios != null) {
            this.listaFuncionarios = new ArrayList<>(listaFuncionarios);
        }
    }

    public Usuario getGerenteLogado() {
        return gerenteLogado;
    }

    public void setGerenteLogado(Usuario gerenteLogado) throws RegistroException {
        if (gerenteLogado == null) {
            throw new RegistroException("O gerente logado não pode ser nulo.");
        }
        if (!gerenteLogado.isGerente()) {
            throw new RegistroException("O usuário informado não é o gerente do sistema.");
        }
        this.gerenteLogado = gerenteLogado;
    }

    public double getFaturamentoTotal() {
        return faturamentoTotal;
    }

    public void setFaturamentoTotal(double faturamentoTotal) throws RegistroException {
        if (faturamentoTotal >= 0) {
            this.faturamentoTotal = faturamentoTotal;
        } else {
            throw new RegistroException("Negado! Insira um valor positivo de faturamento.");
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

    public void salvarFuncionarioNoSistema(Usuario fun) throws RegistroException {
        if (fun == null) {
            throw new RegistroException("Não é possível salvar um funcionário nulo no sistema.");
        }
        if (fun.isGerente()) {
            throw new RegistroException("O gerente não pode ser adicionado como funcionário.");
        }
        listaFuncionarios.add(fun);
    }

    public void registrarAluguel(Aluguel aluguel) throws RegistroException {
        if (aluguel == null) {
            throw new RegistroException("Negado! Aluguel inválido.");
        }
        listaAlugueis.add(aluguel);
        faturamentoTotal += aluguel.getValorBase();
    }

    public void registrarDevolucaoItem(double valorMultaDoItem) throws RegistroException {
        if (valorMultaDoItem < 0) {
            throw new RegistroException("Valor de multa inválido.");
        }
        faturamentoTotal += valorMultaDoItem;
    }

    public void gerarRelatorioAlugados(String categoria) throws RegistroException {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new RegistroException("A categoria do relatório não pode ser vazia.");
        }
    }

    public double calcularFaturamentoMensal(int mes) throws RegistroException {
        if (mes < 1 || mes > 12) {
            throw new RegistroException("Mês inválido informado para o cálculo.");
        }
        return 0.0;
    }
}