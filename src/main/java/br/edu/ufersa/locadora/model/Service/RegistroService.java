package br.edu.ufersa.locadora.model.service;

import br.edu.ufersa.locadora.model.DAO.RegistroDAO;
import br.edu.ufersa.locadora.model.DAO.UsuarioFuncionarioDAO;
import br.edu.ufersa.locadora.model.entities.Registro;
import br.edu.ufersa.locadora.model.entities.UsuarioFuncionario;
import br.edu.ufersa.locadora.model.entities.Cliente;
import br.edu.ufersa.locadora.model.entities.ItemAcervo;
import java.util.List;

public class RegistroService {

    private RegistroDAO dao = new RegistroDAO();
    private UsuarioFuncionarioDAO funcionarioDAO = new UsuarioFuncionarioDAO();

    // Funções referentes ao CRUD usado no DAO
    public Registro salvar(Registro reg) {
        return dao.Create(reg);
    }

    public Registro buscarPorId(Long id) {
        List<Registro> resultados = dao.Read(String.valueOf(id));
        if (!resultados.isEmpty()) {
            return resultados.get(0);
        }
        return null;
    }

    public boolean atualizar(Registro reg) {
        return dao.Update(reg);
    }

    public boolean deletar(Registro reg) {
        return dao.Delete(reg);
    }

    // Demais métodos
    public void salvarFuncionarioNoSistema(UsuarioFuncionario f) {
        if (f != null) {
            funcionarioDAO.Create(f);
            Registro.salvarFuncionarioNoSistema(f);
        }
    }

    public void registrarAluguel(Registro reg, Cliente c, ItemAcervo i) {
        if (reg != null && c != null && i != null) {
            reg.registrarAluguel(c, i);
            dao.Update(reg);
        }
    }

    public void registrarDevolucao(ItemAcervo i) {
        if (i != null) {
            System.out.println("Registrando devolução do item no sistema...");
        }
    }

    public void gerarRelatorioAlugados(String categoria) {
        System.out.println("SISTEMA SERVICE: Buscando dados para categoria -> " + categoria);
    }

    public double calcularFaturamentoMensal(int mes) {
        System.out.println("SISTEMA SERVICE: Calculando faturamento do mês " + mes);
        return 0.0;
    }
}