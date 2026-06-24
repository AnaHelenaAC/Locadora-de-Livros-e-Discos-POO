package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.ConnectionFactory;
import br.edu.ufersa.locadora.model.DAO.RegistroDAO;
import br.edu.ufersa.locadora.model.DAO.UsuarioDAO;
import br.edu.ufersa.locadora.model.entities.Registro;
import br.edu.ufersa.locadora.model.entities.Aluguel;
import br.edu.ufersa.locadora.model.entities.ItemAluguel;
import br.edu.ufersa.locadora.model.entities.Usuario;
import br.edu.ufersa.locadora.exceptions.RegistroException;
import br.edu.ufersa.locadora.exceptions.UsuarioException;

import java.util.List;
import java.time.LocalDate;

public class RegistroService {

    private final RegistroDAO dao;
    private final UsuarioDAO usuarioDAO;
    private final AluguelService aluguelService;

    public RegistroService(ConnectionFactory connectionFactory) {
        this.dao = new RegistroDAO(connectionFactory);
        this.usuarioDAO = new UsuarioDAO(connectionFactory);
        this.aluguelService = new AluguelService(connectionFactory);
    }

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

    public void salvarFuncionarioNoSistema(Usuario fun) throws RegistroException {
        if (fun == null) {
            throw new RegistroException("Funcionário inválido!");
        }
        if (fun.isGerente()) {
            throw new RegistroException("Este método destina-se apenas a funcionários, não ao gerente!");
        }
        try {
            usuarioDAO.Create(fun);
        } catch (UsuarioException e) {
            throw new RegistroException("Erro ao salvar funcionário: " + e.getMessage());
        }
    }

    public void registrarAluguel(Registro reg, Aluguel aluguel) throws RegistroException {
        if (reg == null || aluguel == null) {
            throw new RegistroException("Dados inválidos para registrar aluguel!");
        }
        aluguelService.inserir(aluguel);
        reg.registrarAluguel(aluguel);
        dao.Update(reg);
    }

    public void registrarDevolucaoDeItem(Registro reg, Aluguel aluguel, ItemAluguel item,
                                         LocalDate dataDevolucao) throws RegistroException {
        if (reg == null || aluguel == null || item == null || dataDevolucao == null) {
            throw new RegistroException("Dados insuficientes para realizar a devolução do item!");
        }
        double multaAntes = aluguel.getValorMulta();
        aluguelService.finalizarItemEspecifico(aluguel, item, dataDevolucao);
        double multaGeradaPeloItem = aluguel.getValorMulta() - multaAntes;
        if (multaGeradaPeloItem > 0) {
            reg.registrarDevolucaoItem(multaGeradaPeloItem);
        }
        dao.Update(reg);
    }

    public void gerarRelatorioAlugados(String categoria) throws RegistroException {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new RegistroException("A categoria não pode ser vazia!");
        }
        // TODO: implementar geração de relatório
    }

    public double calcularFaturamentoMensal(int mes, int ano) throws RegistroException {
        if (mes < 1 || mes > 12) {
            throw new RegistroException("Mês informado deve estar entre 1 e 12!");
        }
        if (ano <= 0) {
            throw new RegistroException("Ano informado inválido!");
        }
        // TODO: implementar consulta real ao banco por mês/ano
        return 0.0;
    }

    public double calcularFaturamentoMensal(Integer mes, Integer ano) throws RegistroException {
        if (mes == null || ano == null) {
            throw new RegistroException("Mês e ano informados não podem ser nulos!");
        }
        return calcularFaturamentoMensal(mes.intValue(), ano.intValue());
    }
}