package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.AluguelDAO;
import br.edu.ufersa.locadora.model.DAO.ItemAluguelDAO;
import br.edu.ufersa.locadora.model.entities.Aluguel;
import br.edu.ufersa.locadora.model.entities.Carrinho;
import br.edu.ufersa.locadora.model.entities.ItemAluguel;

import java.time.LocalDate;
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

    // método para salvar o aluguel e seus itens no banco de dados
    public Aluguel inserir(Aluguel aluguel) {

        if (aluguel == null) {
            throw new IllegalArgumentException("Aluguel inválido.");
        }

        // salva aluguel
        aluguel = aluguelDAO.Create(aluguel);

        if (aluguel == null) {
            return null;
        }

        // salva itens
        List<ItemAluguel> itens = aluguel.getItensAlugados();

        for (ItemAluguel item : itens) {
            itemAluguelDAO.Create(item, aluguel.getId());
        }

        return aluguel;
    }

    // método para finalizar o aluguel, calculando multa e atualizando no banco
    public Aluguel finalizarAluguel(Aluguel aluguel, LocalDate dataFim) {

        if (aluguel == null) {
            throw new IllegalArgumentException("Aluguel inválido.");
        }

        if (aluguel.getDataFim() != null) {
            throw new IllegalStateException("Aluguel já finalizado.");
        }

        aluguel.finalizarAluguel(dataFim);

        return aluguelDAO.Update(aluguel);
    }
}