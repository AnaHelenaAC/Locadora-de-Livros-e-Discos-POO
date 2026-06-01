package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.DiscoDAO;
import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.exceptions.DiscoException;

import java.util.List;
import java.util.ArrayList;

public class DiscoService {
    private final DiscoDAO discoDAO;
    
    public DiscoService(DiscoDAO discoDAO) {
        this.discoDAO = discoDAO;
    }
    
    public void adicionarDisco(Disco disco) {
        if (disco == null) throw new DiscoException("Não é possível enviar um formulário vazio.");
        if (disco.titulo.length > 100 || disco.criadoPor.length > 100 || disco.genero.length > 100) throw new DiscoException("Campos de texto do formulário (Título, Criado Por e Gênero) não podem conter mais de 100 caracteres.");
        if (disco.valor < 0 || disco.qtdItens < 0 || disco.duracao < 0) throw new DiscoException("Os campos Valor, Quantidade de Itens e Duração não podem ter valores negativos.");
        
        discoDAO.create(disco);
        System.out.prinln(disco.titulo + " adicionado com sucesso.");
    }
    public list<Disco> lerDisco() {
        if (discoDAO.read().isEmpty()) throw new DiscoException("Nenhum disco foi encontrado no banco de dados.");
        else {
            System.out.println("Disco(s) encontrado(s).");
            return discoDAO.read();
        }
    }
    public Disco lerDiscoPorID(String ID) {
        if (discoDAO.readByID(ID) == null) throw new DiscoException("Nenhum disco foi com o ID utilizado (" + ID + ") foi encontrado no banco de dados.");
        else {
            System.out.println("Disco encontrado.");
            return discoDAO.readByID(ID);
        }
    }
    public void atualizarDisco(Disco disco) {
        if (disco == null) throw new DiscoException("Não é possível enviar um formulário vazio.");
        if (discoDAO.readByID(ID) == null) throw new DiscoException("Nenhum disco foi com o ID utilizado (" + ID + ") foi encontrado no banco de dados.");
        if (disco.titulo.length > 100 || disco.criadoPor.length > 100 || disco.genero.length > 100) throw new DiscoException("Campos de texto do formulário (Título, Criado Por e Gênero) não podem conter mais de 100 caracteres.");
        if (disco.valor < 0 || disco.qtdItens < 0 || disco.duracao < 0) throw new DiscoException("Os campos Valor, Quantidade de Itens e Duração não podem ter valores negativos.");

        discoDAO.update(disco);
        System.out.prinln(disco.titulo + " alterado com sucesso.");
    }
    public void apagarDisco(String ID) {
        if (discoDAO.delete(ID) == null) throw new DiscoException("Nenhum disco foi com o ID utilizado (" + ID + ") foi encontrado no banco de dados.");
        else {
            System.out.println("Disco apagado.");
            discoDAO.delete(ID);
        }
    }
}
