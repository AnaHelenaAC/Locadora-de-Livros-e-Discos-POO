package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.AluguelDAO;
import br.edu.ufersa.locadora.model.DAO.ConnectionFactory;
import br.edu.ufersa.locadora.model.DAO.ItemAluguelDAO;
import br.edu.ufersa.locadora.model.DAO.DiscoDAO;
import br.edu.ufersa.locadora.model.DAO.LivroDAO;
import br.edu.ufersa.locadora.model.entities.Aluguel;
import br.edu.ufersa.locadora.model.entities.Carrinho;
import br.edu.ufersa.locadora.model.entities.ItemAluguel;

import java.time.LocalDate;
import java.util.List;

public class AluguelService {

    private final AluguelDAO aluguelDAO;
    private final ItemAluguelDAO itemAluguelDAO;
    private final DiscoDAO discoDAO;
    private final LivroDAO livroDAO;

    public AluguelService(ConnectionFactory connectionFactory) {
        aluguelDAO = new AluguelDAO(connectionFactory);
        itemAluguelDAO = new ItemAluguelDAO(connectionFactory);
        discoDAO = new DiscoDAO(connectionFactory);
        livroDAO = new LivroDAO(connectionFactory);
    }

    // metodo para criar um novo aluguel em memória
    public Aluguel criarAluguel(Carrinho carrinho, int diasAlugados) {
        if (carrinho == null || carrinho.getItensNoCarrinho().isEmpty()) {
            throw new IllegalArgumentException("Carrinho inválido ou vazio.");
        }
        return Aluguel.builder().fromCarrinho(carrinho, diasAlugados).build();
    }

    // salva aluguel e itens no banco, e atualiza estoque
    public Aluguel inserir(Aluguel aluguel) {

        if (aluguel == null) {
            throw new IllegalArgumentException("Aluguel inválido.");
        }

        for (ItemAluguel item : aluguel.getItensAlugados()) {
            if (item.getItem().getQtdItens() <= 0) {
                throw new IllegalStateException("O item '" + item.getItem().getTitulo() + "' está esgotado!");
            }
        }
        aluguel = aluguelDAO.Create(aluguel);

        if (aluguel != null) {
            for (ItemAluguel item : aluguel.getItensAlugados()) {
                // persiste o item do aluguel
                itemAluguelDAO.Create(item, aluguel.getId());
                // atualiza estoque
                int novaQuantidade = item.getItem().getQtdItens() - 1;
                item.getItem().setQtdItens(novaQuantidade);
                if (item.getItem().getIsDisco()) {
                    discoDAO.updateQuantidade(item.getItem().getID(), novaQuantidade);
                } else {
                    livroDAO.updateQuantidade(item.getItem().getID(), novaQuantidade);
                }
            }
        } else {
            throw new IllegalStateException("Erro ao salvar aluguel.");
        }

        return aluguel;
    }

    public Aluguel buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return aluguelDAO.Read(id);
    }

    public List<Aluguel> listarAtivos() {
        try {
            return aluguelDAO.ReadAtivos();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Aluguel> listarTodos() {
        return aluguelDAO.ReadAll();
    }

    // atualiza aluguel (valor_multa)
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

    // Finaliza TODOS os itens de uma vez (devolução completa)
    public Aluguel finalizarAluguelCompleto(Aluguel aluguel, LocalDate dataDevolucao) {
        if (aluguel == null) {
            throw new IllegalArgumentException("Aluguel inválido.");
        }

        for (ItemAluguel item : aluguel.getItensAlugados()) {
            if (item.getDataFim() == null) {
                devolverItemAoEstoque(item);
            }
        }

        aluguel.finalizarAluguelCompleto(dataDevolucao);

        // persiste a data_fim de cada item devolvido
        for (ItemAluguel item : aluguel.getItensAlugados()) {
            itemAluguelDAO.Update(item);
        }

        return aluguelDAO.Update(aluguel);
    }

    // Finaliza apenas um item específico (devolução parcial)
    public Aluguel finalizarItemEspecifico(Aluguel aluguel, ItemAluguel item, LocalDate dataDevolucao) {
        if (aluguel == null || item == null) {
            throw new IllegalArgumentException("Parâmetros inválidos para finalização.");
        }

        if (item.getDataFim() != null) {
            throw new IllegalStateException("Este item já foi devolvido anteriormente.");
        }

        devolverItemAoEstoque(item);
        aluguel.finalizarItemEspecifico(item, dataDevolucao);

        // persiste a data_fim deste item específico
        itemAluguelDAO.Update(item);

        return aluguelDAO.Update(aluguel);
    }

    private void devolverItemAoEstoque(ItemAluguel item) {
        int novaQuantidade = item.getItem().getQtdItens() + 1;
        item.getItem().setQtdItens(novaQuantidade);

        if (item.getItem().getIsDisco()) {
            discoDAO.updateQuantidade(item.getItem().getID(), novaQuantidade);
        } else {
            livroDAO.updateQuantidade(item.getItem().getID(), novaQuantidade);
        }
    }
}