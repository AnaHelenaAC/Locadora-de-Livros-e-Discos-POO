package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.DiscoDAO;
import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.exceptions.DiscoException;
import java.util.List;

public class DiscoService {
    private final DiscoDAO discoDAO;

    public DiscoService(DiscoDAO discoDAO) {
        this.discoDAO = discoDAO;
    }

    public void adicionarDisco(Disco disco) {
        if (disco == null) throw new DiscoException("Não é possível enviar um formulário vazio.");
        if (disco.getTitulo().length() > 100 || disco.getCriadoPor().length() > 100 || disco.getGenero().length() > 100)
            throw new DiscoException("Campos de texto do formulário (Título, Criado Por e Gênero) não podem conter mais de 100 caracteres.");
        if (disco.getValor() < 0 || disco.getQtdItens() < 0 || disco.getDuracao() < 0)
            throw new DiscoException("Os campos Valor, Quantidade de Itens e Duração não podem ter valores negativos.");

        discoDAO.create(disco);
        System.out.println(disco.getTitulo() + " adicionado com sucesso."); // Corrigido prinln para println
    }

    public List<Disco> lerDisco() {
        // Presumindo que você criou um método read() sem parâmetros no DAO
        List<Disco> discos = discoDAO.read(); // Salva na variável para evitar duas idas ao banco
        if (discos == null || discos.isEmpty()) {
            throw new DiscoException("Nenhum disco foi encontrado no banco de dados.");
        }
        System.out.println("Disco(s) encontrado(s).");
        return discos;
    }

    public Disco lerDiscoPorID(String ID) {
        Disco disco = discoDAO.readByID(ID); // Busca apenas uma vez
        if (disco == null) {
            throw new DiscoException("Nenhum disco com o ID utilizado (" + ID + ") foi encontrado no banco de dados.");
        }
        System.out.println("Disco encontrado.");
        return disco;
    }

    public void atualizarDisco(Disco disco) {
        if (disco == null) throw new DiscoException("Não é possível enviar um formulário vazio.");

        // Corrigido: Usando disco.getID() em vez de uma variável ID solta
        if (discoDAO.readByID(disco.getID()) == null) {
            throw new DiscoException("Nenhum disco com o ID utilizado (" + disco.getID() + ") foi encontrado no banco de dados.");
        }

        if (disco.getTitulo().length() > 100 || disco.getCriadoPor().length() > 100 || disco.getGenero().length() > 100)
            throw new DiscoException("Campos de texto do formulário (Título, Criado Por e Gênero) não podem conter mais de 100 caracteres.");
        if (disco.getValor() < 0 || disco.getQtdItens() < 0 || disco.getDuracao() < 0)
            throw new DiscoException("Os campos Valor, Quantidade de Itens e Duração não podem ter valores negativos.");

        discoDAO.update(disco);
        System.out.println(disco.getTitulo() + " alterado com sucesso."); // Corrigido prinln para println
    }

    public void apagarDisco(String ID) {
        // Corrigido: Verifica a existência através do readByID, já que o delete retorna void
        if (discoDAO.readByID(ID) == null) {
            throw new DiscoException("Nenhum disco com o ID utilizado (" + ID + ") foi encontrado no banco de dados.");
        }

        discoDAO.delete(ID);
        System.out.println("Disco apagado.");
    }
}