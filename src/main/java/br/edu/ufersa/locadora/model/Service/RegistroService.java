package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.RegistroDAO;
import br.edu.ufersa.locadora.model.DAO.UsuarioFuncionarioDAO;
import br.edu.ufersa.locadora.model.entities.Registro;
import br.edu.ufersa.locadora.model.entities.UsuarioFuncionario;
import br.edu.ufersa.locadora.model.entities.Cliente;
import br.edu.ufersa.locadora.model.entities.ItemAcervo;
import br.edu.ufersa.locadora.exceptions.RegistroException;
import java.util.List;

public class RegistroService {

    private RegistroDAO dao = new RegistroDAO();
    private UsuarioFuncionarioDAO funcionarioDAO = new UsuarioFuncionarioDAO();

    public Registro salvar(Registro reg) throws RegistroException {
        if (reg == null) {
            throw new RegistroException("Não é possível salvar um registro nulo!");
        }
        return dao.Create(reg);
    }

    public Registro buscarPorId(Long id) throws RegistroException {
        if (id == null) {
            throw new RegistroException("O ID informado para busca não pode ser nulo!");
        }
        List<Registro> resultados = dao.Read(String.valueOf(id));
        if (!resultados.isEmpty()) {
            return resultados.get(0);
        }
        throw new RegistroException("Nenhum registro encontrado com o ID: " + id);
    }

    public boolean atualizar(Registro reg) throws RegistroException {
        if (reg == null || reg.getIdRegistro() == null) {
            throw new RegistroException("Registro inválido para atualização!");
        }
        return dao.Update(reg);
    }

    public boolean deletar(Registro reg) throws RegistroException {
        if (reg == null || reg.getIdRegistro() == null) {
            throw new RegistroException("Registro inválido para exclusão!");
        }
        return dao.Delete(reg);
    }

    public void salvarFuncionarioNoSistema(UsuarioFuncionario fun) throws RegistroException {
        if (fun == null) {
            throw new RegistroException("Funcionário inválido!");
        }
        funcionarioDAO.Create(fun);
        Registro.salvarFuncionarioNoSistema(fun);
    }

    public void registrarAluguel(Registro reg, Cliente cli, ItemAcervo ite) throws RegistroException {
        if (reg == null || cli == null || ite == null) {
            throw new RegistroException("Dados insuficientes para realizar o aluguel!");
        }
        reg.registrarAluguel(cli, ite);
        dao.Update(reg);
    }

    public void registrarDevolucao(Registro reg, ItemAcervo ite) throws RegistroException {
        if (reg == null || ite == null) {
            throw new RegistroException("Dados insuficientes para realizar a devolução!");
        }
        reg.registrarDevolucao(ite);
        dao.Update(reg);
    }

    public void gerarRelatorioAlugados(String categoria) throws RegistroException {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new RegistroException("A categoria não pode ser vazia!");
        }
    }

    public double calcularFaturamentoMensal(int mes) throws RegistroException {
        if (mes < 1 || mes > 12) {
            throw new RegistroException("Mês informado deve estar entre 1 e 12!");
        }
        return 0.0;
    }
}