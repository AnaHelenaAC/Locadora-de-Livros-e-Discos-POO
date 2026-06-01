package br.edu.ufersa.locadora.model.entities;

import br.edu.ufersa.locadora.exceptions.RegistroException;
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

    public static void salvarFuncionarioNoSistema(UsuarioFuncionario fun) throws RegistroException {
        if (fun == null) {
            throw new RegistroException("Não é possível salvar um funcionário nulo no sistema.");
        }
        listaFuncionarios.add(fun);
    }

    public void registrarAluguel(Aluguel aluguel) throws RegistroException {
        if (aluguel == null) {
            throw new RegistroException("Negado! Aluguel inválido.");
        }
        listaAlugueis.add(aluguel);

        faturamentoTotal += aluguel.getValorBase();
        faturamentoTotal += aluguel.getValorMulta();
    }

    public void registrarDevolucao(ItemAcervo ite) throws RegistroException {
        if (ite == null) {
            throw new RegistroException("Item inválido para devolução.");
        }
    }

    public void generarRelatorioAlugados(String categoria) throws RegistroException {
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