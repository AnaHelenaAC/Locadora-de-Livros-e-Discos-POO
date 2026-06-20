package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.AluguelDAO;
import br.edu.ufersa.locadora.model.DAO.ItemAluguelDAO;
import br.edu.ufersa.locadora.model.entities.Aluguel;
import br.edu.ufersa.locadora.model.entities.Carrinho;
import br.edu.ufersa.locadora.model.entities.ItemAluguel;

import java.util.List;

public class AluguelService {

    private final AluguelDAO aluguelDAO;
    private final ItemAluguelDAO itemAluguelDAO;

    public AluguelService() {
        aluguelDAO = new AluguelDAO();
        itemAluguelDAO = new ItemAluguelDAO();
    }

    // método para criar um novo aluguel
    public Aluguel criarAluguel(Carrinho carrinho, int diasAlugados) {

        if (carrinho == null || carrinho.getItensNoCarrinho().isEmpty()) {
            throw new IllegalArgumentException("Carrinho inválido ou vazio.");
        }
        return new Aluguel(carrinho, diasAlugados);
    }

    // salva aluguel e itens no banco
    public Aluguel inserir(Aluguel aluguel) {

        if (aluguel == null) {
            throw new IllegalArgumentException("Aluguel inválido.");
        }
        aluguel = aluguelDAO.Create(aluguel);

        if (aluguel == null) {
            throw new IllegalStateException("Erro ao salvar aluguel.");
        }

        List<ItemAluguel> itens = aluguel.getItensAlugados();

        for (ItemAluguel item : itens) {
            ItemAluguel salvo = itemAluguelDAO.Create(item, aluguel.getId());

            if (salvo == null) {
                throw new IllegalStateException("Erro ao salvar item do aluguel.");
            }
        }
        return aluguel;
    }

    // busca aluguel por id
    public Aluguel buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        Aluguel aluguel = aluguelDAO.Read(id);

        if (aluguel != null) {
            List<ItemAluguel> itens = itemAluguelDAO.Read(id); // Busca na tabela item_aluguel
            aluguel.adicionarItens(itens); // Junta os itens no objeto
        }
        return aluguel;
    }

    // lista todos os aluguéis ativos
    public List<Aluguel> listarTodos() {
        List<Aluguel> alugueis = aluguelDAO.ReadAll();

        for (Aluguel aluguel : alugueis) {
            List<ItemAluguel> itens = itemAluguelDAO.Read(aluguel.getId());
            aluguel.adicionarItens(itens);
        }
        return alugueis;
    }

    // atualiza aluguel
    public Aluguel atualizar(Aluguel aluguel) {

        if (aluguel == null || aluguel.getId() <= 0) {
            throw new IllegalArgumentException("Aluguel inválido.");
        }
        return aluguelDAO.Update(aluguel);
    }

    // remove aluguel ativo
    public boolean cancelar(Aluguel aluguel) {

        if (aluguel == null) {
            throw new IllegalArgumentException("Aluguel inválido.");
        }
        return aluguelDAO.Delete(aluguel);
    }
}