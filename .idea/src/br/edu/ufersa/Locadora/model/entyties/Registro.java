package br.edu.ufersa.Locadora.model.entyties;

import java.util.ArrayList;
import java.util.List;

public class Registro {

    private static List<UsuarioFuncionario> listaFuncionarios = new ArrayList<>();
    private UsuarioGerente gerenteLogado;
    private double faturamentoTotal;

    public static void salvarFuncionarioNoSistema(UsuarioFuncionario f) {
        listaFuncionarios.add(f);
        System.out.println(">>> [BANCO DE DADOS] Funcionário " + f.getNome() + " salvo com sucesso!");
    }

    public static List<UsuarioFuncionario> getTodosFuncionarios() {
        return listaFuncionarios;
    }
}